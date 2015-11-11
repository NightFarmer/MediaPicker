package com.nightfarmer.mediapicker.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nightfarmer.mediapicker.R;
import com.nightfarmer.mediapicker.activity.MediaPickActivity;
import com.nightfarmer.mediapicker.bean.MediaItem;
import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;
import com.nightfarmer.mediapicker.provider.MediaItemClickListener;
import com.nightfarmer.mediapicker.provider.MediaPickerAdapter;

import java.util.ArrayList;


/**
 * 图片选择控件
 * Created by zhangfan on 2015/10/26.
 */
public class MediaPickerView extends RelativeLayout {

    public static final int REQUST_CODE = MediaPickerView.class.hashCode() % 10000;
    public static final String RESULT = "MediaPickerViewResult";

    private int orientation;

    MediaPickerAdapter adapter;
    RecyclerView recyclerView;

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
        recyclerView = new RecyclerView(getContext());
        addView(recyclerView);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        adapter = new MediaPickerAdapter(orientation, MediaImageLoaderImpl.getInstance(getContext()), true);
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

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (REQUST_CODE != requestCode) return;
        if (resultCode != Activity.RESULT_OK) return;
        ArrayList<MediaItem> result = null;
        try {
            result = data.getParcelableArrayListExtra(MediaPickerView.RESULT);
        } catch (Exception e) {
        }
        if (result == null) return;
        adapter.dataList = result;
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
//        File bmp_catch = StorageUtils.getIndividualCacheDirectory(getContext(), "bmp_catch");
//        bmp_catch.delete();
        Log.i("", result.size() + "");
    }

    public class MediaPickedItemClickListener implements MediaItemClickListener {

        @Override
        public void onClick(View v, int type) {
            switch (type) {
                case TYPE_END:
                    //add
                    Activity activity = (Activity) v.getContext();
                    Intent intent = new Intent(v.getContext(), MediaPickActivity.class);
                    intent.putParcelableArrayListExtra(RESULT, adapter.dataList);
                    activity.startActivityForResult(intent, REQUST_CODE);
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
