package com.nightfarmer.mediapicker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择展示adapter
 * Created by zhangfan on 2015/10/27.
 */
public class MediaPickerAdapter extends RecyclerView.Adapter<MediaPickerAdapter.MediaPickedItemHolder> {
    private int orientation;


    private MediaItemClickListener mediaItemClickListener;

    public final List<MediaItem> dataList = new ArrayList<>();

    public MediaPickerAdapter(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public MediaPickedItemHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final TextView textView = new TextView(parent.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaItemClickListener != null) {
                    mediaItemClickListener.onClick(v, viewType);
                }
            }
        });
        parent.addView(textView);
        final ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        switch (orientation) {
            case LinearLayout.VERTICAL:
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = parent.getWidth() - parent.getPaddingRight() - parent.getPaddingLeft();
                break;
            case LinearLayout.HORIZONTAL:
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
                break;
        }

        textView.setBackgroundColor(0xffff0000);
        return new MediaPickedItemHolder(textView);
    }

    @Override
    public void onBindViewHolder(MediaPickedItemHolder holder, int position) {
        final int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MediaItemClickListener.TYPE_MEDIA:
                final MediaItem data = dataList.get(position);
                holder.itemView.setTag(data);
                holder.textView.setText(data.getUriOrigin().toString());
                break;
            case MediaItemClickListener.TYPE_END:
                holder.textView.setText("新增");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        return position > dataList.size() - 1 ? MediaItemClickListener.TYPE_END : MediaItemClickListener.TYPE_MEDIA;
    }

    public class MediaPickedItemHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MediaPickedItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setMediaItemClickListener(MediaItemClickListener mediaItemClickListener) {
        this.mediaItemClickListener = mediaItemClickListener;
    }

}
