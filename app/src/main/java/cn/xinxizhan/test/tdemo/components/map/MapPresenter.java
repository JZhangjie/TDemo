package cn.xinxizhan.test.tdemo.components.map;

import android.location.Location;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.esri.TianDiTuTiledMapServiceLayer;

/**
 * Created by admin on 2017/10/10.
 */

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View mView;
    private MapContract.Callback mCallback;
    private boolean isFirst = true;
    private MapView mMap;
    private GraphicsLayer mVectorLayer;

    public MapPresenter(MapContract.View view, MapContract.Callback callback) {
        this.mView = view;
        this.mCallback = callback;
        this.mView.setPresenter(this);
        mMap = this.mView.getMap();
    }

    @Override
    public void start() {
        load(false);
    }

    public void load(boolean overwrite) {
        if (overwrite || isFirst) {
            isFirst = false;
            TianDiTuTiledMapServiceLayer mZXYXLayer = new TianDiTuTiledMapServiceLayer.Builder()
                    .setUrl(ApplicationConstants.ZXYXURL)
                    .setLayerId("img")
                    .setStyle("default")
                    .setFormat("tiles")
                    .setTileMatrixSet("c").builder();
            TianDiTuTiledMapServiceLayer mZXSLLayer = new TianDiTuTiledMapServiceLayer.Builder()
                    .setUrl(ApplicationConstants.ZXSLURL)
                    .setLayerId("vec")
                    .setStyle("default")
                    .setFormat("tiles")
                    .setTileMatrixSet("c").builder();
            mZXSLLayer.setVisible(false);
            mMap.addLayer(mZXYXLayer);
            mMap.addLayer(mZXSLLayer);
        }
    }

    @Override
    public void zoomTo(Geometry geometry) {
        this.mMap.setExtent(geometry);
    }

    @Override
    public void changeVectorLayer(GraphicsLayer layer) {
        if (mVectorLayer != null) {
            mVectorLayer.clearSelection();
            this.mMap.removeLayer(mVectorLayer);
        }
        if (layer != null) {
            mVectorLayer = layer;
            this.mMap.addLayer(mVectorLayer);
            this.mView.changeVectorLayerControl(true);
        } else {
            mVectorLayer = null;
            this.mView.changeVectorLayerControl(false);
        }
    }

    @Override
    public void setLayerVisible(int index, Boolean visible) {
        this.mMap.getLayer(index).setVisible(visible);
    }

    @Override
    public void clickVectorLayer(float x, float y) {
        if (mVectorLayer != null && mVectorLayer.isVisible()) {
            int[] ids = mVectorLayer.getGraphicIDs(x, y, 8);
            if (ids != null && ids.length > 0) {
                int id = ids[0];
                mVectorLayer.clearSelection();
                mVectorLayer.setSelectedGraphics(new int[]{id}, true);
                mCallback.onGraphicClick(id);
            }
        }
    }

    @Override
    public Location getCurrentLocation() {
        return mView.getLocation();
    }
}
