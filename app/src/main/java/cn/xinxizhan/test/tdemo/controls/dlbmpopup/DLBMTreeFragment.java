package cn.xinxizhan.test.tdemo.controls.dlbmpopup;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.jdz.glib.data.KeyValueItem;
import cn.xinxizhan.test.tdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DLBMTreeFragment extends DialogFragment implements ListView.OnItemClickListener {
    private ListView listView1;
    private ListView listView2;
    private List<KeyValueItem> mList;
    private List<KeyValueItem> mLevelList;
    private KeyValueItem selectedItem;
    private DLBMTreeFragmentCallback callback;

    public DLBMTreeFragment() {
    }

    public void selectItem(KeyValueItem item){
        selectedItem = item;
        if(callback!=null)
            callback.chooseItem(selectedItem);
        this.getDialog().dismiss();
    }

    public DLBMTreeFragmentCallback getCallback() {
        return callback;
    }

    public void setCallback(DLBMTreeFragmentCallback callback) {
        this.callback = callback;
    }

    public List<KeyValueItem> getList() {
        return mList;
    }

    public void setList(List<KeyValueItem> mList) {
        this.mList = mList;
        this.mLevelList = new ArrayList<>();
        for(KeyValueItem k :mList)
        {
            if(k.getLevel()==0) {
                this.mLevelList.add(k);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dlbm_tree, container, false);
        listView1 = (ListView)view.findViewById(R.id.dlbm_tree_level_1);
        listView2 = (ListView)view.findViewById(R.id.dlbm_tree_level_2);
        listView1.setOnItemClickListener(this);
        listView2.setOnItemClickListener(this);
        listView1.setAdapter(new DLBMListAdapter(this.getActivity(),mLevelList));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Adapter adapter = parent.getAdapter();
        Object o =adapter.getItem(position);
        if(o instanceof KeyValueItem)
        {
            KeyValueItem item = (KeyValueItem)o;
            if(!item.isHasChildren() || parent.getId()==R.id.dlbm_tree_level_2){
                selectItem(item);
            }
            else if(item.isHasChildren() && parent.getId() ==R.id.dlbm_tree_level_1)
            {
                List<KeyValueItem> templist = new ArrayList<>();
                for(KeyValueItem k :mList)
                {
                    if(k.getParentCode()!=null && k.getParentCode().equals(item.getCode())) {
                        templist.add(k);
                    }
                }
                listView2.setAdapter(new DLBMListAdapter(this.getActivity(),templist));
            }
        }
    }

    public static abstract class DLBMTreeFragmentCallback {
        protected abstract void chooseItem(KeyValueItem item);
    }
}
