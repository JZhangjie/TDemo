package cn.jdz.glib.utils;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ZP on 2017/10/17.
 */

public class ImgHelper {

    public static String masterPassword = "z";//密钥

    public static void setGPS(String path, double lat, double lon){

        try {
            ExifInterface exif = new ExifInterface(path);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                    decimalToDMS(lat));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                    decimalToDMS(lon));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,AESHelper.encrypt(String.valueOf(lat),masterPassword));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,AESHelper.encrypt(String.valueOf(lon),masterPassword));
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test", "setGPS: 出错！");
        }
    }

    public static void setOrientation(String path, float[] value){

        try {
            ExifInterface exif = new ExifInterface(path);
            //value 0,1,2 对应 z,x,y
            exif.setAttribute(ExifInterface.TAG_GPS_IMG_DIRECTION_REF,"0.19293.4003;F");
            Log.d("test", "setOrientation: 执行！");
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test", "setOrientation: 出错！");
        }
    }

    public static void show(String path){
        try {
            ExifInterface exif = new ExifInterface(path);
            //value 0,1,2 对应 z,x,y
            String s = exif.getAttribute(ExifInterface.TAG_GPS_IMG_DIRECTION_REF);
            Log.d("test", "show TAG_GPS_IMG_DIRECTION_REF: ==================================！"+s);
            String s1 = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            Log.d("test", "show TAG_GPS_LONGITUDE_REF: ==================================！"+s1);

            String s2 = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            Log.d("test", "show TAG_GPS_LONGITUDE_REF: ==================================！"+s2);

            String s3 = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            Log.d("test", "show TAG_GPS_LONGITUDE_REF: ==================================！"+s3);

            String s4 = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            Log.d("test", "show TAG_GPS_LONGITUDE_REF: ==================================！"+s4);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test", "show: 出错！");
        }
    }

    /**
     * 浮点型经纬度值转成度分秒格式
     *
     * @param coord
     * @return
     */
    public static String decimalToDMS(double coord) {
        String output, degrees, minutes, seconds;

        double mod = coord % 1;
        int intPart = (int) coord;

        degrees = String.valueOf(intPart);


        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        if (intPart < 0) {
            intPart *= -1;
        }
        minutes = String.valueOf(intPart);

        coord = mod * 60;
        intPart = (int) coord;
        if (intPart < 0) {
            intPart *= -1;
        }

        seconds = String.valueOf(intPart);

        output = degrees + "/1," + minutes + "/1," + seconds + "/1";

        return output;
    }

}
