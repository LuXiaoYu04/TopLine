package com.itheima.topline.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.itheima.topline.R;
import com.itheima.topline.utils.MD5Utils;
import com.itheima.topline.utils.UtilsHelper;
import com.itheima.topline.view.SwipeBackLayout;

public class ModifyPswActivity extends AppCompatActivity {
    private TextView tv_main_title, tv_back;
    private Button btn_save;
    private RelativeLayout rl_title_bar;
    private EditText et_original_psw, et_new_psw;
    private String originalPsw, newPsw;
    private String userName;
    private SwipeBackLayout layout;
    private ImageView iv_show_psw;
    private boolean isShowPsw = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_modify_psw);
        init();
        userName = UtilsHelper.readLoginUserName(this);
    }
    /**
     * 获取界面控件并处理相关控件的点击事件
     */
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        et_original_psw = (EditText) findViewById(R.id.et_original_psw);
        et_new_psw = (EditText) findViewById(R.id.et_new_psw);
        iv_show_psw = (ImageView) findViewById(R.id.iv_show_psw);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyPswActivity.this.finish();
            }
        });
        iv_show_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPsw = et_new_psw.getText().toString();
                if (isShowPsw) {
                    iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                    //隐藏密码
                    et_new_psw.setTransformationMethod(PasswordTransformationMethod.
                            getInstance());
                    isShowPsw = false;
                    if (newPsw != null) {
                        et_new_psw.setSelection(newPsw.length());
                    }
                } else {
                    iv_show_psw.setImageResource(R.drawable.show_psw_icon);
                    //显示密码
                    et_new_psw.setTransformationMethod(HideReturnsTransformationMethod.
                            getInstance());
                    isShowPsw = true;
                    if (newPsw != null) {
                        et_new_psw.setSelection(newPsw.length());
                    }
                }
            }
        });
        //保存按钮的点击事件
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(originalPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "请输入原始密码",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!MD5Utils.md5(originalPsw).equals(readPsw())) {
                    Toast.makeText(ModifyPswActivity.this, "输入的密码与原始密码不一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (MD5Utils.md5(newPsw).equals(readPsw())) {
                    Toast.makeText(ModifyPswActivity.this, "输入的新密码与原始密码不能一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "请输入新密码",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(ModifyPswActivity.this, "新密码设置成功",
                            Toast.LENGTH_SHORT).show();
                    //修改登录成功时保存在SharedPreferences中的密码
                    modifyPsw(newPsw);
                    Intent intent = new Intent(ModifyPswActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    SettingActivity.instance.finish(); //关闭设置界面
                    ModifyPswActivity.this.finish();   //关闭本界面
                }
            }
        });
    }
    /**
     * 获取控件上的字符串
     */
    private void getEditString() {
        originalPsw = et_original_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
    }
    /**
     * 修改登录成功时保存在SharedPreferences中的密码
     */
    private void modifyPsw(String newPsw) {
        String md5Psw = MD5Utils.md5(newPsw);            //把密码用MD5加密
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();  //获取编辑器
        editor.putString(userName, md5Psw);             //保存新密码
        editor.commit();                                    //提交修改
    }
    /**
     * 从SharedPreferences中读取原始密码
     */
    private String readPsw() {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        return spPsw;
    }
}
