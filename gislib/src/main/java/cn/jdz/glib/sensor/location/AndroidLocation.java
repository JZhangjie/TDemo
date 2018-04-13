package cn.jdz.glib.sensor.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by admin on 2017/11/13.
 */

public class AndroidLocation implements ILocation {

    private Context mContext;
    private LocationListener mCallback;
    private LocationManager mLocatonManager;
    private Location mLocation;
    private long mMinTime = 1 * 1000;
    private long mMinDistance = 1;
    private String mProvider;

    public AndroidLocation(Context context,LocationListener callback) {
        mContext = context;
        mCallback = callback;
        mLocatonManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mProvider = LocationManager.GPS_PROVIDER;
    }

    @Override
    public Object getValues() {
        return mLocation;
    }

    @Override
    public boolean isWorking() {
        return mLocatonManager!=null && mLocatonManager.isProviderEnabled(mProvider);
    }

    @Override
    public void start() {
        if (mLocatonManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "未获取定位权限", Toast.LENGTH_SHORT).show();
                return;
            }
            mLocatonManager.requestLocationUpdates(mProvider, mMinTime, mMinDistance, mLocationListener);
        }
    }

    @Override
    public void stop() {
        if(mLocatonManager != null){
            mLocatonManager.removeUpdates(mLocationListener);
            mLocation = null;
        }
    }

    @Override
    public void setConfig(long mind, long mint, String provider) {
        mMinDistance = mind;
        mMinTime = mint;
        if(provider != null)
        {
            mProvider = provider;
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            if(mCallback!=null)
                mCallback.onLocationChanged(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if(mCallback!=null)
                mCallback.onStatusChanged(provider,status,extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            if(mCallback!=null)
                mCallback.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            mLocation = null;
            if(mCallback!=null)
                mCallback.onProviderDisabled(provider);
        }
    };

}
