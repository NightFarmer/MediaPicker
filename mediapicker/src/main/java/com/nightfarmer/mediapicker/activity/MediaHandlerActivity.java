package com.nightfarmer.mediapicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nightfarmer.mediapicker.R;
import com.nightfarmer.mediapicker.bean.MediaItem;
import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;
import com.nightfarmer.mediapicker.widget.MediaPickerView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class MediaHandlerActivity extends Activity {

    boolean showTools = true;
    ArrayList<MediaItem> dataList;
    MediaImageLoaderImpl mMediaImageLoader;

    View tools;
    TextView percentText;
    private ToggleListener toggleListener;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_handler);
        Intent intent = getIntent();
        dataList = intent.getParcelableArrayListExtra(MediaPickerView.RESULT);
        MediaItem current = intent.getExtras().getParcelable(MediaPickerView.CURRENT);
        mMediaImageLoader = MediaImageLoaderImpl.getInstance(getApplicationContext());
        percentText = (TextView) findViewById(R.id.percent);
        tools = findViewById(R.id.tools);
        findViewById(R.id.button_Remove).setOnClickListener(new OnRemoveListener());
        findViewById(R.id.button_OK).setOnClickListener(new OnOkListener());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        toggleListener = new ToggleListener();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                percentText.setText((position + 1) + "/" + dataList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        int position = dataList.indexOf(current);
        viewPager.setCurrentItem(position);
        percentText.setText((position + 1) + "/" + dataList.size());
    }

    private class OnOkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(MediaPickerView.RESULT, dataList);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class OnRemoveListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (dataList.size() > 0) {
                dataList.remove(viewPager.getCurrentItem());
            }
            adapter.notifyDataSetChanged();
            if (dataList.size() == 0) {
                percentText.setText("无图片");
                if (toast == null) {
                    toast = Toast.makeText(MediaHandlerActivity.this, "已移除所有图片", Toast.LENGTH_SHORT);
                }
                toast.show();
                return;
            }
            percentText.setText((viewPager.getCurrentItem() + 1) + "/" + dataList.size());
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        public ViewPagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, container, false);
            imageLayout.setOnClickListener(toggleListener);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            mMediaImageLoader.displayImage(dataList.get(position).getUriOrigin(), imageView, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {     // 获取图片失败类型
                        case IO_ERROR:              // 文件I/O错误
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:        // 解码错误
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:        // 网络延迟
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:         // 内存不足
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:               // 原因不明
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(MediaHandlerActivity.this, message, Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);       // 不显示圆形进度条
                }
            });

            container.addView(imageLayout, 0);     // 将图片增加到ViewPager
            return imageLayout;
        }
    }

    private class ToggleListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showTools = !showTools;
            if (showTools) {
                tools.setVisibility(View.VISIBLE);
                percentText.setVisibility(View.VISIBLE);
            } else {
                tools.setVisibility(View.GONE);
                percentText.setVisibility(View.GONE);
            }
        }
    }

}
