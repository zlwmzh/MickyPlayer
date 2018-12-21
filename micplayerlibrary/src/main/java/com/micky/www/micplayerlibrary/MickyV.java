package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
    protected SurfaceView mSurfaceView;
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
        this.context = context;
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
            if (mListener != null) mListener.onSurfaceCreated();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

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
   public SurfaceView getSurfaceView()
   {
       return mSurfaceView;
   }
}
