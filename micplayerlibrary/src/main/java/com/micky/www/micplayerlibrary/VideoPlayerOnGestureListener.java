package com.micky.www.micplayerlibrary;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @ClassName VideoPlayerOnGestureListener
 * @Description TODO
 * @Author Micky
 * @Date 2018/12/22 11:10
 * @Version 1.0
 */
public class VideoPlayerOnGestureListener extends GestureDetector.SimpleOnGestureListener{

    private static final String TAG = "VideoPlayerOnGestureLis";
    // 滑动模式，默认无状态
    protected int mScrollMode = MicPlayerConfig.NONE;
    // 回掉接口
    protected VideoGestureListener mListener;
    // 快进快退滑动最少距离
    protected float offsetX = MicPlayerConfig.OFFSETX;
    // 依附图层
    protected View view;

    public VideoPlayerOnGestureListener(VideoGestureListener listener, View view)
    {
        this.mListener = listener;
        this.view = view;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        mScrollMode = MicPlayerConfig.NONE;
        if (mListener != null) mListener.onDown(e);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll: e1:" + e1.getX() + "," + e1.getY());
        Log.d(TAG, "onScroll: e2:" + e2.getX() + "," + e2.getY());
        Log.d(TAG, "onScroll: X:" + distanceX + "  Y:" + distanceY);
        switch (mScrollMode)
        {
            case MicPlayerConfig.NONE:
                Log.d(TAG,"滑动状态：NONE");
                if (Math.abs(distanceX) - Math.abs(distanceY) > offsetX) {
                    mScrollMode = MicPlayerConfig.FF_REW;
                } else {
                    if (e1.getX() < view.getWidth() / 2) {
                        mScrollMode = MicPlayerConfig.BRIGHTNESS;
                    } else {
                        mScrollMode = MicPlayerConfig.VOLUME;
                    }
                }
                break;
            case MicPlayerConfig.BRIGHTNESS:
                Log.d(TAG,"滑动状态：BRIGHTNESS");
                if (mListener != null) mListener.onBrightnessGesture(e1,e2,distanceX,distanceY);
                break;
            case MicPlayerConfig.VOLUME:
                Log.d(TAG,"滑动状态：VOLUME");
                if (mListener != null) mListener.onVolumeGesture(e1,e2,distanceX,distanceY);
                break;
            case MicPlayerConfig.FF_REW:
                Log.d(TAG,"滑动状态：FF_REW");
                if (mListener != null) mListener.onFF_REWGesture(e1,e2,distanceX,distanceY);
                break;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG,"单击");
        if (mListener != null) mListener.onSingleTapGesture(e);
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        if (mListener != null) mListener.onDoubleTapGesture(e);
        return super.onDoubleTapEvent(e);
    }
}
