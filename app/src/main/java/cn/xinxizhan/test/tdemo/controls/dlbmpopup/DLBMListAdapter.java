package cn.xinxizhan.test.tdemo.controls.dlbmpopup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jdz.glib.data.KeyValueItem;
import cn.xinxizhan.test.tdemo.R;

/**
 * Created by admin on 2017/9/15.
 */

public class DLBMListAdapter extends BaseAdapter {
    private Context mContext;
    private List<KeyValueItem> mList;

    public DLBMListAdapter(Context context, List<KeyValueItem> list)
    {
        this.mContext = context;
        if(list==null)
            list=new ArrayList<>();
        this.mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return makeItemView(this.mList.get(position),convertView);
    }

    private View makeItemView(KeyValueItem item,View convertView)
    {
        View itemview = convertView;
        if(itemview==null)
        {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemview = inflater.inflate(R.layout.item_dlbm_list,null);
        }
        TextView name = (TextView)itemview.findViewById(R.id.dlbm_item_list_name);
        ImageView childshow = (ImageView)itemview.findViewById(R.id.dlbm_item_list_children);
        if(item.isHasChildren())
            childshow.setVisibility(View.VISIBLE);
        else
            childshow.setVisibility(View.INVISIBLE);
        name.setText(item.getValue());
        return itemview;
    }
}
