package com.hongcanyang.customview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.widget.SlidingFinishView;
import com.hongcanyang.customview.widget.SlidingFinishView.OnFinishListener;

public class SlidingFinish4ListviewActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliding_finish_listview);
        listView = (ListView) findViewById(R.id.sliding_finish_listview);
        List<String> list = new ArrayList<>();

        for (int i = 0; i <= 30; i++) {
            list.add("测试数据" + i);
        }

        ListView listView = (ListView) findViewById(R.id.sliding_finish_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        SlidingFinishView slidingFinishView = (SlidingFinishView) findViewById(R.id.activity_sliding_finish_listview);
        slidingFinishView.setFinishListener(new OnFinishListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SlidingFinish4ListviewActivity.this, SlidingFinish4NormalActivity.class);
                startActivity(intent);
            }
        });
    }
}
