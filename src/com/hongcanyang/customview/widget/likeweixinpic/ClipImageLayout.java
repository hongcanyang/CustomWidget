package com.hongcanyang.customview.widget.likeweixinpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.widget.ScaleImageView;

public class ClipImageLayout extends RelativeLayout {

    private ScaleImageView scaleImageView ;

    public ClipImageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ClipImageBorderView clipImageBorderView = new ClipImageBorderView(context);
        scaleImageView = new ScaleImageView(context);

        scaleImageView.setImageDrawable(getResources().getDrawable(
                R.drawable.a));


        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        clipImageBorderView.setHorizontalPadding(50);
        scaleImageView.setHorizontalPadding(50);

        addView(scaleImageView, lp);
        addView(clipImageBorderView, lp);
        requestLayout();
    }

    public ClipImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageLayout(Context context) {
        this(context, null);
    }

    public Bitmap clipBitmap() {
        return scaleImageView.clip();
    }

}
