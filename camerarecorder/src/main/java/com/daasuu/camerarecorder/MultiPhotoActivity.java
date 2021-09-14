package com.daasuu.camerarecorder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MultiPhotoActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    String in_link="empty";
    GestureDetector gestureDetector;
    private String order = "empty";
    String photo_likes="0";
    private boolean isliked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_multiphoto);
        gestureDetector = new GestureDetector(MultiPhotoActivity.this, MultiPhotoActivity.this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data = this.getIntent();
        in_link = data.getStringExtra("link");
        photo_likes = data.getStringExtra("likes");
        String photo_views = data.getStringExtra("views");
        order = data.getStringExtra("order");
        LinearLayout out1 = findViewById(R.id.out1);
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outmetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outmetrics);
        int this_width = outmetrics.widthPixels;
        String[] s2 = in_link.split("-:-");
        for (int j = 0; j < s2.length; j++) {
            String[] s3 = s2[j].split("=");
            ImageView photo = new ImageView(this);
            out1.addView(photo);
            Picasso picasso = Picasso.with(this);
            float x1 = Float.parseFloat(s3[1]);
            String this_link="https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/"+s3[0]+".png";
            RequestCreator requestCreator = picasso.load(this_link);
            requestCreator.into(photo);
            photo.getLayoutParams().width=this_width;
            photo.requestLayout();
            photo.setScaleType(ImageView.ScaleType.FIT_XY);
            photo.getLayoutParams().width=this_width;
            photo.requestLayout();
        }
        findViewById(R.id.opt_b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeFunc();
            }
        });
        findViewById(R.id.opt_b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFunc();
            }
        });
        findViewById(R.id.opt_b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFunc();
            }
        });
    }
    void CommentFunc() {
    }
    void ShareFunc() {
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return this.gestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent event) {
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent event) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }
    @Override public boolean onDoubleTapEvent(MotionEvent event) {
        LikeFunc();
        return true;
    }
    @Override public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }
    void LikeFunc() {
        Intent intent = new Intent(this, LikeActivity.class);
        startActivity(intent);
        if (!Objects.equals(order, "empty")) {
            String x1 = in_link.split("=")[0];
            if (!isliked) {
                Increment();
                isliked=true;
            }
        }
    }
    void Increment()
    {
        TextView t = findViewById(R.id.imgb2_t);
        int t_like=Integer.parseInt(photo_likes)+1;
        String st_like=String.valueOf(t_like);
        t.setText(st_like);
    }
}