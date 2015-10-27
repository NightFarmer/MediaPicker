package com.nightfarmer.mediapicker;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhangfan on 2015/10/27.
 */
public class MediaItemGridAdapter extends RecyclerViewCursorAdapter<MediaItemGridAdapter.GridItemHolder> {

    //    List<MediaItem> dataList;
    private MediaItemClickListener mediaItemClickListener;

    public MediaItemGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public GridItemHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final TextView textView = new TextView(parent.getContext());
        parent.addView(textView);
        final ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.height = parent.getWidth() / 3;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaItemClickListener != null) {
                    mediaItemClickListener.onClick(v, viewType);
                }
            }
        });
        return new GridItemHolder(textView);
    }

//    @Override
//    public void onBindViewHolder(GridItemHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case MediaItemClickListener.TYPE_START:
//
//                break;
//            case MediaItemClickListener.TYPE_MEDIA:
//                holder.itemView.setTag(dataList.get(position - 1));
//                holder.textView.setText(dataList.get(position - 1).getUriOrigin().toString());
//                break;
//        }
//    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? MediaItemClickListener.TYPE_START : MediaItemClickListener.TYPE_MEDIA;
    }

    @Override
    public void onBindViewHolder(GridItemHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MediaItemClickListener.TYPE_START:

                break;
            case MediaItemClickListener.TYPE_MEDIA:
                if (!mDataValid && position < mCursor.getCount() - 1) {
                    throw new IllegalStateException("this should only be called when the cursor is valid");
                }
                if (!mCursor.moveToPosition(position - 1)) {
                    throw new IllegalStateException("couldn't move cursor to position " + position);
                }
                String id = mCursor.getString(mCursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                holder.textView.setText(id);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public class GridItemHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public GridItemHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView;
        }
    }

    public void setMediaItemClickListener(MediaItemClickListener mediaItemClickListener) {
        this.mediaItemClickListener = mediaItemClickListener;
    }


}
