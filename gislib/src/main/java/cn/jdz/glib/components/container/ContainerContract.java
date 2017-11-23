package cn.jdz.glib.components.container;

import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import cn.jdz.glib.components.BasePresenter;
import cn.jdz.glib.components.BaseView;


/**
 * Created by admin on 2017/10/12.
 */

public interface ContainerContract {

    interface Presenter extends BasePresenter {

        void add(Fragment fragment, String key, Boolean showinbar);

        void add(Fragment fragment, String key);

        void show(String key);

        void show();

        void close();

    }

    interface View extends BaseView<Presenter> {

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
