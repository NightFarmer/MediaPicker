package com.nightfarmer.mediapicker;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static com.nightfarmer.mediapicker.MediaPickerAdapter.*;

/**
 * 图片选择控件
 * Created by zhangfan on 2015/10/26.
 */
public class MediaPickerView extends RelativeLayout {

    private int orientation;

    MediaPickerAdapter adapter;

    public MediaPickerView(Context context) {
        this(context, null);
    }

    public MediaPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MediaPickerView, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.MediaPickerView_orientation) {
                orientation = typedArray.getInt(attr, 0);
            }
        }
        typedArray.recycle();
    }


    @Override
    protected void onFinishInflate() {
        final RecyclerView recyclerView = new RecyclerView(getContext());
        addView(recyclerView);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        adapter = new MediaPickerAdapter(orientation);
        recyclerView.setAdapter(adapter);
        adapter.setMediaItemClickListener(new MediaPickedItemClickListener());
        final ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        super.onFinishInflate();
    }

    public MediaPickerAdapter getAdapter() {
        return adapter;
    }

    public class MediaPickedItemClickListener implements MediaItemClickListener{

        @Override
        public void onClick(View v, int type) {
            switch (type) {
                case TYPE_END:
                    //add
                    Toast.makeText(v.getContext(), "add", Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(new Intent(v.getContext(), MediaPickActivity.class));
                    break;
                case TYPE_MEDIA:
                    //show
                    MediaItem data = (MediaItem) v.getTag();
                    Toast.makeText(v.getContext(), "" + data.getUriOrigin(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
