package com.itheima.topline.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.itheima.topline.R;
import com.itheima.topline.bean.BigSizeBean;
import com.itheima.topline.bean.ColorsBean;
import com.itheima.topline.view.MyHorizontalScrollView;
import com.itheima.topline.view.ScrawView;

import java.util.ArrayList;
import java.util.List;

public class ScrawActivity extends AppCompatActivity implements View.OnClickListener,
        MyHorizontalScrollView.OnScrollListener {
    private ImageView imageview_background;
    private ScrawView tuyaView;
    FrameLayout tuyaFrameLayout =null;
    private static final int UNDO_PATH = 1;
    private static final int REDO_PATH = 2;
    private static final int USE_ERASER = 3;
    private static final int USE_PAINT = 4;
    private LinearLayout linearlayout;
    private Button colortag;
    private Button bigtag;
    private ScrollView scrollviewcolor;
    private ScrollView scrollviewbig;
    private MyHorizontalScrollView hscrollViewcolor;
    private MyHorizontalScrollView hscrollViewsize;
    private List<ColorsBean> colors = new ArrayList();
    private ColorsBean color;
    private List<BigSizeBean> sizes = new ArrayList();
    private BigSizeBean size;
    private int index;
    private int CANCLE_BACKGROUND_IMAGE = 0;
    private final int defaultColor= Color.parseColor("#C9DDFE");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraw);
        initView();
    }
    private void initView(){
        tuyaFrameLayout =(FrameLayout) findViewById(R.id.tuya_layout);
        imageview_background = (ImageView) findViewById(R.id.imageview_background);
        tuyaView=(ScrawView)findViewById(R.id.tuyaView);
        colortag = (Button)this.findViewById(R.id.colortag);
        bigtag = (Button)this.findViewById(R.id.bigtag);
        scrollviewcolor = (ScrollView)this.findViewById(R.id.scrollviewcolor);
        scrollviewbig = (ScrollView)this.findViewById(R.id.scrollviewbig);
        hscrollViewcolor = (MyHorizontalScrollView)this.findViewById(R.id.
                HorizontalScrollView01);
        hscrollViewcolor.setOnScrollListener(this);
        hscrollViewsize = (MyHorizontalScrollView)this.findViewById(R.id.
                HorizontalScrollView02);
        hscrollViewsize.setOnScrollListener(this);
        linearlayout = (LinearLayout)this.findViewById(R.id.ScrollView01);
        initColorButton();
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNDO_PATH:
                    int undo = tuyaView.undo(); //撤销上次操作
                    System.out.println("可以撤销："+undo);
                    if(undo<0){
                        CANCLE_BACKGROUND_IMAGE ++;
                        switch (CANCLE_BACKGROUND_IMAGE) {
                            case 0:
                                break;
                            case 1:
                                System.out.println("设置imageview为默认");
                                imageview_background.setBackgroundColor(defaultColor);
                                imageview_background.setImageBitmap(null);
                                CANCLE_BACKGROUND_IMAGE =0;
                                break;
                        }
                    }
                    break;
                case REDO_PATH:
                    int redo = tuyaView.redo(); //返回上次操作
                    System.out.println("可以前进："+ redo);
                    if(redo<1){
                        //设置按钮不可用
                    }
                    break;
                case USE_ERASER:
                    if(linearlayout.getVisibility()==View.VISIBLE){
                        linearlayout.setVisibility(View.GONE);
                    }
                    ScrawView.color= Color.parseColor("#C9DDFE");
                    ScrawView.srokeWidth = 15;
                    break;
                case USE_PAINT:
                    if(linearlayout.getVisibility()==View.GONE){
                        linearlayout.setVisibility(View.VISIBLE);
                        ScrawView.srokeWidth = sizes.get(index).getName()+10;
                        for(int i=0;i<colors.size();i++){
                            if(colors.get(i).getButtonbg().getVisibility()==View.VISIBLE){
                                ScrawView.color = Color.parseColor("#"+colors.get(i).
                                        getName());
                                break;
                            }
                        }
                    }else{
                        linearlayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.button_undo) {
            // 撤销
            Message undo_message = new Message();
            undo_message.what = UNDO_PATH;
            handler.sendMessage(undo_message);
        } else if (viewId == R.id.button_eraser) {
            // 橡皮擦
            Message eraser_message = new Message();
            eraser_message.what = USE_ERASER;
            handler.sendMessage(eraser_message);
        } else if (viewId == R.id.button_pen) {
            // 画笔
            Message pen_message = new Message();
            pen_message.what = USE_PAINT;
            handler.sendMessage(pen_message);
        } else if (viewId == R.id.colortag) {
            // 画笔颜色
            if (linearlayout.getVisibility() == View.VISIBLE) {
                scrollviewcolor.setVisibility(View.VISIBLE);
                scrollviewbig.setVisibility(View.GONE);
                colortag.setBackgroundResource(R.drawable.tuya_selectedtrue);
                bigtag.setBackgroundResource(R.drawable.tuya_selectedfalse);
            }
        } else if (viewId == R.id.bigtag) {
            // 画笔粗细
            if (linearlayout.getVisibility() == View.VISIBLE) {
                scrollviewcolor.setVisibility(View.GONE);
                scrollviewbig.setVisibility(View.VISIBLE);
                bigtag.setBackgroundResource(R.drawable.tuya_selectedtrue);
                colortag.setBackgroundResource(R.drawable.tuya_selectedfalse);
            }
        } else if (viewId == R.id.btn_clear) {
            // 清除画布
            tuyaView.clear();
        } else if (viewId == R.id.btn_save) {
            // 保存
            try {
                tuyaView.saveBitmap(ScrawActivity.this); // 保存涂鸦信息
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //颜色
        int j = -1;
        for(int i=0;i<colors.size();i++){
            if(v.getId()==colors.get(i).getTag()){
                j=i;
            }
        }
        if (j != -1) {
            for (int i = 0; i < colors.size(); i++) {
                colors.get(i).getButtonbg().setVisibility(View.INVISIBLE);
            }
            colors.get(j).getButtonbg().setVisibility(View.VISIBLE);
            //改变颜色
            colors.get(j).getName();
            ScrawView.color = Color.parseColor("#"+colors.get(j).getName());
            return;
        }
        //大小
        for(int i=0;i<sizes.size();i++){
            if(v.getId()==sizes.get(i).getTag()){
                j=i;
            }
        }
        if (j != -1) {
            for (int i = 0; i < sizes.size(); i++) {
                sizes.get(i).getButton().setBackgroundResource(0);
            }
            sizes.get(j).getButton().setBackgroundResource(R.drawable.
                    tuya_brushsizeselectedbg);
            //改变大小
            sizes.get(j).getName();
            ScrawView.srokeWidth=sizes.get(j).getName()+10;
            index = j;
            return;
        }
    }
    @Override
    public void onRight() {
    }
    @Override
    public void onLeft() {
    }
    @Override
    public void onScroll() {
    }
    private void initColorButton() {
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button01));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview01));
        color.setName("140c09");
        color.setTag(R.id.button01);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button02));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview02));
        color.setName("fe0000");
        color.setTag(R.id.button02);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button03));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview03));
        color.setName("ff00ea");
        color.setTag(R.id.button03);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button04));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview04));
        color.setName("011eff");
        color.setTag(R.id.button04);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button05));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview05));
        color.setName("00ccff");
        color.setTag(R.id.button05);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button06));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview06));
        color.setName("00641c");
        color.setTag(R.id.button06);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button07));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview07));
        color.setName("9bff69");
        color.setTag(R.id.button07);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button08));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview08));
        color.setName("f0ff00");
        color.setTag(R.id.button08);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button09));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview09));
        color.setName("ff9c00");
        color.setTag(R.id.button09);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button10));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview10));
        color.setName("ff5090");
        color.setTag(R.id.button10);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button11));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview11));
        color.setName("9e9e9e");
        color.setTag(R.id.button11);
        colors.add(color);
        color = new ColorsBean();
        color.setButton((Button)this.findViewById(R.id.button12));
        color.setButtonbg((ImageView)this.findViewById(R.id.imageview12));
        color.setName("f5f5f5");
        color.setTag(R.id.button12);
        colors.add(color);
        for(int i=0;i<colors.size();i++){
            colors.get(i).getButton().setOnClickListener(this);
            colors.get(i).getButtonbg().setVisibility(View.INVISIBLE);
        }
        colors.get(1).getButtonbg().setVisibility(View.VISIBLE);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton01));
        size.setName(15);
        size.setTag(R.id.sizebutton01);
        sizes.add(size);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton02));
        size.setName(10);
        size.setTag(R.id.sizebutton02);
        sizes.add(size);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton03));
        size.setName(5);
        size.setTag(R.id.sizebutton03);
        sizes.add(size);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton04));
        size.setName(0);
        size.setTag(R.id.sizebutton04);
        sizes.add(size);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton05));
        size.setName(-5);
        size.setTag(R.id.sizebutton05);
        sizes.add(size);
        size = new BigSizeBean();
        size.setButton((Button)this.findViewById(R.id.sizebutton06));
        size.setName(-10);
        size.setTag(R.id.sizebutton06);
        sizes.add(size);
        for(int i=0;i<sizes.size();i++){
            sizes.get(i).getButton().setOnClickListener(this);
            sizes.get(i).getButton().setBackgroundResource(0);
        }
        sizes.get(2).getButton().setBackgroundResource(R.drawable.
                tuya_brushsizeselectedbg);
        index = 2;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScrawView.color = Color.parseColor("#fe0000");
        ScrawView.srokeWidth = 15;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(linearlayout.getVisibility()==View.VISIBLE){
                linearlayout.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
