package cn.xinxizhan.test.tdemo.activity;

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

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.utils.FileHelper;

public class ImageShowActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {
    public static final String IMAGEPATH="cn.xinxizhan.test.tdemo.activity.ImageShowActivity.currentimagepath";
    private String mImagePath= FileHelper.getPath("/b.jpg").getPath();
    private ImageSwitcher mImageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_show);
        mImagePath = getImagePath();
        mImageSwitcher = (ImageSwitcher)findViewById(R.id.image_show_imageswitcher);
        mImageSwitcher.setFactory(this);
        mImageSwitcher.setImageURI(Uri.fromFile(new File(mImagePath)));
    }

    private String getImagePath(){
        return getIntent().getStringExtra(IMAGEPATH);
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return i ;
    }
}
