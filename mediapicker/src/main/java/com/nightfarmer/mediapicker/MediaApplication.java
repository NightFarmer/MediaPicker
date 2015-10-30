package com.nightfarmer.mediapicker;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by zhangfan on 2015/10/28.
 */
public class MediaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
//                .writeDebugLogs() // 打印log信息
//                .threadPoolSize(5) //推荐配置1-5 减少线程池中线程的个数
//                .memoryCache(new WeakMemoryCache()) // 见下面注释
//                .build();
        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);

//        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(this)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .memoryCacheSizePercentage(30)
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                .writeDebugLogs().threadPoolSize(3)
//                .build();

//        ImageLoader.getInstance().init(imageLoaderConfig);
//        Fresco.initialize(this);
    }
}
