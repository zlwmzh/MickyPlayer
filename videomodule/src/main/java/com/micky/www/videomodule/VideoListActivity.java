package com.micky.www.videomodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    private static final String TAG = "VideoListActivity";
    
    private RecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;
    private List<VideoBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mRecyclerView = findViewById(R.id.recyclerview);

        mList = new ArrayList<>();
        mAdapter = new VideoListAdapter(this,mList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int fPosition = layoutManager.findFirstVisibleItemPosition();
                int lPosition = layoutManager.findLastVisibleItemPosition();
                Log.d(TAG,"第一个可见："+fPosition+"；最后一个可见："+lPosition);
                int currentPlayPosition = mAdapter.getCurrentPosition();
                if (currentPlayPosition < fPosition || currentPlayPosition > lPosition)
                {
                    mAdapter.getPlayerView().enterTinyWindow();
                }else
                {
                    mAdapter.getPlayerView().exitTinyWindow();
                }
               /* if (position != mAdapter.getCurrentPosition() && mAdapter.getCurrentPosition() != -1)
                {
                    mAdapter.getPlayerView().releasePlayer();
                }*/
            }
        });
        addTest();
    }

    private void addTest()
    {
        VideoBean v1 = new VideoBean("核爆神曲都听过？进化神曲有没有勾起你童年回忆？","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547524521&di=90c4506fe84ad22485be7a215adca0d2&imgtype=jpg&er=1&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2018-02-01%2F5a72d6ee6b46f.jpg",
                "http://video-hroot.oss-cn-hangzhou.aliyuncs.com/year201812/video-201812151103153554568397/video/file_181227104855618347.mp4","04:09");
        VideoBean v2 = new VideoBean("外国人唱神曲《生僻字》，一开口就给跪了","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547524653&di=5e2d39204a5d196bdd20af84e5c3c3e1&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F574e9258d109b3de245c354bc6bf6c81800a4c36.jpg",
                "http://video-hroot.oss-cn-hangzhou.aliyuncs.com/year201812/video-201812151103153554568397/video/file_181224095707684314.mp4","04:09");
        VideoBean v3 = new VideoBean("改革春风吹满地，硬核少女自称念诗女王！你顶得住吗？「凡仔」","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1313797669,2141985152&fm=26&gp=0.jpg",
                "http://video-hroot.oss-cn-hangzhou.aliyuncs.com/year201812/video-201812151103153554568397/video/file_181217134358777752.mp4","04:09");
        mList.add(v1);
        mList.add(v2);
        mList.add(v3);
        mList.add(v1);
        mList.add(v2);
        mList.add(v3);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
