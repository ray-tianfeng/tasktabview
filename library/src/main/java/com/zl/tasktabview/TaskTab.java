package com.zl.tasktabview;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * 每个item数据
 * Time: 2021/2/23 0023
 * Author: zoulong
 */
public class TaskTab {
    //icon图标
    protected Bitmap norIcon;
    //点击图标
    protected Bitmap preIcon;
    //文字描述
    protected String describe;
    //icon的位置
    protected Point iconPoint = new Point();
    //文字描述的文字
    protected Point describePoint = new Point();
    //icon透明度
    protected int iconAlpha = 255;
    //文字透明度
    protected int describeAlpha = 255;
    //所在页面
    protected int page;
    //是否按压状态
    protected boolean isSelected = false;
}
