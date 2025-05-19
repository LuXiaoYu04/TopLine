package com.itheima.topline.bean;

import android.graphics.Color;
import android.widget.Button;

public class BigSizeBean {
    private Button button;  //选择画笔的大小所在的按钮
    private int tag;         //画笔大小所在的按钮的Id
    private int name;       //画笔的大小
    public Button getButton() {
        return button;
    }
    public void setButton(Button button) {
        this.button = button;
    }
    public int getTag() {
        return tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
        int i = Color.RED;
    }
}
