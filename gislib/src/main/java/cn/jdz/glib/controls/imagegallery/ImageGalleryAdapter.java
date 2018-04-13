package cn.jdz.glib.controls.imagegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jdz.glib.utils.ImgHelper;

/**
 * Created by admin on 2017/11/24.
 */

public class ImageGalleryAdapter extends BaseAdapter {
    private Context mContext;
    private List<File> mImages;
    private Map<File,Bitmap> mBitmaps;

    public ImageGalleryAdapter(Context context,List<File> images){
        mContext = context;
        mImages = new ArrayList<>();
        mBitmaps = new HashMap<>();
        if(images!=null){
            for(File file :images){
                mImages.add(file);
            }
        }
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        else {
            imageView = (ImageView)convertView;
        }

        File image = mImages.get(position);
        Bitmap bitmap = mBitmaps.get(image);
        if(bitmap == null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(),options);
            options.inSampleSize = ImgHelper.computeSampleSize(options,-1,512*512);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(image.getPath(),options);
            mBitmaps.put(image,bitmap);
        }
        imageView.setImageBitmap(bitmap);
        return imageView;
    }
}
