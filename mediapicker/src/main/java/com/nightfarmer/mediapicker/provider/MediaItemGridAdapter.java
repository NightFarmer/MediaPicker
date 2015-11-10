package com.nightfarmer.mediapicker.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightfarmer.mediapicker.R;
import com.nightfarmer.mediapicker.bean.MediaItem;
import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangfan on 2015/10/27.
 */
public class MediaItemGridAdapter extends RecyclerViewCursorAdapter<MediaItemGridAdapter.GridItemHolder> {

    private MediaItemClickListener mediaItemClickListener;

    private MediaImageLoaderImpl mMediaImageLoader;

    private List<MediaItem> selectedItemList;

    public File fileHolder;

    private static final int REQUEST_PHOTO_CAPTURE = 100;
//    private FileObserverTask mFileObserverTask;

    public MediaItemGridAdapter(Activity context, Cursor c, int flags, MediaImageLoaderImpl mMediaImageLoader) {
        super(context, c, flags);
        this.mMediaImageLoader = mMediaImageLoader;
    }

    @Override
    public GridItemHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == MediaItemClickListener.TYPE_START) {
            View inflate_camera = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_camera_item, parent, false);
            inflate_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
            return new GridItemHolder(inflate_camera);
        }
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media_griditem, parent, false);
        GridItemHolder gridItemHolder = new GridItemHolder(inflate);
        inflate.setTag(R.id.draweView, gridItemHolder.draweView);
        gridItemHolder.draweView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaItemClickListener != null) {
                    mediaItemClickListener.onClick(inflate, viewType);
                }
            }
        });
        gridItemHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckedListener != null) {
                    MediaItem tag = (MediaItem) inflate.getTag();
                    if (tag != null && tag.isChecked != isChecked) {
                        tag.isChecked = isChecked;
                        onCheckedListener.onChecked(isChecked, inflate);
                    }
                }
            }
        });

        return gridItemHolder;
    }

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
                    Log.e("MediaItemGridAdapter", "this should only be called when the cursor is valid");
//                    throw new IllegalStateException("this should only be called when the cursor is valid");
                }
                if (!mCursor.moveToPosition(position - 1)) {
                    Log.e("MediaItemGridAdapter", "couldn't move cursor to position " + position);
//                    throw new IllegalStateException("couldn't move cursor to position " + position);
                }
//                String id = mCursor.getString(mCursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
//                holder.textView.setText(MediaUtils.getPhotoUri(mCursor).toString());
//                holder.draweView.setImageURI(MediaUtils.getPhotoUri(mCursor));
//                ImageLoader.getInstance().displayImage(MediaUtils.getPhotoUri(mCursor).toString(), holder.draweView);
//                holder.draweView.setImageURI(MediaUtils.getPhotoUri(mCursor));
                mMediaImageLoader.displayImage(MediaUtils.getPhotoUri(mCursor), holder.draweView);
                MediaItem mediaItem = new MediaItem(MediaItem.PHOTO, MediaUtils.getPhotoUri(mCursor));
                holder.itemView.setTag(mediaItem);
//                holder.textView.setText(mediaItem.getPathOrigin(holder.textView.getContext()));
//                holder.checkImage.setImageResource(R.drawable.ab_picker_camera);
                boolean checked = selectedItemList != null && selectedItemList.indexOf(mediaItem) != -1;
                mediaItem.isChecked = checked;
                holder.checkBox.setChecked(checked);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public class GridItemHolder extends RecyclerView.ViewHolder {
        ImageView draweView;
        CheckBox checkBox;

        public GridItemHolder(View itemView) {
            super(itemView);
            draweView = (ImageView) itemView.findViewById(R.id.draweView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkImage);
        }
    }

    public void setMediaItemClickListener(MediaItemClickListener mediaItemClickListener) {
        this.mediaItemClickListener = mediaItemClickListener;
    }

    public interface OnCheckedListener {
        void onChecked(boolean checked, View view);
    }

    OnCheckedListener onCheckedListener;

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public void setSelectedItemList(List<MediaItem> selectedItemList) {
        this.selectedItemList = selectedItemList;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
//            File file = mMediaOptions.getPhotoFile();
            try {
                fileHolder = MediaUtils.createDefaultImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileHolder != null) {
//                mPhotoFileCapture = file;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(fileHolder));
                mContext.startActivityForResult(takePictureIntent, REQUEST_PHOTO_CAPTURE);
//                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                MediaFileObserver mediaFileObserver = new MediaFileObserver(MediaUtils.getStorageDir().getPath(), FileObserver.CREATE);
//                mediaFileObserver.startWatching();
//                mFileObserverTask = new FileObserverTask();
//                mFileObserverTask.execute();
//                mFileObserverTask = new FileObserverTask();
//                mFileObserverTask.execute();
            }
        }
    }

//    RecursiveFileObserver mFileObserver;
//
//    private class FileObserverTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if (isCancelled()) return null;
//            if (mFileObserver == null) {
////                mFileObserver = new RecursiveFileObserver(Environment
////                        .getExternalStorageDirectory().getAbsolutePath(),
////                        FileObserver.CREATE);
//                mFileObserver = new RecursiveFileObserver(MediaUtils.getStorageDir().getPath(),
//                        FileObserver.CREATE);
//
////                mFileObserver
////                        .setFileCreatedListener(mOnFileCreatedListener);
//            }
//            mFileObserver.startWatching();
//            return null;
//        }
//    }
}
