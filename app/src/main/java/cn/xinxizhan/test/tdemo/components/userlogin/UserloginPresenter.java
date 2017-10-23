package cn.xinxizhan.test.tdemo.components.userlogin;

import java.util.HashMap;

import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.model.User;
import cn.xinxizhan.test.tdemo.utils.HttpHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/10/13.
 */

public class UserloginPresenter implements UserloginContract.Presenter {

    private User lastUser;
    private UserloginContract.View mView;
    private UserloginContract.Callback mCallback;
    private boolean isFirst = true;

    public UserloginPresenter(UserloginContract.View view,UserloginContract.Callback callback){
        this.mView = view;
        this.mCallback = callback;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        if(isFirst){
            this.lastUser = mCallback.getLastLoginUser();
            mCallback.initHistoryUsers();
        }
        if(this.lastUser!=null)
            this.mView.setLastUser(this.lastUser);
    }

    @Override
    public void login() {
        final User user = this.mView.getUserFromView();
        if(user==null || user.getUsername()==null || user.getUsername()==""){
            this.mView.showToastMessage("用户名不能为空");
            return;
        }
        mView.showLoginProgress();
        User.login(ApplicationConstants.LOGINURL,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull User user) {
                        if(user==null){
                            mView.showToastMessage("用户名或密码错误");
                            return;
                        }
                        User.getUserInfo(user.getUserInfoLink(),user)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<User>() {
                                    @Override
                                    public void accept(@NonNull User user) throws Exception {
                                        User.getBranchInfo(user.getBranchInfoLink(),user)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<User>() {
                                                    @Override
                                                    public void accept(@NonNull User user) throws Exception {
                                                        ApplicationConstants.USER = user;
                                                        ApplicationConstants.HISTORYUSERS.add(user);
                                                        mView.showToastMessage("登录成功");
                                                        mView.hideLoginProgress();
                                                        mCallback.logined();
                                                    }
                                                });
                                    }
                                });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(user.equals(lastUser)){
                            mView.showToastMessage("离线登录");
                            ApplicationConstants.USER = lastUser;
                            mCallback.logined();
                        }
                        else {
                            mView.showToastMessage("登录失败");
                        }
                        mView.hideLoginProgress();
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public User getLastLoginUser() {
        return this.lastUser;
    }

    @Override
    public void rememberPassword() {
    }
}
