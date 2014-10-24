package com.hongcanyang.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class ScaleImageView extends ImageView implements OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener{

    private static final String TAG = ScaleImageView.class.getSimpleName();
    private static final float SCALE_MAX = 3.0f;
    private static final float SCALE_MIN = 1.0f;

    private static final float SCALE_BIGGING = 1.08f;
    private static final float SCALE_SMALLING = 0.92f;

    private boolean once = true;

    private ScaleGestureDetector scaleGestureDetector;

    private Matrix matrix = new Matrix();

    private float initScale = 1.0f;

    private long lastMoveUpTime = 0;
    private boolean isBigMode = false;
    private boolean isScaling = false;

    private int downX;
    private int downY;

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];

    public ScaleImageView(Context context) {
        this(context, null);
    }

    @SuppressLint("NewApi")
    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }
            int width = getWidth();
            int height = getHeight();
            // 拿到图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height) {
                scale = width * 1.0f / dw;
            }
            if (dh > height && dw <= width) {
                scale = height * 1.0f / dh;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            initScale = scale;

            matrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            matrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            // 图片移动至屏幕中心
            setImageMatrix(matrix);
            once = false;
        }
    }

    // 双指滑动缩放
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Drawable d = getDrawable();
        if (d == null) {
            return true;
        }
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        Log.i(TAG, "scale : " + scale + " scale factor : " + scaleFactor);
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            checkBorderAndCenterWhenScale();
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            setImageMatrix(matrix);
        }
        return true;
    }

    /**
     * 在缩放时，进行图片显示范围的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix tempMatrix = matrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            tempMatrix.mapRect(rect);
        }
        return rect;
    }

    public final float getScale() {
        matrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = (int) event.getRawX();
            downY = (int) event.getRawY();
            break;
        }
        case MotionEvent.ACTION_UP: {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            long time = System.currentTimeMillis();
            if (time - lastMoveUpTime < 300) {
                if (Math.abs(downX - x) > 5 || Math.abs(downY - y) > 5) {
                    return true;
                }
                if (isScaling) {
                    return true;
                }
                if (isBigMode) {
                    isBigMode = false;
                    postDelayed(new AsyScaleTask(1.0 / SCALE_MAX, 16, getWidth() / 2, getHeight() / 2), 16);
                } else {
                    isBigMode = true;
                    postDelayed(new AsyScaleTask(SCALE_MAX, 16, getWidth() / 2, getHeight() / 2), 16);
                }
            }
            lastMoveUpTime = time;
            return true;
        }

        default:
            break;
        }
        return true;
    }

    // 支持点击速度缩放的效果
    private class AsyScaleTask implements Runnable {

        private double targetScale;
        private long delayTime;
        private int x;
        private int y;

        private float tempScale;

        public AsyScaleTask(double targetScale, long delayTime, int x, int y) {
            this.targetScale = targetScale * getScale();
            this.delayTime = delayTime;
            this.x = x;
            this.y = y;
            Log.i(TAG, " cur : " + getScale());

            if (getScale() < targetScale) {
                tempScale = SCALE_BIGGING;
            } else {
                tempScale = SCALE_SMALLING;
            }
            isScaling = true;
        }

        @Override
        public void run() {
            float currentScale = getScale();
            Log.i(TAG, "cur scale : " + currentScale + " tag : " + targetScale);
            checkBorderAndCenterWhenScale();
            matrix.postScale(tempScale, tempScale, x, y);
            setImageMatrix(matrix);
            if ((tempScale > 1.0f && currentScale < targetScale)||(tempScale < 1.0f && currentScale > targetScale)) {
                ScaleImageView.this.postDelayed(this, delayTime);
            } else {
                tempScale = (float) (targetScale * 1.0f / currentScale);
                checkBorderAndCenterWhenScale();
                matrix.postScale(tempScale, tempScale, x, y);
                setImageMatrix(matrix);
                isScaling = false;
            }
        }
    }
}
