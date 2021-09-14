package com.daasuu.camerarecorder.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.*;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.camerarecorder.CommentActivity;
import com.daasuu.camerarecorder.LikeActivity;
import com.daasuu.camerarecorder.LikeActivity2;
import com.daasuu.camerarecorder.MultiPhotoActivity;
import com.daasuu.camerarecorder.PZoomActivity;
import com.daasuu.camerarecorder.VideoHolder;
import com.daasuu.camerarecorder.ZoomMultiActivity;
import com.daasuu.camerarecorder.mode.Feed;
import com.daasuu.camerarecorder.mode.Photo;
import com.daasuu.camerarecorder.mode.Video;
import com.daasuu.camerarecorder.ui.activity.profileActivity;
import com.daasuu.camerarecorder.ui.view.CameraAnimation;
import com.daasuu.camerarecorder.ui.view.VideoView;
import com.kalasa.library.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by HoangAnhTuan on 1/21/2018.
 */
public class PhotoFeedAdapter extends BaseAdapter<Feed> {
    private static final int PHOTO_M1 = 0;
    private static final int PHOTO_M2 = 1;
    private static final int VIDEO_M1 = 2;
    private static String AES = "AES";
    private static final int VIDEO_M2 = 3;
    private static int screenWight = 0;
    public PhotoFeedAdapter(Activity activity) {
        super(activity);
        screenWight = getScreenWight();
    }
    @Override
    public int getItemViewType(int position) {
        Feed feed = list.get(position);
        if (feed.getInfo() instanceof Photo) {
            if (feed.getModel() == Feed.Model.M1) return PHOTO_M1;
            return PHOTO_M2;
        } else {
            if (feed.getModel() == Feed.Model.M1) return VIDEO_M1;
            return VIDEO_M2;
        }
    }

