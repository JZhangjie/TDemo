package cn.jdz.glib.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/11/16.
 */

public class ExpandLayout extends LinearLayout {

    private LinearLayout mContent;
    public ExpandLayout(Context context) {
        super(context);
        init(context,null,0,0);
    }

    public ExpandLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public ExpandLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    public ExpandLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        mContent = new LinearLayout(context,attrs,defStyleAttr,defStyleRes);
        int i =getChildCount();

    }
}
