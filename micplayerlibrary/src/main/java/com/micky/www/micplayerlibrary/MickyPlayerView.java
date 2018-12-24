package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
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
     * 创建mediaplayer 并开始准备
     */
    protected void prepareMediaPlayer()
    {
        // 准备播放
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
    public void videoSize(int width, int height) {
     //  LayoutParams params = (LayoutParams) mickyV.getSurfaceView().getLayoutParams();
     //  params.height = params.width * (height / width);
     //  mickyV.getSurfaceView().setLayoutParams(params);
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
                changeToFull();
                break;

        }
    }

    int mHeight;
    /**
     * 切换到全屏状态
     */
    protected void changeToFull()
    {
       // 隐藏ActionBar、状态栏、并横屏
        MickyUtils.hideActionBar(context);
        MickyUtils.scanForActivity(context)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);;
        removeAllViews();
        ViewGroup contentView = (ViewGroup) MickyUtils.scanForActivity(context)
                .findViewById(android.R.id.content);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mickyV, params);
        contentView.addView(mControlV,params);
    }

    protected void changeToSmall()
    {
       MickyUtils.showActionBar(context);
       MickyUtils.scanForActivity(context)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewGroup contentView = (ViewGroup) MickyUtils.scanForActivity(context)
                .findViewById(android.R.id.content);
        contentView.removeView(mickyV);
        contentView.removeView(mControlV);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mickyV, params);
        addView(mControlV,params);
    }


}
