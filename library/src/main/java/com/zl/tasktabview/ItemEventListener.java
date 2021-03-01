package com.zl.tasktabview;

/**
 * item事件监听，实现了点击和长按
 * Time: 2021/2/26 0026
 * Author: zoulong
 */
public interface ItemEventListener {
    void onItemOnClick(int itemIndex);
    void onItemLongClick(int itemIndex);
}
