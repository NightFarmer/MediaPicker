package com.nightfarmer.mediapicker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfan on 2015/10/27.
 */
public class MediaPickFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;
    RecyclerView recyclerViewSelected;

    List<MediaItem> dataList = new ArrayList<>();
    List<MediaItem> selectedList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.activity_media_pick, container, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        recyclerViewSelected = (RecyclerView) inflate.findViewById(R.id.recyclerViewSelected);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        recyclerView.setAdapter(new MediaItemGridAdapter(dataList));

        List<MediaItem> list = new ArrayList<>();
        MediaItem mediaItem = new MediaItem();
        mediaItem.setType(MediaItem.PHOTO);
        mediaItem.setUriOrigin(Uri.parse("http://www.baidu.com"));
        list.add(mediaItem);
        mediaItem = new MediaItem();
        mediaItem.setUriOrigin(Uri.parse("http://www.baidu.comxxxx"));
        list.add(mediaItem);
        mediaItem = new MediaItem();
        mediaItem.setUriOrigin(Uri.parse("http://www.baidu.comyyy"));
        list.add(mediaItem);
        dataList.addAll(list);


        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        final MediaPickerAdapter selectedAdapter = new MediaPickerAdapter(LinearLayout.HORIZONTAL);
        selectedList = selectedAdapter.dataList;
        recyclerViewSelected.setAdapter(selectedAdapter);
        Log.i("xx","onCreateView");
        return inflate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("xx", "onCreate");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("xx", "onActivityCreated");

        requestMedia();
    }

    private void requestMedia(){
        getLoaderManager().initLoader(0,null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String order = MediaStore.MediaColumns.DATE_ADDED + " DESC";

//        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 这个是视频
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                order);
        Log.i("xx", "onCreateLoader");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("xx", "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("xx", "onLoaderReset");
    }

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID
    };
}
