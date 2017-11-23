package cn.xinxizhan.test.tdemo.activity;

import android.content.DialogInterface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.esri.core.map.Graphic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.jdz.glib.components.container.ContainerContract;
import cn.jdz.glib.components.container.ContainerPresenter;
import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.components.casedetail.CaseDetailContract;
import cn.xinxizhan.test.tdemo.components.casedetail.CaseDetailFragment;
import cn.xinxizhan.test.tdemo.components.casedetail.CaseDetailPresenter;
import cn.xinxizhan.test.tdemo.components.caselist.CaseListContract;
import cn.xinxizhan.test.tdemo.components.caselist.CaseListFragment;
import cn.xinxizhan.test.tdemo.components.caselist.CaseListPresenter;
import cn.xinxizhan.test.tdemo.components.dblist.DBListContract;
import cn.xinxizhan.test.tdemo.components.dblist.DBListFragment;
import cn.xinxizhan.test.tdemo.components.dblist.DBListPresenter;
import cn.xinxizhan.test.tdemo.components.map.MapContract;
import cn.xinxizhan.test.tdemo.components.map.MapPresenter;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;

import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.data.model.DBFile;

import cn.xinxizhan.test.tdemo.utils.FileHelper;
import cn.xinxizhan.test.tdemo.utils.PermissionHelper;
import cn.xinxizhan.test.tdemo.utils.XMLHelper;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout mDrawer;
    NavigationView mNavigationView;
    TextView mUserName;

    private MapContract.Presenter mMapPresenter;
    private ContainerContract.Presenter mContainerPresenter;
    private DBListContract.Presenter mDBListPresenter;
    private CaseDetailContract.Presenter mCaseDetailPresenter;
    private CaseListContract.Presenter mCaseListPresenter;

    @Override
    public void onBackPressed() {
        if(mDBListPresenter!=null && mDBListPresenter.backPressed()){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("退出");
        builder.setMessage("是否要返回登录界面");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionHelper.getPermission(this);
        initConstant();
        initView();
        initComponents();
    }

    private void initView(){
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mUserName = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.usernameText);
        mUserName.setText(ApplicationConstants.USER.getAliasname());
    }

    /**
     * 初始化各组件
     */
    private void initComponents(){
        MapContract.View mapview = (MapContract.View)getSupportFragmentManager().findFragmentById(R.id.main_map);
        mMapPresenter = new MapPresenter(mapview,mapCallback);
        mMapPresenter.start();
        ContainerContract.View containerview = (ContainerContract.View)getSupportFragmentManager().findFragmentById(R.id.main_container);
        mContainerPresenter = new ContainerPresenter(containerview,containerCallback);
        mContainerPresenter.start();

        DBListFragment dblistFragment = new DBListFragment();
        mDBListPresenter = new DBListPresenter(dblistFragment,dbListCallback);
        CaseDetailFragment caseFragment= new CaseDetailFragment();
        mCaseDetailPresenter = new CaseDetailPresenter(caseFragment,caseDetailCallback);
        CaseListFragment caseListFragment = new CaseListFragment();
        mCaseListPresenter = new CaseListPresenter(caseListFragment,caseListCallback);

        mContainerPresenter.add(caseFragment,ApplicationConstants.CASEDETAIL);
        mContainerPresenter.add(caseListFragment,ApplicationConstants.CASELIST);
        mContainerPresenter.add(dblistFragment,ApplicationConstants.FILELIST);
    }

    /**
     * 初始化常量数据，复制assets文件，初始化地类编码
     */
    private void initConstant(){
        try {
            FileHelper.copyFiles("dlbm", FileHelper.getRootPath()+"/"+ApplicationConstants.XMLPATH,this);

            File cc = FileHelper.getPath("dlbm/CC.xml");
            File gd = FileHelper.getPath("dlbm/GD.xml");
            File fgd = FileHelper.getPath("dlbm/FGD.xml");
            ApplicationConstants.CCBMList = XMLHelper.getCodesFromXML(cc);
            ApplicationConstants.GDBMList = XMLHelper.getCodesFromXML(gd);
            ApplicationConstants.FGDBMList = XMLHelper.getCodesFromXML(fgd);
            if (ApplicationConstants.CCBMList == null) {
                ApplicationConstants.CCBMList = new ArrayList<>();
                Toast.makeText(this, "CC.xml文件不存在", Toast.LENGTH_SHORT).show();
            }
            if( ApplicationConstants.GDBMList == null ) {
                ApplicationConstants.GDBMList = new ArrayList<>();
                Toast.makeText(this, "GD.xml文件不存在", Toast.LENGTH_SHORT).show();
            }
            if(ApplicationConstants.FGDBMList == null) {
                ApplicationConstants.FGDBMList = new ArrayList<>();
                Toast.makeText(this, "FGD.xml文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navi:
                mDrawer.openDrawer(Gravity.LEFT);
                break;
        }
    }

    //region Presenter回调

    private CaseDetailContract.Callback caseDetailCallback = new CaseDetailContract.Callback() {
        @Override
        public void zoomToCurrentCase(Graphic graphic) {
            mMapPresenter.zoomTo(graphic.getGeometry());
        }

        @Override
        public void showCaseList() {
            mContainerPresenter.show(ApplicationConstants.CASELIST);
        }

        @Override
        public Location getLocation() {
            return mMapPresenter.getCurrentLocation();
        }

    };

    private CaseListContract.Callback caseListCallback = new CaseListContract.Callback() {

        @Override
        public void onCaseClick(DBCase dbCase) {
            mMapPresenter.zoomTo(dbCase.getGraphic().getGeometry());
            mCaseDetailPresenter.setCurrentModel(dbCase);
            mContainerPresenter.show(ApplicationConstants.CASEDETAIL);
        }

    };

    private DBListContract.Callback dbListCallback = new DBListContract.Callback() {
        @Override
        public void onDBListToggleClick(DBFile dbFile) {
            mMapPresenter.changeVectorLayer(dbFile);
            mCaseListPresenter.setCurrentDBFile(dbFile);
            mCaseDetailPresenter.setCurrentModel(dbFile);

            if(dbFile !=null){
                DBCase dbCase = dbFile.getDefaultCase();
                mMapPresenter.zoomTo(dbCase.getGraphic().getGeometry());
            }
        }

        @Override
        public void onDBListDeleteClick(DBFile dbFile) {
            mMapPresenter.changeVectorLayer(null);
        }

    };

    private MapContract.Callback mapCallback = new MapContract.Callback() {
        @Override
        public void onGraphicClick(int graphicId) {
            mCaseDetailPresenter.setCurrentModel(graphicId);
            mContainerPresenter.show(ApplicationConstants.CASEDETAIL);
        }
    };

    private ContainerContract.Callback containerCallback = new ContainerContract.Callback() {
        @Override
        public void containerTopButtonClick(String key) {

        }

    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_filelist:
                mContainerPresenter.show(ApplicationConstants.FILELIST);
                break;
            case R.id.nav_tblist:
                mContainerPresenter.show(ApplicationConstants.CASELIST);
                break;
            case R.id.nav_tbdetail:
                mContainerPresenter.show(ApplicationConstants.CASEDETAIL);
                break;
        }
        mDrawer.closeDrawer(Gravity.LEFT);
        return false;
    }

    //endregion

}
