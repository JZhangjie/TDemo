package cn.jdz.glib.capture;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 17-11-13.
 */

public class CaptureView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG="captureview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CaptureView(Context context) {
        super(context);
        init();
    }

    public CaptureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CaptureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        Log.d(TAG, "init: 初始化相机预览界面");
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "init: 创建相机预览界面");
        if(mCamera==null){
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "init: 开启相机预览界面");
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "init: 销毁相机预览界面");
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照，调用camera的takePicture功能
     * @param shutter
     * @param raw
     * @param jpeg
     */
    public void capture(final Camera.ShutterCallback shutter, final Camera.PictureCallback raw,
                        final Camera.PictureCallback jpeg){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.takePicture(shutter,raw,jpeg);
                }
            }
        });
    }

    public void restart(){
        mCamera.startPreview();
    }

    /**
     * 初始化相机，调整预览图方向，防止变形。
     */
    private void initCamera() {
        Camera.Parameters parameters = mCamera.getParameters();//获取camera的parameter实例
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        Camera.Size optionSize = getOptimalPreviewSize(sizeList, this.getWidth(), this.getHeight());//获取一个最为适配的camera.size
        parameters.setPreviewSize(optionSize.width,optionSize.height);//把camera.size赋值到parameters
        mCamera.setParameters(parameters);//把parameters设置给camera
        mCamera.startPreview();//开始预览
        mCamera.setDisplayOrientation(90);//将预览旋转90度
    }

    /**
     * 获取支持的相机尺寸，选择合适的作为当前尺寸
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
