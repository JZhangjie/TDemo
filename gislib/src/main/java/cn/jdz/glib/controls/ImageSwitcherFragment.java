package cn.jdz.glib.controls;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.jdz.glib.R;
import cn.jdz.glib.capture.ImageShowActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSwitcherFragment extends Fragment implements android.widget.ImageSwitcher.ViewFactory,View.OnTouchListener{
    private String mImagePath ;
    private android.widget.ImageSwitcher mImageSwitcher;
    private TextView mImageNumTv;

    private List<File> mImages;
    private File mCurrentImage;
    private float downX;
    private static final float moveDis = 0.5f;

    public ImageSwitcherFragment() {
    }

    public void setImages(List<File> images){
        mCurrentImage = null;
        mImages = images;
        nextImage();
    }

    public void addImage(File image){
        if(mImages!=null){
            if(mImages.contains(image)){
                jumpToImage(image);
            }
            else {
                mImages.add(image);
                mCurrentImage = null;
                preImage();
            }
        }
    }

    public void removeImage(File image){
        if(mImages!=null && mImages.contains(image) ){
            if(mCurrentImage.equals(mImages)){
                nextImage();
            }
            mImages.remove(image);
            showImageNum();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_image_switcher, container, false);
        mImageNumTv = (TextView) view.findViewById(R.id.fragment_imageswitcher_num);
        mImageSwitcher = (android.widget.ImageSwitcher)view.findViewById(R.id.fragment_imageswitcher);
        mImageSwitcher.setOnTouchListener(this);
        mImageSwitcher.setFactory(this);
        return view;
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(getActivity());
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new android.widget.ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return i ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if(upX - downX <-1*moveDis){
                    //下一张
                    nextImage();
                }
                else if(upX - downX >moveDis){
                    //上一张
                    preImage();
                }
                else {
                    showImage();
                }
                break;
        }
        return true;
    }

    private void nextImage(){
        if(mImages == null || mImages.size()<=0){
            mImageNumTv.setText("0/0");
            return;
        }
        if(mCurrentImage == null){
            mCurrentImage = mImages.get(0);
        }
        else if(mImages.indexOf(mCurrentImage) == mImages.size()-1) {
            Toast.makeText(getActivity(), "已经是最后一张照片了!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mCurrentImage = mImages.get(mImages.indexOf(mCurrentImage)+1);
        }

        mImageSwitcher.setInAnimation(getActivity(),R.anim.slide_in_right);
        mImageSwitcher.setOutAnimation(getActivity(),R.anim.slide_out_left);
        mImageSwitcher.setImageURI(Uri.fromFile(mCurrentImage));

        showImageNum();
    }

    private void preImage(){
        if(mImages == null || mImages.size()<=0){
            mImageNumTv.setText("0/0");
            return;
        }
        if(mCurrentImage == null){
            mCurrentImage = mImages.get(mImages.size()-1);
        }
        else if(mImages.indexOf(mCurrentImage) == 0) {
            Toast.makeText(getActivity(), "已经是第一张照片了!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mCurrentImage = mImages.get(mImages.indexOf(mCurrentImage)-1);
        }

        mImageSwitcher.setInAnimation(getActivity(),R.anim.slide_in_left);
        mImageSwitcher.setOutAnimation(getActivity(),R.anim.slide_out_right);
        mImageSwitcher.setImageURI(Uri.fromFile(mCurrentImage));
        showImageNum();
    }

    private void showImage(){
        if(mCurrentImage != null){
            Intent intent = new Intent(this.getActivity(),ImageShowActivity.class);
            intent.putExtra(ImageShowActivity.IMAGEPATH, mCurrentImage.getPath());
            startActivity(intent);
        }
    }

    private void jumpToImage(File image){
        if(image != null){
            int tempc = mImages.indexOf(mCurrentImage);
            int tempi = mImages.indexOf(image);
            while(tempc>tempi){
                preImage();
                tempc =mImages.indexOf(mCurrentImage);
            }
            while (tempc<tempi){
                nextImage();
                tempc =mImages.indexOf(mCurrentImage);
            }
        }
    }

    private void showImageNum(){
        int allN = mImages.size();
        int currentN = mImages.indexOf(mCurrentImage)+1;
        mImageNumTv.setText(currentN+"/"+allN);
    }
}
