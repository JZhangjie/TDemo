package cn.xinxizhan.test.tdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.components.userlogin.UserloginContract;
import cn.xinxizhan.test.tdemo.components.userlogin.UserloginPresenter;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.model.User;
import cn.xinxizhan.test.tdemo.data.model.UserRepository;
import cn.xinxizhan.test.tdemo.utils.PermissionHelper;

public class LoginActivity extends AppCompatActivity {

    private UserloginContract.Presenter mUserloginPresenter;
    static final String HISTORYUSERS="historyusers";
    static final String LASTUSER = "lastusers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PermissionHelper.getPermission(LoginActivity.this);

        UserloginContract.View view = (UserloginContract.View)getSupportFragmentManager().findFragmentById(R.id.login_activity_fragment);
        mUserloginPresenter = new UserloginPresenter(view,mUserloginCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveToSharedPreferences();
    }

    private void saveToSharedPreferences(){
        SharedPreferences mShadow=LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor=mShadow.edit();
        mEditor.putString(HISTORYUSERS,ApplicationConstants.HISTORYUSERS.string());
        mEditor.putString(LASTUSER,User.string(ApplicationConstants.USER));
        mEditor.commit();
    }

    private UserloginContract.Callback mUserloginCallback = new UserloginContract.Callback() {
        @Override
        public void initHistoryUsers() {
            SharedPreferences sp = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
            String users = sp.getString(HISTORYUSERS, null);
            ApplicationConstants.HISTORYUSERS = new UserRepository();
            ApplicationConstants.HISTORYUSERS.initFromString(users);
        }

        @Override
        public User getLastLoginUser() {
            SharedPreferences sp = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
            String users = sp.getString(LASTUSER, null);
            return User.fromString(users);
        }

        @Override
        public void logined() {
            Intent mIntent=new Intent();
            mIntent.setClass(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(mIntent);
        }
    };
}
