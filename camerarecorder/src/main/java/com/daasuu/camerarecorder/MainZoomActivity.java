package com.daasuu.camerarecorder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Objects;

public class MainZoomActivity extends AppCompatActivity {
    String photo_link="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pzoom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        photo_link=data.getStringExtra("link");
        String photo_likes=data.getStringExtra("likes");
        String photo_views=data.getStringExtra("views");
        String thisstory=data.getStringExtra("story");
        String type=data.getStringExtra("type");
        if(!Objects.equals(type, "none")) {
            findViewById(R.id.btn_zoom).setVisibility(View.VISIBLE);
            String[] a = thisstory.split("=:=");
            findViewById(R.id.btn_zoom).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (type) {
                        case "mp4":
                            storyvideo_fun(a);
                            break;
                        case "png":
                            storypicture_fun(a);
                            break;
                        case "txt":
                            break;
                    }
                }
            });
        }
        ImageView photo=findViewById(R.id.img_photo);
        Picasso picasso = Picasso.with(this);
        RequestCreator requestCreator = picasso.load(photo_link);
        requestCreator.into(photo);
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
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
    void LikeFunc()
    {
        Intent intent = new Intent(this, LikeActivity.class);
        startActivity(intent);
    }
    void CommentFunc()
    {

    }
    void ShareFunc()
    {

    }
     void storyvideo_fun(String[] x1)
    {
        int y=Integer.parseInt(x1[1]);
        String y1=String.valueOf(y*4);
        String x2=x1[5]+"--"+"2"+"--"+x1[1]+"--"+y1+"--"+"0";
        Intent intent = new Intent(this, StoryVideoActivity.class);
        intent.putExtra("options",x2);
        startActivity(intent);
    }
     void storypicture_fun(String[] x1)
    {
        int y=Integer.parseInt(x1[1]);
        String y1=String.valueOf(y*4);
        Intent intent = new Intent(this, PZoomActivity.class);
        intent.putExtra("link",x1[5]);
        intent.putExtra("likes",x1[1]);
        intent.putExtra("views",y1);
        intent.putExtra("height","2");
        startActivity(intent);
        }
}