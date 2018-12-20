package com.micky.www.videomodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.micky.www.mickyplayer.MickyPlayerView;
import com.micky.www.mickyplayer.PlayerHelper;

public class MainActivity extends AppCompatActivity {

    private MickyPlayerView mickyPlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mickyPlayerView = findViewById(R.id.mickyplayerview);
        mickyPlayerView.setMediaPath("http://upos-hz-mirrorwcsu.acgvideo.com/upgcxcode/35/19/66571935/66571935-1-208.mp4?ua=tvproj&deadline=1545300590&gen=playurl&nbs=1&oi=2501663261&os=wcsu&platform=tvproj&trid=9c1a5e17a18049488fd5c0a02e8f2e33&uipk=5&upsig=7518c9ea563b18e45119377589f5fcab");
    }
}
