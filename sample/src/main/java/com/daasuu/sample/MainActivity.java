package com.daasuu.sample;

import android.app.ActivityOptions;
import android.content.Intent;
import com.daasuu.camerarecorder.BaseCameraActivity;
import com.daasuu.camerarecorder.CollageActivity;
import com.daasuu.camerarecorder.CommentActivity;
import com.daasuu.camerarecorder.ExtraOptionsActivity;
import com.daasuu.camerarecorder.MainFrameActivity;
import com.daasuu.camerarecorder.MessageActivity;
import com.daasuu.camerarecorder.OpenOptions;
import com.daasuu.camerarecorder.PPicCrop;
import com.daasuu.camerarecorder.ProfileCropper;
import com.daasuu.camerarecorder.SearchGiffActivity;
import com.daasuu.camerarecorder.ShowUpdateActivity;
import com.daasuu.camerarecorder.StoryVideoActivity;
import com.daasuu.camerarecorder.UpdateProfileActivity;
import com.daasuu.camerarecorder.ZoomActivity;
import com.daasuu.camerarecorder.ZoomVideoActivity;
import com.daasuu.camerarecorder.friendListMenu;
import com.daasuu.camerarecorder.ui.activity.VideoRecyclerActivity;
import com.daasuu.camerarecorder.ui.activity.profileActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 88888;
    private static final int ADD_MAIN_REQUEST_CODE = 4444;
    private static String AES = "AES";
    String textfile="";
    String stri="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cr();
            }
        }, 400);
        }
    private void writeInFile(String text) {
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
        void cr()
        {
            Intent intent = new Intent(this, profileActivity.class);
            intent.putExtra("Collarge","610201e591fd40612eebda8d::divyom sharma::12-7-1996<>near karatkot,Uttar Pradesh<>pursuing greatness in everything<>Burger King<>4::63AD5483-02.png1.0:-:1B1FF04D-C6=0.9-:-526E9452-04=1.3--Ji--610201e591fd40612eebda8dWed Aug 25 202110:14:07 AM UTC--0--10<-:>repFF862C83.mp40.5--Best class \uD83E\uDD23\uD83D\uDE02--610201e591fd40612eebda8dThu Jul 29 20214:38:26 AM UTC--1--2<-:>022BC906-07.png1.3--Rachel radhe--610201e591fd40612eebda8dThu Jul 29 20211:20:31 AM UTC--1--1:mystory:@@.mp4*3=:=3=:=3=:=610201e591fd40612eebda8dWed Sep 01 20211:06:15 PM UTC=:=0=:=610201e591fd40612eebda8d");
            intent.putExtra("Password","@#aaaaaaa#@");
            intent.putExtra("CoUserName","Divyom Singh");
        startActivityForResult( intent, ADD_MAIN_REQUEST_CODE , ActivityOptions.makeCustomAnimation(this,R.anim.activity_slider_in_right,R.anim.activity_slide_in_left).toBundle());
        }
        /*
60a6858e62d57c0dc1db4542::
infos::
1EC37E238F8.mp40.5--God bless farmers of India --60a6858e62d57c0dc1db4542Sun Jan 17 202110:58:48 AM UTC--193--60a6858e62d57c0dc1db4542--15::
D3F5628.png0.5--Yaar jigri kasuuti zindagi--60a6858e62d57c0dc1db4542Sat Jan 16 20218:56:27 AM UTC--439--60a6858e62d57c0dc1db4542--9
*/@Override
    public void onBackPressed() {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MAIN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("ChoosedText")) {
            }
        }
    }
}