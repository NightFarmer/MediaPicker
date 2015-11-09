package com.nightfarmer.mediapicker;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * 多媒体资源描述类
 * Created by zhangfan on 2015/10/26.
 */
public class MediaItem implements Parcelable{


    /**
     * 图片
     */
    public static final int PHOTO = 1;
    /**
     * 视频
     */
    public static final int VIDEO = 2;

    /**
     * 多媒体类型  {@link #PHOTO} 或者 {@link #VIDEO}
     */
    private int type;

    /**
     * 裁剪过后的uri
     */
    private Uri uriCropped;
    /**
     * 原始uri
     */
    private Uri uriOrigin;

    boolean isChecked;

    public MediaItem(int type, Uri uriOrigin) {
        this.type = type;
        this.uriOrigin = uriOrigin;
    }

    private Bitmap cache;

    public Bitmap getCache(Context context) {
        if (cache == null || cache.isRecycled()) {
            File bmp_catch = StorageUtils.getIndividualCacheDirectory(context, "bmp_catch");
            try {
                File file = new File(bmp_catch, uriOrigin.toString().hashCode()+"");
                if (!file.exists()) return null;
                FileInputStream fileInputStream = new FileInputStream(file);
                cache = BitmapFactory.decodeStream(fileInputStream);
                return cache;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

    public void setCache(Bitmap cache, Context context) {
        this.cache = Bitmap.createBitmap(cache);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cache.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        byte[] cacheBytes = baos.toByteArray();//创建分配字节数组
        File bmp_catch = StorageUtils.getIndividualCacheDirectory(context, "bmp_catch");
        try {
            File file = new File(bmp_catch, uriOrigin.toString().hashCode()+"");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(cacheBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getUriCropped() {
        return uriCropped;
    }

    public void setUriCropped(Uri uriCropped) {
        this.uriCropped = uriCropped;
    }

    public Uri getUriOrigin() {
        return uriOrigin;
    }

    public void setUriOrigin(Uri uriOrigin) {
        this.uriOrigin = uriOrigin;
    }


    /**
     * @param context
     * @return Path of origin file.
     */
    public String getPathOrigin(Context context) {
        return getPathFromUri(context, uriOrigin);
    }


    public boolean isPhoto() {
        return type == PHOTO;
    }

    /**
     * @param context
     * @return Path of cropped file.
     */
    public String getPathCropped(Context context) {
        return getPathFromUri(context, uriCropped);
    }

    private String getPathFromUri(Context context, Uri uri) {
        if (uri == null)
            return null;
        String scheme = uri.getScheme();
        if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            return uri.getPath();
        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            if (isPhoto()) {
                return MediaUtils.getRealImagePathFromURI(
                        context.getContentResolver(), uri);
            } else {
                return MediaUtils.getRealVideoPathFromURI(
                        context.getContentResolver(), uri);
            }
        }
        return uri.toString();
    }

    @Override
    public boolean equals(Object o) {
        return uriOrigin.equals(((MediaItem) o).uriOrigin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uriCropped, 1);
        dest.writeParcelable(uriOrigin, 2);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        cache.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
//        byte[] cacheBytes = baos.toByteArray();//创建分配字节数组

//        if (null != cache && !cache.isRecycled()) {
//            cache.recycle();
//        }

//        dest.writeByteArray(cacheBytes);
        dest.writeInt(type);
        dest.writeValue(isChecked);
    }

    public static final Parcelable.Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };

    private MediaItem(Parcel source) {
        uriCropped = source.readParcelable(Uri.class.getClassLoader());
        uriOrigin = source.readParcelable(Uri.class.getClassLoader());
//        byte[] cacheBytes = source.createByteArray();
//        cache = BitmapFactory.decodeByteArray(cacheBytes, 0, cacheBytes.length);
        type = source.readInt();
        isChecked = (boolean) source.readValue(Boolean.class.getClassLoader());
    }
}
