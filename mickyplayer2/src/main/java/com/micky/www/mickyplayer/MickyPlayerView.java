package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Micky on 2018/12/18.
 * 视频播放展示层
 */

public class MickyPlayerView extends FrameLayout{

    // 上下文环境
    protected Context context;
    // 视频预览View
    protected SurfaceView mSurfaceView;
    // 播放器回掉接口
    protected MickyPlayerListener listener;
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
            load();
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
        // 关联SurfaceView回掉
        mSurfaceView.getHolder().addCallback(ICallBack);
        // 设置宽高,居中显示
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mSurfaceView.setLayoutParams(params);
        addView(mSurfaceView);
    }

    SurfaceHolder.Callback2 ICallBack = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
           // 创建成功后开始播放视频
            load();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };

    /**
     * 加载多媒体
     */
    protected void load()
    {
       // 每次都重复建立MediaPlayer
        createPlayer();
    }

    /**
     *  创建Player
     */
    protected void createPlayer()
    {
        if (PlayerHelper.getInstance() != null)
        {
            // 重置之前设置的参数
            PlayerHelper.getInstance().stop();
            PlayerHelper.getInstance().setDisplay(null);
            PlayerHelper.getInstance().release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

        PlayerHelper.setMediaPlayer(ijkMediaPlayer);

        if (listener != null) {
            PlayerHelper.getInstance().setOnPreparedListener(listener);
            PlayerHelper.getInstance().setOnInfoListener(listener);
            PlayerHelper.getInstance().setOnSeekCompleteListener(listener);
            PlayerHelper.getInstance().setOnBufferingUpdateListener(listener);
            PlayerHelper.getInstance().setOnErrorListener(listener);
        }

        // 设置播放路径
        try {
            PlayerHelper.getInstance().setDataSource(mMediaPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mediaPlayer设置视图
        PlayerHelper.getInstance().setDisplay(mSurfaceView.getHolder());
        PlayerHelper.getInstance().prepareAsync();
    }

    /**
     * 添加控制层
     * @param view
     * @return
     */
    public MickyPlayerView addContrlView(ControlView view)
    {
        this.addView(view);
        setPlayerListener(view.getPlayListener());
        return this;
    }


    /**
     *  设置播放器监听接口
     * @param listener
     * @return
     */
    protected void setPlayerListener(MickyPlayerListener listener)
    {
        this.listener = listener;
    }
}
