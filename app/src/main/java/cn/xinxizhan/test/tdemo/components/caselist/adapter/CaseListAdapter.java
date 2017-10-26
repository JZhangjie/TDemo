package cn.xinxizhan.test.tdemo.components.caselist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.utils.DLBMHelper;

/**
 * Created by admin on 2017/9/6.
 */

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.CaseViewHolder> {
    private List<DBCase> mCaseList;
    private Context mContext;
    private CaseListAdapterListener mListener;


    public CaseListAdapter(Context context, List<DBCase> caseList, CaseListAdapterListener listener) {
        this.mContext=context;
        this.mCaseList=caseList;
        this.mListener = listener;
        if(this.mCaseList==null)
        {
            this.mCaseList=new ArrayList<>();
        }
    }

    public void setData(List<DBCase> caseList){
        this.mCaseList = caseList;
    }

    //region 继承方法
    @Override
    public CaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_caselist, parent, false);
        CaseListAdapter.CaseViewHolder holder = new CaseListAdapter.CaseViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CaseViewHolder holder, int position) {
        DBCase caseModel = mCaseList.get(position);
        holder.fid.setText(caseModel.getBsm()+"");
        holder.cc.setText(caseModel.getCc()==null?"": DLBMHelper.findValueByCode(caseModel.getCc().toString(), ApplicationConstants.CCBMList));
        holder.view.setTag(caseModel);
        holder.view.setOnClickListener(mListener);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mCaseList==null?0: mCaseList.size();
    }
    //endregion

    public class CaseViewHolder extends RecyclerView.ViewHolder {
        TextView fid;
        TextView cc;
        View view;

        public CaseViewHolder(View itemView) {
            super(itemView);
            fid = (TextView) itemView.findViewById(R.id.tbhc_tbbh);
            cc = (TextView) itemView.findViewById(R.id.tbhc_tblx);
            view=itemView;
        }
    }
    //region事件监听器
    public static abstract class CaseListAdapterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            if(o instanceof DBCase) {
                click((DBCase)o);
            }
        }
        protected abstract void click(DBCase caseModel);
    }
    //endregion

    /**
     * 动画效果4  仿QQ的缩放动画效果
     */
    public void imitateQQ(View view, float position) {
        if (position >= -1 && position <= 1) {
            view.setPivotX(position > 0 ? 0 : view.getWidth() / 2);
            //view.setPivotY(view.getHeight()/2);
            view.setScaleX((float) ((1 - Math.abs(position) < 0.5) ? 0.5 : (1 - Math.abs(position))));
            view.setScaleY((float) ((1 - Math.abs(position) < 0.5) ? 0.5 : (1 - Math.abs(position))));
        }
    }
}
