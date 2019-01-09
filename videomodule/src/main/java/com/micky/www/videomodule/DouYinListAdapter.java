package com.micky.www.videomodule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.micky.www.micplayerlibrary.MickyPlayerView;

import java.util.List;

/**
 * Created by Micky on 2019/1/9.
 */

public class DouYinListAdapter extends RecyclerView.Adapter<DouYinListAdapter.DouYinViewHolder>{
    private List<VideoBean> mList;
    private Context context;
    private MickyPlayerView mickyPlayerView;
    public DouYinListAdapter(Context context,List<VideoBean> list)
    {
        this.context = context;
        this.mList = list;
    }

    @Override
    public DouYinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DouYinViewHolder(LayoutInflater.from(context).inflate(R.layout.item_douyin,parent,false));
    }

    @Override
    public void onBindViewHolder(DouYinViewHolder holder, int position) {
       if (mickyPlayerView == null)
       {
           play(position,holder);
       }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void play(int position, DouYinViewHolder viewHolder)
    {
        MickyPlayerView mickyPlayerView = getMickyPlayerView();
        mickyPlayerView.releasePlayer();
        viewHolder.mVideoContainer.addView(mickyPlayerView);
        mickyPlayerView.setVideoPath(mList.get(position).getVideoUrl());
    }

    private MickyPlayerView getMickyPlayerView()
    {
        if (mickyPlayerView == null)
        {
            mickyPlayerView = new MickyPlayerView(context);
        }
        return mickyPlayerView;
    }

    class DouYinViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout mVideoContainer;
        public DouYinViewHolder(View itemView) {
            super(itemView);
            mVideoContainer = itemView.findViewById(R.id.video_container);
        }
    }
}
