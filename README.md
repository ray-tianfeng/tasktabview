高仿了一个小米状状态的伸缩式菜单栏  
小米：![ezgif-4-745c4fd37592.gif](https://github.com/ray-tianfeng/tasktabview/blob/master/gif/ezgif-4-745c4fd37592.gif)  
仿制：![ezgif-4-b56cab2f6afa.gif](https://github.com/ray-tianfeng/tasktabview/blob/master/gif/ezgif-4-b56cab2f6afa.gif)  
主要功能点，横向滚动纵向收缩，横向滚动联代底部指示器变化，纵向收缩时，随着收缩距离动态的改变透明度，当下面两排itemview收缩完成后第五个item移动到第一排。最后是点击后记录状态，改变item背景、图标、描述颜色。  

## 导入
1. 中央厂库配置
~~~gralde
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
~~~
2.依赖配置
~~~gradle
 implementation 'com.github.ray-tianfeng:tasktabview:v1.0.3'
~~~
## 使用
~~~java
        TaskTabView taskTabView = findViewById(R.id.task_tab);
        //norIcons(未选中/点击时icon)，preIcons(选中和点击时icon)、describes（描述文字）
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
~~~
##属性
| 字段                    | 布局中设置                    | 代码中设置                                | 说明                                              |
|:-----------------------|:-----------------------------|:----------------------------------------|:--------------------------------------------------|
| margin                 | app:margin                   | TaskTabConfig.setMargin                 | item距离top、left、right间距，底部没有用margin作为间距 |
| itemPadding            | app:item_padding             | TaskTabConfig.setItemPadding            | icon到外部圆圈的间距                                |
| textSize               | app:text_size                | TaskTabConfig.setTextSize               | 图标下部描述文字大小                                 |
| textInterval           | app:text_interval            | TaskTabConfig.setTextInterval           | 描述文字和图标间的间距                               |
| indicatorInterval      | app:indicator_interval       | TaskTabConfig.setIndicatorInterval      | 指示器间的间距                                      |
| indicatorMB            | app:indicator_margin_buttom  | TaskTabConfig.setIndicatorMB            | 指示器和底部的间距                                   |
| indicatorRadius        | app:indicator_radius         | TaskTabConfig.setIndicatorRadius        | 指示器半径，指示器用画实心圆实现的                     |
| circleColor            | app:circle_color             | TaskTabConfig.setCircleColor            | item背景圆圈的颜色                                  |
| circleSelectedColor    | app:circle_selected_color    | TaskTabConfig.setCircleSelectedColor    | item背景圆圈选中/点击时的颜色                         |
| circleBorderColor      | app:circle_border_color      | TaskTabConfig.setCircleBorderColor      | item背景圆圈边框的颜色                               |
| textColor              | app:text_color               | TaskTabConfig.setTextColor              | 描述文字颜色                                        |
| textSelectedColor      | app:text_selected_color      | TaskTabConfig.setTextSelectedColor      | 描述选中/点击时文字颜色                              |
| indicatorColor         | app:indicator_color          | TaskTabConfig.setIndicatorColor         | 指示器颜色                                         |
| indicatorSelectedColor | app:indicator_selected_color | TaskTabConfig.setIndicatorSelectedColor | 指示器高亮时指示器颜色                               |

代码中设置配置：
~~~java
        TaskTabConfig taskTabConfig = new TaskTabConfig(this);
                 ...设置属性
        taskTabView.config(taskTabConfig);
~~~

布局中使用：
~~~xml
 <com.zl.tasktabview.TaskTabView
        android:id="@+id/task_tab"
        android:layout_width="match_parent"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_height="wrap_content"
        android:background="@drawable/backgroud"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:margin = "15dp"
        app:item_padding="10dp"
        app:text_size="12sp"
        app:text_interval="10dp"
        app:indicator_interval="8dp"
        app:indicator_margin_buttom="10dp"
        app:indicator_radius="2dp"
        app:circle_color="#d9dfe2"
        app:circle_selected_color="#0099ff"
        app:circle_border_color="#b0b5b7"
        app:text_color="#464849"
        app:text_selected_color="@android:color/black"
        app:indicator_color="#bec3c4"
        app:indicator_selected_color="#474949"
        />
~~~
代码设置优先级高于布局中，控件宽度必须指明不能“wrap_content”，高度是通过宽度计算出来的，所以必须设置为“wrap_content”。
[代码传送门](https://github.com/ray-tianfeng/tasktabview)