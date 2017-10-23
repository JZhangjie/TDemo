package cn.xinxizhan.test.tdemo.controls.container;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import cn.xinxizhan.test.tdemo.components.BasePresenter;
import cn.xinxizhan.test.tdemo.components.BaseView;

/**
 * Created by admin on 2017/10/12.
 */

public interface ContainerContract {

    interface Presenter extends BasePresenter{

        void add(Fragment fragment,String key,Boolean showinbar);

        void add(Fragment fragment,String key);

        void show(String key);

        void show();

        void close();

    }

    interface View extends BaseView<ContainerContract.Presenter>{

        ArrayAdapter<String> getTitleAdapter();

        void addFragment(Fragment fragment);

        void showFragment(Fragment fragment);

        void changeSpinnerselected(String key);

        void show();

        void hideFragment(Fragment fragment);

        void hide();


    }

    interface Callback {
        void containerTopButtonClick(String key);
    }

}
