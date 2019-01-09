package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Micky on 2018/12/21.
 * 视频播放器：视频图像层(视频影响显示层)、控制层(播放、快进、当前播放进度、全屏按钮等)及信息层（开始封面、结束封面）
 */

public class MickyPlayerView extends FrameLayout implements MickyVlListener,ControlVListener{
    private static final String TAG = "MickyPlayerView";

    // 上下文环境
    protected Context context;
    // 图像层
    protected MickyV mickyV;
    // 控制层
    protected ControlV mControlV;
    // 视频路径
    protected String mVideoPath;
    public MickyPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public MickyPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MickyPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     * @param context
     */
    protected void init(Context context)
    {
        this.context = context;
        addTopView();
    }

    /**
     * 添加视频图像层
     */
    protected void addTopView()
    {
        addMickV();
        addControlV();
    }

    /**
     * 添加图像层
     */
    protected void addMickV()
    {
        mickyV = new MickyV(context);
        // 设置回调监听
        mickyV.setMickyVListener(this);
        addView(mickyV);
    }

    /**
     * 添加控制层，需要在图像层准备完成后在添加
     */
    protected void addControlV()
    {
        // 控制信息层
        mControlV = new ControlV(context);
        mControlV.setControlVListener(this);
        addView(mControlV);
    }

    /**
     * 设置视频路径
     * @param url
     * @return
     */
    public MickyPlayerView setVideoPath(String url)
    {
        this.mVideoPath = url;
        // 判断SurfaceVie
        if (mickyV.getSurfaceView() == null)
        {
            // 创建
            mickyV.createSurfaceView();

        }else
        {
             prepareMediaPlayer();
        }
        return this;
    }


    /**
     * 设置视频相关信息
     * @param title 标题
     * @param cover 封面
     * @param totalTime 总时长
     * @return
     */
    public MickyPlayerView setVideoInfo(String title,String cover,String totalTime)
    {
        mControlV.setVideoInfo(title,cover,totalTime);
        return this;
    }

    /**
     * 设置视频标题
     * @param title
     * @return
     */
    public MickyPlayerView setVideoTitle(String title)
    {
        mControlV.setVideoTitle(title);
        return this;
    }

    /**
     * 设置视频封面
     * @param cover
     * @return
     */
    public MickyPlayerView setVideoCover(String cover)
    {

        mControlV.setVideoCover(cover);
        return this;
    }

    /**
     * 设置视频总时长
     * @param time
     * @return
     */
    public MickyPlayerView setVideoTotalTime(String time)
    {
        mControlV.setVideoTotalTime(time);
        return this;
    }

    /**
     * 释放播放器
     * @return
     */
    public MickyPlayerView releasePlayer()
    {
        ViewGroup viewGroup = (ViewGroup) getParent();
        // 移除播放器
        if (viewGroup != null) viewGroup.removeView(this);
        mControlV.releaseView();
        mickyV.releaseView();
        return this;
    }

    /**
     * 创建mediaplayer 并开始准备
     */
    protected void prepareMediaPlayer()
    {
        // 准备播放
      //  if (mickyV.getSurfaceView().getSurfaceTexture() == null) return;
        mControlV.createPlayer(mVideoPath,mickyV.getSurface());
       // MickyMediaPlayer.getInstance().setDisplay(mickyV.getSurfaceView().getHolder());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface) {
        // 准备播放
        prepareMediaPlayer();
    }


    @Override
    public void fullOrSmallScreen(int screenState) {
      changeView(screenState);
    }

    @Override
    public void firstPlay() {

    }

    @Override
    public void videoSize(int width, int height) {
       mickyV.getSurfaceView().setVideoSize(width,height);
    }

    @Override
    public void complete() {
        // 处理播放完成的情况
        int sceenState = mControlV.getScreenState();
        switch (sceenState)
        {
            case MicPlayerConfig.HORIZONTAL_SCREEN:
                releasePlayer();
                break;
            case MicPlayerConfig.FULL_SCREEN:

                break;
            case MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN_TO_FULL_SCREEN:

                break;
            case MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN:
                removeParent();
                releasePlayer();
                break;
        }
    }

    @Override
    public void error() {

    }

