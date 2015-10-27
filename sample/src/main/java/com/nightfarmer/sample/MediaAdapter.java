package com.nightfarmer.sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nightfarmer.mediapicker.MediaItem;

import java.util.List;

/**
 *
 * Created by zhangfan on 2015/10/26.
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaItemViewHolder> {

    List<MediaItem> dataList;

    public MediaAdapter(List<MediaItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater.from(parent.getContext()).inflate()
        final TextView textView = new TextView(parent.getContext());
        return new MediaItemViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(MediaItemViewHolder holder, int position) {
        String s = dataList.get(position).getUriOrigin().toString();
        holder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class MediaItemViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MediaItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
