package cn.jdz.glib.components.container;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/12.
 */

public class ContainerPresenter implements ContainerContract.Presenter
{
    private ContainerContract.View mView;
    private ContainerContract.Callback mCallback;
    private List<Fragment> mFragments;
    private List<String> mKeys;
    private ArrayAdapter<String> mTitleAdapter;
    private boolean isShow;
    private boolean isFirst;

    public ContainerPresenter(ContainerContract.View view,ContainerContract.Callback callback){
        this.mView = view;
        this.mCallback = callback;
        this.mView.setPresenter(this);
        mTitleAdapter = this.mView.getTitleAdapter();
        isFirst =true;
    }

    @Override
    public void start() {
        if(isFirst) {
            init();
            isFirst=false;
        }
    }

    private void init(){
        mFragments = new ArrayList<>();
        mKeys = new ArrayList<>();
        isShow = true;
    }

    @Override
    public void add(Fragment fragment, String key, Boolean showinbar) {
        this.mFragments.add(fragment);
        this.mKeys.add(key);
        if(showinbar){
            this.mTitleAdapter.add(key);
        }
        this.mView.addFragment(fragment);
        show(key);
    }

    @Override
    public void add(Fragment fragment, String key) {
        add(fragment,key,true);
    }

    @Override
    public void show(String key) {
        if(this.mKeys.contains(key)){
            for(Fragment f:this.mFragments){
                this.mView.hideFragment(f);
            }
            Fragment fragment = this.mFragments.get(this.mKeys.indexOf(key));
            this.mView.showFragment(fragment);
            this.mView.changeSpinnerselected(key);
            show();
        }
    }

    @Override
    public void show() {
        if(!isShow) {
            this.mView.show();
            isShow=true;
        }
    }

    @Override
    public void close() {
        if(isShow) {
            this.mView.hide();
            isShow=false;
        }
    }
}
