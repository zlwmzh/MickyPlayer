package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Micky on 2018/12/18.
 * 视频播放展示层
 */

public class MickyPlayerView extends FrameLayout implements IjkMediaPlayer.OnPreparedListener
                            ,IjkMediaPlayer.OnBufferingUpdateListener,IjkMediaPlayer.OnInfoListener
                            ,IjkMediaPlayer.OnSeekCompleteListener,IjkMediaPlayer.OnErrorListener
                            ,IjkMediaPlayer.OnCompletionListener,IjkMediaPlayer.OnVideoSizeChangedListener
                            ,IjkMediaPlayer.OnTimedTextListener{
    private static final String TAG = "MickyPlayerView";
    // 上下文环境
    protected Context context;
    // 视频预览View
    protected SurfaceView mSurfaceView;
    // 外部图层
    protected ControlView mControlView;
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
     * 添加图层
     */
    protected void addDefaultTopView()
    {
        if (mControlView == null)
        {
            mControlView = new MickyPlayerControlV(context);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            mControlView.setLayoutParams(params);
            addContrlView(mControlView);
        }
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
        if (mSurfaceView == null)
        {
            // 第一次播放视频，创建SurfaceView
            createSurfaceView();
        }else
        {
            // 直接播放
            load();
        }
        // 添加顶层view
        addDefaultTopView();
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
        //准备好不自动播放，0:禁用自动播放  1:自动播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        // 添加seekTo支持
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        PlayerHelper.setMediaPlayer(ijkMediaPlayer);
        PlayerHelper.getInstance().setOnPreparedListener(this);
        PlayerHelper.getInstance().setOnInfoListener(this);
        PlayerHelper.getInstance().setOnSeekCompleteListener(this);
        PlayerHelper.getInstance().setOnBufferingUpdateListener(this);
        PlayerHelper.getInstance().setOnErrorListener(this);
        PlayerHelper.getInstance().setOnCompletionListener(this);
        PlayerHelper.getInstance().setOnVideoSizeChangedListener(this);
        PlayerHelper.getInstance().setOnTimedTextListener(this);


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

    /**
     * 准备状态处理的事情
     */
    protected void onStatePrepared()
    {
        Log.d(TAG,"onStatePrepared");
        PlayerHelper.setPlayerPlayState(PlayerConfig.PLAYER_STATE_PREPARING);
    }

    /**
     * 缓存更新状态
     */
    protected void onStateOnBufferingUpdate()
    {
       // PlayerHelper.setPlayerPlayState(PlayerConfig.PLAYER_STATE_PLAYING);
    }

    /**
     * 快进完成
     */
    protected void onStateOnSeekComplete()
    {
        Log.d(TAG,"onStateOnSeekComplete");
    }
    /**
     * 播放信息
     */
    protected void onStateOnInfo()
    {
        Log.d(TAG,"onStateOnInfo");
    }

    /**
     * 播放错误状态
     */
    protected void onStateOnError()
    {
        Log.d(TAG,"onStateOnError");
        PlayerHelper.setPlayerPlayState(PlayerConfig.PLAYER_STATE_ERROR);
    }

    /**
     * 播放完成
     */
    protected void onStateOnComplete()
    {
        PlayerHelper.setPlayerPlayState(PlayerConfig.PLAYER_STATE_COMPLETE);
    }



    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        // 准备播放
        onStatePrepared();
        if (listener != null)
        {
              listener.onPrepared(iMediaPlayer);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        // 缓冲进度
        onStateOnBufferingUpdate();
        Log.d(TAG,"onStateOnBufferingUpdate："+i);
       if (listener != null)
        {
            listener.onBufferingUpdate(iMediaPlayer,i);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        onStateOnInfo();
        if (listener != null)
        {
            listener.onInfo(iMediaPlayer,i,i1);
        }
        return false;
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        onStateOnSeekComplete();
        if (listener != null)
        {
            listener.onSeekComplete(iMediaPlayer);
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        onStateOnError();
        if (listener != null)
        {
            listener.onError(iMediaPlayer,i,i1);
        }
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        onStateOnComplete();
        if (listener != null)
        {
            listener.onCompletion(iMediaPlayer);
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        Log.d(TAG,"onVideoSizeChanged");
        if (listener != null)
        {
            listener.onVideoSizeChanged(iMediaPlayer,i,i1,i2,i3);
        }
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
        // 字幕解析，暂不用
        Log.d(TAG,"onTimedText："+ijkTimedText.getText());
    }


}
