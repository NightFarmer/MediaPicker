package com.nightfarmer.mediapicker.imageloader.mediapicker.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nightfarmer.mediapicker.imageloader.MediaImageLoaderImpl;


/**
 * Created by TungDX
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    protected MediaImageLoaderImpl mMediaImageLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        FragmentHost host = (FragmentHost) activity;
        mMediaImageLoader = host.getImageLoader();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }
}
