package cn.xinxizhan.test.tdemo.components.map;

import android.location.Location;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import cn.xinxizhan.test.tdemo.components.BasePresenter;
import cn.xinxizhan.test.tdemo.components.BaseView;

/**
 * Created by admin on 2017/10/10.
 */

public interface MapContract {

    interface Presenter extends BasePresenter{

        void zoomTo(Geometry geometry);

        void changeVectorLayer(GraphicsLayer layer);

        void setLayerVisible(int index,Boolean visible);

        void clickVectorLayer(float x,float y);

        Location getCurrentLocation();
    }

    interface View extends BaseView<Presenter>{

        MapView getMap();

        Location getLocation();

        void changeVectorLayerControl(Boolean isAdd);
    }

    interface Callback{

        void onGraphicClick(int graphicId);

    }
}
