package com.nightfarmer.mediapicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhangfan on 2015/10/29.
 */
public class MediaSelectedView extends ImageView {
    public MediaSelectedView(Context context) {
        super(context);
    }

    public MediaSelectedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
