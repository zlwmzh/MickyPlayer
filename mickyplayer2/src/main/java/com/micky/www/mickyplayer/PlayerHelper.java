package com.micky.www.mickyplayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Micky on 2018/12/18.
 * 获取ijkplayer 实例，保证全局只有一个实例，采用单例模式
 */

public class PlayerHelper {
    // ijkplayer提供，用于播放视频，需要给他传入一个surfaceView显示
    private static IMediaPlayer mMediaPlayer = null;

    private PlayerHelper() {

    }

    public static IMediaPlayer getInstance() {
        return mMediaPlayer;
    }

    public static void setMediaPlayer(IjkMediaPlayer ijkMediaPlayer)
    {
        mMediaPlayer = ijkMediaPlayer;
    }

    /**
     * 开始播放
     */
    public static void start()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     * @return
     */
    public static void pause()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.pause();
        }
    }

    /**
     * 停止播放
     * @return
     */
    public static void stop()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
        }
    }

    /**
     *  重置
     * @return
     */
    public static void release()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 重设
     * @return
     */
    public static void reset()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.reset();
        }
    }

    /**
     * 获取当前进度
     * @return
     */
    public static long getDuration()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 快进
     * @param duration  快进位置
     * @return
     */
    public static void seekTo(long duration)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.seekTo(duration);
        }
    }
}


