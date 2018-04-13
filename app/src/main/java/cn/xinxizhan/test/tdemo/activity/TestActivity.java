package cn.xinxizhan.test.tdemo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jdz.glib.capture.CaptureActivity;
import cn.jdz.glib.capture.CaptureConfigStatus;
import cn.jdz.glib.controls.ExpandContainer;
import cn.jdz.glib.controls.ImageSwitcherFragment;
import cn.jdz.glib.controls.imagegallery.ImageGalleryFragment;
import cn.jdz.glib.controls.imageviewpager.ImageViewPagerAdapter;
import cn.jdz.glib.controls.imageviewpager.ImageViewPagerFragment;
import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.utils.PermissionHelper;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{
    ImageViewPagerFragment fragment;
    File temp;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Object l = data.getExtras().get(CaptureActivity.LOCATION);
            Object o = data.getExtras().get(CaptureActivity.ORIENTATION);

            Location location = (Location) l ;
            float[]  orientation = (float[]) o;
            int a ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        PermissionHelper.getPermission(TestActivity.this);

        fragment = (ImageViewPagerFragment) getSupportFragmentManager().findFragmentById(R.id.test_imageshow);
        File files = new File("/sdcard/GeoStarPatrol/image/0");
        List<File> images = Arrays.asList(files.listFiles());
        temp = images.get(0);
        fragment.setAdapter(images);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_capture:
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String photoPath = "/sdcard/abc.jpg";
//                File file = new File(photoPath);
//                Uri mOutPutFileUri = Uri.fromFile(file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
//                TestActivity.this.startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,new File(photoPath));
                intent.putExtra(CaptureActivity.LOCATION, CaptureConfigStatus.REQUEST);
                intent.putExtra(CaptureActivity.ORIENTATION, CaptureConfigStatus.NONE);
                startActivityForResult(intent, 1);
        }
    }
}
