package cn.jdz.glib.data.dbaccess.excel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import cn.jdz.glib.data.dbaccess.Column;
import cn.jdz.glib.data.dbaccess.DBField;
import cn.jdz.glib.data.dbaccess.IDBManager;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Boolean;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

/**
 * Created by admin on 2017/11/22.
 * T是实体类，实体类的字段类型有所限制。需要扩展getValueFromDB
 */

public class ExcelManager<T> implements IDBManager<T> {

    private static final String FILEPOSTFIX = ".xls";
    //表头占据的行数
    private int mHeaderLineNum;
    private String mTitle;
    //表字段数组，长度即表列数，代表表结构
    private List<DBField> mFields;
    //excel文件，和Sheet名
    private File mExcelFile;
    private String mTableName;
    private Class<T> mEntityClass;
    private List<T> mDatas;

    public ExcelManager(File excelfile, String title, Class<T> entityClass) {
        this(excelfile, "Sheet1", 2, title, entityClass);
    }

    public ExcelManager(File excelfile, String tablename, int headerLineNum, String title, Class<T> entityClass) {
        this.mExcelFile = excelfile;
        this.mTableName = tablename;
        this.mEntityClass = entityClass;
        this.mHeaderLineNum = headerLineNum;
        this.mTitle = title;
        initFromModel(this.mEntityClass);
        initFromExcel();
    }

    private void initFromExcel() {
        mDatas = getAll();
    }

