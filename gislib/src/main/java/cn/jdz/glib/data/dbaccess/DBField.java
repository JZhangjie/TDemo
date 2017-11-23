package cn.jdz.glib.data.dbaccess;

/**
 * Created by admin on 2017/11/22.
 */

public class DBField {
    private String fieldName;
    private String fieldType;
    private String modelFieldName;
    private Class modelFieldType;
    private ColumnAccessMode accessMode;

    public DBField(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public DBField(String fieldName, String fieldType, String modelFieldName, Class modelFieldType, ColumnAccessMode accessMode) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.modelFieldName = modelFieldName;
        this.modelFieldType = modelFieldType;
        this.accessMode = accessMode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getModelFieldName() {
        return modelFieldName;
    }

    public void setModelFieldName(String modelFieldName) {
        this.modelFieldName = modelFieldName;
    }

    public Class getModelFieldType() {
        return modelFieldType;
    }

    public void setModelFieldType(Class modelFieldType) {
        this.modelFieldType = modelFieldType;
    }

    public ColumnAccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(ColumnAccessMode accessMode) {
        this.accessMode = accessMode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DBField){
            DBField temp = (DBField) obj;
            return this.getFieldName().equals(temp.getFieldName()) && this.getFieldType().equals(temp.getFieldType());
        }
        return false;
    }
}
