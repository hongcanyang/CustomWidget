package com.hongcanyang.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 支持右滑退出界面的效果 适用任何的view和viewgroup
 *
 * @author canyang
 */
public class SlidingFinishView extends RelativeLayout implements OnTouchListener {

    private static final String TAG = SlidingFinishView.class.getSimpleName();

    private int screenWidth;

    private int lastX;

    private int downX;
    private int downY;

    private int scrollEnd;

    private Scroller scroller;

    private ViewGroup parentView;

    private View touchView;

    private boolean isFinish = false;

    private boolean isSliding = false;

    private ViewPager viewPager;

    private int touchSlop;

    private OnFinishListener onFinishListener;

    public SlidingFinishView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingFinishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void measureChildViewType() {
        int count = getChildCount();
        if (count < 1) {
            return;
        }
        for(int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView instanceof ScrollView) {
                touchView = childView;
            }
            if (childView instanceof AbsListView) {
                touchView = childView;
            }
            if (childView instanceof ViewPager) {
                viewPager = (ViewPager) childView;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        parentView = (ViewGroup) this.getParent();
        screenWidth = parentView.getWidth();
        measureChildViewType();
        setTouchView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isTouchOnViewPager()) {
            int item = viewPager.getCurrentItem();
            if (item > 0) {
                return false;
            }
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if (moveX - downX > touchSlop
                        && Math.abs((int) ev.getRawY() - downY) < touchSlop) {
                    return true;
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = (int) event.getRawX();
            lastX = downX;
            downY = (int) event.getRawY();
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int x = (int) event.getRawX();
            int moveY = (int) event.getRawY();
            int moveX = x;
            if (Math.abs(moveX - downX) > touchSlop
                    && Math.abs(moveY - downY) < touchSlop) {
                isSliding = true;
                if (isTouchOnAbsListView()) {
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    v.onTouchEvent(cancelEvent);
                }
            } else {
                if (isTouchOnAbsListView() && Math.abs(moveX - downX) < Math.abs((int) event.getRawY() - downY)) {
                    return v.onTouchEvent(event);
                }
            }

            int dx = lastX - x;
            if (parentView.getScrollX() >= 0 && dx > 0) {
                dx = 0;
            }
            if (isSliding) {
                parentView.scrollBy(dx, 0);
            }
            lastX = x;
            if (isTouchOnScrollerView() || isTouchOnAbsListView()) {
                return true;
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            scrollEnd = parentView.getScrollX();
            if (wantToFinish()) {
                isFinish = true;
                scrollToRight();
            } else {
                isFinish = false;
                scrollToOrigin();
            }
            isSliding = false;
            postInvalidate();
            break;
        }
        default:
            break;
        }
        if (isTouchOnScrollerView() || isTouchOnAbsListView()) {
            return v.onTouchEvent(event);
        }
        return true;
    }

    private boolean isTouchOnScrollerView() {
        return touchView != null && touchView instanceof ScrollView;
    }

    private boolean isTouchOnAbsListView() {
        return touchView != null && touchView instanceof AbsListView;
    }

    private boolean isTouchOnViewPager() {
        return viewPager != null;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            parentView.scrollTo(scroller.getCurrX(), 0);
            invalidate();
            if (onFinishListener != null && scroller.isFinished() && isFinish) {
                onFinishListener.onFinish();
            }
        } else {
            if (onFinishListener != null && isFinish) {
                onFinishListener.onFinish();
            }
        }
    }

    private void scrollToOrigin() {
        int delta = parentView.getScrollX();
        scroller.startScroll(parentView.getScrollX(), 0, -delta, 0, Math.abs(delta));
    }

    private void scrollToRight() {
        int delta = screenWidth + scrollEnd;
        scroller.startScroll(parentView.getScrollX(), 0, -delta + 10, 0, Math.abs(delta));
    }

    @SuppressLint("NewApi")
    private boolean wantToFinish() {
        return (scrollEnd < -screenWidth / 2);
    }

    private void setTouchView() {
        if (touchView == null) {
            touchView = this;
        }
        touchView.setOnTouchListener(this);
    }

    public void setFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }

    public static interface OnFinishListener {
        void onFinish();
    }

}
