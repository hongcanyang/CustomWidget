package com.hongcanyang.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 *
 * @author canyang 支持垂直滑动viewpager
 *
 */
public class VerticalViewGroup extends LinearLayout {

	private static final String TAG = VerticalViewGroup.class.getSimpleName();

	private int lastY;

	private int scrollStart;
	private int scrollEnd;

	private int screenHeight;
	private Scroller scroller;

	private VelocityTracker velocityTracker;

	private boolean isScrolling = false;

	private PagerChangeListener pagerChangeListener;

	public VerticalViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenHeight = outMetrics.heightPixels;
		scroller = new Scroller(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		for (int i = 0; i < count; ++i) {
			View childView = getChildAt(i);
			measureChild(childView, widthMeasureSpec, screenHeight);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childCount = getChildCount();
			MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
			lp.height = screenHeight * childCount;
			setLayoutParams(lp);
			layoutForChild(l, r, childCount);
		}
	}

	private void layoutForChild(int l, int r, int childCount) {
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				child.layout(l, i * screenHeight, r, (i + 1) * screenHeight);
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isScrolling) {
			super.onTouchEvent(event);
		}
		obtainVelocityTracker(event);
		int action = event.getAction();
		int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			scrollStart = getScrollY();
			lastY = y;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			int dy = lastY - y;
			int scrollY = getScrollY();
			// 已经滑到顶部
			if (dy < 0 && scrollY + dy < 0) {
				dy = -scrollY;
			}
			// 已经滑到底部
			if (dy > 0 && (scrollY + dy) > getHeight() - screenHeight) {
				dy = -((scrollY + screenHeight) - getHeight());
			}
			scrollBy(0, dy);
			lastY = y;
			break;
		}
		case MotionEvent.ACTION_UP: {
			scrollEnd = getScrollY();
			int dScrollY = scrollEnd - scrollStart;
			if (wantScrollToNext()) {
				if (shouldScrollToNext()) {
					scroller.startScroll(0, getScrollY(), 0, screenHeight
							- dScrollY);
				} else {
					scroller.startScroll(0, getScrollY(), 0, -dScrollY);
				}
			}
			if (wantScrollToPre()) {
				if (shouldScrollToPre()) {
					scroller.startScroll(0, getScrollY(), 0, -screenHeight
							- dScrollY);
				} else {
					scroller.startScroll(0, getScrollY(), 0, -dScrollY);
				}
			}
			isScrolling = true;
			postInvalidate();
			recycleVelocity();
			break;
		}
		default:
			break;
		}
		return true;
	}

	private boolean shouldScrollToPre() {
		return ((scrollStart - scrollEnd) > screenHeight / 3)
				|| Math.abs(getVelocity()) > 600;
	}

	private boolean shouldScrollToNext() {
		return ((scrollEnd - scrollStart) > screenHeight / 3)
				|| Math.abs(getVelocity()) > 600;
	}

	private double getVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		return velocityTracker.getYVelocity();
	}

	private void recycleVelocity() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(0, scroller.getCurrY());
			postInvalidate();
		} else {
			int position = (int) (getScaleY() / screenHeight);
			Log.i(TAG, "finish position : " + position);
			if (pagerChangeListener != null) {
			    pagerChangeListener.pagerChange(position);
			}
			isScrolling = false;
		}
	}

	private boolean wantScrollToNext() {
		return scrollEnd > scrollStart;
	}

	private boolean wantScrollToPre() {
		return scrollStart > scrollEnd;
	}

	private void obtainVelocityTracker(MotionEvent event) {
		velocityTracker = VelocityTracker.obtain();
		velocityTracker.addMovement(event);
	}

	public void setPagerChangeListener(PagerChangeListener listener) {
		this.pagerChangeListener = listener;
	}

	public static interface PagerChangeListener {
		void pagerChange(int position);
	}
}
