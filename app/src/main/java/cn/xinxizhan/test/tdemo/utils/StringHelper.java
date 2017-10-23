package cn.xinxizhan.test.tdemo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/10/9.
 */

public class StringHelper {

    public static Date getDateFromDB(String date){
        try {
            if(date==null || date == "")
                return null;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToDb(Date date){
        if(date==null)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String getImageName(){
        Date date = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_"+date.getTime()+".jpg";
    }

}
