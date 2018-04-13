package cn.jdz.glib.sensor.location;

import cn.jdz.glib.sensor.ISensor;

/**
 * Created by admin on 2017/11/13.
 */

public interface ILocation extends ISensor {

    void setConfig(long mind,long mint,String provider);

}
