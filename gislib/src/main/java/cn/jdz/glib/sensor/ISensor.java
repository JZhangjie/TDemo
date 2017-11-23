package cn.jdz.glib.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by admin on 2017/11/13.
 */

public interface ISensor {

    float[] getValues();

    boolean isWorking();

    void start();

    void stop();

}
