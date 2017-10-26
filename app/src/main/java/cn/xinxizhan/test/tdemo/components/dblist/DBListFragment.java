package cn.xinxizhan.test.tdemo.components.dblist;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.components.dblist.adapter.DBListAdapter;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import cn.xinxizhan.test.tdemo.data.model.DBFileStatus;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.design.R.id.wrap_content;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBListFragment extends Fragment implements DBListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipe;
    private DBListContract.Presenter mPresenter;
    private DBListAdapter mAdapter;
    private PopupWindow progressbar;

    public DBListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dblist, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.dbmanage_fragment_list);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.dbmanage_fragment_swipe);
        mAdapter = new DBListAdapter(new ArrayList<DBFile>(),dbListAdapterListener);
        mSwipe.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void setPresenter(DBListContract.Presenter presenter) {
        this.mPresenter = presenter;
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

    private void delete(final DBFile dbFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DBListFragment.this.getActivity());
        builder.setTitle("DB文件");
        builder.setMessage("确定要删除DB文件吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteDBFile(dbFile);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void upload(final DBFile dbFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DBListFragment.this.getActivity());
        builder.setTitle("DB文件及图片上传");
        builder.setMessage("确定要打包并上传数据吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.uploadDBFile(dbFile);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void setAdapterData(List<DBFile> dbfileList) {
        this.mAdapter.setDBFiles(dbfileList);
    }

    @Override
    public void stopRefresh() {
        this.mSwipe.setRefreshing(false);
    }

    @Override
    public void refreshAdapter() {
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProgressbar(String message) {
        if(progressbar !=null && progressbar.isShowing()){
            progressbar.dismiss();
        }
        View progressbarview = LayoutInflater.from(this.getActivity()).inflate(R.layout.control_working,null);
        TextView progressbartext = (TextView) progressbarview.findViewById(R.id.progressBar_text);
        progressbartext.setText(message==null?"执行中...":message+"...");
        progressbar = new PopupWindow(progressbarview);
        progressbar.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        progressbar.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        progressbar.setOutsideTouchable(false);
        progressbar.setFocusable(false);
        progressbar.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        progressbar.showAtLocation(this.getView(), Gravity.CENTER,0,0);
    }

    @Override
    public void stopProgressbar() {
        if(progressbar !=null)
            progressbar.dismiss();
    }

    @Override
    public boolean backPressed() {
        if(progressbar!=null && progressbar.isShowing()){
            AlertDialog.Builder builder = new AlertDialog.Builder(DBListFragment.this.getActivity());
            builder.setTitle("取消当前任务");
            builder.setMessage("是否取消当前任务");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressbar.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
            return true;
        }
        return false;
    }

    private DBListAdapter.DBListAdapterListener dbListAdapterListener = new DBListAdapter.DBListAdapterListener() {
        @Override
        protected void downloadClick(DBFile dbModel) {
            mPresenter.downloadDBFile(dbModel);
        }

        @Override
        protected void deleteClick(DBFile dbModel) {
            delete(dbModel);
        }

        @Override
        protected void toggleClick(DBFile dbModel) {
            mPresenter.toggleStatusDBFile(dbModel);
        }

        @Override
        protected void uploadClick(DBFile dbModel) {
            upload(dbModel);
        }
    };

    @Override
    public void onRefresh() {
        if(this.mPresenter!=null){
            this.mPresenter.getDBFiles();
        }
    }
}
