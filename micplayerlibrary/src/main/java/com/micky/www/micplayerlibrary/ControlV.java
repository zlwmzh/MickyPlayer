package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
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

import static tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_INFO_BUFFERING_END;
import static tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_INFO_BUFFERING_START;

/**
 * Created by Micky on 2018/12/21.
 * 控制层
 */

public class ControlV extends MickyPlayerGestureFrameLayout implements IjkMediaPlayer.OnBufferingUpdateListener,IjkMediaPlayer.OnPreparedListener
                            ,IjkMediaPlayer.OnCompletionListener,IjkMediaPlayer.OnVideoSizeChangedListener
                            ,IjkMediaPlayer.OnErrorListener,IjkMediaPlayer.OnTimedTextListener,IjkMediaPlayer.OnInfoListener
                            ,IjkMediaPlayer.OnSeekCompleteListener,View.OnClickListener
                            ,SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "ControlV";
    // 上下文环境
    protected Context context;
    // 播放链接
    protected String url;
    // 标题
    protected String mVideoTitle;
    // 封面
    protected String mVideoCover;
    // 视频时长
    protected String mVideoTotalTime;
    // 视频是否准备好
    protected boolean isPrepare = false;
    // 视频当前的状态：全屏、非全屏
    protected int mPlayScreenState = MicPlayerConfig.HORIZONTAL_SCREEN;
    // 查询进度
    protected Disposable mResearchProgress;
    // 控制层隐藏显示
    protected Disposable mControlHide;
    // 控制层隐藏时间
    protected long mControlViewDelayTime = MicPlayerConfig.CONTROL_HIDE_TIME;
    // 控制层回掉接口
    protected ControlVListener mListener;


    // 视频信息图层
    protected FrameLayout mCoverFrame;
    // 控制层图层
    protected FrameLayout mControlFrame;
    // loading层
    protected FrameLayout mLoadingFrame;
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
        // loading图层
        mLoadingFrame = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.layout_loading,null);
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
        // 获取播放状态
        int state = MickyMediaPlayer.getPlayerPlayState();
        // 如果视频处于loading状态，则不处理任务添加图层的操作
        if (state == MicPlayerConfig.PLAYER_STATE_BUFFER)
        {
            return;
        }
        if (state == MicPlayerConfig.PLAYER_STATE_PLAYING ||
                state == MicPlayerConfig.PLAYER_STATE_PAUSE)
        {
            addControlView();
        }else
        {
            addCoverView();
        }
        // 如果播放状态的话，需要延迟相关时间，移除控制层
        controlVRemoveDelay();
    }

    /**
     * 添加控制层
     */
    protected void addControlView()
    {
        // 判断控制层是否为空
        if (mControlFrame == null) return;
        // 判断控制层是否有父view
        if (mControlFrame.getParent() != null)
        {
            removeControlView();
            return;
        }
        // 判断父布局是否有其他子类，如果有全部移除
        if (getChildCount() != 0) removeAllViews();
        // 添加view
        addView(mControlFrame);

    }

    /**
     * 移除控制层
     */
    protected void removeControlView()
    {
        // 判断控制层是否为空
        if (mControlFrame == null) return;
        // 判断控制层是否有父view
        if (mControlFrame.getParent() == null) return;
        removeView(mControlFrame);
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
     * 移除信息层
     */
    protected void removeCoverView()
    {
        // 判断信息层是否为空
        if (mCoverFrame == null) return;
        // 判断信息层是否有父view
        if (mCoverFrame.getParent() == null) return;
        removeView(mCoverFrame);
    }

    /**
     * 添加loading层
     */
    protected void addLoadingView()
    {
        // 判断信息层是否为空
        if (mLoadingFrame == null) return;
        // 判断信息层是否有父view
        if (mLoadingFrame.getParent() != null) return;
        // 判断父布局是否有其他子类，如果有全部移除
        if (getChildCount() != 0) removeAllViews();
        // 添加view
        addView(mLoadingFrame);
    }

    /**
     * 移除loading层
     */
    protected void removeLoadingView()
    {
        // 判断信息层是否为空
        if (mLoadingFrame == null) return;
        // 判断信息层是否有父view
        if (mLoadingFrame.getParent() == null) return;
        removeView(mLoadingFrame);
    }

    /**
     *  创建Player
     *  @param  url 视频路径
     *  @param  surface 视频显示画面
     */
    protected void createPlayer(String url, Surface surface)
    {
        this.url = url;
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
        // 播放时屏幕常亮
        ijkMediaPlayer.setScreenOnWhilePlaying(true);
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
        MickyMediaPlayer.getInstance().setSurface(surface);
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
            if (mListener != null) mListener.firstPlay();
           // MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_PLAYING);
            // changeShowView();
          //  progressListener();
            return;
        }
        if (id == R.id.btn_control)
        {
            // 控制层播放暂停按钮
            mControlBtnPlayOrPause.setSelected(!mControlBtnPlayOrPause.isSelected());
            if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PAUSE || MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_COMPLETE)
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
            mControlBtnFullorSmall.setSelected(!mControlBtnFullorSmall.isSelected());
            if (mListener != null) mListener.fullOrSmallScreen((mPlayScreenState = (mPlayScreenState == MicPlayerConfig.HORIZONTAL_SCREEN ? MicPlayerConfig.FULL_SCREEN : MicPlayerConfig.HORIZONTAL_SCREEN)));
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
        // 获取之前的播放进度，直接跳转原播放进度
       // MickyMediaPlayer.seekTo(MickyUtils.getSavedPlayPosition(context,url));
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
     *  重置view
     */
    protected void resetView()
    {
        mControlBtnPlayOrPause.setSelected(false);
        changeShowView();
    }

    /**
     * 初始化时长
     */
    protected void initTimeInfo()
    {
        String time = MediaTimeUtils.stringForTime((int) MickyMediaPlayer.getDuration());
        mCoverTotalTime.setText(time);
        mControlPlayTime.setText(MediaTimeUtils.stringForTime((int) MickyMediaPlayer.getCurrentDuration()));
        mControlTotalTime.setText(time);
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

                        if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PLAYING
                                || MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_BUFFER)
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

    /**
     * 刷新进度UI
     */
    protected void refreshProgressUI()
    {
        long duration = MickyMediaPlayer.getDuration();
        long currentPosition = MickyMediaPlayer.getCurrentDuration();
        // int progress = (int) (currentPosition * 100 / (duration == 0 ? 1 : duration));
        mControlSeekBar.setProgress((int) currentPosition);
        mControlSeekBar.setMax((int) duration);
        mControlPlayTime.setText(MediaTimeUtils.stringForTime((int) currentPosition));

        // 保存当前进度
        MickyUtils.savePlayPosition(context,url,currentPosition);
    }

    /**
     *  延迟移除control view：无操作状态下不显示控制窗口
     */
    protected void controlVRemoveDelay()
    {
       // 每次发出延迟之前关闭上一次延迟
       if (mControlHide != null) mControlHide.dispose();
       mControlHide = Observable.timer(mControlViewDelayTime,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (MickyMediaPlayer.getPlayerPlayState() == MicPlayerConfig.PLAYER_STATE_PLAYING)removeControlView();
                    }
                });
    }

    /**
     * 获取屏幕状态
     * @return
     */
    public int getScreenState()
    {
        return mPlayScreenState;
    }

    /**
     * 设置控制层的回掉监听
     * @param listener
     * @return
     */
    public ControlV setControlVListener(ControlVListener listener)
    {
        this.mListener = listener;
        return this;
    }

    /**
     * 设置视频相关信息
     * @param title 标题
     * @param cover 封面
     * @param totalTime 总时长
     * @return
     */
    public ControlV setVideoInfo(String title,String cover,String totalTime)
    {
        setVideoTitle(title);
        setVideoCover(cover);
        setVideoTotalTime(totalTime);
        return this;
    }

    /**
     * 设置视频标题
     * @param title
     * @return
     */
    public ControlV setVideoTitle(String title)
    {
        this.mVideoTitle = title;
        mControlTitle.setText(title);
        mCoverTitle.setText(title);
        return this;
    }

    /**
     * 设置视频封面
     * @param cover
     * @return
     */
    public ControlV setVideoCover(String cover)
    {
        this.mVideoCover = cover;
        mCoverImg.setImageURI(cover);
        return this;
    }

    public ControlV setVideoTotalTime(String time)
    {
        if (TextUtils.isEmpty(time))
        {
            time = MediaTimeUtils.stringForTime((int) MickyMediaPlayer.getDuration());
        }
        this.mVideoTotalTime = time;
        mCoverTotalTime.setText(time);
        mControlPlayTime.setText(MediaTimeUtils.stringForTime((int) MickyMediaPlayer.getCurrentDuration()));
        mControlTotalTime.setText(time);
        return this;
    }

    /**
     * 视频缓冲处理
     * @param iMediaPlayer
     * @param i
     * @param i1
     */
    protected void buffer(IMediaPlayer iMediaPlayer, int i, int i1)
    {
        Log.d(TAG,"缓冲："+i +"; "+i1+"播放器状态："+MickyMediaPlayer.getPlayerPlayState());
        switch (i)
        {
            case MEDIA_INFO_BUFFERING_START:
                MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_BUFFER);
                addLoadingView();
                break;
            case MEDIA_INFO_BUFFERING_END:
                MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_PLAYING);
                removeLoadingView();
                break;
        }
    }

    @Override
    public void onSingleTapGesture(MotionEvent e) {
        // 单击
        changeShowView();
    }

    @Override
    public void onDoubleTapGesture(MotionEvent e) {
        // 调用暂停或者是播放的点击事件
        mControlBtnPlayOrPause.performClick();
    }

    @Override
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 亮度调节
    }

    @Override
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 音量调节

    }

    @Override
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 快进快退

    }


    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_PREPARING);
       // 准备完成后才可以调用start方法
        Log.d(TAG,"onPrepared");
        isPrepare = true;
        initTimeInfo();
        start();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        Log.d(TAG,"onBufferingUpdate");
        mControlSeekBar.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
       MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_COMPLETE);
       resetView();
       Log.d(TAG,"onCompletion");
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        MickyMediaPlayer.setPlayerPlayState(MicPlayerConfig.PLAYER_STATE_ERROR);
        changeShowView();
        Log.d(TAG,"onError");
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.d(TAG,"onInfo");
        buffer(iMediaPlayer,i,i1);
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
        Log.d(TAG,"onVideoSizeChanged："+"i："+i+"；i1："+i1+"；i2："+i2+"；i3："+i3);
        if (mListener != null) mListener.videoSize(i,i1);
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
