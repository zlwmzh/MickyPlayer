package com.micky.www.videomodule;

import android.app.Application;

import com.micky.www.micplayerlibrary.MickyMediaPlayer;

/**
 * Created by Micky on 2018/12/20.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        MickyMediaPlayer.init(this);
    }
}
