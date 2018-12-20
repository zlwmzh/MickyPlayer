package com.micky.www.mickyplayer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Micky on 2018/12/19.
 * 控制层父类
 */

public abstract class ControlView extends FrameLayout{

    public ControlView(@NonNull Context context) {
        super(context);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 子类必须实现
     * @return
     */
    protected abstract MickyPlayerListener getPlayListener();


}
