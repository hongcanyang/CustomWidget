package com.hongcanyang.customview;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.verticalviewpagerdemo.R;
import com.hongcanyang.customview.widget.likeweixinpic.ClipImageLayout;

public class LikeWeixinTakePhotoActivity extends Activity {

    private ClipImageLayout clipImageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_weixin_takephoto);
        clipImageLayout = (ClipImageLayout) findViewById(R.id.like_weixin_clip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.id_action_clip:
            Bitmap bitmap = clipImageLayout.clipBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            Intent intent = new Intent(this, ShowImageActivity.class);
            intent.putExtra("bitmap", datas);
            startActivity(intent);

            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
