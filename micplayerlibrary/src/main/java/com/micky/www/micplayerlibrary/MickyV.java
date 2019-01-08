package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.FrameLayout;

/**
 * Created by Micky on 2018/12/21.
 * 视频图像层
 */

public class MickyV extends FrameLayout{
    private static final String TAG = "MickyV";

    // 上下文环境
    protected Context context;
    // 视频渲染SurfaceVie
    protected TextureView mTextureView;
    // 当前SurfaceTexture
    protected  SurfaceTexture surfaceTexture;
    // 视图创建成功后的回调
    protected MickyVlListener mListener;

    public MickyV(@NonNull Context context) {
        this(context,null);
    }

    public MickyV(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MickyV(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     *  初始化操作
     * @param context
     */
    protected void init(Context context)
    {
        Log.d(TAG,"init");
        this.context = context;
    }


    /**
     * 创建视频预览SurfaceView
     */
    protected void createSurfaceView()
    {
        // mTextureView 不为空 且已经被添加
        if (mTextureView != null && mTextureView.getParent() != null) return;
        // 实例化mTextureView，并添加到容器中
        mTextureView = new TextureView(context);
        // 关联mTextureView回掉
        mTextureView.setSurfaceTextureListener(IListener);
        // 设置宽高,居中显示
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mTextureView.setLayoutParams(params);
        addView(mTextureView);
    }


    TextureView.SurfaceTextureListener IListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG,"onSurfaceTextureAvailable");
            if (MickyMediaPlayer.getInstance() != null)
            {
                if (mTextureView.getSurfaceTexture() == surfaceTexture || surfaceTexture == null) return;
                mTextureView.setSurfaceTexture(surfaceTexture);
            }else
            {
                // 创建成功后开始播放视频
                if (mListener != null) mListener.onSurfaceTextureAvailable(surface);
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG,"onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG,"onSurfaceTextureDestroyed");
            // 销毁的时候手动接管生命周期，保存之前的surfaceTexture
            surfaceTexture = surface;
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            Log.d(TAG,"onSurfaceTextureUpdated");
        }
    };


    /**
     *  设置回调监听
     * @param mickyVListener
     * @return
     */
    public MickyV setMickyVListener(MickyVlListener mickyVListener)
    {
        this.mListener = mickyVListener;
        return this;
    }

    /**
     * 获取视频画布
     * @return
     */
   public TextureView getSurfaceView()
   {
       return mTextureView;
   }

    /**
     * 释放view
     */
   public void  releaseView()
   {
       removeView(mTextureView);
       mTextureView = null;
       surfaceTexture = null;
   }

    /**
     * 获取Surface画布
     * @return
     */
   public Surface getSurface()
   {
       if (mTextureView.getSurfaceTexture() != null)
       {
           return new Surface(mTextureView.getSurfaceTexture());
       }
       if (surfaceTexture != null)
       {
           return new Surface(surfaceTexture);
       }
       return null;
   }
}
