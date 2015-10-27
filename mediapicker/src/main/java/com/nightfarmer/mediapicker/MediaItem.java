package com.nightfarmer.mediapicker;

import android.net.Uri;

import java.io.Serializable;

/**
 * 多媒体资源描述类
 * Created by zhangfan on 2015/10/26.
 */
public class MediaItem implements Serializable{
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

    public static int getPHOTO() {
        return PHOTO;
    }

    public static int getVIDEO() {
        return VIDEO;
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
}
