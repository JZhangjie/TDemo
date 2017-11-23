package cn.xinxizhan.test.tdemo.utils;

import java.util.List;

import cn.jdz.glib.data.KeyValueItem;

/**
 * Created by admin on 2017/9/4.
 */

public class DLBMHelper {

    public static String findValueByCode(String code, List<KeyValueItem> list) {
        if(list!=null&&code!=null)
        {
            for(KeyValueItem item :list)
            {
                if(code.equals(item.getCode()))
                {
                    return item.getValue();
                }
            }
        }
        return code;
    }

    public static String findCodeByValue(String value, List<KeyValueItem> list) {
        if(list!=null&&value!=null)
        {
            for(KeyValueItem item :list)
            {
                if(value.equals(item.getValue()))
                {
                    return item.getCode();
                }
            }
        }
        return value;
    }
}
