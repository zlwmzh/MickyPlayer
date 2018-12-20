package com.micky.www.mickyplayer;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Micky on 2018/12/18.
 *
 */

public class MickyPlayer {


    /**
     * 初始化操作
     */
    public static void init(Context context)
    {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        Fresco.initialize(context);
    }
}
