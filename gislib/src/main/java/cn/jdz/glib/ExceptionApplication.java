package cn.jdz.glib;

import android.app.Application;

import cn.jdz.glib.utils.CrashHandler;

/**
 * Created by admin on 2017/12/12.
 */

public class ExceptionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
