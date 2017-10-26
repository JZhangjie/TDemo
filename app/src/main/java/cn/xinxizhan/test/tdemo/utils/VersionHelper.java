package cn.xinxizhan.test.tdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ZP on 2017/10/20.
 */

public class VersionHelper {

    public static String getPackageVersion(Context context){

        String version = "1.0";

        //通过上下文获取Packagemanager
        PackageManager manager = context.getPackageManager();

        try {
            //通过manager获取package信息
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            //获取了app版本号
            version = packageInfo.versionName;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }

}
