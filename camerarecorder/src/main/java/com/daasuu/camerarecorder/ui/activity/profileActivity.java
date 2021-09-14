package com.daasuu.camerarecorder.ui.activity;

import android.app.assist.AssistStructure;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.daasuu.camerarecorder.AutoPlayVideoRecyclerView;
import com.daasuu.camerarecorder.MainZoomActivity;
import com.daasuu.camerarecorder.PZoomActivity;
import com.daasuu.camerarecorder.StoryVideoActivity;
import com.daasuu.camerarecorder.mode.Feed;
import com.daasuu.camerarecorder.mode.Photo;
import com.daasuu.camerarecorder.mode.Video;
import com.daasuu.camerarecorder.ui.adapter.PhotoFeedAdapter;
import com.daasuu.camerarecorder.ui.view.CenterLayoutManager;
import com.kalasa.library.R;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {
    public AutoPlayVideoRecyclerView listFeed;
    private PhotoFeedAdapter adapter;
    public static String cids = "fj";
    String allCollarge="empty";
    public static String cusernames = "fj";
    public static String cpasswords = "fj";
    private boolean cond=false;
    private String type="empty";
    private boolean done=false;
    private String replier="empty";
    private String thisLink="";
    private  String infos="";
    String name="";
    private boolean cond_feed=false;
    private String thisstory="empty";
    private boolean opened=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videorecycler);
        listFeed=findViewById(R.id.listFeed);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        allCollarge=data.getStringExtra("Collarge");
        cids=data.getStringExtra("Collarge");
        cpasswords=data.getStringExtra("Password");
        cusernames=data.getStringExtra("CoUserName");
        thisstory=data.getStringExtra("story");
        listFeed.setNestedScrollingEnabled(true);
        listFeed.setHasFixedSize(false);
        ScrollView sc_about=findViewById(R.id.sc_about);
        LinearLayout sub_main=findViewById(R.id.sub_main);
        ScrollView sc_main=findViewById(R.id.sc_main);
        TextView p_name=findViewById(R.id.p_name);
        /*sc_main.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY>400 && opened==false) {
                        //sc_main.scrollTo(0,sc_main.getBottom());
                        sc_main.scrollBy(0,700);
                        Toast.makeText(profileActivity.this, "greater", Toast.LENGTH_SHORT).show();
                        sc_about.setVisibility(View.GONE);
                        opened=true;
                    }
            }
        });*/
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listFeed.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if(!cond_feed ) {
                            if (opened) {
                            }
                        }
                    }
                });
            }
        }, 2000);*/
        findViewById(R.id.expand_about).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!opened)
                {
                    opened=true;
                    sc_main.smoothScrollTo(0,900);
                    sc_about.setVisibility(View.VISIBLE);
                    if(!done)
                    {
                        done=true;
                        DoOnceFUn();
                    }
                }
                else {
                    opened=false;
                    sc_main.smoothScrollTo(0,sc_main.getBottom());
                    sc_about.setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                replier="message";
                finish();
            }
        });
        findViewById(R.id.p_texture).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!Objects.equals(thisstory, "empty")) {
                    String[] a = thisstory.split("=:=");
                    String b = a[0].substring(2,6);
                    switch (b) {
                        case ".png":
                            type="png";
                            storypicture_fun(a);
                            break;
                        case ".txt":
                            type="txt";
                            storytext_fun(a[0]);
                            break;
                        case ".mp4":
                            type="mp4";
                            storyvideo_fun(a);
                            break;
                        default:
                            type="none";
                            break;
                    }
                }
                else{
                    Toast.makeText(profileActivity.this, "No Story posted yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_frequest).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(profileActivity.this, "Friend Request Send to "+name, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_voice).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(profileActivity.this, "Service currently not availbale in your Region", Toast.LENGTH_SHORT).show();
            }
        });
        initView(p_name);
    }
    private void initView(TextView p_name) {
        AutoPlayVideoRecyclerView listFeed=findViewById(R.id.listFeed);
        adapter = new PhotoFeedAdapter(this);
        listFeed.setLayoutManager(new CenterLayoutManager(this));
        listFeed.setAdapter(adapter);
        initData(p_name);
    }
    void storyvideo_fun(String[] x1)
    {
        int y=Integer.parseInt(x1[1]);
        String y1=String.valueOf(y*4);
        String linked="https://s3.ap-south-1.amazonaws.com/toasterco.com/"+thisLink+".png";
        Intent intent = new Intent(this, MainZoomActivity.class);
        intent.putExtra("link",linked);
        intent.putExtra("likes",x1[1]);
        intent.putExtra("views",y1);
        intent.putExtra("story",thisstory);
        intent.putExtra("type",type);
        startActivity(intent);
    }
    void storypicture_fun(String[] x1)
    {
        int y=Integer.parseInt(x1[1]);
        String y1=String.valueOf(y*4);
        String linked="https://s3.ap-south-1.amazonaws.com/toasterco.com/"+thisLink+".png";
        Intent intent = new Intent(this, MainZoomActivity.class);
        intent.putExtra("link",linked);
        intent.putExtra("likes",x1[1]);
        intent.putExtra("views",y1);
        intent.putExtra("story",thisstory);
        intent.putExtra("type",type);
        startActivity(intent);
    }
    void storytext_fun(String link)
    {

    }
    public void writeInFile(String text) {
        OutputStream outputStream;
        String textfile="/storage/emulated/0/Android/data/com.datas.writes/" + Calendar.getInstance().getTimeInMillis()+".cache";
        try {
            outputStream = getContentResolver().openOutputStream(Uri.fromFile(new File(textfile)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
private void DoOnceFUn()
{
    TextView pv1=findViewById(R.id.t_opt1);
    TextView pv2=findViewById(R.id.t_opt2);
    TextView pv3=findViewById(R.id.t_opt3);
    if(infos.contains("<>"))
    {
        String[] a1=infos.split("<>");
        if(a1[0].length()>1)
        {
            pv1.setText(a1[0]);
        }
        else{
            pv1.setText("Nothing to Show Here");
            pv1.setTextColor(Color.parseColor("#B3000000"));
        }
        if(a1[0].length()>1)
        {
            pv2.setText(a1[1]);
        }
        else{
            pv2.setText("Nothing to Show Here");
            pv2.setTextColor(Color.parseColor("#B3000000"));
        }
        if(a1[0].length()>1)
        {
            pv3.setText(a1[2]);
        }
        else{
            pv3.setText("Nothing to Show Here");
            pv3.setTextColor(Color.parseColor("#B3000000"));
        }
    }
    else{
        pv1.setText("Nothing to Show Here");
        pv2.setText("Nothing to Show Here");
        pv3.setText("Nothing to Show Here");
        pv3.setTextColor(Color.parseColor("#B3000000"));
        pv2.setTextColor(Color.parseColor("#B3000000"));
        pv1.setTextColor(Color.parseColor("#B3000000"));
    }
}
    private void initData(TextView p_name) {
        CircleImageView p_tex=findViewById(R.id.p_texture);
        String[] a = allCollarge.split("::");
        thisLink=a[0];
        String Ilink="https://s3.ap-south-1.amazonaws.com/toasterco.com/"+thisLink+"_fsmall.png";
        Glide.with(this).load(Ilink).into(p_tex);
        if(a.length>2) {
            if (a[2].contains("<>")) {
                infos = a[2];
            }
            p_name.setText(a[1]);
            name=a[1];
            if(a.length>3) {
                if (a[3].length() > 0 && a[3].contains("<-:>")) {
                    String[] x = a[3].split("<-:>");
                    for (int i = 0; i < x.length; i++) {
                        String[] b = x[i].split("--");
                        if(!b[2].contains(":-:")) {
                            String z = b[2].substring(24, b[2].length());
                            if (b[0].contains(".png")) {
                                String y = b[0].substring(0, b[0].length() - 3);
                                adapter.add(new Feed(new Photo("https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + y), Feed.Model.M1, b[1], z, b[3]));
                            } else {
                                String y = b[0].substring(0, b[0].length() - 7);
                                adapter.add(new Feed(new Video("https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + y + "-00001.png",
                                        "https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + y + ".mp4",
                                        0), Feed.Model.M2, b[1], z, b[3]));
                            }
                        }
                    }
                } else {
                    showNoPost();
                }
            }
            else{
                ShowInfos();
            }
        }
        else
        {
            ShowInfos();
        }
    }
    void showNoPost()
    {
        ImageView nopost=findViewById(R.id.no_post);
        nopost.setVisibility(View.VISIBLE);
    }
    void ShowInfos()
    {
        ImageView nopost=findViewById(R.id.no_post);
        nopost.setVisibility(View.VISIBLE);
        ScrollView sc_about=findViewById(R.id.sc_about);
        sc_about.setVisibility(View.VISIBLE);
        TextView pv1=findViewById(R.id.t_opt1);
        TextView pv2=findViewById(R.id.t_opt2);
        TextView pv3=findViewById(R.id.t_opt3);
        pv1.setText("Nothing to Show Here");
        pv2.setText("Nothing to Show Here");
        pv3.setText("Nothing to Show Here");
        pv3.setTextColor(Color.parseColor("#B3000000"));
        pv2.setTextColor(Color.parseColor("#B3000000"));
        pv1.setTextColor(Color.parseColor("#B3000000"));
    }
    @Override
    protected void onResume() {
        super.onResume();
        listFeed=findViewById(R.id.listFeed);
        if (listFeed.getHandingVideoHolder() != null) listFeed.getHandingVideoHolder().playVideo();
    }
    @Override
    protected void onPause() {
        super.onPause();
        listFeed=findViewById(R.id.listFeed);
        if (listFeed.getHandingVideoHolder() != null) listFeed.getHandingVideoHolder().stopVideo();
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("ReplyArray",replier);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}