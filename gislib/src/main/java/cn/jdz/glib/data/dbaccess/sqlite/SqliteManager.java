package cn.jdz.glib.data.dbaccess.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jdz.glib.data.dbaccess.Column;
import cn.jdz.glib.data.dbaccess.ColumnAccessMode;
import cn.jdz.glib.data.dbaccess.DBField;
import cn.jdz.glib.data.dbaccess.IDBManager;


/**
 * Created by admin on 2017/10/9.
 * DB文件读写，转换为一个List对象
 * 未添加db插入功能
 */

public class SqliteManager<T> implements IDBManager<T>{
    //从db文件获取的字段
    private List<DBField> mFields;
    private DBField uniqueField;
    private String selectAllSQL;
    private String selectOneSQL;
    private String deleteSQL;
    private String updateSQL;
    private String insertSQL;
    private String mTableName;
    private String mDBName;
    private String mDBPath;
    private Class<T> mEntityClass;
    private List<T> mDatas;
    private boolean isLoaded;
    public static final String POSTFIX = ".db";

    public SqliteManager(String _dbPath, String _tableName, String _uniqueField, Class<T> _entityClass) {
        this.mDBPath = _dbPath;
        this.mTableName = _tableName;
        this.mEntityClass = _entityClass;
        initFromModel(_entityClass);
        initFromDB(_dbPath, _uniqueField);
        initSQL();
    }

