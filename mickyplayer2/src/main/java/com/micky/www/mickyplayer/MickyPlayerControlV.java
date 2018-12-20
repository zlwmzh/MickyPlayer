package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Micky on 2018/12/19.
 * 控制层View,自定义Control需要集成ControlView，并实现抽象类MickyPlayerListener
 */

public class MickyPlayerControlV extends ControlView implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    // 查询进度
    protected Disposable mResearchProgress;
    // 进度查询时间
    protected long mSpeedRefreshUiTime = PlayerConfig.PROGRESS_REFRESH;

    public MickyPlayerControlV(@NonNull Context context) {
        this(context,null);
    }

    public MickyPlayerControlV(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MickyPlayerControlV(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

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
    /**
     * 初始化操作
     * @param context  上下文环境
     */
    protected void init(Context context)
    {
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


        // 首先添加的是信息图层
        changeTopView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
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
            if (PlayerHelper.getPlayerPlayState() == PlayerConfig.PLAYER_STATE_PAUSE )
            {
                // 暂停到播放
                start();
                return;
            }
            // 暂停
            pause();
            return;
        }
        if (id == R.id.control_full_small)
        {
            // 全屏或者缩放

            return;
        }
    }

    /**
     * 添加图层
     */
    protected void changeTopView()
    {
        if (PlayerHelper.getPlayerPlayState() == PlayerConfig.PLAYER_STATE_PLAYING ||
                PlayerHelper.getPlayerPlayState() == PlayerConfig.PLAYER_STATE_PAUSE)
        {
            addControlView();
            return;
        }
        addCoverView();
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
     * 开始播放
     */
    protected void start()
    {
        PlayerHelper.start();
        prgressListener();
    }

    /**
     * 暂停播放
     */
    protected void pause()
    {
        PlayerHelper.pause();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
         PlayerHelper.seekTo(seekBar.getProgress());
    }



    @Override
    protected MickyPlayerListener getPlayListener() {
        return listener;
    }

    // 视频播放回掉监听
    MickyPlayerListener listener = new MickyPlayerListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
             mControlSeekBar.setSecondaryProgress(i);
        }

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            changeTopView();
        }

        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            changeTopView();
            return false;
        }

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
           // addControlView();
        }

        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
           changeTopView();
        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

        }
    };

    /**
     * 查询进度
     */
    protected void prgressListener()
    {
        changeTopView();
        mResearchProgress = Observable.interval(mSpeedRefreshUiTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        if (PlayerHelper.getPlayerPlayState() == PlayerConfig.PLAYER_STATE_PLAYING)
                        {
                            // 如果是正在下载的话，回掉进度：
                            long duration = PlayerHelper.getDuration();
                            long currentPosition = PlayerHelper.getCurrentDuration();
                           // int progress = (int) (currentPosition * 100 / (duration == 0 ? 1 : duration));
                            mControlSeekBar.setProgress((int) currentPosition);
                            mControlSeekBar.setMax((int) duration);
                            mControlPlayTime.setText(MediaTimeUtils.stringForTime((int) currentPosition));
                            mControlTotalTime.setText(MediaTimeUtils.stringForTime((int) duration));
                        }else
                        {
                            // 其他情况关闭轮询
                            if (mResearchProgress != null)
                            {
                                mResearchProgress.dispose();
                            }
                        }
                    }
                });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mControlFrame.getParent() != null)
        {
           removeView(mControlFrame);
        }else
        {
            changeTopView();
        }

        return super.onTouchEvent(event);
    }

    public void  startFull()
    {
       // ViewGroup vp = getContext().findViewById(Window.ID_ANDROID_CONTENT);
    }
}