    /**
     * view横全屏切换
     * @param screenState
     */
    protected void changeView(int screenState)
    {
        switch (screenState)
        {
            case MicPlayerConfig.HORIZONTAL_SCREEN:
                changeToSmall();
                break;
            case MicPlayerConfig.FULL_SCREEN:
            case MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN_TO_FULL_SCREEN:
                changeToFull(screenState);
                break;
            case MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN:
                enterTinyWindow();
                break;
        }
    }

    int mHeight;
    /**
     * 切换到全屏状态
     */
    protected void changeToFull(int state)
    {
        // 显示部分control层view
        mControlV.showSomeControlView();
       // 隐藏ActionBar、状态栏、并横屏
        MickyUtils.hideActionBar(context);
        MickyUtils.scanForActivity(context)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        removeAllViews();
        removeParent();
        ViewGroup contentView = (ViewGroup) MickyUtils.scanForActivity(context)
                .findViewById(android.R.id.content);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mickyV, params);
        contentView.addView(mControlV,params);
        mControlV.setPlayScreenState(state);
    }

    /**
     * 退出全屏
     */
    protected void changeToSmall()
    {
       // 显示部分control层view
       mControlV.showSomeControlView();
       MickyUtils.showActionBar(context);
       MickyUtils.scanForActivity(context)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        removeParent();
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mickyV, params);
        addView(mControlV,params);
        mControlV.setPlayScreenState(MicPlayerConfig.HORIZONTAL_SCREEN);
    }

    /**
     * 进入小窗
     */
    public void enterTinyWindow()
    {
        // 判断当前播放状态，如果是正在播放和暂停播放才进入小窗
        if (MickyMediaPlayer.getPlayerPlayState() != MicPlayerConfig.PLAYER_STATE_PLAYING &&
                MickyMediaPlayer.getPlayerPlayState() != MicPlayerConfig.PLAYER_STATE_PAUSE &&
                MickyMediaPlayer.getPlayerPlayState() != MicPlayerConfig.PLAYER_STATE_BUFFER) return;
        if (mControlV.getScreenState() == MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN) return;
        // 隐藏部分control层view
        mControlV.hideSomeControlView();
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        FrameLayout.LayoutParams params;
        // 如果是全屏状态切换到小屏幕，首先切换到正常状态，然后再切换到小屏幕
        if (mControlV.getScreenState() == MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN_TO_FULL_SCREEN)
        {
            MickyUtils.showActionBar(context);
            MickyUtils.scanForActivity(context)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            params = new FrameLayout.LayoutParams(
                    (int) (MickyUtils.getScreenHeight(context) * 0.6f),
                    (int) (MickyUtils.getScreenHeight(context) * 0.6f * 9f / 16f));
        }else
        {
            params = new FrameLayout.LayoutParams(
                    (int) (MickyUtils.getScreenWidth(context) * 0.6f),
                    (int) (MickyUtils.getScreenWidth(context) * 0.6f * 9f / 16f));
        }
        mControlV.setPlayScreenState(MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN);
        removeAllViews();
        removeParent();
        ViewGroup contentView = (ViewGroup)  MickyUtils.scanForActivity(context)
                .findViewById(android.R.id.content);

        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.rightMargin = MickyUtils.dp2px(context, 8f);
        params.bottomMargin = MickyUtils.dp2px(context, 8f);
        contentView.addView(mickyV, params);
        contentView.addView(mControlV,params);
        mControlV.setPlayScreenState(MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN);
    }

    public void exitTinyWindow() {
        if (mControlV.getScreenState() != MicPlayerConfig.PLAYER_TINY_WINDOW_SCREEN) return;
        // 显示部分control层view
        mControlV.showSomeControlView();
         mControlV.setPlayScreenState(MicPlayerConfig.HORIZONTAL_SCREEN);
            ViewGroup contentView = (ViewGroup) MickyUtils.scanForActivity(context)
                    .findViewById(android.R.id.content);
       removeParent();
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mickyV, params);
        addView(mControlV,params);
    }

    protected void removeParent()
    {
        ViewGroup v = (ViewGroup) mickyV.getParent();
        ViewGroup v2 = (ViewGroup) mControlV.getParent();
        if (v != null) v.removeView(mickyV);
        if (v2 != null) v2.removeView(mControlV);
    }
}
