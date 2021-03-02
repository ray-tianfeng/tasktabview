package com.zl.tasktabview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * 伸缩式菜单栏
 * 根据宽度求高度
 * Time: 2021/2/23 0023
 * Author: zoulong
 */
public class TaskTabView extends View {
    //pageCount（总的页数，从0开始）、scrollDistanceX（当次在x轴上的滚动距离，按下-移动-抬起，这一次的距离）、alpha（透明度0-255）、shrinkHeight（收缩高度）
    private int pageCount, scrollDistanceX, alpha, shrinkHeight;
    //view状态STATE_OPEN：打开、STATE_CLOSE：关闭（只显示一排的时候）、STATE_SHRINK（收缩/展开）
    private final int STATE_OPEN = 0x1, STATE_CLOSE = 0x2, STATE_SHRINK = 0x3;
    private int STATE = STATE_OPEN;
    //滚动状态MOVE_X：x轴上滚动、MOVE_Y：y轴上滚动、MOVE_U（抬起）
    private final int MOVE_X = 0x1, MOVE_Y = 0x2, MOVE_U = 0x3;
    private int MOVE_STATE = MOVE_U;
    //属性配置
    private TaskTabConfig TC;
    //数据
    private ArrayList<TaskTab> tabs = new ArrayList<>();
    //当前页面
    private int pageIndex;
    private Scroller mScroller;
    //手势监听
    private GestureDetector mGestureDetector;
    //收缩/展开动画
    ValueAnimator shrinkAnimation;
    //item事件监控
    private ItemEventListener itemEventListener;
    //iconPaint：icon画笔、circlePaint：圆圈背景画笔、strokePaint：圆圈边框背景画笔、describePaint：文字描述画笔、indicatorPaint：指示器画笔
    private Paint iconPaint, circlePaint, strokePaint, describePaint, indicatorPaint;

    /**
     * 初始化配置
     */
    private void init(){
        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(TC.circleColor);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setColor(TC.circleBorderColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1);
        strokePaint.setAntiAlias(true);

        describePaint = new Paint();
        describePaint.setColor(TC.textColor);
        describePaint.setStyle(Paint.Style.STROKE);
        describePaint.setTextSize(TC.textSize);

        indicatorPaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setAntiAlias(true);

        mScroller = new Scroller(getContext());
        mGestureDetector = new TaskGestureDetector(getContext(), mTaskOnGestureListener);
    }

    private TaskOnGestureListener mTaskOnGestureListener = new TaskOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //确定滚动方向
            if(STATE == STATE_OPEN && MOVE_STATE == MOVE_U){
                MOVE_STATE = Math.abs(distanceX) > Math.abs(distanceY) ? MOVE_X : MOVE_Y;
            }
            if(MOVE_STATE == MOVE_X){//x轴上滚动处理
                scrollDistanceX += distanceX;
                scrollBy((int) distanceX, 0);
                postInvalidate();
            }else{//y轴上收缩/展开
                shrinkHeight += distanceY;
                updateHeight();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //长安监听
            if(itemEventListener == null) return;
            int itemIndex = getItemIndexByPosition(e);
            if(itemIndex != -1) itemEventListener.onItemLongClick(itemIndex);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //短暂按压时，更改按下item状态
            if(itemEventListener == null) return false;
            int itemIndex = getItemIndexByPosition(e);
            if(itemIndex != -1) tabs.get(itemIndex).isSelected = !tabs.get(itemIndex).isSelected;
          return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //点击
            if(itemEventListener == null) return false;
            int itemIndex = getItemIndexByPosition(e);
            if(itemIndex != -1) itemEventListener.onItemOnClick(itemIndex);
            postInvalidate();
            return itemIndex != -1;
        }

