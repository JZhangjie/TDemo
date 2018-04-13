package cn.jdz.glib.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by admin on 2017/11/13.
 */

public class OrientationSensor implements ISensor {
    private Context mContext;
    private SensorEventListener mCallback;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float[] mOrientation;

    public OrientationSensor(Context context,SensorEventListener callback){
        mContext = context;
        mCallback = callback;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public Object getValues() {
        return mOrientation;
    }

    @Override
    public boolean isWorking() {
        return mSensorManager!=null && mOrientation != null;
    }

    @Override
    public void start() {
        if (mSensorManager != null) {
            mSensorManager.registerListener(mSensorEventListener,mSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void stop() {
        if(mSensorManager != null)
        {
            mSensorManager.unregisterListener(mSensorEventListener);
            mOrientation=null;
        }
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mOrientation = event.values;
            if(mCallback!=null)
                mCallback.onSensorChanged(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            if(mCallback!=null)
                mCallback.onAccuracyChanged(sensor,accuracy);
        }
    };
}
