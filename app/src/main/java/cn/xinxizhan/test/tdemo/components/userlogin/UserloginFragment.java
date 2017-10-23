package cn.xinxizhan.test.tdemo.components.userlogin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.data.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserloginFragment extends Fragment implements  UserloginContract.View,View.OnClickListener {

    private EditText mUsername=null;
    private  EditText   mPassword=null;
    private Button mLoginBtn=null;
    private ProgressBar mProgressBar=null;

    private UserloginContract.Presenter mPresenter;

    public UserloginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userlogin, container, false);

        mLoginBtn=(Button)  view.findViewById(R.id.loginBtn);
        mUsername=(EditText)view.findViewById(R.id.tbUsername);
        mPassword=(EditText)view.findViewById(R.id.tbPassword);
        mProgressBar=(ProgressBar)view.findViewById(R.id.tbhc_progressBar);

        mLoginBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter!=null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(UserloginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public User getUserFromView() {
        User user = new User(mUsername.getText().toString(),mPassword.getText().toString());
        return user;
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginProgress() {
        mLoginBtn.setEnabled(false);
        mUsername.setEnabled(false);
        mPassword.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginProgress() {
        mLoginBtn.setEnabled(true);
        mUsername.setEnabled(true);
        mPassword.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setLastUser(User lastUser) {
        User user = mPresenter.getLastLoginUser();
        mUsername.setText(user.getUsername());
        mPassword.setText(user.getPassword());
    }

    @Override
    public void setRememberPassword(Boolean remember) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                mPresenter.login();
                break;
        }
    }
}
