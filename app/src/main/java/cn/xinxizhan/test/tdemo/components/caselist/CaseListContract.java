package cn.xinxizhan.test.tdemo.components.caselist;

import java.util.List;

import cn.xinxizhan.test.tdemo.components.BasePresenter;
import cn.xinxizhan.test.tdemo.components.BaseView;
import cn.xinxizhan.test.tdemo.components.casedetail.CaseDetailContract;
import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.data.model.DBFile;

/**
 * Created by admin on 2017/10/16.
 */

public interface CaseListContract {

    interface Presenter extends BasePresenter{

        void setCurrentDBFile(DBFile dbFile);

        void showFilterCaseList(CaselistFilterType filterType);

        void clickCase(DBCase dbCase);

    }

    interface View extends BaseView<CaseListContract.Presenter>{

        void setAdapterData(List<DBCase> caseList);

        void setFilterType(CaselistFilterType filterType);

    }

    interface Callback{

        void onCaseClick(DBCase dbCase);

    }

}
