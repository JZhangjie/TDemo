package cn.xinxizhan.test.tdemo.components.userlogin;

import cn.jdz.glib.components.BasePresenter;
import cn.jdz.glib.components.BaseView;
import cn.xinxizhan.test.tdemo.data.model.User;

/**
 * Created by admin on 2017/10/13.
 */

public interface UserloginContract {

    interface Presenter extends BasePresenter {

        void login();

        User getLastLoginUser();

        void rememberPassword();

    }

    interface View extends BaseView<Presenter> {

        User getUserFromView();

        void showToastMessage(String message);

        void showLoginProgress();

        void hideLoginProgress();

        void setLastUser(User lastUser);

        void setRememberPassword(Boolean remember);

    }

    interface Callback {

        void initHistoryUsers();

        User getLastLoginUser();

        void logined();

    }

}
