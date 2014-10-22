package com.hongcanyang.customview.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class AutoScrollViewpager extends ViewPager {

    private static final String TAG = AutoScrollViewpager.class.getSimpleName();
    private List<Object> banners;
    private TextView[] tips;
    private List<ImageView> imageViews;
    private ViewGroup tipViewGroup;

    private float radio;

    private OnItemPicClickListener listener;

    private static final int AUTO_SCROLL_MODE = 0;
    private boolean isPause = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (isPause) {
                return;
            }
            if (msg.what == AUTO_SCROLL_MODE) {
                int count = imageViews.size();
                if (count > 1) {
                    int index = getCurrentItem();
                    index = index % (count - 2) + 1; // 这里修改过
                    setCurrentItem(index, true);
                }
            }
        };
    };

    public AutoScrollViewpager(Context context) {
        super(context);
    }

    public AutoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO TypedArray a = context.obtainStyledAttributes(attrs,
        // R.styleable.BannerView);
        // TODO radio = a.getFloat(R.styleable.BannerView_wHradio, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * radio);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTips(ViewGroup viewGroup) {
        this.tipViewGroup = viewGroup;
    }

    private void initTip(Context context) {
        tipViewGroup.removeAllViews();
        if (banners == null || banners.size() == 0) {
            return;
        }

        tips = new TextView[banners.size()];
        // TODO
        /*
         * for (int i = 0; i < tips.length; i++) { TextView textView = new
         * TextView(context); textView.setText(i + 1 + ""); LayoutParams params
         * = new LayoutParams(); textView.setGravity(Gravity.CENTER);
         * textView.setWidth(UIUtil.dp2Px(getContext(), 18));
         * textView.setHeight(UIUtil.dp2Px(getContext(), 18));
         * textView.setLayoutParams(params);
         * textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
         * textView.setTextColor
         * (getContext().getResources().getColor(R.color.white_bg)); tips[i] =
         * textView; if (i == 0) {
         * tips[i].setBackgroundResource(R.drawable.ic_topic_select); } else {
         * tips[i].setBackgroundResource(R.drawable.ic_topic_nor); }
         * LinearLayout.LayoutParams layoutParams = new
         * LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
         * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
         * layoutParams.leftMargin = UIUtil.px2Dp(getContext(), 4);
         * layoutParams.rightMargin = UIUtil.px2Dp(getContext(), 4);
         * layoutParams.bottomMargin = UIUtil.px2Dp(getContext(), 7);
         * layoutParams.topMargin = UIUtil.px2Dp(getContext(), 6);
         * layoutParams.gravity = Gravity.CENTER; tipViewGroup.addView(textView,
         * layoutParams); }
         */
    }

    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            downX = ev.getX();
            downY = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
            break;

        case MotionEvent.ACTION_MOVE:
            if (Math.abs(ev.getX() - downX) > Math.abs(ev.getY() - downY)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
            break;
        case MotionEvent.ACTION_UP:
        default:
            getParent().requestDisallowInterceptTouchEvent(false);
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void addPageChangeListener(final Context context) {
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setSelectTip(context, arg0);
            }

            private void setSelectTip(final Context context, int pos) {
                if (imageViews.size() > 1) { // 多于1，才会循环跳转
                    if (pos < 1) { // 首位之前，跳转到末尾（N）
                        // 注意这里是mList，而不是mViews
                        setCurrentItem(banners.size(), false);
                    } else if (pos > banners.size()) { // 末位之后，跳转到首位（1）
                        setCurrentItem(1, false); // false:不m显示跳转过程的动画
                    }
                }
                // TODO
                /*
                 * for (int i = 0; i < tips.length; i++) { if (i ==
                 * (getCurrentItem() - 1)) {
                 * tips[i].setBackgroundResource(R.drawable.ic_topic_select); }
                 * else {
                 * tips[i].setBackgroundResource(R.drawable.ic_topic_nor); } }
                 */
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                    startHandleMsg();
                } else if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
                    pauseHandleMsg();
                } else if (arg0 == ViewPager.SCROLL_STATE_SETTLING) {
                    pauseHandleMsg();
                } else {
                    pauseHandleMsg();
                }
            }

            private void startHandleMsg() {
                handler.removeMessages(AUTO_SCROLL_MODE);
                isPause = false;
                Message msg = handler.obtainMessage(AUTO_SCROLL_MODE);
                handler.sendMessageDelayed(msg, 3000);
            }

        });
    }

    private void pauseHandleMsg() {
        handler.removeMessages(AUTO_SCROLL_MODE);
        isPause = true;
    }

    private class AutoScrollViewPageAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = imageViews.get(position);
            container.addView(view);
            return view;
        }
    }

    public void init(Context context, final List<Object> banners) {
        pauseHandleMsg();
        if (banners == null || banners.size() == 0) {
            return;
        }
        if (banners.equals(this.banners)) {
            return;
        }
        this.banners = banners;
        if (banners != null) {
            // 注意，如果不只1个，mViews比mList多两个（头尾各多一个）
            // 假设：mList为mList[0~N-1], mViews为mViews[0~N+1]
            // mViews[0]放mList[N-1], mViews[i]放mList[i-1], mViews[N+1]放mList[0]
            // mViews[1~N]用于循环；首位之前的mViews[0]和末尾之后的mViews[N+1]用于跳转
            // 首位之前的mViews[0]，跳转到末尾（N）；末位之后的mViews[N+1]，跳转到首位（1）
            imageViews = new ArrayList<ImageView>();
            if (banners.size() > 1) { // 多于1个要循环
                // 无论是否多于1个，都要初始化第一个（index:0）
                addFirstView(context, banners, banners.size() - 1);

                addAutoScrollViews(context, banners);
                // 最后一个（index:N+1）
                addLastView(context, banners, 0);
            } else {
                addItemView(context, banners, 0);
            }
        }

        setAdapter(new AutoScrollViewPageAdapter());
        addPageChangeListener(context);
        initTip(context);
        if (banners.size() > 1) {
            setCurrentItem(1);
        }
        isPause = false;
        Message msg = handler.obtainMessage(AUTO_SCROLL_MODE);
        handler.sendMessageDelayed(msg, 3000);
        requestLayout();
    }

    private void addFirstView(Context context, final List<Object> banners, final int position) {
        addItemView(context, banners, position);
    }

    private void addItemView(Context context, final List<Object> banners, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ScaleType.FIT_XY);
        LayoutParams params = new LayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        imageView.setLayoutParams(params);
        // TODO
        /*
         * imageView.setBackgroundResource(R.drawable.ic_icon_loading); String
         * url = banners.get(position).imageInfos.getSmallUrl();
         * UIUtil.setImage(url, imageView, true);
         */
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
        imageViews.add(imageView);
    }

    private void addLastView(Context context, List<Object> banners, int position) {
        addFirstView(context, banners, position);
    }

    private void addAutoScrollViews(Context context, List<Object> banners) {
        for (int i = 0; i < banners.size(); i++) {
            addItemView(context, banners, i);
        }
    }

    public void setOnItemClickListener(OnItemPicClickListener listener) {
        this.listener = listener;
    }

    public static interface OnItemPicClickListener {
        void onClick(int position);
    }
}
