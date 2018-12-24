package com.micky.www.micplayerlibrary;

/**
 * @ClassName ControlVListener
 * @Description 控制层回掉监听
 * @Author Micky
 * @Date 2018/12/23 10:47
 * @Version 1.0
 */
public interface ControlVListener {
    void fullOrSmallScreen(int screenState);
    void videoSize(int width,int height);
}
