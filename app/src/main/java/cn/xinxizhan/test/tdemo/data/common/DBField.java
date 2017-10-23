package cn.xinxizhan.test.tdemo.data.common;

/**
 * Created by admin on 2017/10/9.
 */

public class DBField {
    private String cid;
    private String name;
    private Class type;
    private String notnull;
    private String dflt_value;
    private String pk;
    private String propertyName;

    //从属性注释新建
    public DBField(String name, Class type, String propertyName) {
        this.name = name;
        this.type = type;
        this.propertyName = propertyName;
    }

    //从db文件新建
    public DBField(String cid, String name, Class type, String notnull, String dflt_value, String pk) {
        this.cid = cid;
        this.name = name;
        this.type = type;
        this.notnull = notnull;
        this.dflt_value = dflt_value;
        this.pk = pk;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getNotnull() {
        return notnull;
    }

    public void setNotnull(String notnull) {
        this.notnull = notnull;
    }

    public String getDflt_value() {
        return dflt_value;
    }

    public void setDflt_value(String dflt_value) {
        this.dflt_value = dflt_value;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DBField){
            return this.getName().toLowerCase().equals(((DBField) obj).getName().toLowerCase());
        }
        return false;
    }
}
