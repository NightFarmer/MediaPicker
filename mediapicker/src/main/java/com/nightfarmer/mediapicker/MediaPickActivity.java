package com.nightfarmer.mediapicker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MediaPickActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;
    RecyclerView recyclerViewSelected;

    List<MediaItem> dataList = new ArrayList<>();
    List<MediaItem> selectedList;

    MediaItemGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_pick);
//        setContentView(R.layout.laytou_content);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, new MediaPickFragment())
//                .commit();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewSelected = (RecyclerView) findViewById(R.id.recyclerViewSelected);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new MediaItemGridAdapter(this, null, 0);
        recyclerView.setAdapter(adapter);

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


        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final MediaPickerAdapter selectedAdapter = new MediaPickerAdapter(LinearLayout.HORIZONTAL);
        selectedList = selectedAdapter.dataList;
        recyclerViewSelected.setAdapter(selectedAdapter);


        getSupportLoaderManager().initLoader(0,null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_pick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String order = MediaStore.MediaColumns.DATE_ADDED + " DESC";
        CursorLoader cursorLoader = new CursorLoader(
                this,
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
        adapter.swapCursor(data);
        Log.i("xx", "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
        Log.i("xx", "onLoaderReset");
    }

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID
    };
}
