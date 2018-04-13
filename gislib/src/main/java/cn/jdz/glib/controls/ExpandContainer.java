package cn.jdz.glib.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jdz.glib.R;

/**
 * Created by admin on 2017/11/16.
 */

public class ExpandContainer extends LinearLayout implements View.OnClickListener {

    private LinearLayout mContent;
    private View mView;
    private ImageView mBtn;
    private ImageView mTitleImg;
    private TextView mTxt;
    private boolean isExpanded = false;

    public ExpandContainer(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ExpandContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ExpandContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ExpandContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mView = LayoutInflater.from(context).inflate(R.layout.control_expandcontainer, this, true);
        mContent = (LinearLayout) mView.findViewById(R.id.item_expandcontainer_content_container);
        mTxt = (TextView) mView.findViewById(R.id.item_expandcontainer_title_text);
        mTitleImg = (ImageView) mView.findViewById(R.id.item_expandcontainer_title_img);
        mBtn = (ImageView) mView.findViewById(R.id.item_expandcontainer_title_btn);
        mBtn.setOnClickListener(this);

        TypedArray ta =context.obtainStyledAttributes(attrs,R.styleable.ExpandContainer);
        String titleText = ta.getString(R.styleable.ExpandContainer_titleText);
        int contetR = ta.getResourceId(R.styleable.ExpandContainer_contentRes,android.R.layout.activity_list_item);
        int titleimgR= ta.getResourceId(R.styleable.ExpandContainer_titleImg,R.drawable.expand_title);
        ta.recycle();

        mTxt.setText(titleText);
        mTitleImg.setImageResource(titleimgR);
        View view =LayoutInflater.from(context).inflate(contetR,mContent,true);
        mContent.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_expandcontainer_title_btn) {
            if (isExpanded) {
                mContent.setVisibility(GONE);
                mBtn.setImageResource(R.drawable.expand_more);
            } else {
                mContent.setVisibility(VISIBLE);
                mBtn.setImageResource(R.drawable.expand_less);
            }
            isExpanded = !isExpanded;
        }
    }

    public void setTitleImg(int resR){
        if(mTitleImg != null){
            mTitleImg.setImageResource(resR);
        }
    }

    public void setTitleText(String title){
        if(mTxt != null){
            mTxt.setText(title);
        }
    }
}
