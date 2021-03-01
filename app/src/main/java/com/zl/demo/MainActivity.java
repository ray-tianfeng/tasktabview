package com.zl.demo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zl.tasktabview.ItemEventListener;
import com.zl.tasktabview.TaskTabView;

public class MainActivity extends AppCompatActivity {
    int[] norIcons = {
            R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7,
            R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12, R.drawable.icon13, R.drawable.icon14,
            R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18
            ,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,
            R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7,
            R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12, R.drawable.icon13, R.drawable.icon14,
            R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18
            ,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,
            R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18
    };
    int[] preIcons = {
            R.drawable.icon_press1, R.drawable.icon_press2, R.drawable.icon_press3, R.drawable.icon_press4, R.drawable.icon_press5, R.drawable.icon_press6, R.drawable.icon_press7,
            R.drawable.icon_press8, R.drawable.icon_press9, R.drawable.icon_press10, R.drawable.icon_press11, R.drawable.icon_press12, R.drawable.icon_press13, R.drawable.icon_press14,
            R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18
            ,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,
            R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,R.drawable.icon_press1, R.drawable.icon_press2, R.drawable.icon_press3, R.drawable.icon_press4, R.drawable.icon_press5, R.drawable.icon_press6, R.drawable.icon_press7,
            R.drawable.icon_press8, R.drawable.icon_press9, R.drawable.icon_press10, R.drawable.icon_press11, R.drawable.icon_press12, R.drawable.icon_press13, R.drawable.icon_press14,
            R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18
            ,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18,
            R.drawable.icon_press15, R.drawable.icon_press16, R.drawable.icon_press17, R.drawable.icon_press18
    };
    String[] describes = {
            "音量", "网络", "铲子", "锤子", "啤酒箱", "上传", "奖杯",
            "传输", "数据库", "添加", "资料", "成功", "下载", "扫描",
            "文档", "酒杯", "收藏", "开关","文档", "酒杯", "收藏", "开关",
            "文档", "酒杯", "收藏", "开关","文档", "酒杯", "收藏", "开关",
            "文档", "酒杯", "收藏", "开关","音量", "网络", "铲子", "锤子", "啤酒箱", "上传", "奖杯",
            "传输", "数据库", "添加", "资料", "成功", "下载", "扫描",
            "文档", "酒杯", "收藏", "开关","文档", "酒杯", "收藏", "开关",
            "文档", "酒杯", "收藏", "开关","文档", "酒杯", "收藏", "开关",
            "文档", "酒杯", "收藏", "开关"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskTabView taskTabView = findViewById(R.id.task_tab);
        taskTabView.setTab(norIcons, preIcons, describes);
        taskTabView.addItemEventListener(new ItemEventListener() {
            @Override
            public void onItemOnClick(int itemIndex) {
                Toast.makeText(MainActivity.this, "点击：" + itemIndex, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex) {
                Toast.makeText(MainActivity.this, "长按：" + itemIndex, Toast.LENGTH_SHORT).show();
            }
        });
//        taskTabView.config(new TaskTabConfig(this));
    }
}