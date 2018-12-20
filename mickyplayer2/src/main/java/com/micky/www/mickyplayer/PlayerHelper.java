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
    // 当前播放状态,默认初始状态
    private static int mMediaPlayerState = PlayerConfig.PLAYER_STATE_INIT;

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
            setPlayerPlayState(PlayerConfig.PLAYER_STATE_PLAYING);
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
            mMediaPlayerState = PlayerConfig.PLAYER_STATE_PAUSE;
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
            mMediaPlayerState = PlayerConfig.PLAYER_STATE_STOP;
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
            mMediaPlayerState = PlayerConfig.PLAYER_STATE_INIT;
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
            setPlayerPlayState(PlayerConfig.PLAYER_STATE_INIT);
            mMediaPlayer.reset();
        }
    }

    /**
     *  设置播放器播放状态
     * @param playerState
     */
    public static void setPlayerPlayState(int playerState)
    {
        mMediaPlayerState = playerState;
    }

    /**
     * 获取播放器当前播放状态
     * @return
     */
    public static int getPlayerPlayState()
    {
        return mMediaPlayerState;
    }


    /**
     * 获取总长度
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
     * 获取当前播放进度
     * @return
     */
    public static long getCurrentDuration()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.getCurrentPosition();
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


