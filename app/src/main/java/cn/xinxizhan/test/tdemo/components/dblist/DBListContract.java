package cn.xinxizhan.test.tdemo.components.dblist;

import java.util.List;

import cn.jdz.glib.components.BasePresenter;
import cn.jdz.glib.components.BaseView;
import cn.xinxizhan.test.tdemo.data.model.DBFile;

/**
 * Created by admin on 2017/10/10.
 */

public interface DBListContract {

    interface Presenter extends BasePresenter {

        void getDBFiles();

        void downloadDBFile(DBFile dbFile);

        void toggleStatusDBFile(DBFile dbFile);

        void deleteDBFile(DBFile dbFile);

        void uploadDBFile(DBFile dbFile);

        boolean backPressed();

    }

    interface View extends BaseView<Presenter> {

        void setAdapterData(List<DBFile> dbfileList);

        void stopRefresh();

        void refreshAdapter();

        void showToastMessage(String message);

        void startProgressbar(String message);

        void stopProgressbar();

        boolean backPressed();

    }

    interface Callback{

        void onDBListToggleClick(DBFile dbFile);

        void onDBListDeleteClick(DBFile dbFile);

    }

}
