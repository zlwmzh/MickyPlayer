package com.micky.www.videomodule;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Micky on 2019/1/8.
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration{

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.setMargins(20,20,20,0);
        view.setLayoutParams(lp);
        super.getItemOffsets(outRect, view, parent, state);
    }
}
