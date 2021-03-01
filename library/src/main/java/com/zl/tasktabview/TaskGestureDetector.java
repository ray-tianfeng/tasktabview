package com.zl.tasktabview;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 增加了手机抬起事件
 * Time: 2021/2/24 0024
 * Author: zoulong
 */
public class TaskGestureDetector extends GestureDetector {
    private OnGestureListener listener;
    public TaskGestureDetector(OnGestureListener listener, Handler handler) {
        super(listener, handler);
        this.listener = listener;
    }

    public TaskGestureDetector(OnGestureListener listener) {
        super(listener);
        this.listener = listener;
    }

    public TaskGestureDetector(Context context, OnGestureListener listener) {
        super(context, listener);
        this.listener = listener;
    }

    public TaskGestureDetector(Context context, OnGestureListener listener, Handler handler) {
        super(context, listener, handler);
        this.listener = listener;
    }

    public TaskGestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
        super(context, listener, handler, unused);
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if(ev.getAction() == MotionEvent.ACTION_UP && listener instanceof TaskOnGestureListener)
            ((TaskOnGestureListener) listener).onEventUp();
        return result;
    }
}
