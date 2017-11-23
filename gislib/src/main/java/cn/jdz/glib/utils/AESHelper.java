package cn.jdz.glib.utils;

import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by admin on 2017/10/20.
 */
public class AESHelper {
    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/ECB/PKCS5Padding";

    ///** 创建密钥 **/
    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    // /** 加密字节数据 **/
    public static byte[] encrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            System.out.println(key);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///** 加密(结果为16进制字符串) **/
    public static String encrypt(String content, String password) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, password);
        byte byteArr[]= Base64.encode(data, Base64.DEFAULT);
        return new String(byteArr);
    }

    // /** 解密字节数组 **/
    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///** 解密16进制的字符串为字符串 **/
    public static String decrypt(String content, String password) {
        byte[] data = null;
        try {
            data= Base64.decode(content, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password);
        if (data == null)
            return null;
        String result = null;
        try {
            result = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void check(String path){

        try{
            ExifInterface eif = new ExifInterface(path);
            String lat = eif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lat_ref = eif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lon = eif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String lon_ref = eif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

            Log.d("test", "lat ==== "+lat);
            Log.d("test", "lat_ref 加密 ==== "+lat_ref);
            Log.d("test", "lat_ref 解密 ==== "+AESHelper.decrypt(lat_ref, ImgHelper.masterPassword));
            Log.d("test", "lon ==== "+lon);
            Log.d("test", "lon_ref 加密 ==== "+lon_ref);
            Log.d("test", "lon_ref 解密 ==== "+AESHelper.decrypt(lon_ref, ImgHelper.masterPassword));

        }catch (Exception e){
            Log.d("test", "解密失败");
        }

    }
}
