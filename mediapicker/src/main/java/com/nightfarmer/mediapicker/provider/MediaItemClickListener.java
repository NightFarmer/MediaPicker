package com.nightfarmer.mediapicker.provider;

import android.view.View;

/**
 * Created by zhangfan on 2015/10/27.
 */
public interface MediaItemClickListener {
    int TYPE_START = 0;
    int TYPE_END = 1;
    int TYPE_MEDIA = 2;

    void onClick(View v, int type);
}
