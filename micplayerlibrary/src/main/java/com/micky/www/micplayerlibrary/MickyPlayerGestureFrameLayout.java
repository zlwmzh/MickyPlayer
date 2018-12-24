package com.micky.www.micplayerlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @ClassName MickyPlayerGestureFrameLayout
 * @Description 自定义处理手势的FrameLayout
 * @Author Micky
 * @Date 2018/12/22 11:35
 * @Version 1.0
 */
public class MickyPlayerGestureFrameLayout extends FrameLayout implements VideoGestureListener{
    private static final String TAG = "MickyPlayerGestureFrame";

    // 手势监听器
    protected VideoPlayerOnGestureListener mListener;
    protected GestureDetector mGestureDetector;
    public MickyPlayerGestureFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public MickyPlayerGestureFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MickyPlayerGestureFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *  初始化操作
     */
    protected void init()
    {
        mListener = new VideoPlayerOnGestureListener(this,this);
        mGestureDetector = new GestureDetector(getContext(),mListener);
        // 取消长按事件
        mGestureDetector.setIsLongpressEnabled(false);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onSingleTapGesture(MotionEvent e) {

    }

    @Override
    public void onDoubleTapGesture(MotionEvent e) {

    }

    @Override
    public void onDown(MotionEvent e) {

    }

    @Override
    public void onEndFF_REW(MotionEvent e) {

    }
}
