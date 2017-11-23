package cn.jdz.glib.location;

import android.location.Location;

/**
 * Created by admin on 2017/11/13.
 */

public interface ILocation {

    Location getLocation();

    boolean isWorking();

    void start();

    void stop();

}
