package cn.xinxizhan.test.tdemo.components.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;

import cn.xinxizhan.test.tdemo.R;

/**
 * Created by admin on 2017/10/10.
 */

public class MapFragment extends Fragment implements MapContract.View,View.OnClickListener,CompoundButton.OnCheckedChangeListener,OnSingleTapListener {
    private MapView mMapView;
    private View mLayerControlButton;
    private FloatingActionButton mLocationButton;
    private LocationDisplayManager mLocationManager;
    private PopupWindow mLayerControlPopupWindow;
    private ToggleButton mLayerControlRbtVector;
    private RadioButton mLayerControlRbtZXSL;
    private RadioButton mLayerControlRbtZXYX;
    private Location mLocation;

    private MapContract.Presenter mPresenter;
    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)view.findViewById(R.id.map_map);
        mLayerControlButton = view.findViewById(R.id.map_btn_layercontrol);
        mLocationButton = (FloatingActionButton)view.findViewById(R.id.map_btn_location);
        View v = inflater.inflate(R.layout.control_layercontrol_popupwindow,null);
        mLayerControlPopupWindow = new PopupWindow(v, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,true);
        mLayerControlPopupWindow.setFocusable(false);
        mLayerControlPopupWindow.setOutsideTouchable(false);
        mLayerControlRbtVector = (ToggleButton)v.findViewById(R.id.layercontrol_toggle_tb);
        mLayerControlRbtVector.setChecked(true);
        mLayerControlRbtZXSL=(RadioButton) v.findViewById(R.id.layercontrol_toggle_zx_sl);
        mLayerControlRbtZXYX=(RadioButton) v.findViewById(R.id.layercontrol_toggle_zx_yx);

        //添加地图状态监听，地图初始化后，开启位置监听
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(o==mMapView && status==STATUS.INITIALIZED) {
                    mLocationManager = mMapView.getLocationDisplayManager();
                    mLocationManager.start();
                    mLocationManager.setAllowNetworkLocation(true);
                    mLocationManager.setShowLocation(false);
                    mLocationManager.setShowPings(false);
                    mLocationManager.setLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLocation = location;
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
            }
        });
        mMapView.setOnSingleTapListener(this);
        mLocationButton.setOnClickListener(this);
        mLayerControlButton.setOnClickListener(this);
        mLayerControlRbtVector.setOnCheckedChangeListener(this);
        mLayerControlRbtZXSL.setOnCheckedChangeListener(this);
        mLayerControlRbtZXYX.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter!=null)
            mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public MapView getMap() {
        return mMapView;
    }

    @Override
    public Location getLocation() {
        return mLocation;
    }

    @Override
    public void changeVectorLayerControl(Boolean isAdd) {
        if(isAdd){
            ((LinearLayout)mLayerControlRbtVector.getParent()).setVisibility(View.VISIBLE);
        }
        else {
            ((LinearLayout)mLayerControlRbtVector.getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_btn_location:
                if(mLocationManager!= null){
                    mLocationManager.setShowLocation(!mLocationManager.isShowLocation());
                    mLocationManager.setShowPings(mLocationManager.isShowLocation());
                    mLocation =mLocationManager.getLocation();
                    Point point = new Point(mLocation.getLongitude(),mLocation.getLatitude());
                    mMapView.centerAt(point,true);
                }
                break;
            case R.id.map_btn_layercontrol:
                if(mLayerControlPopupWindow.isShowing()){
                    mLayerControlPopupWindow.dismiss();
                }
                else {
                    mLayerControlPopupWindow.showAsDropDown(v);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.layercontrol_toggle_tb:
                mPresenter.setLayerVisible(2,isChecked);
                break;
            case R.id.layercontrol_toggle_zx_sl:
                mPresenter.setLayerVisible(1,isChecked);
                break;
            case R.id.layercontrol_toggle_zx_yx:
                mPresenter.setLayerVisible(0,isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSingleTap(float v, float v1) {
        mPresenter.clickVectorLayer(v,v1);
    }
}
