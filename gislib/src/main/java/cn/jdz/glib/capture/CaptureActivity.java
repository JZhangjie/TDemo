package cn.jdz.glib.capture;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.jdz.glib.R;
import cn.jdz.glib.sensor.location.AndroidLocation;
import cn.jdz.glib.sensor.location.ILocation;
import cn.jdz.glib.sensor.ISensor;
import cn.jdz.glib.sensor.OrientationSensor;
import cn.jdz.glib.utils.ImgHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 拍照
 * 实现坐标、方位角记录，二者可选择REQUEST(必填，获取不到时无法拍照）、INPUT(传入二者的信息记录）、NONE(不记录二者的信息）、OPTIONAL(获取到则记录，未获取到则不记录)
 *
 */
public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CAPTURE_RESULT_CODE=10000;
    public static final int ACCESS_LOCATION_REQUEST=10001;
    public static final String LOCATION = "location_status";
    public static final String ORIENTATION = "orientation_status";
    public static final String LOCATION_INPUT_VALUE = "location_input_value";
    public static final String ORIENTATION_INPUT_VALUE = "orientation_input_value";

    private static final String TAG="gotoCaptureView";
    private static final File DEFAULTPATH = new File("/sdcard/pictures");
    private CaptureConfigStatus mLocationStatus = CaptureConfigStatus.NONE;
    private CaptureConfigStatus mOrientationStatus = CaptureConfigStatus.NONE;
    private TextView mOrientationTv;
    private TextView mLocationTv;
    private CaptureView mCaptureView;
    private View mCapture;
    private View mShow;
    private View mPostioning;
    private ImageView mPositioningImg;
    private ILocation mLocation;
    private ISensor mSensor;
    private Location mCurrentLocation;
    private float[] mCurrentOrientation;
    private File imageFile;
    private Bitmap tempImageData;
    private boolean isPositioning=false;

    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        getDataFromIntent(getIntent());
        //判断是否需要定位
        if(mLocationStatus != CaptureConfigStatus.NONE){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_LOCATION_REQUEST);
            }
            else {
                mLocation = new AndroidLocation(this,mLocationListener);
                mLocation.start();
            }
            mLocationTv= (TextView) findViewById(R.id.capture_location);
        }
        //判断是否需要方位
        if(mOrientationStatus != CaptureConfigStatus.NONE){
            mSensor = new OrientationSensor(this,mSensorEventListener);
            mOrientationTv = (TextView) findViewById(R.id.capture_orientation);
        }

        mCaptureView = (CaptureView) findViewById(R.id.capture_captureview);
        mCapture = findViewById(R.id.capture_capture);
        mShow = findViewById(R.id.capture_show);
        mPostioning= findViewById(R.id.capture_positioning);
        mPositioningImg = (ImageView)findViewById(R.id.capture_positioning_img);

        //动画初始化
        mAnimator = ObjectAnimator.ofFloat(mPositioningImg,"translationY",0f,-30f)
                .setDuration(500);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocation!=null)
            mLocation.stop();
        if(mSensor!=null)
            mSensor.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLocation!=null)
            mLocation.start();
        if(mSensor!=null)
            mSensor.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == ACCESS_LOCATION_REQUEST){
            mLocation = new AndroidLocation(this,mLocationListener);
            mLocation.start();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //确认
        if(id == R.id.capture_show_ok){
            capturePicture();
        }
        //取消
        if(id == R.id.capture_show_cancel){
            gotoCaptureView();
        }
        //拍照
        if (id == R.id.capture_capture_btn) {
            capture();
        }
    }

    /**
     * 获取使用通过intent传入的参数
     * @param intent
     */
    private void getDataFromIntent(Intent intent){
        this.imageFile = null;
        this.mCurrentLocation = null;
        this.mCurrentOrientation = null;

        Object gps = intent.getExtras().get(LOCATION);
        if(gps != null && gps instanceof CaptureConfigStatus){
            mLocationStatus = (CaptureConfigStatus)gps;
        }
        Object ori = intent.getExtras().get(ORIENTATION);
        if(ori != null && ori instanceof CaptureConfigStatus){
            mOrientationStatus = (CaptureConfigStatus) ori;
        }

        Object gps_value = intent.getExtras().get(LOCATION_INPUT_VALUE);
        if(gps != null && gps instanceof Location){
            mCurrentLocation = (Location)gps_value;
        }
        Object ori_value = intent.getExtras().get(ORIENTATION_INPUT_VALUE);
        if(ori != null && ori instanceof float[]){
            mCurrentOrientation = (float[]) ori_value;
        }

        Object o = intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
        if(o instanceof File){
            this.imageFile = (File) o;
            return;
        }
        else if(o instanceof Uri) {
            //TO-DO 使用URI进行保存路径传递。与系统相机保持一致。
            Uri uri = (Uri)o;
            this.imageFile = new File(uri.getPath());
        }
    }

    /**
     * 确认，保存照片
     */
    private void capturePicture(){
        if(mLocation!=null)
            mLocation.stop();
        if(mSensor!=null)
            mSensor.stop();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Integer> e) throws Exception {
                saveImage(tempImageData);
                addImageExtra(imageFile);
                e.onNext(1);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer o) throws Exception {
                        Toast.makeText(CaptureActivity.this, "保存在："+imageFile.getPath(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageFile.getPath());
                        intent.putExtra(CaptureActivity.LOCATION,mCurrentLocation);
                        intent.putExtra(CaptureActivity.ORIENTATION,mCurrentOrientation);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });
    }

    /**
     * 保存照片
     * @param bitmap
     */
    private void saveImage(Bitmap bitmap){
        File newfile=null;
        if(imageFile==null) {
            newfile = new File(DEFAULTPATH.getPath() + "//" + (new Date()).toString() + ".jpg");
        }
        else{
            newfile = imageFile;
        }
        imageFile = newfile;
        try {
            FileOutputStream fos = new FileOutputStream(newfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加GPS和方位角信息
     * @param file
     */
    private void addImageExtra(File file){
        if(mOrientationStatus != CaptureConfigStatus.NONE && mCurrentOrientation != null){
            ImgHelper.setOrientation(file.getPath(),mCurrentOrientation);
        }
        if(mLocationStatus != CaptureConfigStatus.NONE && mCurrentLocation != null){
            ImgHelper.setGPS(file.getPath(),mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }
    }

    /**
     * 照片预览
     * @param data
     */
    private void showImage(byte[] data){
        Bitmap bitmap = makebitmatp(BitmapFactory.decodeByteArray(data,0,data.length));
        tempImageData = bitmap;
        mCapture.setVisibility(View.GONE);
        mShow.setVisibility(View.VISIBLE);
    }

    /**
     * 拍照按钮点击响应
     */
    private void capture(){
        if (mLocationStatus == CaptureConfigStatus.REQUEST && !mLocation.isWorking()){
            Toast.makeText(this, "GPS不可用。", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mOrientationStatus == CaptureConfigStatus.REQUEST && !mSensor.isWorking()){
            Toast.makeText(this, "传感器不可用。", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mLocationStatus == CaptureConfigStatus.REQUEST) {
            Location l = (Location)mLocation.getValues();
            if(l==null){
                Toast.makeText(this, "正在定位...", Toast.LENGTH_SHORT).show();
                if(mAnimator !=null)
                    mAnimator.start();
                mPostioning.setVisibility(View.VISIBLE);
                return;
            }
            mCurrentLocation =l;
        }
        else if(mLocationStatus == CaptureConfigStatus.OPTIONAL) {
            Location l = (Location)mLocation.getValues();
            if(l==null && !isPositioning){
                Toast.makeText(this, "正在定位,若不需坐标信息，可再次点击拍照...", Toast.LENGTH_LONG).show();
                if(mAnimator !=null)
                    mAnimator.start();
                mPostioning.setVisibility(View.VISIBLE);
                isPositioning=true;
                return;
            }
            if(isPositioning) {
                mPostioning.setVisibility(View.GONE);
                if(mAnimator.isRunning()){
                    mAnimator.pause();
                }
                isPositioning=false;
            }
            mCurrentLocation =l;
        }
        if(mOrientationStatus == CaptureConfigStatus.REQUEST){
            float[] o = (float[])mSensor.getValues();
            if(o == null && mOrientationStatus == CaptureConfigStatus.REQUEST){
                Toast.makeText(this, "正在获取传感器参数...", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                mCurrentOrientation = o;
            }
        }

        mCaptureView.capture(mShutter,mRaw,jpeg);
    }

    //重新跳转到拍照界面,取消按钮
    private void gotoCaptureView(){
        if(mLocation!=null)
            mLocation.start();
        if(mSensor!=null)
            mSensor.start();
        tempImageData = null;
        mCaptureView.restart();
        mCapture.setVisibility(View.VISIBLE);
        mShow.setVisibility(View.GONE);
    }

    //旋转拍摄的结果
    private Bitmap makebitmatp(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap result = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return result;
    }

    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mRaw = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            showImage(data);
        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocationTv.setText(location.toString());
            isPositioning = false;
            if(mCurrentLocation == null){
                mCurrentLocation=location;
                Toast.makeText(CaptureActivity.this, "坐标已获取，可以开始拍照！", Toast.LENGTH_SHORT).show();
                mPostioning.setVisibility(View.GONE);
                if(mAnimator.isRunning()){
                    mAnimator.pause();
                }
            }
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
    };

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mOrientationTv.setText("z:"+event.values[0]+"\nx:"+event.values[1]+"\ny:"+event.values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
