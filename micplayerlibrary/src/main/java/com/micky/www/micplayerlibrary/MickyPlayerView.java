package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Micky on 2018/12/21.
 * 视频播放器：视频图像层(视频影响显示层)、控制层(播放、快进、当前播放进度、全屏按钮等)及信息层（开始封面、结束封面）
 */

public class MickyPlayerView extends FrameLayout implements MickyVlListener{
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
     * 添加视频图像层、信息层
     */
    protected void addTopView()
    {
        // 图像层
        mickyV = new MickyV(context);
        // 设置回调监听
        mickyV.setMickyVListener(this);
        addView(mickyV);

        // 控制信息层
        mControlV = new ControlV(context);
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
     * 创建mediaplayer 并开始准备
     */
    protected void prepareMediaPlayer()
    {
        // 准备播放
        mControlV.createPlayer(mVideoPath,mickyV.getSurfaceView());
    }

    @Override
    public void onSurfaceCreated() {
       // 准备播放
        prepareMediaPlayer();
    }
}
