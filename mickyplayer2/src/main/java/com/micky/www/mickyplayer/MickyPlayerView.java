package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Micky on 2018/12/18.
 * 视频播放展示层
 */

public class MickyPlayerView extends FrameLayout{

    // ijkplayer提供，用于播放视频，需要给他传入一个surfaceView显示
    protected IMediaPlayer mMediaPlayer = null;
    // 上下文环境
    protected Context context;
    // 视频预览View
    protected SurfaceView mSurfaceView;
    // 视频播放地址
    protected String mMediaPath;

    public MickyPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public MickyPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MickyPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     * @param context 上下文环境
     */
    protected void init(Context context)
    {
        this.context = context;
    }

    /**
     *  设置多媒体播放地址
     * @param url 地址
     * @return
     */
    public MickyPlayerView setMediaPath(String url)
    {
        // 设置视频播放路径
        this.mMediaPath = url;
        if (TextUtils.isEmpty(url))
        {
            // 第一次播放视频，创建SurfaceView
            createSurfaceView();
        }else
        {
            // 直接播放
            // TODO
        }
        return this;
    }

    /**
     * 创建视频预览SurfaceView
     */
    protected void createSurfaceView()
    {
        // mSurfaceView 不为空 且已经被添加
        if (mSurfaceView != null && mSurfaceView.getParent() != null) return;
        // 实例化SurfaceView，并添加到容器中
        mSurfaceView = new SurfaceView(context);
        // 设置宽高,居中显示
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mSurfaceView.setLayoutParams(params);
        addView(mSurfaceView);
    }
}
