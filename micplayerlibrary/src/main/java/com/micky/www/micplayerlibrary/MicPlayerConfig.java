package com.micky.www.micplayerlibrary;

/**
 * Created by Micky on 2018/12/21.
 * 播放器配置参数
 */

public class MicPlayerConfig {

    // ======= 播放状态 =========
    public static final int PLAYER_STATE_INIT = 0x00;  // 初始化状态
    public static final int PLAYER_STATE_PREPARING = 0x01;  // 准备中
    public static final int PLAYER_STATE_PLAYING = 0x02; // 播放中
    public static final int PLAYER_STATE_PAUSE = 0x03; // 暂停中
    public static final int PLAYER_STATE_STOP = 0x04;  // 停止
    public static final int PLAYER_STATE_COMPLETE = 0x05; // 播放完成
    public static final int PLAYER_STATE_ERROR = 0x06; // 播放错误

    // 刷新进度默认时间
    public static final long PROGRESS_REFRESH = 1 * 1000;
}
