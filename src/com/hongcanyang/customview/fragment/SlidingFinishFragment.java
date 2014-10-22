package com.hongcanyang.customview.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.SlidingFinish4ListviewActivity;

public class SlidingFinishFragment extends Fragment{

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sliding_finish, null);

        ListView listView = (ListView) root.findViewById(R.id.fragment_list);
        List<String> list = new ArrayList<String>();

        for (int i = 0; i <= 30; i++) {
            list.add("测试数据" + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SlidingFinish4ListviewActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
