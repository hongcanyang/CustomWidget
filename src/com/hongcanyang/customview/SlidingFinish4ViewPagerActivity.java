package com.hongcanyang.customview;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.fragment.SlidingFinishFragment;
import com.hongcanyang.customview.widget.SlidingFinishView;
import com.hongcanyang.customview.widget.SlidingFinishView.OnFinishListener;

public class SlidingFinish4ViewPagerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliding_finish_viewpager);
        SlidingFinishView slidingFinishView = (SlidingFinishView) findViewById(R.id.activity_sliding_finish_viewpager);
        slidingFinishView.setFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.sliding_finish_viewpager);
        SlidingFinishPagerAdapter adapter = new SlidingFinishPagerAdapter(getSupportFragmentManager());
        SlidingFinishFragment fragment1 = new SlidingFinishFragment();
        SlidingFinishFragment fragment2 = new SlidingFinishFragment();
        SlidingFinishFragment fragment3 = new SlidingFinishFragment();
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        adapter.setFragments(list);
        viewPager.setAdapter(adapter);
    }

    private static class SlidingFinishPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list = new ArrayList<Fragment>();

        public SlidingFinishPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<Fragment> list) {
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }


        @Override
        public int getCount() {
            return list.size();
        }
    }
}