    private static SecretKeySpec generateKeySpec(String password) throws Exception{
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] keySpec = messageDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySpec, "AES");
        return secretKeySpec;
    }
    private static String encryption(String input, String password) throws Exception {
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptValue = cipher.doFinal(input.getBytes());
        String encryptedValue = Base64.encodeToString(encryptValue, Base64.DEFAULT);
        return encryptedValue;
    }
    private static String decryption(String output, String password) throws Exception{
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.decode(output, Base64.DEFAULT);
        byte[] decriptValue = cipher.doFinal(decodedValue);
        String decriptedValue = new String(decriptValue);
        return decriptedValue;
    }
    private void writeInFile(String text) {

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PHOTO_M1:
                view = activity.getLayoutInflater().inflate(R.layout.item_photo, parent, false);
                return new Photo11Holder(view);
            case PHOTO_M2:
                view = activity.getLayoutInflater().inflate(R.layout.item_photo, parent, false);
                return new Photo169Holder(view);
            case VIDEO_M1:
                view = activity.getLayoutInflater().inflate(R.layout.item_video, parent, false);
                return new Video11Holder(view);
            default:
                view = activity.getLayoutInflater().inflate(R.layout.item_video, parent, false);
                return new Video169Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Feed feed = list.get(position);
        if (holder instanceof Video11Holder) {
            onBindVideo11Holder((Video11Holder) holder, feed);
        } else if (holder instanceof Video169Holder) {
            onBindVideo169Holder((Video169Holder) holder, feed);
        } else if (holder instanceof Photo11Holder) {
            onBindPhoto11Holder((Photo11Holder) holder, feed);
        } else if (holder instanceof Photo169Holder) {
            onBindPhoto169Holder((Photo169Holder) holder, feed);
        }
    }
    private void onBindPhoto11Holder(Photo11Holder holder, Feed feed) {
        String link=feed.getInfo().getUrlPhoto();
        if(link.contains(":-:"))
        {
            String[] arr=link.split(":-:");
            String y = arr[0].substring(0, arr[0].length() - 3);
            link=y;
            Picasso.with(activity)
                    .load(link)
                    .resize(screenWight, screenWight)
                    .centerCrop()
                    .into(holder.ivInfo);
        }
        else{
            Picasso.with(activity)
                    .load(link)
                    .resize(screenWight, screenWight)
                    .centerCrop()
                    .into(holder.ivInfo);
        }
        holder.ivthiscomment.setText(feed.getmycomment());
        holder.ivdate.setText(feed.getpdate());
        holder.ivlike.setText(feed.getlike());
    }

    private void onBindPhoto169Holder(Photo169Holder holder, Feed feed) {
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight * 9 / 16)
                .centerCrop()
                .into(holder.ivInfo);
        holder.ivthiscomment.setText(feed.getmycomment());
        holder.ivdate.setText(feed.getpdate());
        holder.ivlike.setText(feed.getlike());
    }

    private void onBindVideo11Holder(final DemoVideoHolder holder, Feed feed) {
        holder.vvInfo.setVideo((Video) feed.getInfo());
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight)
                .centerCrop()
                .into(holder.ivInfo);
        holder.ivthiscomment.setText(feed.getmycomment());
        holder.ivdate.setText(feed.getpdate());
        holder.ivlike.setText(feed.getlike());
    }

    private void onBindVideo169Holder(final DemoVideoHolder holder, Feed feed) {
        holder.vvInfo.setVideo((Video) feed.getInfo());
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                //.resize(screenWight, screenWight * 9 / 16)
                .resize(screenWight, screenWight)
                .centerCrop()
                .into(holder.ivInfo);
        holder.ivthiscomment.setText(feed.getmycomment());
        holder.ivdate.setText(feed.getpdate());
        holder.ivlike.setText(feed.getlike());
    }

    private int getScreenWight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView ivInfo;
        TextView ivthiscomment;
        TextView ivdate;
        TextView ivlike;
        ImageView ivopt1;
        ImageView ivopt2;
        boolean isliked;
        public PhotoHolder(View itemView) {
            super(itemView);
            ivInfo=itemView.findViewById(R.id.ivInfo);
            ivthiscomment=itemView.findViewById(R.id.ivthiscomment);
            ivopt1=itemView.findViewById(R.id.ivopt1);
            ivopt2=itemView.findViewById(R.id.ivopt2);
            ivdate=itemView.findViewById(R.id.ivdate);
            ivlike=itemView.findViewById(R.id.txttxt1);
            isliked=false;
            ivInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] a = profileActivity.cids.split("::");
                    String[] b = a[3].split("<-:>");
                    String[] id1 = b[getAdapterPosition()].split("--");
                    if (id1[0].contains(":-:")) {
                        String[] arr = id1[0].split(":-:");
                        String y = arr[0].substring(0, arr[0].length() - 3);
                        Intent openProgramActivity=new Intent(v.getContext(), MultiPhotoActivity.class);
                        openProgramActivity.putExtra("link", arr[1]);
                        openProgramActivity.putExtra("likes", 190);
                        openProgramActivity.putExtra("views", 230);
                        openProgramActivity.putExtra("order", String.valueOf(getAdapterPosition()));
                        v.getContext().startActivity(openProgramActivity, ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.activity_slider_in_right, R.anim.activity_slide_in_left).toBundle());
                    } else {
                        String id = id1[0].substring(0, id1[0].length() - 3);
                        Intent openProgramActivity = new Intent(v.getContext(), PZoomActivity.class);
                        openProgramActivity.putExtra("link", "https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + id);
                        openProgramActivity.putExtra("likes", "190");
                        openProgramActivity.putExtra("views", "230");
                        openProgramActivity.putExtra("height", "720");
                        v.getContext().startActivity(openProgramActivity, ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.activity_slider_in_right, R.anim.activity_slide_in_left).toBundle());
                    }
                }
            });
            ivopt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openProgramActivity = new Intent(v.getContext(), LikeActivity2.class);
                    v.getContext().startActivity(openProgramActivity);
                    String[] a = profileActivity.cids.split("::");
                    String[] b=a[3].split("<-:>");
                    String[] id1=b[getAdapterPosition()].split("--");
                    String file_name=id1[0].substring(0,id1[0].length()-7);
                    Toast.makeText(v.getContext(), "file_name::"+file_name, Toast.LENGTH_SHORT).show();
                    String textfile="/storage/emulated/0/wsff.gaff";
                    String xx="";
                    File check_pref=new File(textfile);
                    if(check_pref.exists()) {
                        InputStream inputStream;
                        StringBuilder text = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(check_pref));
                            String line;
                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();
                            xx = text.toString();
                            if (!Objects.equals(xx, "null")) {
                                String xx2 = null;
                                try {
                                    xx2 = decryption(xx, "open");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String[] arr = xx2.split("//");
                                if (Arrays.asList(arr).contains(file_name)) {
                                    if (ivopt1.getDrawable().getConstantState() == v.getContext().getDrawable( R.drawable.ufi_heart).getConstantState()) {
                                        ivopt1.setImageResource(R.drawable.ic_theart);
                                    }
                                    else if (ivopt1.getDrawable().getConstantState() == v.getContext().getDrawable( R.drawable.ic_heart).getConstantState()) {
                                        ivopt1.setImageResource(R.drawable.ufi_heart);
                                    }
                                } else {
                                    pref(getAdapterPosition(), v, ivlike, isliked, ivopt1, file_name, b[getAdapterPosition()]);
                                }
                            } else {
                                pref(getAdapterPosition(), v, ivlike, isliked, ivopt1, file_name, b[getAdapterPosition()]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            pref(getAdapterPosition(),v,ivlike,isliked,ivopt1,file_name,b[getAdapterPosition()]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            ivopt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] a = profileActivity.cids.split("::");
                    String[] b=a[3].split("<-:>");
                    String[] id1=b[getAdapterPosition()].split("--");
                    String id=id1[0].substring(0,id1[0].length()-7);
                    Intent openProgramActivity = new Intent(v.getContext(), CommentActivity.class);
                    openProgramActivity.putExtra("CId",id);
                    openProgramActivity.putExtra("CUserName",profileActivity.cusernames);
                    openProgramActivity.putExtra("CPassword",profileActivity.cpasswords);
                    v.getContext().startActivity(openProgramActivity);
                }
            });
        }

    }
    public static class Photo11Holder extends PhotoHolder {
        public Photo11Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight;
            ivInfo.setLayoutParams(layoutParams);
        }
    }
    static void pref(int index,View v,TextView ivlike,boolean isliked,ImageView ivopt1,String file_name,String out_t) throws Exception {
                String out_text="empty";
                String out_etext="empty";
                String password=file_name.substring(0,5);
                out_text=file_name;
                try {
                    out_etext=encryption(out_t,password);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                OutputStream outputStream;
                String textfile="/storage/emulated/0/Android/data/com.datas.writes/" +file_name+".cache";
                try {
                    String a1="";
                    String textfile2="/storage/emulated/0/wsff.gaff";
                    outputStream = v.getContext().getContentResolver().openOutputStream(Uri.fromFile(new File(textfile)));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bw.write(out_etext);
                    String bwx1="";
                    bw.flush();
                    bw.close();
                    InputStream inputStream;
                    StringBuilder text2 = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(textfile2));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text2.append(line);
                            text2.append('\n');
                        }
                        br.close();
                        bwx1=text2.toString();
                        String bwx2=decryption(bwx1,"open");
                        String tenc=bwx2+out_text+"//";
                        a1=encryption(tenc,"open");
                        outputStream = v.getContext().getContentResolver().openOutputStream(Uri.fromFile(new File(textfile2)));
                        BufferedWriter w2 = new BufferedWriter(new OutputStreamWriter(outputStream));
                        w2.write(a1);
                        w2.flush();
                        w2.close();
                    } catch (Exception e) {
                        String tenc=out_text+"//";
                        a1=encryption(out_text,"open");
                        outputStream = v.getContext().getContentResolver().openOutputStream(Uri.fromFile(new File(textfile2)));
                        BufferedWriter w2 = new BufferedWriter(new OutputStreamWriter(outputStream));
                        w2.write(a1);
                        w2.flush();
                        w2.close();
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(isliked)
                {
                    String x1= (String)ivlike.getText();
                    int x2=Integer.parseInt(x1)-1;
                    String x12=String.valueOf(x2);
                    ivlike.setText(x12);
                    ivopt1.setImageResource(R.drawable.ufi_heart);
                    isliked=false;
                }
                else {
                    String x1= (String)ivlike.getText();
                    int x2=Integer.parseInt(x1)+1;
                    String x12=String.valueOf(x2);
                    ivlike.setText(x12);
                    ivopt1.setImageResource(R.drawable.ic_heart);
                    isliked=true;
                }
            }
    public static class Photo169Holder extends PhotoHolder {


        public Photo169Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight * 9 / 16;
            ivInfo.setLayoutParams(layoutParams);
        }
    }
    public static class DemoVideoHolder extends VideoHolder {
        ImageButton btn_playing;
        VideoView vvInfo;
        ImageView ivInfo;
        Button ivliked;
        Button ivcomment;
        TextView ivthiscomment;
        TextView ivdate;
        TextView ivlike;
        Button ivvolume;
        TextView comment_id;
        ImageView icon_playing;
        ImageView icon_volume;
        ImageView ivopt1;
        ImageView ivopt2;
        CameraAnimation ivCameraAnimation;
        boolean cond=true;
        boolean volume_status=true;
        boolean isliked=false;
        public DemoVideoHolder(View itemView) {
            super(itemView);
            btn_playing=itemView.findViewById(R.id.btn_playing);
            icon_playing=itemView.findViewById(R.id.icon_playing);
            vvInfo=itemView.findViewById(R.id.vvInfo);
            ivInfo=itemView.findViewById(R.id.ivInfo);
            ivthiscomment=itemView.findViewById(R.id.ivthiscomment);
            ivdate=itemView.findViewById(R.id.ivdate);
            ivlike=itemView.findViewById(R.id.txttxt1);
            icon_volume=itemView.findViewById(R.id.ghjf1);
            ivCameraAnimation=itemView.findViewById(R.id.ivCameraAnimation);
            ivliked=itemView.findViewById(R.id.btn_liked);
            comment_id=itemView.findViewById(R.id.comment_id);
            ivopt1=itemView.findViewById(R.id.ivopt1);
            ivopt2=itemView.findViewById(R.id.ivopt2);
            ivcomment=itemView.findViewById(R.id.btn_comment);
            ivvolume=itemView.findViewById(R.id.btn_volume);
            ivvolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(volume_status==true)
                    {
                        vvInfo.SetVolumeOFF();
                        icon_volume.setImageResource(R.drawable.volumeofff);
                        volume_status=false;
                    }
                    else
                    {
                        vvInfo.SetVolumeON();
                        icon_volume.setImageResource(R.drawable.volumeon);
                        volume_status=true;
                    }
                }
            });
            ivInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ivliked.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent openProgramActivity = new Intent(v.getContext(), LikeActivity2.class);
                                                   v.getContext().startActivity(openProgramActivity);
                                                   String[] a = profileActivity.cids.split("::");
                                                   String[] b=a[3].split("<-:>");
                                                   String[] id1=b[getAdapterPosition()].split("--");
                                                   String file_name=id1[0].substring(0,id1[0].length()-7);
                                                   Toast.makeText(v.getContext(), "file_vname"+file_name, Toast.LENGTH_SHORT).show();
                                                   String textfile="/storage/emulated/0/wsff.gaff";
                                                   String xx="";
                                                   File check_pref=new File(textfile);
                                                   if(check_pref.exists()) {
                                                       InputStream inputStream;
                                                       StringBuilder text = new StringBuilder();
                                                       try {
                                                           BufferedReader br = new BufferedReader(new FileReader(check_pref));
                                                           String line;
                                                           while ((line = br.readLine()) != null) {
                                                               text.append(line);
                                                               text.append('\n');
                                                           }
                                                           br.close();
                                                           xx = text.toString();
                                                           if (!Objects.equals(xx, "null")) {
                                                               String xx2 = null;
                                                               try {
                                                                   xx2 = decryption(xx, "open");
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }
                                                               String[] arr = xx2.split("//");
                                                               if (Arrays.asList(arr).contains(file_name)) {
                                                                   if (ivopt1.getDrawable().getConstantState() == v.getContext().getDrawable( R.drawable.ufi_heart).getConstantState()) {
                                                                       ivopt1.setImageResource(R.drawable.ic_theart);
                                                                   }
                                                                   else if (ivopt1.getDrawable().getConstantState() == v.getContext().getDrawable( R.drawable.ic_heart).getConstantState()) {
                                                                       ivopt1.setImageResource(R.drawable.ufi_heart);
                                                                   }
                                                               } else {
                                                                   pref(getAdapterPosition(), v, ivlike, isliked, ivopt1, file_name, b[getAdapterPosition()]);
                                                               }
                                                           } else {
                                                               pref(getAdapterPosition(), v, ivlike, isliked, ivopt1, file_name, b[getAdapterPosition()]);
                                                           }
                                                       } catch (Exception e) {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                                   else {
                                                       try {
                                                           pref(getAdapterPosition(),v,ivlike,isliked,ivopt1,file_name,b[getAdapterPosition()]);
                                                       } catch (Exception e) {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                               }
            });
            ivcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] a = profileActivity.cids.split("::");
                    String[] b=a[3].split("<-:>");
                    String[] id1=b[getAdapterPosition()].split("--");
                    String id=id1[0].substring(0,id1[0].length()-7);
                    Intent openProgramActivity = new Intent(v.getContext(), CommentActivity.class);
                    openProgramActivity.putExtra("CId",id);
                    openProgramActivity.putExtra("CUserName",profileActivity.cusernames);
                    openProgramActivity.putExtra("CPassword",profileActivity.cpasswords);
                    v.getContext().startActivity(openProgramActivity);
                }
            });
            btn_playing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cond==true) {
                        vvInfo.stop();
                        icon_playing.setImageResource(R.drawable.pause);
                        //icon_playing.setImageResource(R.drawable.avd_play_to_pause);
                        cond=false;
                    }
                    else
                    {
                        playVideo();
                        icon_playing.setImageResource(R.drawable.play);
                        //icon_playing.setImageResource(R.drawable.avd_pause_to_play);
                        cond=true;
                    }
                }
            });
        }
        @Override
        public View getVideoLayout() {
            return vvInfo;
        }
        public void openProgramActivity(View view, int position) {
            Intent openProgramActivity = new Intent(view.getContext(), LikeActivity.class);
            openProgramActivity.putExtra("index",position);
            view.getContext().startActivity(openProgramActivity);
        }
        @Override
        public void playVideo() {
            ivInfo.setVisibility(View.VISIBLE);
            icon_playing.setVisibility(View.GONE);
            ivCameraAnimation.start();
            vvInfo.play(new VideoView.OnPreparedListener() {
                @Override
                public void onPrepared() {
                    ivInfo.setVisibility(View.GONE);
                    ivCameraAnimation.stop();
                }
            });
        }

        @Override
        public void stopVideo() {
            ivInfo.setVisibility(View.VISIBLE);
            icon_playing.setVisibility(View.VISIBLE);
            ivCameraAnimation.stop();
            vvInfo.stop();
        }
    }
    public static class Video11Holder extends DemoVideoHolder {
        public Video11Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vvInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight;
            vvInfo.setLayoutParams(layoutParams);
        }
    }
    public static class Video169Holder extends DemoVideoHolder {

        public Video169Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vvInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight * 9 / 16;
            vvInfo.setLayoutParams(layoutParams);
        }
    }

}
