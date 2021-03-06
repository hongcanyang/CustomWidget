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
    private Button scaleImageBtn, likeWXTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingFinishBtn = (Button) findViewById(R.id.main_sliding_finish);
        slidingFinishBtn.setOnClickListener(this);
        verticalViewGroupBtn = (Button) findViewById(R.id.main_vertical_viewgroup);
        verticalViewGroupBtn.setOnClickListener(this);
        scaleImageBtn = (Button)findViewById(R.id.main_scale_image);
        scaleImageBtn.setOnClickListener(this);
        likeWXTP = (Button) findViewById(R.id.main_like_weixin_takephoto);
        likeWXTP.setOnClickListener(this);
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
        case R.id.main_scale_image: {
            Intent intent3 = new Intent(MainActivity.this, ScaleImageActivity.class);
            startActivity(intent3);
            break;
        }
        case R.id.main_like_weixin_takephoto: {
            Intent intent5 = new Intent(MainActivity.this, LikeWeixinTakePhotoActivity.class);
            startActivity(intent5);
            break;
        }

        default:
            break;
        }
    }
}
