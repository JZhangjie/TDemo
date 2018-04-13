package cn.jdz.glib.sensor;

/**
 * Created by admin on 2017/11/13.
 */

public interface ISensor {

    Object getValues();

    boolean isWorking();

    void start();

    void stop();

}
