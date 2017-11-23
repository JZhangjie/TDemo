package cn.xinxizhan.test.tdemo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import cn.jdz.glib.capture.CaptureActivity;
import cn.jdz.glib.capture.CaptureConfigStatus;
import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.utils.PermissionHelper;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        PermissionHelper.getPermission(TestActivity.this);
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
                intent.putExtra(CaptureActivity.LOCATION, CaptureConfigStatus.OPTIONAL);
                intent.putExtra(CaptureActivity.ORIENTATION, CaptureConfigStatus.NONE);
                startActivityForResult(intent, 1);
        }
    }
}
