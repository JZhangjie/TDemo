package cn.xinxizhan.test.tdemo.components.caselist;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.components.caselist.adapter.CaseListAdapter;
import cn.xinxizhan.test.tdemo.components.caselist.adapter.CaseListAdapter.CaseListAdapterListener;
import cn.xinxizhan.test.tdemo.data.model.DBCase;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseListFragment extends Fragment implements CaseListContract.View,SwipeRefreshLayout.OnRefreshListener,RadioGroup.OnCheckedChangeListener{


    private RecyclerView mContainer;
    private SwipeRefreshLayout mSwipe;
    private RadioGroup mRadioGroupFilter;
    private LinearLayoutManager mLinearLayoutManager;
    private CaseListAdapter mAdapter;
    private CaseListContract.Presenter mPresenter;


    public CaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_case_list, container, false);

        mContainer = (RecyclerView)mView.findViewById(R.id.caselist_fragment_list);
        mSwipe = (SwipeRefreshLayout)mView.findViewById(R.id.caselist_fragment_swipe);
        mRadioGroupFilter = (RadioGroup)mView.findViewById(R.id.caselist_fragment_rgp_filter);
        mRadioGroupFilter.setOnCheckedChangeListener(this);
        mSwipe.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mContainer.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CaseListAdapter(this.getActivity(),new ArrayList<DBCase>(),this.caseListAdapterListener);
        mContainer.setAdapter(mAdapter);
        return mView;
    }

    @Override
    public void setPresenter(CaseListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setAdapterData(List<DBCase> caseList) {
        this.mAdapter.setData(caseList);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFilterType(CaselistFilterType filterType) {
        switch (filterType){
            case ALL:
                mRadioGroupFilter.check(R.id.caselist_fragment_rbtn_all);
                break;
            case DHC:
                mRadioGroupFilter.check(R.id.caselist_fragment_rbtn_dhc);
                break;
            case YHC:
                mRadioGroupFilter.check(R.id.caselist_fragment_rbtn_yhc);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.caselist_fragment_rbtn_all:
                mPresenter.showFilterCaseList(CaselistFilterType.ALL);
                break;
            case R.id.caselist_fragment_rbtn_dhc:
                mPresenter.showFilterCaseList(CaselistFilterType.DHC);
                break;
            case R.id.caselist_fragment_rbtn_yhc:
                mPresenter.showFilterCaseList(CaselistFilterType.YHC);
                break;
        }
    }

    private CaseListAdapterListener caseListAdapterListener = new CaseListAdapterListener() {
        @Override
        protected void click(DBCase caseModel) {
            if(mPresenter!=null)
                mPresenter.clickCase(caseModel);
        }
    };
}
