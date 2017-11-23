package cn.xinxizhan.test.tdemo.components.useredit;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.model.User;
import cn.xinxizhan.test.tdemo.utils.HttpHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/10/26.
 */

public class UsereditPresenter implements UsereditContract.Presenter {

    private UsereditContract.View mView;
    private UsereditContract.Callback mCallback;
    private User mUser;

    public UsereditPresenter(UsereditContract.View view,UsereditContract.Callback callback){
        mView = view;
        mCallback = callback;
        mView.setPresenter(this);
    }
    @Override
    public void start() {
        mUser = ApplicationConstants.USER;
    }

    @Override
    public boolean checkInput() {
        String newpassword = mView.getNewPassword();
        return (newpassword !=null) && newpassword.equals(mView.getCheckPassword());
    }

    @Override
    public void savePassword() {
        if(mUser != null){
            String newpassword = mView.getNewPassword();
            String oldpassword = mView.getOldPassword();
            if(mUser.getPassword().equals(oldpassword)){
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("loginname",mUser.getUsername());
                params.put("password",mUser.getPassword());

                HttpHelper.postValues(ApplicationConstants.LOGINURL,params).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {

                    }
                });
            }
            else {
                mView.showToastMessage("原密码输入错误");
            }
        }
        else {
            mView.showToastMessage("当前用户为空");
        }
    }

    @Override
    public void cancel() {
        if(mCallback!=null)
            mCallback.onCancel();
    }
}
