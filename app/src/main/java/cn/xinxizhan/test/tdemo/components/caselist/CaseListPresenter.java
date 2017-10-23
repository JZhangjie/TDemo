package cn.xinxizhan.test.tdemo.components.caselist;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.data.model.DBFile;

/**
 * Created by admin on 2017/10/16.
 */

public class CaseListPresenter implements CaseListContract.Presenter {

    private CaseListContract.View mView;
    private CaseListContract.Callback mCallback;
    private boolean isFirst = true;

    private DBFile mCurrentDBFile;

    public CaseListPresenter(CaseListContract.View view,CaseListContract.Callback callback){
        this.mView = view;
        this.mCallback = callback;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        showFilterCaseList(CaselistFilterType.ALL);
        if(isFirst){

        }
    }

    @Override
    public void setCurrentDBFile(DBFile dbFile) {
        this.mCurrentDBFile = dbFile;
        showFilterCaseList(CaselistFilterType.ALL);
    }

    @Override
    public void showFilterCaseList(CaselistFilterType filterType) {
        List<DBCase> tempCaseList = new ArrayList<>();
        if(this.mCurrentDBFile != null && this.mCurrentDBFile.getManager() != null){
            switch (filterType){
                case ALL:
                    for(DBCase dbCase:this.mCurrentDBFile.getManager()){
                        tempCaseList.add(dbCase);
                    }
                    break;
                case DHC:
                    for(DBCase dbCase:this.mCurrentDBFile.getManager()){
                        if(dbCase.getSfydc()==0)
                            tempCaseList.add(dbCase);
                    }
                    break;
                case YHC:
                    for(DBCase dbCase:this.mCurrentDBFile.getManager()){
                        if(dbCase.getSfydc()==1)
                            tempCaseList.add(dbCase);
                    }
                    break;
            }
        }
        this.mView.setAdapterData(tempCaseList);
        this.mView.setFilterType(filterType);
    }

    @Override
    public void clickCase(DBCase dbCase) {
        this.mCallback.onCaseClick(dbCase);
    }
}
