package com.nightfarmer.mediapicker;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by zhangfan on 2015/10/28.
 */
public class MediaUtils {
    public static Uri getPhotoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getVideoUri(Cursor cursor) {
        return getMediaUri(cursor, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static Uri getMediaUri(Cursor cursor, Uri uri) {
        String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        return Uri.withAppendedPath(uri, id);
    }



    /**
     * Get path of image from uri
     *
     * @param contentResolver
     * @param contentURI
     * @return path of image. Null if not found.
     */
    public static String getRealImagePathFromURI(ContentResolver contentResolver,
                                                 Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null,
                null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }


    /**
     * Get path of video from uri
     *
     * @param contentResolver
     * @param contentURI
     * @return path of video. Null if not found.
     */
    public static String getRealVideoPathFromURI(ContentResolver contentResolver,
                                                 Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null,
                null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }
}
