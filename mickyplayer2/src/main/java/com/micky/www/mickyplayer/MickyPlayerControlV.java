package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Micky on 2018/12/19.
 * 控制层View,自定义Control需要集成ControlView，并实现抽象类MickyPlayerListener
 */

public class MickyPlayerControlV extends ControlView{

    public MickyPlayerControlV(@NonNull Context context) {
        super(context);
    }

    public MickyPlayerControlV(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MickyPlayerControlV(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected MickyPlayerListener getPlayListener() {
        return listener;
    }

    // 视频播放回掉监听
    MickyPlayerListener listener = new MickyPlayerListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

        }

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {

        }

        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {

        }

        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

        }
    };
}
