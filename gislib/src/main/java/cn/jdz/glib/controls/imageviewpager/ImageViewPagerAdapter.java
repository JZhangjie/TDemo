package cn.jdz.glib.controls.imageviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jdz.glib.capture.CaptureActivity;
import cn.jdz.glib.utils.ImgHelper;

/**
 * Created by admin on 2017/11/24.
 */

public class ImageViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<File> mImages;
    private HashMap<File,View> mViews;

    public ImageViewPagerAdapter(Context context,List<File> images){
        mContext =context;
        mImages = new ArrayList<>();
        mViews = new HashMap<>();
        if(images !=null){
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
    public Object instantiateItem(ViewGroup container, int position) {
        File image = mImages.get(position);
        View view = mViews.get(image);
        if(view == null){
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getPath(),options);
            options.inSampleSize = ImgHelper.computeSampleSize(options,-1,512*512);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),options);

            imageView.setImageBitmap(bitmap);

            view = imageView;
            mViews.put(image,view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(mImages.get(position)));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
