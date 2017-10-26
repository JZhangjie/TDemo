package cn.xinxizhan.test.tdemo.components.dblist;

import java.util.List;

import cn.xinxizhan.test.tdemo.components.BasePresenter;
import cn.xinxizhan.test.tdemo.components.BaseView;
import cn.xinxizhan.test.tdemo.components.dblist.adapter.DBListAdapter;
import cn.xinxizhan.test.tdemo.components.map.MapContract;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import io.reactivex.Observable;

/**
 * Created by admin on 2017/10/10.
 */

public interface DBListContract {

    interface Presenter extends BasePresenter{

        void getDBFiles();

        void downloadDBFile(DBFile dbFile);

        void toggleStatusDBFile(DBFile dbFile);

        void deleteDBFile(DBFile dbFile);

        void uploadDBFile(DBFile dbFile);

        boolean backPressed();

    }

    interface View extends BaseView<DBListContract.Presenter>{

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
