package cn.xinxizhan.test.tdemo.controls.container;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.Key;
import java.util.ArrayList;

import cn.xinxizhan.test.tdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContainerFragment extends Fragment implements ContainerContract.View, View.OnTouchListener, View.OnClickListener,Spinner.OnItemSelectedListener {
    private ImageButton mTitleButton;
    private ImageButton mCloseButton;
    private Spinner mTitleSpinner;
    private TextView mTitleText;
    private ArrayAdapter<String> mTitleAdapter;
    private FrameLayout mRoot;
    private int lastY;
    private int minY = 100;
    private int topY = 200;

    private ContainerContract.Presenter mPresenter;

    public ContainerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_container, container, false);
        mTitleButton = (ImageButton) mView.findViewById(R.id.container_move);
        mCloseButton = (ImageButton) mView.findViewById(R.id.container_close);
        mTitleText = (TextView)mView.findViewById(R.id.container_title);
        mRoot = (FrameLayout) mView.findViewById(R.id.container_root);
        mTitleSpinner = (Spinner) mView.findViewById(R.id.container_text);
        mTitleAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,new ArrayList<String>());
        mTitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(mTitleAdapter);
        mTitleSpinner.setOnItemSelectedListener(this);
        mCloseButton.setOnClickListener(this);
        mTitleButton.setOnTouchListener(this);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.mPresenter!=null)
            this.mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setPresenter(ContainerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public ArrayAdapter<String> getTitleAdapter() {
        return mTitleAdapter;
    }

    @Override
    public void addFragment(Fragment fragment) {
        FragmentManager manager = this.getActivity().getSupportFragmentManager();
        manager.beginTransaction().add(R.id.container_content,fragment).hide(fragment).commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        this.getActivity().getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    @Override
    public void changeSpinnerselected(String key) {
        int index = mTitleAdapter.getPosition(key);
        if(index==-1) {
            this.mTitleSpinner.setVisibility(View.GONE);
            this.mTitleText.setText(key);
            this.mTitleText.setVisibility(View.VISIBLE);
        }
        else {
            this.mTitleSpinner.setVisibility(View.VISIBLE);
            this.mTitleText.setVisibility(View.GONE);
            this.mTitleSpinner.setSelection(index);
        }
    }

    @Override
    public void show() {
        this.getActivity().getSupportFragmentManager().beginTransaction().show(this).commit();
    }

    @Override
    public void hideFragment(Fragment fragment) {
        this.getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public void hide() {
        this.getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int move = lastY - y;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRoot.getLayoutParams();
                if (y > topY) {
                    params.height = params.height + move;
                }
                if (params.height < minY) {
                    params.height = minY + 1;
                }
                mRoot.setLayoutParams(params);
                lastY = y;
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_close:
                this.mPresenter.close();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.mPresenter.show(this.mTitleAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
