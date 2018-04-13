package cn.jdz.glib.controls.imageviewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.jdz.glib.R;
import cn.jdz.glib.components.container.ContainerContract;
import cn.jdz.glib.controls.imagegallery.ImageGalleryAdapter;

/**
 *
 * A simple {@link Fragment} subclass.
 */
public class ImageViewPagerFragment extends Fragment {

    private ViewPager mViewPager;
    private ImageViewPagerAdapter mAdapter;
    private TextView mTextView;

    public ImageViewPagerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_image_view_pager, container, false);
        mTextView = (TextView) view.findViewById(R.id.fragment_imageviewpager_num);
        mViewPager = (ViewPager)view.findViewById(R.id.fragment_imageviewpager_image);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTextView.setText((position+1)+"/"+mAdapter.getCount());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    public void setAdapter(List<File> images){
        mAdapter = new ImageViewPagerAdapter(getActivity(),images);
        mViewPager.setAdapter(mAdapter);
    }
}
