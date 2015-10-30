package com.nightfarmer.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nightfarmer.mediapicker.MediaItem;
import com.nightfarmer.mediapicker.MediaPickerAdapter;
import com.nightfarmer.mediapicker.MediaPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//    @Bind(R.id.recyclerView)
//    RecyclerView recyclerView;

    LinearLayoutManager layout;

    @Bind(R.id.mediaPickerView)
    MediaPickerView mediaPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        List<MediaItem> list = new ArrayList<>();
//        MediaItem mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//        mediaItem = new MediaItem(MediaItem.PHOTO, Uri.parse("content://media/external/images/media/117345"));
//        list.add(mediaItem);
//
////        final MediaAdapter adapter = new MediaAdapter(list);
////        layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
////        recyclerView.setLayoutManager(layout);
////        recyclerView.setAdapter(adapter);
//        final MediaPickerAdapter adapter = mediaPickerView.getAdapter();
////        adapter.dataList.addAll(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("xx", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mediaPickerView.handleResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        Log.i("xx", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i("xx", "onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i("xx", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("xx", "onPause");
        super.onPause();
    }
}
