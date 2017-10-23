package cn.xinxizhan.test.tdemo.components.dblist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.List;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.data.common.DBField;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import cn.xinxizhan.test.tdemo.data.model.DBFileStatus;

/**
 * Created by admin on 2017/10/10.
 */

public class DBListAdapter extends RecyclerView.Adapter<DBListAdapter.DBViewHolder> {

    private List<DBFile> mDBFiles;
    private DBListAdapterListener mListener;

    public DBListAdapter(List<DBFile> dbFiles,DBListAdapterListener listener){
        this.mDBFiles = dbFiles;
        this.mListener = listener;
    }

    public void setDBFiles(List<DBFile> list){
        this.mDBFiles = list;
    }

    @Override
    public DBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dblist, parent, false);
        DBViewHolder holder = new DBViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DBViewHolder holder, int position) {
        DBFile db = mDBFiles.get(position);
        holder.delete.setTag(db);
        holder.delete.setOnClickListener(mListener);
        holder.download.setTag(db);
        holder.download.setOnClickListener(mListener);
        holder.upload.setTag(db);
        holder.upload.setOnClickListener(mListener);
        holder.toggle.setTag(db);
        holder.toggle.setOnClickListener(mListener);
        holder.toggle.setChecked(db.getStatus()== DBFileStatus.USING);
        holder.name.setText(db.getName());
        switch (db.getStatus()){
            case LOADING:
                holder.download.setVisibility(View.INVISIBLE);
                holder.toggle.setVisibility(View.INVISIBLE);
                holder.delete.setVisibility(View.INVISIBLE);
                holder.upload.setVisibility(View.INVISIBLE);
                holder.downloadavi.setVisibility(View.VISIBLE);
                holder.downloadavi.show();
                break;
            case NOTDOWMLOAD:
                holder.download.setVisibility(View.VISIBLE);
                holder.toggle.setVisibility(View.INVISIBLE);
                holder.delete.setVisibility(View.INVISIBLE);
                holder.upload.setVisibility(View.INVISIBLE);
                holder.downloadavi.setVisibility(View.INVISIBLE);
                holder.downloadavi.hide();
                break;
            case DOWNLOADED:
            case USING:
                holder.download.setVisibility(View.INVISIBLE);
                holder.toggle.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.upload.setVisibility(View.VISIBLE);
                holder.upload.setImageResource(R.drawable.file_upload);
                holder.downloadavi.setVisibility(View.INVISIBLE);
                holder.downloadavi.hide();
                break;
            case UPLOADED:
                holder.download.setVisibility(View.INVISIBLE);
                holder.toggle.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.upload.setVisibility(View.VISIBLE);
                holder.upload.setImageResource(R.drawable.file_uploaded);
                holder.downloadavi.setVisibility(View.INVISIBLE);
                holder.downloadavi.hide();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.mDBFiles.size();
    }

    public class DBViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton download;
        RadioButton toggle;
        ImageButton delete;
        ImageButton upload;
        AVLoadingIndicatorView downloadavi;

        public DBViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.dbmanage_item_name);
            download = (ImageButton) itemView.findViewById(R.id.dbmanage_item_download);
            delete = (ImageButton) itemView.findViewById(R.id.dbmanage_item_delete);
            toggle = (RadioButton) itemView.findViewById(R.id.dbmanage_item_toggle);
            upload = (ImageButton)itemView.findViewById(R.id.dbmanage_item_upload);
            downloadavi = (AVLoadingIndicatorView) itemView.findViewById(R.id.dbmanage_item_downloadavi);
        }
    }

    public static abstract class DBListAdapterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.dbmanage_item_download:
                    if (v.getTag() instanceof DBFile && v instanceof ImageButton) {
                        DBFile dbtemp = (DBFile) v.getTag();
                        downloadClick(dbtemp);
                    }
                    break;
                case R.id.dbmanage_item_delete:
                    if (v.getTag() instanceof DBFile && v instanceof ImageButton) {
                        DBFile dbtemp = (DBFile) v.getTag();
                        deleteClick(dbtemp);
                    }
                    break;
                case R.id.dbmanage_item_toggle:
                    if (v.getTag() instanceof DBFile && v instanceof RadioButton) {
                        DBFile dbtemp = (DBFile) v.getTag();
                        toggleClick(dbtemp);
                    }
                    break;
                case R.id.dbmanage_item_upload:
                    if (v.getTag() instanceof DBFile && v instanceof ImageButton) {
                        DBFile dbtemp = (DBFile) v.getTag();
                        uploadClick(dbtemp);
                    }
            }
        }
        protected abstract void downloadClick(DBFile dbModel);
        protected abstract void deleteClick(DBFile dbModel);
        protected abstract void toggleClick(DBFile dbModel);
        protected abstract void uploadClick(DBFile dbModel);
    }

}
