package com.hongcanyang.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.verticalviewpagerdemo.R;

public class MainActivity extends Activity implements OnClickListener {

    private Button slidingFinishBtn;

    private Button verticalViewGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingFinishBtn = (Button) findViewById(R.id.main_sliding_finish);
        slidingFinishBtn.setOnClickListener(this);
        verticalViewGroupBtn = (Button) findViewById(R.id.main_vertical_viewgroup);
        verticalViewGroupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.main_sliding_finish:
            Intent intent = new Intent(MainActivity.this, SlidingFinish4ViewPagerActivity.class);
            startActivity(intent);
            break;

        case R.id.main_vertical_viewgroup: {
            Intent intent2 = new Intent(MainActivity.this, VerticalViewGroupActivity.class);
            startActivity(intent2);
            break;
        }

        default:
            break;
        }
    }
}
