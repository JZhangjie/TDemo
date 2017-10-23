package cn.xinxizhan.test.tdemo.utils;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.data.base.KeyValueItem;


/**
 * Created by admin on 2017/9/4.
 */

public class DLBMHelper {

    public static String FindValueByCode(String code,List<KeyValueItem> list) {
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
}
