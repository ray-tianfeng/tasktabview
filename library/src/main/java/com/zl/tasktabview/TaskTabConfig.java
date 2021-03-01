package com.zl.tasktabview;

import android.content.Context;
import android.graphics.Color;

/**
 * 配置类
 * Time: 2021/2/26 0026
 * Author: zoulong
 */
public class TaskTabConfig {
    //view宽度
    protected int viewWidth;
    //view高度
    protected int viewHeight;
    protected int itemSize;
    protected int margin;
    protected int itemPadding;
    protected int textSize;
    protected int itemIntervalX;
    protected int itemShrinkIntervalX;
    protected int itemIntervalY;
    protected int textInterval;
    protected int indicatorInterval;
    protected int indicatorMB;
    protected int indicatorRadius;

    protected int circleColor;
    protected int circleBorderColor;
    protected int circleSelectedColor;
    protected int textColor;
    protected int textSelectedColor;
    protected int indicatorColor;
    protected int indicatorSelectedColor;
;

    protected int scrollMaxLimit;
    protected int shrinkMaxLimit;
    protected int flipMinLimit;

    protected float itemISRatio;

    public TaskTabConfig(Context mContext) {
        this.margin = dip2px(mContext, 20);
        this.itemPadding = dip2px(mContext, 10);
        this.textSize = dip2px(mContext, 12);
        this.textInterval = dip2px(mContext, 15);
        this.indicatorInterval = dip2px(mContext, 10);
        this.indicatorMB = dip2px(mContext, 10);
        this.indicatorRadius = dip2px(mContext, 2);

        this.circleColor = Color.parseColor("#d9dfe2");
        this.circleSelectedColor = Color.parseColor("#0099ff");
        this.circleBorderColor = Color.parseColor("#c9ced1");
        this.textColor = Color.parseColor("#464849");
        this.textSelectedColor = Color.parseColor("#b0b5b7");
        this.indicatorColor = Color.parseColor("#bec3c4");
        this.indicatorSelectedColor = Color.parseColor("#474949");
    }

    private  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    boolean isQuantization(){
        return viewWidth != 0 &&  viewHeight != 0;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setItemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextInterval(int textInterval) {
        this.textInterval = textInterval;
    }

    public void setIndicatorInterval(int indicatorInterval) {
        this.indicatorInterval = indicatorInterval;
    }

    public void setIndicatorMB(int indicatorMB) {
        this.indicatorMB = indicatorMB;
    }

    public void setIndicatorRadius(int indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setCircleBorderColor(int circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    public void setCircleSelectedColor(int circleSelectedColor) {
        this.circleSelectedColor = circleSelectedColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setIndicatorSelectedColor(int indicatorSelectedColor) {
        this.indicatorSelectedColor = indicatorSelectedColor;
    }


}
