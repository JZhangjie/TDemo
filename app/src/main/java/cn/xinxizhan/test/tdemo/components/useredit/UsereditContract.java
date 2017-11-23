package cn.xinxizhan.test.tdemo.components.useredit;

import cn.jdz.glib.components.BasePresenter;
import cn.jdz.glib.components.BaseView;

/**
 * Created by admin on 2017/10/26.
 */

public interface UsereditContract {

    interface Presenter extends BasePresenter {

        boolean checkInput();

        void savePassword();

        void cancel();

    }

    interface View extends BaseView<Presenter> {

        String getOldPassword();

        String getNewPassword();

        String getCheckPassword();

        void showToastMessage(String message);

    }

    interface Callback{

        void onCancel();

    }

}