    /**
     * 依据Model类的属性的注释初始化查询、插入、更新、删除的sql语句
     *
     * @param entityClass Model类
     */
    private void initFromModel(Class<T> entityClass) {
        this.mFields = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                DBField tempField = new DBField(column.ColumnName(), column.ColumnType(), field.getName(),field.getType(),column.AccessMode());
                mFields.add(tempField);
            }
        }
    }

    /**
     * 获取db文件的结构，初始化标识字段
     *
     * @param dbpath          db文件路径
     * @param uniquefieldname 标识字段名称
     */
    private void initFromDB(String dbpath, String uniquefieldname) {
        File file = new File(dbpath);
        if (!file.exists() || !file.getName().endsWith(POSTFIX)) {
            throw new RuntimeException("db文件不存在或非db文件");
        }
        if(mFields == null || mFields.size()<=0){
            throw new RuntimeException("表结构未初始化！");
        }
        this.mDBName = file.getName().substring(0, file.getName().length() - POSTFIX.length());
        //获取db表结构
        String sql = "pragma table_info(" + this.mTableName + ")";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery(sql, null);

        List<DBField> tempFields = new ArrayList<>();
        while (cursor.moveToNext()) {
            String cid = cursor.getString(cursor.getColumnIndex("cid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String notnull = cursor.getString(cursor.getColumnIndex("notnull"));
            String dflt_value = cursor.getString(cursor.getColumnIndex("dflt_value"));
            String pk = cursor.getString(cursor.getColumnIndex("pk"));
            DBField tempfield = new DBField(name,type);

            if(!mFields.contains(tempfield)){
                throw new RuntimeException(name+"字段在实体类中没有对应的属性！");
            }
            if (name.equals(uniquefieldname)) {
                uniqueField = mFields.get(mFields.indexOf(tempfield));
            }
            tempFields.add(tempfield);
        }
        if (uniqueField == null) {
            throw new RuntimeException("未指定标识字段！");
        }

        //表与实体类比对
        if(mFields.size() != tempFields.size()){
            throw new RuntimeException("属性在表中没有对应字段！");
        }
        cursor.close();
        database.close();
    }

    /**
     * 构造sqlite数据库访问的sql语句
     */
    private void initSQL(){
        //sql语句初始化
        selectAllSQL = "select ";
        insertSQL = "insert into " + this.mTableName + "(";
        updateSQL = "update " + this.mTableName + " set ";
        //插入
        String tempinsert = " values (";
        for (DBField field : this.mFields) {
            selectAllSQL += " " + field.getFieldName() + ",";
            insertSQL += field.getFieldName() + ",";
            tempinsert += "?,";
            if(field.getAccessMode() == ColumnAccessMode.ReadAndWrite){
                updateSQL += field.getFieldName() + "=?,";
            }
        }

        selectAllSQL = selectAllSQL.substring(0, selectAllSQL.length() - 1);
        selectOneSQL = selectAllSQL;
        selectAllSQL += " from " + this.mTableName;
        selectOneSQL += " from " + this.mTableName + " where " + this.uniqueField.getFieldName() + " =?";
        insertSQL = insertSQL.substring(0, insertSQL.length() - 1) + ") ";
        tempinsert = tempinsert.substring(0, tempinsert.length() - 1) + ")";
        insertSQL += tempinsert;

        updateSQL = updateSQL.substring(0, updateSQL.length() - 1) + " where " + this.uniqueField.getFieldName() + " =?";
        deleteSQL = "delete from " + this.mTableName + " where " + this.uniqueField.getFieldName() + "=?";
    }

    @Override
    public boolean create() {
        return false;
    }

    @Override
    public boolean insert(T t) {
        return false;
    }

    @Override
    public boolean update(T t) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
        List args = new ArrayList();

        try {
            for (DBField sqliteField : this.mFields) {
                if(sqliteField.getAccessMode() == ColumnAccessMode.ReadAndWrite){
                    Field field = mEntityClass.getDeclaredField(sqliteField.getModelFieldName());
                    args.add(getValueFromModel(field,t));
                }
            }
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getModelFieldName());
            field.setAccessible(true);
            args.add(field.get(t));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("db中不存在对应字段！");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Model中的字段没有访问权限！");
        }
        database.execSQL(this.updateSQL, args.toArray());
        database.close();
        return true;
    }

    @Override
    public boolean delete(T t){
        if(mDatas == null || !this.mDatas.contains(t)){
            return false;
        }
        try {
            SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
            List args = new ArrayList();
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getModelFieldName());
            field.setAccessible(true);
            args.add(field.get(t));
            database.execSQL(this.deleteSQL, args.toArray());
            database.close();
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<T> getAll() {
        load();
        return mDatas;
    }

    private int load() {
        if (isLoaded) {
            return 0;
        }
        this.mDatas = new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery(this.selectAllSQL, null);
        int num = 0;
        while (cursor.moveToNext()) {
            try {
                T feature = mEntityClass.newInstance();
                for (int i = 0; i < this.mFields.size(); i++) {
                    DBField sqliteField = this.mFields.get(i);
                    Field filed = mEntityClass.getDeclaredField(sqliteField.getModelFieldName());
                    filed.setAccessible(true);
                    filed.set(feature, getValueFromCursor(cursor, filed, sqliteField.getFieldName()));
                }
                this.mDatas.add(feature);
                num++;

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        cursor.close();
        database.close();
        isLoaded = true;
        return num;
    }

    @Override
    public T getByUnique(Object uniqueValue){
        try {
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getModelFieldName());
            field.setAccessible(true);
            for(T t :this.mDatas){
                if(uniqueValue.equals(field.get(t)))
                {
                    return t;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取db数据，类型转换
     *
     * @param cursor
     * @param field
     * @param columnName
     * @return
     */
    private Object getValueFromCursor(Cursor cursor, Field field, String columnName) {
        switch (field.getType().getName()) {
            case "int":
            case "java.lang.Integer":
                return cursor.getInt(cursor.getColumnIndex(columnName));
            case "double":
            case "java.lang.Double":
                return cursor.getDouble(cursor.getColumnIndex(columnName));
            case "java.util.Date":
                String date = cursor.getString(cursor.getColumnIndex(columnName));
                return getDateFromDB(date);
            default:
                return cursor.getString(cursor.getColumnIndex(columnName));
        }
    }

    /**
     * 存储数据时，类型转换
     * @param field
     * @param t
     * @return
     * @throws IllegalAccessException
     */
    private Object getValueFromModel(Field field, T t) throws IllegalAccessException {
        field.setAccessible(true);
        switch (field.getType().getName()) {
            case "java.util.Date":
                String date = dateToDb((Date)field.get(t));
                return date;
            default:
                return field.get(t);
        }
    }

    private static Class toJavaClass(String typename) {
        switch (typename.trim().toLowerCase()) {
            case "integer":
            case "int":
                return Integer.class;
            case "real":
            case "numeric":
                return Double.class;
            case "boolean":
            case "bool":
                return Boolean.class;
            default:
                return String.class;
        }
    }

    /**
     * db文件中date字符串转Date对象
     * @param date
     * @return
     */
    private static Date getDateFromDB(String date){
        try {
            if(date==null || date == "")
                return null;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date对象转存储在db中的字符串
     * @param date
     * @return
     */
    private static String dateToDb(Date date){
        if(date==null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
