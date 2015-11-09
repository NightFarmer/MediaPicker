package com.nightfarmer.mediapicker;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择展示adapter
 * Created by zhangfan on 2015/10/27.
 */
public class MediaPickerAdapter extends RecyclerView.Adapter<MediaPickerAdapter.MediaPickedItemHolder> {
    private final MediaImageLoaderImpl mMediaImageLoader;
    private int orientation;


    private MediaItemClickListener mediaItemClickListener;

    ArrayList<MediaItem> dataList = new ArrayList<>();

    public MediaPickerAdapter(int orientation, MediaImageLoaderImpl mMediaImageLoader) {
        this.orientation = orientation;
        this.mMediaImageLoader = mMediaImageLoader;
    }

    @Override
    public MediaPickedItemHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
//        final ImageView imageView = new SimpleDraweeView(parent.getContext());
        final ImageView imageView = new ImageView(parent.getContext());
        int border = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, parent.getContext().getResources().getDisplayMetrics());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaItemClickListener != null) {
                    mediaItemClickListener.onClick(v, viewType);
                }
            }
        });
        parent.addView(imageView);
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) imageView.getLayoutParams();
        if (MediaItemClickListener.TYPE_MEDIA == viewType) {
            imageView.setBackgroundResource(R.drawable.media_selected_border);
        } else {
            imageView.setBackgroundResource(R.drawable.media_selected_blank);
        }
        layoutParams.setMargins(border, 0, border, 0);
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
        return new MediaPickedItemHolder(imageView);
    }

    @Override
    public void onBindViewHolder(final MediaPickedItemHolder holder, int position) {
        final int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MediaItemClickListener.TYPE_MEDIA:
                final MediaItem data = dataList.get(position);
                holder.itemView.setTag(data);
                Bitmap cache = data.getCache(holder.imageView.getContext());
                if (cache != null) {
                    holder.imageView.setImageBitmap(cache);
                } else {
                    mMediaImageLoader.displayImage(data.getUriOrigin(), holder.imageView, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, final View view, final Bitmap loadedImage) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final MediaItem tag = (MediaItem) holder.imageView.getTag();
                                    view.setDrawingCacheEnabled(true);
                                    tag.setCache(view.getDrawingCache(), holder.imageView.getContext());
                                    view.setDrawingCacheEnabled(false);
                                    final Bitmap cache = tag.getCache(holder.imageView.getContext());
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.imageView.setImageBitmap(cache);
                                        }
                                    });
                                }
                            }).start();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
                }
                break;
            case MediaItemClickListener.TYPE_END:
//                holder.textView.setText("新增");
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
        ImageView imageView;

        public MediaPickedItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setMediaItemClickListener(MediaItemClickListener mediaItemClickListener) {
        this.mediaItemClickListener = mediaItemClickListener;
    }

}
