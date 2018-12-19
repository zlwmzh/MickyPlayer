package com.micky.www.mickyplayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Micky on 2018/12/19.
 * 播放器监听接口
 */

public abstract class MickyPlayerListener implements IMediaPlayer.OnBufferingUpdateListener
        , IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener
        , IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener
        , IMediaPlayer.OnSeekCompleteListener{

}
