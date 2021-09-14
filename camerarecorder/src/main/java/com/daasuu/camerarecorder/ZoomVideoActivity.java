package com.daasuu.camerarecorder;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.danikula.videocache.HttpProxyCacheServer;
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

public class ZoomVideoActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    String video_link="empty";
    GestureDetector gestureDetector;
    public VideoView zoom_video=null;
    public boolean playing=true;
    public int dura=1000;
    public int this_progress=1;
    public SeekBar seekBar=null;
    public CountDownTimer c_out=null;
    public int i=1000;
    String AES = "AES";
    private String order = "empty";
    String photo_likes="0";
    private boolean isliked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(ZoomVideoActivity.this, ZoomVideoActivity.this);
        setContentView(R.layout.item_videozoom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        video_link=data.getStringExtra("link");
        photo_likes=data.getStringExtra("likes");
        String photo_views=data.getStringExtra("views");
        String video_height=data.getStringExtra("height");
        order = data.getStringExtra("order");
        zoom_video=findViewById(R.id.zoom_video);
        zoom_video.getLayoutParams().height=(int) Float.parseFloat(video_height);
        try {
            HttpProxyCacheServer proxy = App.getProxy(this);
            String proxyUrl = proxy.getProxyUrl(video_link);
            zoom_video.setVideoPath(video_link);
            zoom_video.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
        findViewById(R.id.v_opt_b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeFunc();
            }
        });
        findViewById(R.id.v_opt_b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFunc();
            }
        });
        findViewById(R.id.v_opt_b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFunc();
            }
        });
        seekBar=findViewById(R.id.video_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this_progress=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pause_seeked=this_progress*zoom_video.getDuration()/100;
                zoom_video.seekTo(pause_seeked);
            }
        });

        StartSeeking();
    }
    private void StartSeeking()
    {
        int x=1;
        dura=zoom_video.getDuration()/1000;
        if(dura==0)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    StartSeeking();
                }
            }, 1000);
        }
        else {
                c_out = new CountDownTimer(dura * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    i = i + 1000;
                    int x = (i / (dura*10));
                    seekBar.setProgress(x);
                }
                public void onFinish() {
                    c_out.cancel();
                }
            }.start();
        }
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
        if(playing) {
            playing=false;
            zoom_video.pause();
        }
        else{
            playing=true;
            zoom_video.start();
        }
        return false;
    }
    public void onBackPressed(){
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
    }
    @Override
    public void finish() {
        c_out.cancel();
        super.finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
    }
    void LikeFunc() {
        Intent intent = new Intent(this, LikeActivity.class);
        startActivity(intent);
        if (!Objects.equals(order, "empty")) {
            String[] basename = video_link.split("/");
            String x1 = basename[basename.length - 1].split("\\.")[0];
            if (!isliked) {
                String tosave = CollageActivity.Alist.split("::")[Integer.parseInt(order)];
                isliked=true;
                try {
                    WriteGAFF(tosave, x1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String encryption(String input, String password) throws Exception {
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptValue = cipher.doFinal(input.getBytes());
        String encryptedValue = Base64.encodeToString(encryptValue, Base64.DEFAULT);
        return encryptedValue;
    }

    private String decryption(String output, String password) throws Exception {
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.decode(output, Base64.DEFAULT);
        byte[] decriptValue = cipher.doFinal(decodedValue);
        String decriptedValue = new String(decriptValue);
        return decriptedValue;
    }

    private SecretKeySpec generateKeySpec(String password) throws Exception {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] keySpec = messageDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySpec, "AES");
        return secretKeySpec;
    }
    void WriteGAFF(String indata,String basename) throws Exception {
        String textfile = "/storage/emulated/0/resg.gaff";
        if(new File(textfile).exists()) {
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(textfile));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            String received = text.toString();
            String de_data=decryption(received,"#aa$aa$");
            String[] arr=de_data.split("--");
            if (Arrays.asList(arr).contains(basename)) {
            }
            else{
                Increment();
                String newdata=de_data+"--"+basename;
                pref(indata,basename,newdata);
            }
        }
        else {
            Increment();
            pref(indata,basename,basename);
        }
    }
    void Increment()
    {
        TextView t = findViewById(R.id.imgb2_t);
        int t_like=Integer.parseInt(photo_likes)+1;
        String st_like=String.valueOf(t_like);
        t.setText(st_like);
    }
    void pref(String indata,String basename,String prenames)
    {
        String textfile = "/storage/emulated/0/resg.gaff";
        OutputStream outputStream;
        try {
            outputStream = getApplicationContext().getContentResolver().openOutputStream(Uri.fromFile(new File(textfile)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            String in = encryption(prenames, "#aa$aa$");
            bw.write(in);
            bw.flush();
            bw.close();
            WriteSingle(indata, basename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void WriteSingle(String indata,String basename) {
        OutputStream outputStream;
        String textfile="/storage/emulated/0/Android/data/com.datas.flusters/" +basename+".cache";
        try {
            String a1 = "";
            outputStream = getApplicationContext().getContentResolver().openOutputStream(Uri.fromFile(new File(textfile)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            String in=encryption(indata,"#aa$aa$");
            bw.write(in);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}