package cn.jdz.glib.capture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.io.File;

import cn.jdz.glib.R;

/**
 * 照片预览
 */
public class ImageShowActivity extends AppCompatActivity  {

    public static final String IMAGEPATH="cn.jdz.glib.capture.ImageShowActivity.currentimagepath";
    private String mImagePath ;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_show);
        mImageView = (ImageView) findViewById(R.id.image_show_image);
        mImagePath = getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
        mImageView.setImageBitmap(bitmap);

    }

    private String getImagePath(){
        return getIntent().getStringExtra(IMAGEPATH);
    }

}
