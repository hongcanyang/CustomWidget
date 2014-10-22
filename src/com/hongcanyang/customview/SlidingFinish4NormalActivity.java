package com.hongcanyang.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.widget.SlidingFinishView;
import com.hongcanyang.customview.widget.SlidingFinishView.OnFinishListener;

public class SlidingFinish4NormalActivity extends Activity {

    private SlidingFinishView slidingFinishView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliding_finish_normal);
        slidingFinishView = (SlidingFinishView) findViewById(R.id.activity_sliding_finish);
        slidingFinishView.setFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {
                finish();

            }
        });
    }
}
