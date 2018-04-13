package cn.jdz.glib.controls.imagegallery;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;

import java.io.File;
import java.util.List;

import cn.jdz.glib.R;

public class ImageGalleryFragment extends Fragment {
    private Gallery mGallery;

    public ImageGalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_image_gallery, container, false);
        mGallery = (Gallery) view.findViewById(R.id.fragment_imagegallery_image);
        return view;
    }

    public void setAdapter(List<File> images){
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity(),images);
        mGallery.setAdapter(adapter);
    }
}
