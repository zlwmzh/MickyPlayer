package com.micky.www.videomodule;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.micky.www.micplayerlibrary.MickyMediaPlayer;
import com.micky.www.micplayerlibrary.MickyPlayerView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MickyPlayerView mickyPlayerView;
    private SurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mickyPlayerView = findViewById(R.id.mickyplayerview);
        mSurfaceView = findViewById(R.id.surfaceview);
        Log.d(TAG,"onCreate");
        //https://test2015data.oss-cn-hangzhou.aliyuncs.com/video-hroot/year201811/video-201811100/Audio/file_181121121432374354.mp4
       // mickyPlayerView.setVideoPath("http://upos-hz-mirrorwcsu.acgvideo.com/upgcxcode/35/19/66571935/66571935-1-208.mp4?ua=tvproj&deadline=1545385649&gen=playurl&nbs=1&oi=2501663261&os=wcsu&platform=tvproj&trid=7291ed541fa84f3584bd3eb83672f236&uipk=5&upsig=f45a27707cdd22371e0c4728f5ee7eb6");
        mickyPlayerView.setVideoPath("http://test2015data.oss-cn-hangzhou.aliyuncs.com/video-hroot/year201811/video-201811100/Audio/file_181121121432374354.mp4");
        mickyPlayerView.setVideoInfo("你好漂亮啊","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545663646684&di=62971cb72583c3e2eda0e13e44ea402e&imgtype=0&src=http%3A%2F%2Fs13.sinaimg.cn%2Fmw690%2F004lhodJgy6YgkEj4fi2c%26690","");
       // handler.sendEmptyMessageDelayed(0x00,10 * 1000);
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
         MickyMediaPlayer.getInstance().setDisplay(mSurfaceView.getHolder());
        }
    };
}
