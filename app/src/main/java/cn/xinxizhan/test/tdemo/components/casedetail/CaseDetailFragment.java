package cn.xinxizhan.test.tdemo.components.casedetail;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.io.File;

import cn.jdz.glib.capture.CaptureActivity;
import cn.jdz.glib.capture.CaptureConfigStatus;
import cn.jdz.glib.capture.ImageShowActivity;
import cn.jdz.glib.data.KeyValueItem;
import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.controls.dlbmpopup.DLBMTreeFragment;
import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.utils.StringHelper;
import cn.xinxizhan.test.tdemo.utils.ViewHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseDetailFragment extends Fragment implements CaseDetailContract.View,View.OnClickListener, View.OnLongClickListener {

    //region 界面对象

    private TextView bsm;
    private TextView xzqmc;
    private TextView cc;
    private TextView dlbm;
    private RadioGroup sfgd;
    private RadioButton sfgd_1; //是
    private RadioButton sfgd_2; //否
    private RadioGroup sflh;
    private RadioButton sflh_1;
    private RadioButton sflh_2;
    private ImageButton location;
    private ImageButton caseList;
    private TextView dcr;
    private TextView dcsj;
    private Button hc;
    private FlexboxLayout mImageBox;
    private ImageButton mAddImage;
    private TextView memo;

    //endregion

    private CaseDetailContract.Presenter mPresenter;
    private File tempImageFile;
    public static final int CAPTURESUCCESS=100;

    public CaseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURESUCCESS) {
            mPresenter.addImage(tempImageFile);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_case_detail, container, false);

        initView(view);

        return view;
    }

    private void initView(View mView){
        bsm = (TextView) mView.findViewById(R.id.attr_edit_fid);
        xzqmc = (TextView) mView.findViewById(R.id.attr_edit_xzqmc);
        dcr = (TextView) mView.findViewById(R.id.attr_edit_dcr);
        dcsj = (TextView) mView.findViewById(R.id.attr_edit_dcsj);
        cc = (TextView) mView.findViewById(R.id.attr_edit_cc);
        memo = (TextView) mView.findViewById(R.id.attr_edit_memo);
        dlbm = (TextView) mView.findViewById(R.id.attr_edit_dlbm);
        dlbm.setOnClickListener(this);
        sfgd = (RadioGroup) mView.findViewById(R.id.attr_edit_sfgd);
        sfgd_1 = (RadioButton) mView.findViewById(R.id.attr_edit_sfgd_rbt1);
        sfgd_2 = (RadioButton) mView.findViewById(R.id.attr_edit_sfgd_rbt2);
        sflh = (RadioGroup) mView.findViewById(R.id.attr_edit_sflh);
        sflh_1 = (RadioButton) mView.findViewById(R.id.attr_edit_sflh_rbt1);
        sflh_2 = (RadioButton) mView.findViewById(R.id.attr_edit_sflh_rbt2);
        hc = (Button) mView.findViewById(R.id.attr_edit_hc);
        mAddImage = (ImageButton) mView.findViewById(R.id.attr_edit_pictureadd);
        mImageBox = (FlexboxLayout) mView.findViewById(R.id.attr_edit_imagebox);
        location = (ImageButton) mView.findViewById(R.id.attr_edit_location);
        caseList = (ImageButton) mView.findViewById(R.id.attr_edit_caselist);
        location.setOnClickListener(this);
        caseList.setOnClickListener(this);
        hc.setOnClickListener(this);
        mAddImage.setOnClickListener(this);
        sfgd_1.setOnClickListener(this);
        sfgd_2.setOnClickListener(this);
        sflh_1.setOnClickListener(this);
        sflh_2.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.mPresenter !=null)
            this.mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setPresenter(CaseDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCase(DBCase showDBCase) {
        DBCase mCase = showDBCase;
        this.bsm.setText(mCase == null ? "" : mCase.getBsm() + "");
        this.xzqmc.setText(mCase == null ? "" : mCase.getXzqmc());

        this.dcr.setText(mCase==null ? "" : mPresenter.getDCRAliasname());

        this.dcsj.setText(mCase == null ? "" : StringHelper.dateToDb(mCase.getDcsj()));
        this.memo.setText(mCase == null ? "" : mCase.getMemo());
        if(mCase == null || mCase.getSfgd()==0){
            this.sfgd_2.setChecked(true);
        }
        else
        {
            this.sfgd_1.setChecked(true);
        }
        if( mCase==null || mCase.getSflh()==0){
            this.sflh_2.setChecked(true);
        }
        else
        {
            this.sflh_1.setChecked(true);
        }
        //土地类型
        this.dlbm.setText(mCase==null?"":mPresenter.getDLMCFromDLBM(mCase.getSjcc()));
        //CC码显示
        this.cc.setText(mCase==null?"":mPresenter.getDLMCFromDLBM(mCase.getCc()));
    }

    @Override
    public DBCase getCaseFromView() {
        DBCase mCaseModel = new DBCase();
        mCaseModel.setBsm(bsm.getText().toString());

        mCaseModel.setMemo(this.memo.getText().toString());
        mCaseModel.setSfgd(this.sfgd.getCheckedRadioButtonId() == R.id.attr_edit_sfgd_rbt1 ? 1 : 0);
        mCaseModel.setSflh(this.sflh.getCheckedRadioButtonId() == R.id.attr_edit_sflh_rbt1 ? 1 : 0);
        String value = this.dlbm.getText().toString().trim();
        String code = mPresenter.getDLBMFromDLMC(value,mCaseModel );
        if(code.equals(value))
            mCaseModel.setSjcc("");
        else
            mCaseModel.setSjcc(code);

        return mCaseModel;
    }

    @Override
    public void capture(File file) {
        this.tempImageFile = file;
        //使用系统相机
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (Build.VERSION.SDK_INT < 24) {
//            //7.0之后不允许这样调用
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        } else {
//            ContentValues contentValues = new ContentValues(1);
//            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
//            Uri uri = this.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }
//        startActivityForResult(intent, CAPTURESUCCESS);

        //使用自定义的相机
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,this.tempImageFile);
        intent.putExtra(CaptureActivity.LOCATION, CaptureConfigStatus.REQUEST);
        intent.putExtra(CaptureActivity.ORIENTATION, CaptureConfigStatus.REQUEST);
        startActivityForResult(intent, CAPTURESUCCESS);
    }

    @Override
    public void addImageView(Bitmap bitmap,File file) {
        ImageView iv = new ImageView(this.getActivity());

        iv.setImageBitmap(bitmap);
        iv.setBackgroundResource(R.drawable.bg_border);
        iv.setOnLongClickListener(this);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof ImageView) {
                    File file = (File)v.getTag();
                    if(file != null)
                        showImage(file);
                }
            }
        });
        iv.setTag(file);
        int w = ViewHelper.getPixelsFromDp(60, this.getActivity());
        int m = ViewHelper.getPixelsFromDp(3, this.getActivity());
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(w, w);
        params.topMargin = m;
        params.leftMargin = m;
        mImageBox.addView(iv, 0, params);
    }

    @Override
    public void clearImages() {
        if(mImageBox !=null){
            mImageBox.removeAllViews();
            mImageBox.addView(mAddImage);
        }
    }

    private void save(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("核查");
        builder.setMessage("是否要核查并保存该图斑");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.saveDBCase();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.showDBCase();
            }
        });
        builder.show();
    }

    private void deleteImage(final ImageView view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("图片");
        builder.setMessage("是否要删除图片");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = (File)view.getTag();
                if(file!=null && file.exists()){
                    file.delete();
                }
                mImageBox.removeView(view);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void selectDBLM(){
        DLBMTreeFragment dialog = new DLBMTreeFragment();
        dialog.setCallback(mDLBMTreeFragmentListener);
        if(sfgd.getCheckedRadioButtonId()==R.id.attr_edit_sfgd_rbt1)
        {
            dialog.setList(ApplicationConstants.GDBMList);
            dialog.show(this.getActivity().getSupportFragmentManager(),"耕地");
        }
        else
        {
            dialog.setList(ApplicationConstants.FGDBMList);
            dialog.show(this.getActivity().getSupportFragmentManager(),"非耕地");
        }
    }

    private void showImage(File file){
        Intent intent = new Intent(this.getActivity(),ImageShowActivity.class);
        intent.putExtra(ImageShowActivity.IMAGEPATH, file.getPath());
        startActivity(intent);
    }

    private DLBMTreeFragment.DLBMTreeFragmentCallback mDLBMTreeFragmentListener = new DLBMTreeFragment.DLBMTreeFragmentCallback() {
        @Override
        protected void chooseItem(KeyValueItem item) {

            CaseDetailFragment.this.dlbm.setTag(item);
            CaseDetailFragment.this.dlbm.setText(item.getValue());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attr_edit_hc:
                save();
                break;
            case R.id.attr_edit_pictureadd:
                mPresenter.capture();
                break;
            case R.id.attr_edit_location:
                mPresenter.zoomToCurrentCase();
                break;
            case R.id.attr_edit_caselist:
                mPresenter.showCaseList();
                break;
            case R.id.attr_edit_dlbm:
                selectDBLM();
                break;
            case R.id.attr_edit_sfgd_rbt1:
            case R.id.attr_edit_sfgd_rbt2:
                this.dlbm.setText("");
                this.dlbm.setTag(null);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v instanceof ImageView) {
            deleteImage((ImageView)v);
        }
        return false;
    }
}