    private void initFromModel(Class<T> entityClass) {
        if (entityClass == null) {
            throw new RuntimeException("未传入Model类对象！");
        }
        mFields = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                DBField tempField = new DBField(column.ColumnName(), column.ColumnType(), field.getName(), field.getType(), column.AccessMode());
                mFields.add(tempField);
            }
        }
    }

    @Override
    public boolean create() {
        if (mFields == null || mFields.size() == 0) {
            throw new RuntimeException("为初始化字段列表！");
        }
        if (mExcelFile != null && mExcelFile.exists()) {
            mExcelFile.delete();
        }
        WritableWorkbook wwb = null;
        try {
            wwb = getWritableWorkbook(mExcelFile);
            WritableSheet ws = getWritableSheet(wwb, mTableName);
            if(mHeaderLineNum > 1){
                int rn = mHeaderLineNum - 1;
                ws.mergeCells(0, 0, mFields.size() - 1, rn - 1);
                ws.addCell(new Label(0, 0, mTitle));
                for (int i = 0; i < mFields.size(); i++) {
                    DBField dbField = mFields.get(i);
                    Label header = new Label(i, rn, dbField.getFieldName());
                    ws.addCell(header);
                }
                wwb.write();
            }
            wwb.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insert(T m) {
        if (mFields == null || mFields.size() == 0) {
            throw new RuntimeException("为初始化字段列表！");
        }
        WritableWorkbook wwb = null;
        try {
            wwb = getWritableWorkbook(mExcelFile);
            WritableSheet ws = getWritableSheet(wwb, mTableName);
            int rn = ws.getRows();
            for (int i = 0; i < mFields.size(); i++) {
                DBField dbField = mFields.get(i);
                ws.addCell(getValueFromModel(i, rn, dbField, m));
            }
            mDatas.add(m);
            wwb.write();
            wwb.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(T m) {
        if (mFields == null || mFields.size() == 0) {
            throw new RuntimeException("为初始化字段列表！");
        }
        if(!mDatas.contains(m)){
            throw new RuntimeException("当前实体不在文件中！");
        }
        int r = mDatas.indexOf(m);
        WritableWorkbook wwb = null;
        try {
            wwb = getWritableWorkbook(mExcelFile);
            WritableSheet ws = getWritableSheet(wwb, mTableName);
            int rn = ws.getRows();
            int uprow = r+mHeaderLineNum;
            if (uprow > rn) {
                throw new RuntimeException("更新数据时出错！");
            }
            for (int i = 0; i < mFields.size(); i++) {
                DBField dbField = mFields.get(i);
                ws.addCell(getValueFromModel(i, uprow, dbField, m));
            }
            wwb.write();
            wwb.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(T m) {
        if (mDatas == null) {
            throw new RuntimeException("没有数据可以删除！");
        }
        int r = mDatas.indexOf(m);
        if (r == -1) {
            return false;
        } else {

            WritableWorkbook wwb = null;
            try {
                wwb = getWritableWorkbook(mExcelFile);
                WritableSheet ws = getWritableSheet(wwb, mTableName);
                int rn = ws.getRows();
                int delrow = r + mHeaderLineNum;
                if (delrow > rn) {
                    throw new RuntimeException("删除时出错！");
                }
                ws.removeRow(delrow);
                mDatas.remove(m);
                wwb.write();
                wwb.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }

            return true;
        }
    }

    @Override
    public List<T> getAll() {
        List<T> temp = new ArrayList<T>();
        if (mExcelFile == null) {
            throw new RuntimeException("未传入excel文件路径");
        }
        if (mFields == null || mFields.size() <=0){
            throw new RuntimeException("表结构未初始化！");
        }
        if(mEntityClass==null){
            throw new RuntimeException("Model类对象未初始化！");
        }
        if (!mExcelFile.exists() || mExcelFile.isDirectory() || !mExcelFile.getName().endsWith(FILEPOSTFIX)) {
            return temp;
        }

        try {
            Workbook workbook = Workbook.getWorkbook(mExcelFile);
            Sheet s = workbook.getSheet(mTableName);
            if(s!=null){
                int temprownum = s.getRows();
                //检查表结构
                for(int i=0;i<mFields.size();i++){
                    DBField dbField = mFields.get(i);
                    Cell cell = s.getCell(i,mHeaderLineNum-1);
                    String cellvalue = cell.getContents();
                    if(cellvalue == null || !cellvalue.equals(dbField.getFieldName())){
                        throw new RuntimeException("表结构不正确！");
                    }
                }
                //读取数据
                for(int i=mHeaderLineNum;i<temprownum;i++){
                    T t =mEntityClass.newInstance();
                    for(int j =0;j<mFields.size();j++){
                        Cell cell = s.getCell(j,i);
                        DBField dbField = mFields.get(j);
                        Field field = mEntityClass.getDeclaredField(dbField.getModelFieldName());
                        field.setAccessible(true);
                        field.set(t,getValueFromDB(cell,dbField));
                    }
                    temp.add(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    public T getByUnique(Object uniqueValue) {
        return null;
    }

    private WritableWorkbook getWritableWorkbook(File file) throws IOException, BiffException {
        if (file != null) {
            if (file.exists() && file.isFile() && file.getName().endsWith(FILEPOSTFIX)) {
                Workbook wb = Workbook.getWorkbook(file);
                WritableWorkbook wwb = Workbook.createWorkbook(file, wb);
                wb.close();
                return wwb;
            } else {
                WritableWorkbook wwb = Workbook.createWorkbook(file);
                return wwb;
            }
        } else {
            throw new RuntimeException("未指定对应文件！");
        }
    }

    private WritableSheet getWritableSheet(WritableWorkbook wwb, String name) {
        WritableSheet ws = wwb.getSheet(name);
        if (ws == null) {
            ws = wwb.createSheet(name, wwb.getNumberOfSheets());
        }
        return ws;
    }

    private Object getValueFromDB(Cell cell,DBField dbField){
        String value = cell.getContents();
        switch (dbField.getModelFieldType().getName()){
            case "int":
            case "java.lang.Integer":
                return Integer.parseInt(value.trim());
            case "double":
            case "java.lang.Double":
                return Double.parseDouble(value.trim());
            case "boolean":
            case "java.lang.Boolean":
                return java.lang.Boolean.parseBoolean(value.trim());
            case "java.util.Date":
                if(cell.getType().equals(CellType.DATE)){
                    DateCell dateCell = (DateCell) cell;
                    return new java.util.Date(dateCell.getDate().getTime());
                }
                return null;
            default:
                    return value;

        }
    }

    private WritableCell getValueFromModel(int c, int r, DBField dbField, T t) {
        Field field = null;
        try {
            field = mEntityClass.getDeclaredField(dbField.getModelFieldName());

            field.setAccessible(true);
            switch (dbField.getFieldName()) {
                case "int":
                case "java.lang.Integer":
                    int a = (int) field.get(t);
                    double b = a;
                    return new Number(c, r, b);
                case "double":
                case "java.lang.Double":
                    return new Number(c, r, (double) field.get(t));
                case "boolean":
                case "java.lang.Boolean":
                    return new Boolean(c, r, (boolean) field.get(t));
                case "java.util.Date":
                    java.util.Date tempdate = (java.util.Date)field.get(t);
                    Date date = new Date(tempdate.getTime());
                    return new DateTime(c,r,date);
                default:
                    return new Label(c, r, field.get(t).toString());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Label(c, r, "");
    }

}
