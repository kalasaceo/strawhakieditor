package com.daasuu.camerarecorder;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.transition.Slide;

import com.kalasa.library.R;
import java.util.ArrayList;
import java.util.Objects;
public class OpenOptions extends AppCompatActivity {
    private String output="empty";
    private int usize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_openoptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            LinearLayout psrc=findViewById(R.id.p_src);
            Intent data=this.getIntent();
            String x=data.getStringExtra("idata");
            String[] a=x.split("::");
            String[] name=a[1].split("--");
            String[] rdata=a[0].split("--");
            usize=rdata.length;
            ArrayList<String> ar=new ArrayList<>();
            ArrayList<String> r=new ArrayList<>();
            for(int z=0;z<usize;z++)
            {
                r.add(rdata[z]);
            }
            ar.add("Try out the all New Toaster Sticko Store and send your Friends as Gifts.");
            ar.add("Kindly update your location to find more people  of your interest.");
            for(int j=0;j<name.length;j++)
            {
                ar.add("You have a new message from"+"<br></br>"+"<large><font color='blue'>"+name[j]+"</font></<large>");
            }
            ar.add("Check new messages in your \ninbox");
            for(int i=0;i<usize;i++)
            {
                setter(psrc,i,ar.get(i),r.get(i));
            }
        }
    }
     private int getusize()
    {
        return usize-1;
    }
    void setter(LinearLayout psrc,int i,String stxt,String isReaded)
    {
        CardView cd = new CardView(this);
        cd.setRadius(40);
        LinearLayout.LayoutParams c = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cd.setLayoutParams(c);
        cd.getLayoutParams().height= 224;
        c.setMargins(6, 6, 6, 0);
        cd.setLayoutParams(c);
        if(Objects.equals(isReaded, "0"))
        {
            cd.setCardBackgroundColor(Color.parseColor("#e1f5fe"));
        }
        else
        {
            cd.setCardBackgroundColor(Color.parseColor("#eceff1"));
        }
        cd.requestLayout();
        TextView tx=new TextView(this);
        LinearLayout.LayoutParams tc = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tc.setMargins(240, 49, 10, 10);
        tx.setLayoutParams(tc);
        tx.setTextColor(Color.BLACK);
        tx.setTextSize(17);
        cd.addView(tx);
        ImageView img=new ImageView(this);
        switch(i){
            case 0:
                img.setImageResource(R.drawable.mess1);
                tx.setText(stxt);
                break;
            case 1:
                img.setImageResource(R.drawable.mess2);
                tx.setText(stxt);
                break;
            default:
                if(i==usize-1)
                {
                    img.setImageResource(R.drawable.mess4);
                    tx.setText(stxt);
                }
                else
                {
                    img.setImageResource(R.drawable.mess3);
                    tx.setText( Html.fromHtml(stxt));
                }
                break;
        }
        cd.setClickable(true);
        //cd.setLongClickable(true);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this_setter(cd,i);
            }
        });
        /*cd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });*/
        LinearLayout.LayoutParams ic = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ic.setMargins(20, 25, 45, 25);
        img.setLayoutParams(ic);
        cd.setForeground(getSelectedItemDrawable());
        cd.addView(img);
        cd.setCardElevation(40);
        cd.setMaxCardElevation(60);
        psrc.addView(cd);
    }
    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }
    void this_setter(CardView cd,int i)
    {
        cd.setCardBackgroundColor(Color.parseColor("#eceff1"));
        output=String.valueOf(i);
        finish();
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
        data.putExtra("create", output);
        this.setResult(RESULT_OK, data);
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left,R.anim.activity_slider_out_left);
    }
}