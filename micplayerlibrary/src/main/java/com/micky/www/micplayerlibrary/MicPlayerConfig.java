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
    public static final int PLAYER_STATE_BUFFER = 0x06; //缓冲中
    public static final int PLAYER_STATE_COMPLETE = 0x05; // 播放完成
    public static final int PLAYER_STATE_ERROR = 0x06; // 播放错误

    // 刷新进度默认时间
    public static final long PROGRESS_REFRESH = 1 * 1000;
    // 控制界面显示到移除的延迟默认时间
    public static final long CONTROL_HIDE_TIME = 3 * 1000;

    // 手势状态
    public static final int BRIGHTNESS = 0x01;
    public static final int VOLUME = 0x02;
    public static final int FF_REW = 0x03;
    public static final int NONE = 0x00;

    public static final float OFFSETX = 200;

    public static final int HORIZONTAL_SCREEN = 0x00;
    public static final int FULL_SCREEN = 0x01;
    public static final int PLAYER_TINY_WINDOW_SCREEN = 0X02;   // 小窗口播放器
    public static final int PLAYER_TINY_WINDOW_SCREEN_TO_FULL_SCREEN = 0x04; // 从小窗口进入全屏
}
