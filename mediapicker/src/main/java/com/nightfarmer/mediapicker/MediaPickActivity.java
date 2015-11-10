package com.nightfarmer.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;

import java.util.ArrayList;
import java.util.List;


public class MediaPickActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;
    RecyclerView recyclerViewSelected;

//    List<MediaItem> dataList = new ArrayList<>();
    List<MediaItem> selectedList;

    MediaItemGridAdapter adapter;
    MediaPickerAdapter selectedAdapter;
    TextView okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_pick);
        ArrayList<MediaItem> parcelableArrayListExtra = getIntent().getParcelableArrayListExtra(MediaPickerView.RESULT);

        okButton = (TextView) findViewById(R.id.ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewSelected = (RecyclerView) findViewById(R.id.recyclerViewSelected);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        final MediaImageLoaderImpl mMediaImageLoader = MediaImageLoaderImpl.getInstance(getApplicationContext());
        adapter = new MediaItemGridAdapter(this, null, 0, mMediaImageLoader);
        recyclerView.setAdapter(adapter);

        adapter.setMediaItemClickListener(new MediaItemClickListener() {
            @Override
            public void onClick(View v, int type) {
                Log.i("xx", "onClick" + type);
            }
        });
        adapter.setOnCheckedListener(new MediaItemGridAdapter.OnCheckedListener() {
            @Override
            public void onChecked(boolean checked, View view) {
                Log.i("xx", "checked" + checked);
                final MediaItem tag = (MediaItem) view.getTag();
                if (checked) {
                    View img = (View) view.getTag(R.id.draweView);
                    if (img != null) {
                        img.setDrawingCacheEnabled(true);
                        tag.setCache(img.getDrawingCache(), MediaPickActivity.this);
                        img.setDrawingCacheEnabled(false);
                    }
                    selectedList.add(tag);
                    selectedAdapter.notifyItemInserted(selectedList.indexOf(tag));
                    recyclerViewSelected.scrollToPosition(selectedList.size());
                } else {
                    final int position = selectedList.indexOf(tag);
                    selectedList.remove(tag);
                    selectedAdapter.notifyItemRemoved(position);
                    recyclerViewSelected.scrollToPosition(position);
                }
                onSelectedItemListChanged();
            }
        });


        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectedAdapter = new MediaPickerAdapter(LinearLayout.HORIZONTAL, mMediaImageLoader);
        selectedList = selectedAdapter.dataList;
        recyclerViewSelected.setAdapter(selectedAdapter);
        selectedAdapter.setMediaItemClickListener(new MediaItemClickListener() {
            @Override
            public void onClick(View v, int type) {
                if (MediaItemClickListener.TYPE_MEDIA == type) {
                    final int indexOf = selectedList.indexOf(v.getTag());
                    if (indexOf < 0) return;
                    selectedList.remove(indexOf);
                    selectedAdapter.notifyItemRemoved(indexOf);
                    adapter.notifyDataSetChanged();
                    onSelectedItemListChanged();
                }
            }
        });
        adapter.setSelectedItemList(selectedList);
        selectedList.addAll(parcelableArrayListExtra);
        getSupportLoaderManager().initLoader(0, null, this);
        onSelectedItemListChanged();
    }

    private void onSelectedItemListChanged() {
        String str_ok = getResources().getString(R.string.ok);
        final int size = selectedList.size();
        String count = size > 0 ? "(" + size + ")" : "";
        okButton.setText(str_ok + count);
    }

    public void onOk(View view){
        Intent intent=getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MediaPickerView.RESULT, (ArrayList<? extends Parcelable>) selectedList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode){
            Log.i("xx", adapter.fileHolder+"ok");
            selectedList.add(new MediaItem(MediaItem.PHOTO, Uri.fromFile(adapter.fileHolder)));
            recyclerViewSelected.scrollToPosition(selectedList.size());
        }else if(Activity.RESULT_CANCELED == resultCode){
            Log.i("xx", adapter.fileHolder+"cancel");
        }
    }
}
