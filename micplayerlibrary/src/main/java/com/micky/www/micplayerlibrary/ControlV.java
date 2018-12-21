package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

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
 * Created by Micky on 2018/12/21.
 * 控制层
 */

public class ControlV extends FrameLayout implements IjkMediaPlayer.OnBufferingUpdateListener,IjkMediaPlayer.OnPreparedListener
                            ,IjkMediaPlayer.OnCompletionListener,IjkMediaPlayer.OnVideoSizeChangedListener
                            ,IjkMediaPlayer.OnErrorListener,IjkMediaPlayer.OnTimedTextListener,IjkMediaPlayer.OnInfoListener
                            ,IjkMediaPlayer.OnSeekCompleteListener,View.OnClickListener
                            ,SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "ControlV";
    // 上下文环境
    protected Context context;
    // 视频是否准备好
    protected boolean isPrepare = false;
    // 查询进度
    protected Disposable mResearchProgress;


    // 视频信息图层
    protected FrameLayout mCoverFrame;
    // 控制层图层
    protected FrameLayout mControlFrame;
    // 信息图层视频封面
    protected SimpleDraweeView mCoverImg;
    // 信息图层标题
    protected TextView mCoverTitle;
    // 信息图层播放按钮
    protected ImageView mCoverBtnPlay;
    // 信息图层总时长
    protected TextView mCoverTotalTime;
    // 控制图层标题
    protected TextView mControlTitle;
    // 控制图层播放暂停按钮
    protected ImageView mControlBtnPlayOrPause;
    // 控制图层已播放时长
    protected TextView mControlPlayTime;
    // 控制图层总时长
    protected TextView mControlTotalTime;
    // 控制图层拖动进度条
    protected SeekBar mControlSeekBar;
    // 控制图层全屏或退出全屏按钮
    protected ImageView mControlBtnFullorSmall;

    public ControlV(@NonNull Context context) {
        this(context,null);
    }

    public ControlV(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlV(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     *  初始化操作
     * @param cotext  上下文环境
     */
    protected void init(Context cotext)
    {
        this.context = cotext;

        // 控制层图层布局
        mControlFrame = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_cotrolview,null);
        // 信息图层
        mCoverFrame = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_cover,null);

        mCoverImg = mCoverFrame.findViewById(R.id.cover_img);
        mCoverBtnPlay = mCoverFrame.findViewById(R.id.cover_play);
        mCoverTitle = mCoverFrame.findViewById(R.id.cover_title);
        mCoverTotalTime = mCoverFrame.findViewById(R.id.cover_time);

        mControlTitle = mControlFrame.findViewById(R.id.controlview_title);
        mControlBtnPlayOrPause = mControlFrame.findViewById(R.id.btn_control);
        mControlPlayTime = mControlFrame.findViewById(R.id.play_time);
        mControlSeekBar = mControlFrame.findViewById(R.id.seekbar);
        mControlTotalTime = mControlFrame.findViewById(R.id.play_total_time);
        mControlBtnFullorSmall = mControlFrame.findViewById(R.id.control_full_small);

        // 对各触发事件进行监听
        mCoverBtnPlay.setOnClickListener(this);
        mControlBtnPlayOrPause.setOnClickListener(this);
        mControlBtnFullorSmall.setOnClickListener(this);
        mControlSeekBar.setOnSeekBarChangeListener(this);

        changeShowView();

    }

    /**
     * 改变顶层图层：显示控制层还是信息层
     */
    protected void changeShowView()
    {
        if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PLAYING ||
                MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PAUSE)
        {
            addControlView();
        }else
        {
            addCoverView();
        }

    }

    /**
     * 添加控制层
     */
    protected void addControlView()
    {
        // 判断控制层是否为空
        if (mControlFrame == null) return;
        // 判断控制层是否有父view
        if (mControlFrame.getParent() != null) return;
        // 判断父布局是否有其他子类，如果有全部移除
        if (getChildCount() != 0) removeAllViews();
        // 添加view
        addView(mControlFrame);

    }

    /**
     * 添加信息层
     */
    protected void addCoverView()
    {
        // 判断信息层是否为空
        if (mCoverFrame == null) return;
        // 判断信息层是否有父view
        if (mCoverFrame.getParent() != null) return;
        // 判断父布局是否有其他子类，如果有全部移除
        if (getChildCount() != 0) removeAllViews();
        // 添加view
        addView(mCoverFrame);
    }

    /**
     *  创建Player
     *  @param  url 视频路径
     *  @param  surfaceView 显示布局
     */
    protected void createPlayer(String url, SurfaceView surfaceView)
    {
        if (MickyMediaPlayer.getInstance() != null)
        {
            // 重置之前设置的参数
            MickyMediaPlayer.getInstance().stop();
            MickyMediaPlayer.getInstance().setDisplay(null);
            MickyMediaPlayer.getInstance().release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        //准备好不自动播放，0:禁用自动播放  1:自动播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        // 添加seekTo支持
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        MickyMediaPlayer.setMediaPlayer(ijkMediaPlayer);
        MickyMediaPlayer.getInstance().setOnPreparedListener(this);
        MickyMediaPlayer.getInstance().setOnInfoListener(this);
        MickyMediaPlayer.getInstance().setOnSeekCompleteListener(this);
        MickyMediaPlayer.getInstance().setOnBufferingUpdateListener(this);
        MickyMediaPlayer.getInstance().setOnErrorListener(this);
        MickyMediaPlayer.getInstance().setOnCompletionListener(this);
        MickyMediaPlayer.getInstance().setOnVideoSizeChangedListener(this);
        MickyMediaPlayer.getInstance().setOnTimedTextListener(this);


        // 设置播放路径
        try {
            MickyMediaPlayer.getInstance().setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mediaPlayer设置视图
        MickyMediaPlayer.getInstance().setDisplay(surfaceView.getHolder());
        MickyMediaPlayer.getInstance().prepareAsync();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cover_play)
        {
            // 信息层点击播放
            // 控制层播放暂停按钮
            mControlBtnPlayOrPause.setSelected(!mControlBtnPlayOrPause.isSelected());
            start();
            return;
        }
        if (id == R.id.btn_control)
        {
            // 控制层播放暂停按钮
            mControlBtnPlayOrPause.setSelected(!mControlBtnPlayOrPause.isSelected());
            if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PAUSE)
            {
                start();
                return;
            }
            if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PLAYING)
            {
                pause();
            }
            return;
        }
        if (id == R.id.control_full_small)
        {
            // 全屏或者缩放

            return;
        }
    }

    /**
     * 开始播放
     */
    protected void start()
    {
        // 视频未准备好，不能播放
        if (!isPrepare) return;
        MickyMediaPlayer.start();
        changeShowView();
        progressListener();
    }

    /**
     *  暂停播放
     */
    protected void pause()
    {
        MickyMediaPlayer.pause();
    }


    /**
     * 进度监听
     */
    protected void progressListener()
    {
        mResearchProgress = Observable.interval(MicPlayerConfig.PROGRESS_REFRESH, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PLAYING)
                        {
                            // 如果是正在下载的话，回掉进度：
                            refreshProgressUI();
                        }else
                        {
                            // 其他情况关闭轮询
                            mResearchProgress.dispose();
                        }
                    }
                });
    }

    protected void refreshProgressUI()
    {
        long duration = MickyMediaPlayer.getDuration();
        long currentPosition = MickyMediaPlayer.getCurrentDuration();
        // int progress = (int) (currentPosition * 100 / (duration == 0 ? 1 : duration));
        mControlSeekBar.setProgress((int) currentPosition);
        mControlSeekBar.setMax((int) duration);
        mControlPlayTime.setText(MediaTimeUtils.stringForTime((int) currentPosition));
        mControlTotalTime.setText(MediaTimeUtils.stringForTime((int) duration));
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
       // 准备完成后才可以调用start方法
        Log.d(TAG,"onPrepared");
        isPrepare = true;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        Log.d(TAG,"onBufferingUpdate");
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
       Log.d(TAG,"onCompletion");
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.d(TAG,"onError");
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.d(TAG,"onInfo");
        return false;
    }



    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        Log.d(TAG,"onSeekComplete");
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
        Log.d(TAG,"onTimedText");
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        Log.d(TAG,"onVideoSizeChanged");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
          MickyMediaPlayer.seekTo(seekBar.getProgress());
    }
}
