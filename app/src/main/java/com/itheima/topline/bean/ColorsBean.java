package com.itheima.topline.bean;

import android.widget.Button;
import android.widget.ImageView;

public class ColorsBean {
    private Button button;         //颜色所在的按钮
    private ImageView buttonbg;   //颜色所在的按钮的背景
    private int tag;                //按钮的Id
    private String name;           //颜色名称
    public Button getButton() {
        return button;
    }
    public void setButton(Button button) {
        this.button = button;
    }
    public ImageView getButtonbg() {
        return buttonbg;
    }
    public void setButtonbg(ImageView buttonbg) {
        this.buttonbg = buttonbg;
    }
    public int getTag() {
        return tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
