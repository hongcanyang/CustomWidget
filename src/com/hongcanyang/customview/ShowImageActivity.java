package com.hongcanyang.customview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.verticalviewpagerdemo.R;


public class ShowImageActivity extends Activity{

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        imageView = (ImageView) findViewById(R.id.id_showImage);
        byte[] b = getIntent().getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
        }
    }
}
