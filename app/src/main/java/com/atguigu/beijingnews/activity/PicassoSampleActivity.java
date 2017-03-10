package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.atguigu.beijingnews.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.gestures.GestureDetector;

import static com.atguigu.beijingnews.R.id.iv_photo;


public class PicassoSampleActivity extends AppCompatActivity {

    private PhotoView photoView;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        photoView = (PhotoView) findViewById(iv_photo);
        String url = getIntent().getStringExtra("url");

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        Picasso.with(this)
            .load(url)
            .into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                attacher.update();
            }

            @Override
            public void onError() {
            }
        });
    }
}
