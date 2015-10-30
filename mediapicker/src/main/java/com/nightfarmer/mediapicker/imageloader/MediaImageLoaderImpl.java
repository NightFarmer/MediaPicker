package com.nightfarmer.mediapicker.imageloader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.nightfarmer.mediapicker.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * @author TUNGDX
 */

public class MediaImageLoaderImpl {

    static MediaImageLoaderImpl mediaImageLoader;

    public static synchronized MediaImageLoaderImpl getInstance(Context context) {
        if (mediaImageLoader == null) {
            mediaImageLoader = new MediaImageLoaderImpl(context.getApplicationContext());
        }
        return mediaImageLoader;
    }

    ;

    private MediaImageLoaderImpl(Context context) {
        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCacheSizePercentage(30)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                .writeDebugLogs().threadPoolSize(3)
                .build();

        ImageLoader.getInstance().init(imageLoaderConfig);
    }

    public void displayImage(Uri uri, ImageView imageView, ImageLoadingListener listener) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.color.picker_imageloading)
                .cacheOnDisk(false)
                .considerExifParams(true).resetViewBeforeLoading(true).build();

        ImageAware imageAware = new ImageViewAware(imageView, false);
        ImageLoader.getInstance().displayImage(uri.toString(), imageAware, displayImageOptions, listener, null);
    }

    public void displayImage(Uri uri, ImageView imageView) {
        displayImage(uri, imageView, null);
    }
}