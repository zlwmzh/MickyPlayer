package com.micky.www.videomodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DouYinActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<VideoBean> mList;
    private DouYinListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_yin);

        mRecyclerView = findViewById(R.id.douyin_recycler);

        mList = new ArrayList<>();
        mAdapter = new DouYinListAdapter(this,mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper()
        {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int tarhetPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                DouYinListAdapter.DouYinViewHolder viewHolder = (DouYinListAdapter.DouYinViewHolder) mRecyclerView.findViewHolderForAdapterPosition(tarhetPosition);
                mAdapter.play(tarhetPosition,viewHolder);
                return tarhetPosition;
            }
        };
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        addTest();
    }

    private void addTest()
    {
        VideoBean v1 = new VideoBean("desc=#朱亚文朱亚文朱亚文 #行走的男性荷尔蒙 有多少喜欢这个声音的","http://p3-dy.bytecdn.cn/large/16b66000432bcce9f0e1f.jpeg",
                "http://v6-dy.ixigua.com/77754ebca8859e89b590ef4c6682989c/5c359ae6/video/m/220eb5b083ca44448e2818f008a3e14123711612e38300004759049df07c/?rc=ajZ4aXRpMztwajMzaGkzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRAanI1XmhkcF9uXy0tXy0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        VideoBean v2 = new VideoBean("desc=妻子抱孩子不远千里远远望着丈夫训练，铁打的中国军人战士此时忍不住泪流满面，致敬！","http://p3-dy.bytecdn.cn/large/16c3c0001fae801d4cfda.jpeg",
                "http://v6-dy.ixigua.com/c236cf5bacf60e9510d140512e063b32/5c359b07/video/m/220b3f0718ec42f4157b8408ee87e8446db11613043f0000919886e1e23a/?rc=anNqbjU7OnVrajMzZmkzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRALmFsYGZubG1uXy0tYS0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        VideoBean v3 = new VideoBean("desc=昨天看到这个，今天老公就带我来吃。挑战成功","http://p9-dy.bytecdn.cn/large/169b8000926d53ea12e34.jpeg",
                "http://v6-dy.ixigua.com/be614d72bf58db4ab9b2bd78e9e1562c/5c359ae7/video/m/220d4889d43bbae4e1787b4cb548f88488711613f1350000002a0d418272/?rc=am84OXZubDk5ajMzPGkzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRAZ25mYnFfa2RuXy0tLy0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        VideoBean v4 = new VideoBean("desc=愿相会于中华腾飞世界时！@抖音小助手","http://p9-dy.bytecdn.cn/large/169b8000926d53ea12e34.jpeg",
                "http://v6-dy.ixigua.com/b5a49df624dad98b11bffab9f7096f91/5c359ae3/video/m/2202633842b237d44819d66467ed05f5a8811612f9050000700b5f7841e2/?rc=am5qOzZlOzZ3ajMzN2kzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRAZjJzaWluM2JtXy0tNi0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        VideoBean v5 = new VideoBean("desc=#摇身一变 #理想女友穿搭 听得出来这是周杰伦的哪首歌不？你们想听什么，我给你们弹。","http://p9-dy.bytecdn.cn/large/169b8000926d53ea12e34.jpeg",
                "http://v6-dy.ixigua.com/bc5661589fa523e0600d1bb0267f6a46/5c359af7/video/m/2201633d48f97fc473cb9af9bfe69cef87f11612a39b00003c6c6c15541f/?rc=am13dWV5OzxpajMzaGkzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRAb2Fza3IvZ19lXy0tMy0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        VideoBean v6 = new VideoBean("desc=坑车坑续集（五），感觉奔驰没磕着，这是轿车呀","http://p3-dy.bytecdn.cn/large/16b830005b1151c438db1.jpeg",
                "http://v6-dy.ixigua.com/1db42ca16db947bacc2065cbd5a41f76/5c359ae9/video/m/2204ba9ae73a3c14a948160bdd044abd45711612f59900004ca6a559c4c5/?rc=M2hlb3A2eGhvajMzaGkzM0ApQHRAbzhIOzU0OjgzNDg4Njw0PDNAKXUpQGczdylAZmh1eXExZnNoaGRmOzRANWc1NGpicmpuXy0tNi0vc3MtbyNvIy81MDM0LS4tLS8yLi0tLi9pOmItbyM6YC1vI3BiZnJoXitqdDojLy5e","00:09");
        mList.add(v1);
        mList.add(v2);
        mList.add(v3);
        mList.add(v4);
        mList.add(v5);
        mList.add(v6);
        mList.add(v1);
        mList.add(v2);
        mList.add(v3);
        mList.add(v4);
        mList.add(v5);
        mList.add(v6);
        mAdapter.notifyDataSetChanged();
    }
}
