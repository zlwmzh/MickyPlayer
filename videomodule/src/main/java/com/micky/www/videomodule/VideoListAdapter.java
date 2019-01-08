package com.micky.www.videomodule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.micky.www.micplayerlibrary.MickyPlayerView;

import java.util.List;

/**
 * Created by Administrator on 2019/1/8.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>{
    private List<VideoBean> mList;
    private Context context;
    private MickyPlayerView mPlayerView;
    private int mCurrentPlayPosition = -1;

    public VideoListAdapter(Context context, List<VideoBean> list)
    {
        this.mList = list;
        this.context = context;
    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video_list,parent,false));
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, int position) {
       VideoBean videoBean = mList.get(position);
       holder.mCoverImg.setImageURI(videoBean.getVideoCover());
       holder.mCoverTitle.setText(videoBean.getVideoName());
       holder.mCoverTotalTime.setText(videoBean.getVideoTime());

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 初始化播放器
     */
    protected MickyPlayerView getPlayerView()
    {
        if (mPlayerView == null)
        {
            mPlayerView = new MickyPlayerView(context);
        }
        return mPlayerView;
    }

    /**
     * 获取当前播放位置
     * @return
     */
    protected int getCurrentPosition()
    {
        return mCurrentPlayPosition;
    }

    class VideoListViewHolder extends RecyclerView.ViewHolder {
        // 信息图层视频封面
        SimpleDraweeView mCoverImg;
        // 信息图层标题
        TextView mCoverTitle;
        // 信息图层播放按钮
        ImageView mCoverBtnPlay;
        // 信息图层总时长
        TextView mCoverTotalTime;
        public VideoListViewHolder(final View itemView) {
            super(itemView);
            mCoverImg = itemView.findViewById(R.id.cover_img);
            mCoverBtnPlay = itemView.findViewById(R.id.cover_play);
            mCoverTitle = itemView.findViewById(R.id.cover_title);
            mCoverTotalTime = itemView.findViewById(R.id.cover_time);

            mCoverBtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurrentPlayPosition = getAdapterPosition();
                    VideoBean videoBean = mList.get(getAdapterPosition());
                    MickyPlayerView mickyPlayerView = getPlayerView();
                    mickyPlayerView.releasePlayer();
                    // 设置播放路径
                    mickyPlayerView.setVideoPath(videoBean.getVideoUrl());
                    ((FrameLayout)itemView).addView(mickyPlayerView);
                }
            });
        }
    }
}
