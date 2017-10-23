package cn.xinxizhan.test.tdemo.data.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by admin on 2017/10/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    //db文件中表字段名称，设置为不区分大小写
    String ColumnName();
    Class ColumnType() default String.class;
    ColumnAccessMode AccessMode() default ColumnAccessMode.ReadOnly;
}
