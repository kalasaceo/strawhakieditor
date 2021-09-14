package com.daasuu.camerarecorder;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ZoomActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    String photo_link = "empty";
    public RelativeLayout showdata = null;
    public RelativeLayout dodata = null;
    String AES = "AES";
    GestureDetector gestureDetector;
    public ImageView promo = null;
    public ConstraintLayout back = null;
    public boolean isSeen = true;
    private String order = "empty";
    String photo_likes="0";
    private boolean isliked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_zoom);
        gestureDetector = new GestureDetector(ZoomActivity.this, ZoomActivity.this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data = this.getIntent();
        photo_link = data.getStringExtra("link");
        photo_likes = data.getStringExtra("likes");
        String photo_views = data.getStringExtra("views");
        String photo_height = data.getStringExtra("height");
        order = data.getStringExtra("order");
        ImageView photo = findViewById(R.id.img_photo);
        Picasso picasso = Picasso.with(this);
        photo.getLayoutParams().height = (int) Float.parseFloat(photo_height);
        photo.requestLayout();
        RequestCreator requestCreator = picasso.load(photo_link);
        requestCreator.into(photo);
        photo.setScaleType(ImageView.ScaleType.FIT_XY);
        photo.getLayoutParams().height = (int) Float.parseFloat(photo_height);
        photo.requestLayout();
        TextView t1 = findViewById(R.id.imgb2_t);
        TextView t2 = findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
        showdata = findViewById(R.id.ure);
        dodata = findViewById(R.id.re);
        back = findViewById(R.id.back);
        promo = findViewById(R.id.promo);
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
    public boolean onTouchEvent(MotionEvent event) {
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

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        LikeFunc();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        if (isSeen) {
            isSeen = false;
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(dodata, "translationY", 1000f);
            animation1.setDuration(250);
            animation1.start();
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(showdata, "translationX", 200f);
            animation2.setDuration(250);
            animation2.start();
            back.setBackgroundResource(R.drawable.activity_special);
            promo.setVisibility(View.GONE);
        } else {
            isSeen = true;
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(dodata, "translationY", 0f);
            animation1.setDuration(250);
            animation1.start();
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(showdata, "translationX", 0f);
            animation2.setDuration(250);
            animation2.start();
            promo.setVisibility(View.VISIBLE);
            back.setBackgroundResource(R.drawable.activity_black);
        }
        return false;
    }
    void LikeFunc() {
        Intent intent = new Intent(this, LikeActivity.class);
        startActivity(intent);
        if (!Objects.equals(order, "empty")) {
            String[] basename = photo_link.split("/");
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
         pref(indata,basename,basename);
            Increment();

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