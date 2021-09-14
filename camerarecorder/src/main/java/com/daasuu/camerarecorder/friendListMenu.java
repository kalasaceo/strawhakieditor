package com.daasuu.camerarecorder;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.kalasa.library.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class friendListMenu extends AppCompatActivity {
    private String output="empty";
    private int usize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_flmenu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            LinearLayout psrc=findViewById(R.id.p_src);
            Intent data=this.getIntent();
            String x=data.getStringExtra("idata");
            String[] a=x.split("::");
            String[] name=a[1].split("--");
            String[] rdata=a[0].split("--");
            String[] rid=a[2].split("--");
            usize=rdata.length;
            ArrayList<String> ar=new ArrayList<>();
            ArrayList<String> r=new ArrayList<>();
            for(int z=0;z<usize;z++)
            {
                r.add(rdata[z]);
            }
            for(int j=0;j<name.length;j++)
            {
                ar.add(name[j]);
            }
            for(int i=0;i<usize;i++)
            {
                setter(psrc,i,ar.get(i),r.get(i),rid[i]);
            }
        }
    }
    void setter(LinearLayout psrc,int i,String name,String status,String this_id)
    {
        CardView cd = new CardView(this);
        cd.setRadius(40);
        LinearLayout.LayoutParams c = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cd.setLayoutParams(c);
        cd.getLayoutParams().height= 224;
        c.setMargins(6, 6, 6, 0);
        cd.setLayoutParams(c);
        cd.setCardBackgroundColor(Color.parseColor("#f5f5f5"));
        cd.requestLayout();
        TextView tx=new TextView(this);
        LinearLayout.LayoutParams tc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tc.setMargins(295, 25, 10, 10);
        tx.setLayoutParams(tc);
        tx.setTextColor(Color.BLACK);
        tx.setTextSize(17);
        cd.addView(tx);
        CircleImageView img=new CircleImageView(this);
        img.setClickable(true);
        String p="/storage/emulated/0/Movies/.required/"+this_id+".png";
        if (new File(p).exists()) {
            img.setImageBitmap(BitmapFactory.decodeFile(p));
            img.setMaxWidth(100);
            img.setMaxHeight(100);
        }
        else {
            img.setImageResource(R.drawable.ppro);
        }
        tx.setText(name);
        cd.setClickable(true);
        //cd.setLongClickable(true);
        /*cd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });*/
        LinearLayout.LayoutParams ic = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ic.setMargins(20, 25, 0, 25);
        ic.width=180;
        ic.height=180;
        img.setLayoutParams(ic);
        img.setForegroundGravity(View.TEXT_ALIGNMENT_TEXT_START);
        GradientDrawable shape1 =  new GradientDrawable();
        shape1.setCornerRadius(16);
        shape1.setColor(Color.parseColor("#e1f5fe"));
        GradientDrawable shape2 =  new GradientDrawable();
        shape2.setCornerRadius(16);
        shape2.setColor(Color.parseColor("#9CCC65"));
        Button btn_a = new Button(this);
        if(Objects.equals(status, "2")) {
            LinearLayout.LayoutParams btn_ac = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btn_ac.setMargins(360, 110, 0, 25);
            btn_a.setLayoutParams(btn_ac);
            btn_a.setAllCaps(false);
            btn_a.setWidth(280);
            btn_a.setText("Decline");
            btn_a.setTextSize(12);
            btn_a.setBackground(shape1);
            cd.addView(btn_a);
        }
        Button btn_d=new Button(this);
        LinearLayout.LayoutParams btn_dc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_dc.setMargins(710, 110, 0, 25);
        btn_d.setLayoutParams(btn_dc);
        btn_d.setTextColor(Color.WHITE);
        switch(status){
            case "0":
                btn_d.setText("Accepted");
                break;
            case "1":
                btn_d.setText("Declined");
                break;
            default:
                btn_d.setText("Accept");
                break;
        }
        btn_d.setTextSize(12);
        btn_d.setWidth(280);
        btn_d.setAllCaps(false);
        btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(status, "2")) {
                    setterA(btn_a, btn_d, i, name);
                }
            }
        });
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(status, "2")) {
                    setterD(btn_a, btn_d, i, name);
                }
            }
        });
        btn_d.setBackground(shape2);
        cd.addView(btn_d);
        cd.setForeground(getSelectedItemDrawable());
        btn_a.setForeground(getSelectedItemDrawable());
        btn_d.setForeground(getSelectedItemDrawable());
        cd.setCardElevation(40);
        cd.setMaxCardElevation(60);
        cd.addView(img);
        psrc.addView(cd);
    }
    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }
    void setterD(Button a,Button d,int i,String name)
    {
        Toast.makeText(this, "Friend Request accepted for "+name.toUpperCase(), Toast.LENGTH_SHORT).show();
        d.setVisibility(View.GONE);
        a.setText("Accepted");
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(16);
        shape.setColor(Color.parseColor("#9CCC65"));
        a.setBackground(shape);
        output=String.valueOf(i+"--1");
    }
    void setterA(Button a,Button d,int i,String name)
    {
        Toast.makeText(this, "Friend Request declined for "+name.toUpperCase(), Toast.LENGTH_SHORT).show();
        d.setVisibility(View.GONE);
        a.setText("Declined");
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(16);
        shape.setColor(Color.parseColor("#e1f5fe"));
        a.setBackground(shape);
        output=String.valueOf(i+"--0");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slide_in_left,R.anim.activity_slider_out_left);
        finish();
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("request", output);
        this.setResult(RESULT_OK, data);
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left,R.anim.activity_slider_out_left);
    }
}