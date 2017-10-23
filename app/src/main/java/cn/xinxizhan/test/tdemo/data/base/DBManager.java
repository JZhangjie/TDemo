package cn.xinxizhan.test.tdemo.data.base;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xinxizhan.test.tdemo.data.common.Column;
import cn.xinxizhan.test.tdemo.data.common.ColumnAccessMode;
import cn.xinxizhan.test.tdemo.data.common.DBField;
import cn.xinxizhan.test.tdemo.utils.StringHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by admin on 2017/10/9.
 */

public class DBManager<T> extends ArrayList<T>{
    //从db文件获取的字段
    private List<DBField> mDBFields;
    //从Model类的注释中获取的字段
    private List<DBField> selectFields;
    private List<DBField> updateFields;
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
    private boolean isLoaded;
    public static final String POSTFIX = ".db";

    //region 访问器
    public String getmTableName() {
        return mTableName;
    }

    public void setmTableName(String mTableName) {
        this.mTableName = mTableName;
    }

    public String getmDBName() {
        return mDBName;
    }

    public void setmDBName(String mDBName) {
        this.mDBName = mDBName;
    }

    public String getmDBPath() {
        return mDBPath;
    }

    public void setmDBPath(String mDBPath) {
        this.mDBPath = mDBPath;
    }

    //endregion

    public DBManager(String _dbPath, String _tableName, String _uniqueField, Class<T> _entityClass) {
        this.mDBPath = _dbPath;
        this.mTableName = _tableName;
        this.mEntityClass = _entityClass;
        initFromDB(_dbPath, _uniqueField);
        initFromModel(_entityClass);
    }

    public boolean update(T t) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
        List args = new ArrayList();

        try {
            for (DBField dbField : this.updateFields) {
                Field field = mEntityClass.getDeclaredField(dbField.getPropertyName());
                args.add(getValueFromModel(field,t));
            }
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getPropertyName());
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

    public int load() {
        if (isLoaded) {
            return 0;
        }
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery(this.selectAllSQL, null);
        int num = 0;
        while (cursor.moveToNext()) {
            try {
                T feature = mEntityClass.newInstance();
                for (int i = 0; i < this.selectFields.size(); i++) {
                    DBField dbField = this.selectFields.get(i);
                    Field filed = mEntityClass.getDeclaredField(dbField.getPropertyName());
                    filed.setAccessible(true);
                    filed.set(feature, getValueFromCursor(cursor, filed, dbField.getName()));
                }
                this.add(feature);
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

    public T getByUnique(Object uniqueValue){
        try {
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getPropertyName());
            field.setAccessible(true);
            for(T t :this){
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

    public int delete(T t){
        if(!this.contains(t)){
            return 0;
        }
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBPath, null, SQLiteDatabase.OPEN_READWRITE);
        List args = new ArrayList();
        try {
            Field field = mEntityClass.getDeclaredField(this.uniqueField.getPropertyName());
            field.setAccessible(true);
            args.add(field.get(t));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
        database.execSQL(this.deleteSQL, args.toArray());
        database.close();
        return 1;
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
        this.mDBName = file.getName().substring(0, file.getName().length() - POSTFIX.length());
        //获取db表结构
        String sql = "pragma table_info(" + this.mTableName + ")";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery(sql, null);
        this.mDBFields = new ArrayList<>();
        while (cursor.moveToNext()) {
            String cid = cursor.getString(cursor.getColumnIndex("cid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String temptype = cursor.getString(cursor.getColumnIndex("type"));
            String notnull = cursor.getString(cursor.getColumnIndex("notnull"));
            String dflt_value = cursor.getString(cursor.getColumnIndex("dflt_value"));
            String pk = cursor.getString(cursor.getColumnIndex("pk"));
            DBField field = new DBField(cid, name, toJavaClass(temptype), notnull, dflt_value, pk);
            this.mDBFields.add(field);
            if (name.toLowerCase().equals(uniquefieldname.toLowerCase())) {
                uniqueField = field;
            }
        }
        if (uniqueField == null) {
            throw new RuntimeException("未指定标识字段！");
        }
        cursor.close();
        database.close();
    }

    /**
     * 依据Model类的属性的注释初始化查询、插入、更新、删除的sql语句
     *
     * @param entityClass Model类
     */
    private void initFromModel(Class<T> entityClass) {
        this.selectFields = new ArrayList<>();
        this.updateFields = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                DBField tempField = new DBField(column.ColumnName(), column.ColumnType(), field.getName());
                if (!this.mDBFields.contains(tempField)) {
                    throw new RuntimeException("db中不存在对应名称的字段");
                }
                DBField dbField = this.mDBFields.get(this.mDBFields.indexOf(tempField));
                dbField.setType(tempField.getType());
                dbField.setPropertyName(field.getName());
                this.selectFields.add(dbField);
                if (column.AccessMode() == ColumnAccessMode.ReadAndWrite) {
                    this.updateFields.add(dbField);
                }
            }
        }
        if (this.selectFields.size() == 0 || this.updateFields.size() == 0) {
            throw new RuntimeException("没有指定和db交互的字段");
        }
        //sql语句初始化
        selectAllSQL = "select ";
        insertSQL = "insert into " + this.mTableName + "(";
        String tempinsert = " values (";
        for (DBField field : this.selectFields) {
            selectAllSQL += " " + field.getName() + ",";
            insertSQL += field.getName() + ",";
            tempinsert += "?,";
        }
        selectAllSQL = selectAllSQL.substring(0, selectAllSQL.length() - 1);
        selectOneSQL = selectAllSQL;
        selectAllSQL += " from " + this.mTableName;
        selectOneSQL += " from " + this.mTableName + " where " + this.uniqueField.getName() + " =?";
        insertSQL = insertSQL.substring(0, insertSQL.length() - 1) + ") ";
        tempinsert = tempinsert.substring(0, tempinsert.length() - 1) + ")";
        insertSQL += tempinsert;

        updateSQL = "update " + this.mTableName + " set ";
        for (DBField field : this.updateFields) {
            updateSQL += field.getName() + "=?,";
        }
        updateSQL = updateSQL.substring(0, updateSQL.length() - 1) + " where " + this.uniqueField.getName() + " =?";
        deleteSQL = "delete from " + this.mTableName + " where " + this.uniqueField.getName() + "=?";
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
                return StringHelper.getDateFromDB(date);
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
                String date = StringHelper.dateToDb((Date)field.get(t));
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
}
