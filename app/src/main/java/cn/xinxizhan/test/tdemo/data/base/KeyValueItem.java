package cn.xinxizhan.test.tdemo.data.base;

/**
 * Created by admin on 2017/9/4.
 */

public class KeyValueItem {
    private String code;
    private String value;
    private int level;
    private String parentCode;
    private boolean hasChildren;

    public KeyValueItem(String code, String value) {
        this(code,value,1,null,false);
    }
    public KeyValueItem(String code, String value, boolean hasChildren) {
        this(code,value,1,null,hasChildren);
    }
    public KeyValueItem(String code, String value, int level, String parent) {
        this(code,value,level,parent,false);
    }
    public KeyValueItem(String code, String value, int level, String parent, boolean hasChildren) {
        this.code=code;
        this.value=value;
        this.level = level;
        this.parentCode = parent;
        this.hasChildren = hasChildren;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