        @Override
        public void onEventUp() {
            //当手指抬起来时，修正滚动和伸展/收缩值
            if(MOVE_STATE == MOVE_X){
                if(Math.abs(scrollDistanceX) > TC.flipMinLimit){
                    pageIndex = scrollDistanceX > 0 ? Math.min(pageIndex + 1, pageCount) : Math.max(pageIndex - 1, 0);
                }
                int distanceX = pageIndex * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, distanceX, 0, 200);
                invalidate();
            }else{
                if(shrinkHeight > TC.shrinkMaxLimit / 2){
                    shrinkAnimation(TC.shrinkMaxLimit);
                }else{
                    shrinkAnimation(0);
                }
            }
            MOVE_STATE = MOVE_U;
            scrollDistanceX = 0;
        }
    };

    public TaskTabView(Context context) {
        super(context);
        this.TC = new TaskTabConfig(context);
        init();
    }

    public TaskTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttribute(context, attrs);
        init();
    }

    public TaskTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttribute(context, attrs);
        init();
    }

    /**
     * 解析自定义属性
     */
    private void parseAttribute(Context context, AttributeSet attrs){
        this.TC = new TaskTabConfig(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TaskTabView);
        int margin = (int) typedArray.getDimension(R.styleable.TaskTabView_margin, -1f);
        int itemPadding = (int) typedArray.getDimension(R.styleable.TaskTabView_item_padding, -1f);
        int textSize = (int) typedArray.getDimension(R.styleable.TaskTabView_text_size, -1f);
        int textInterval = (int) typedArray.getDimension(R.styleable.TaskTabView_text_interval, -1f);
        int indicatorInterval = (int) typedArray.getDimension(R.styleable.TaskTabView_indicator_interval, -1f);
        int indicatorMB = (int) typedArray.getDimension(R.styleable.TaskTabView_indicator_margin_buttom, -1f);
        int indicatorRadius = (int) typedArray.getDimension(R.styleable.TaskTabView_indicator_radius, -1f);

        int circleColor = typedArray.getColor(R.styleable.TaskTabView_circle_color, -1);
        int circleSelectedColor = typedArray.getColor(R.styleable.TaskTabView_circle_selected_color, -1);
        int circleBorderColor = typedArray.getColor(R.styleable.TaskTabView_circle_border_color, -1);
        int textColor = typedArray.getColor(R.styleable.TaskTabView_text_color, -1);
        int textSelectedColor = typedArray.getColor(R.styleable.TaskTabView_text_selected_color, -1);
        int indicatorColor = typedArray.getColor(R.styleable.TaskTabView_indicator_color, -1);
        int indicatorSelectedColor = typedArray.getColor(R.styleable.TaskTabView_indicator_selected_color, -1);
        typedArray.recycle();
        if(margin != -1) this.TC.margin = margin;
        if(itemPadding != -1) this.TC.itemPadding = itemPadding;
        if(textSize != -1) this.TC.textSize = textSize;
        if(textInterval != -1) this.TC.textInterval = textInterval;
        if(indicatorInterval != -1) this.TC.indicatorInterval = indicatorInterval;
        if(indicatorMB != -1) this.TC.indicatorMB = indicatorMB;
        if(indicatorRadius != -1) this.TC.indicatorRadius = indicatorRadius;
        if(circleColor != -1) this.TC.circleColor = circleColor;
        if(circleSelectedColor != -1) this.TC.circleSelectedColor = circleSelectedColor;
        if(circleBorderColor != -1) this.TC.circleBorderColor = circleBorderColor;
        if(textColor != -1) this.TC.textColor = textColor;
        if(textSelectedColor != -1) this.TC.textSelectedColor = textSelectedColor;
        if(indicatorColor != -1) this.TC.indicatorColor = indicatorColor;
        if(indicatorSelectedColor != -1) this.TC.indicatorSelectedColor = indicatorSelectedColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!TC.isQuantization()){//未设置初始值时，需要配置初始值
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if(heightMode != MeasureSpec.AT_MOST) throw new RuntimeException("不可以指定控件高度，高度需要计算");
            if(widthMode == MeasureSpec.AT_MOST) throw new RuntimeException("控件需要宽度的精确值，用于计算view的高度");
            this.TC.itemSize = (widthSize - TC.margin * 2) / 7;
            this.TC.itemIntervalX = (widthSize - TC.margin * 2 - TC.itemSize * 4) / 3;
            this.TC.itemIntervalY = (widthSize - TC.margin * 2 - TC.itemSize * 4) / 3;
            this.TC.itemShrinkIntervalX = (widthSize - TC.margin * 2 - TC.itemSize * 5) / 4;

            this.TC.scrollMaxLimit = tabs.size() / 12 * widthSize;
            this.TC.shrinkMaxLimit = 2 * TC.itemSize + 3 * TC.itemIntervalY - TC.margin;
            this.TC.itemISRatio = (TC.itemIntervalY - TC.itemShrinkIntervalX) * 1f/ (TC.shrinkMaxLimit * 1f);
            this.TC.flipMinLimit = (int) (widthSize * 1f / 5f * 1);

            this.TC.viewHeight = TC.margin  + 3 * TC.itemSize + 3 * TC.itemIntervalY;
            this.TC.viewWidth = widthSize;
        }
        setMeasuredDimension(this.TC.viewWidth, this.TC.viewHeight - shrinkHeight);
        //根据伸缩值计算透明度
        alpha = 255 - (int) ((shrinkHeight * 1f) / (TC.shrinkMaxLimit * 1f) * 255);
        if(shrinkHeight == 0) STATE = STATE_OPEN;
        else if(shrinkHeight == TC.shrinkMaxLimit) {
            //状态为关闭时需要滚动第一页
            pageIndex = 0;
            STATE = STATE_CLOSE;
            scrollTo(0, 0);
        } else STATE = STATE_SHRINK;

    }

    /**
     * 计算item的位置
     */
    public void measureItemPosition(){
       for(int i = 0; i < tabs.size(); i ++){
           //展开正常情况下view的位置和透明度
           TaskTab taskTab = tabs.get(i);
           int index = i % 12;
           taskTab.iconPoint.x = (TC.itemSize + TC.itemIntervalX) * (index % 4) + TC.margin +  i / 12 * TC.viewWidth;
           taskTab.iconPoint.y = (TC.itemSize + TC.itemIntervalY) * (index / 4) + TC.margin;
           taskTab.describePoint.y = taskTab.iconPoint.y + TC.itemSize + TC.textInterval;
           taskTab.describePoint.x = taskTab.iconPoint.x;
           taskTab.describeAlpha = 255;
           taskTab.iconAlpha = 255;
           //关闭状态或者展开/收缩状态
           if(STATE != STATE_OPEN){
               //如果就在第一页，并且当前item页面==0
               if(pageIndex == 0 && pageIndex == taskTab.page){
                   if(index < 4){//第一页第一排需要描述向左下方运动
                       taskTab.iconPoint.x = (int) (taskTab.iconPoint.x - TC.itemISRatio * shrinkHeight * index);
                       taskTab.describeAlpha = alpha;
                       taskTab.describePoint.x = taskTab.describePoint.x - shrinkHeight;
                       taskTab.describePoint.y = taskTab.iconPoint.y + TC.itemSize + TC.textInterval + shrinkHeight;
                   }else{//整体下移
                       taskTab.describeAlpha = alpha;
                       taskTab.iconAlpha = alpha;
                       taskTab.iconPoint.y =  taskTab.iconPoint.y + shrinkHeight;
                       taskTab.describePoint.y = taskTab.iconPoint.y + TC.itemSize + TC.textInterval;
                   }
               }else if(pageIndex == taskTab.page){//没在第一页整体下移
                   taskTab.describeAlpha = alpha;
                   taskTab.iconAlpha = alpha;
                   taskTab.iconPoint.y =  taskTab.iconPoint.y + shrinkHeight;
                   taskTab.describePoint.y = taskTab.iconPoint.y + TC.itemSize + TC.textInterval;
               }
           }
           if(alpha < 100){//透明度小于100时
               if(i == 4 && pageIndex == 0 && pageIndex == taskTab.page){//当item是第4个并且在第一页
                   taskTab.iconPoint.x = (int) ((TC.itemSize + TC.itemIntervalX) * 4 + TC.margin - TC.itemISRatio * shrinkHeight * 4);
                   taskTab.iconPoint.y = TC.margin;
                   taskTab.describeAlpha = 255 - alpha;
                   taskTab.iconAlpha = 255 - alpha;
               }else if(pageIndex > 0){
                   if(i < 5){//第一页的5个item显示在当前第一页第一排
                       taskTab.iconPoint.x = (TC.itemSize + TC.itemShrinkIntervalX) * i + TC.margin +  pageIndex * TC.viewWidth;
                       taskTab.iconPoint.y = TC.margin;
                       taskTab.describeAlpha = 255 - alpha;
                       taskTab.iconAlpha = 255 - alpha;
                   }
               }
           }
       }
   }

    /**
     * 更新通过临界值更新view高度
     */
    private void updateHeight(){
        if(shrinkHeight >= TC.shrinkMaxLimit) this.shrinkHeight = TC.shrinkMaxLimit;
        else if(shrinkHeight <= 0) this.shrinkHeight = 0;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算item位置
        measureItemPosition();
        //根据页面返回需要画的item
        ArrayList<TaskTab> showTab = getShowTabByPage();
        //画icon
        drawIcon(canvas, showTab);
        //画描述
        drawDescribe(canvas, showTab);
        //画指示器
        drawIndicator(canvas);
    }

    private void drawIcon(Canvas canvas,  ArrayList<TaskTab> showTab) {
        int radius = TC.itemSize / 2;
        for(TaskTab taskTab: showTab){
            circlePaint.setAlpha(taskTab.iconAlpha);
            strokePaint.setAlpha(taskTab.iconAlpha);
            iconPaint.setAlpha(taskTab.iconAlpha);
            circlePaint.setColor(taskTab.isSelected ? TC.circleSelectedColor : TC.circleColor);
            canvas.drawCircle(taskTab.iconPoint.x + radius, taskTab.iconPoint.y + radius, radius, circlePaint);
            canvas.drawCircle(taskTab.iconPoint.x + radius, taskTab.iconPoint.y + radius, radius, strokePaint);
            Bitmap iconBitmap = taskTab.isSelected ? taskTab.preIcon : taskTab.norIcon;
            Rect srcRect = new Rect(0, 0, iconBitmap.getWidth(), iconBitmap.getHeight());
            RectF dstRectF = new RectF(taskTab.iconPoint.x + TC.itemPadding, taskTab.iconPoint.y + TC.itemPadding, taskTab.iconPoint.x + TC.itemSize - TC.itemPadding, taskTab.iconPoint.y + TC.itemSize - TC.itemPadding);
            canvas.drawBitmap(iconBitmap, srcRect, dstRectF, iconPaint);
        }
    }

    private void drawDescribe(Canvas canvas,  ArrayList<TaskTab> showTab) {
        for(TaskTab taskTab : showTab){
            Rect bounds = new Rect();
            describePaint.getTextBounds(taskTab.describe, 0, taskTab.describe.length(), bounds);
            float offSet=(bounds.top+bounds.bottom) / 2;
            describePaint.setColor(taskTab.isSelected ? TC.textSelectedColor : TC.textColor);
            describePaint.setAlpha(taskTab.describeAlpha);
            canvas.drawText(taskTab.describe, taskTab.describePoint.x + (TC.itemSize - bounds.width()) / 2, taskTab.describePoint.y -offSet, describePaint);
        }
    }

    private void drawIndicator(Canvas canvas) {
        int posX = getWidth() / 2 - (pageCount) * TC.indicatorInterval / 2 + getScrollX();
        int posY = getHeight() - TC.indicatorMB;
        for(int i = 0; i < pageCount + 1; i++){
            if(i == pageIndex){
                indicatorPaint.setColor(TC.indicatorSelectedColor);
            }else{
                indicatorPaint.setColor(TC.indicatorColor);
            }
            indicatorPaint.setAlpha(alpha);
            canvas.drawCircle(posX + i * TC.indicatorInterval, posY, TC.indicatorRadius, indicatorPaint);
        }
    }

    private void shrinkAnimation(int beShrinkHeight) {
        if(shrinkAnimation != null){
            shrinkAnimation.cancel();
        }
        shrinkAnimation = ValueAnimator.ofInt(shrinkHeight, beShrinkHeight);
        shrinkAnimation.setDuration(200);
        shrinkAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shrinkHeight = (Integer) animation.getAnimatedValue();
                updateHeight();
            }
        });
        shrinkAnimation.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private ArrayList<TaskTab> getShowTabByPage() {
        ArrayList<TaskTab> taskTabs = new ArrayList<>();
        for(int i = 0; i < tabs.size(); i++){
            TaskTab taskTab = tabs.get(i);
            if(taskTab.page == pageIndex || taskTab.page - 1 == pageIndex || taskTab.page + 1 == pageIndex || i < 5){
                taskTabs.add(taskTab);
            }
        }
        return taskTabs;
    }

    @Override
    public void scrollTo(int x, int y) {
        if(x <= 0) x = 0;
        else if(x >= TC.scrollMaxLimit) x = TC.scrollMaxLimit;
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private int getItemIndexByPosition(MotionEvent motionEvent){
        int touchX = (int) (getScrollX() + motionEvent.getX());
        int touchY = (int) (getScrollY() + motionEvent.getY());
        for(int i = 0; i < tabs.size(); i++){
            TaskTab taskTab = tabs.get(i);
            Point iconPoint = taskTab.iconPoint;
            if(iconPoint.x < touchX && iconPoint.x + TC.itemSize > touchX
                    && iconPoint.y < touchY && iconPoint.y + TC.itemSize > touchY)
                return i;
        }
        return -1;
    }

    public void addItemEventListener(ItemEventListener itemEventListener){
        this.itemEventListener = itemEventListener;
    }

    public void setTab(int[] norIcons, int[] preIcons, String[] describe){
        if(norIcons.length!= describe.length)
            throw new RuntimeException("数据数量不一致");
        if(norIcons.length < 5)
            throw new RuntimeException("item数量必须大于5个");
        for(int i = 0; i < norIcons.length; i ++){
            TaskTab taskTab = new TaskTab();
            taskTab.norIcon = BitmapFactory.decodeResource(getResources(), norIcons[i]);
            taskTab.preIcon = BitmapFactory.decodeResource(getResources(), preIcons[i]);
            taskTab.describe = describe[i];
            taskTab.page = i / 12;
            tabs.add(taskTab);
        }
        this.pageCount = tabs.size() / 12;
    }

    public void config(TaskTabConfig tabConfig){
        this.TC = tabConfig;
        init();
    }
}
