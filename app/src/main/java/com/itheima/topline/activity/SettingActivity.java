package com.itheima.topline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.itheima.topline.R;
import com.itheima.topline.utils.UtilsHelper;
import com.itheima.topline.view.SwipeBackLayout;
public class SettingActivity extends AppCompatActivity {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_modify_psw, rl_security_setting, rl_exit_login;
    public static SettingActivity instance = null;
    private SwipeBackLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_setting);
        instance = this;
        init();
    }
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back = (TextView) findViewById(R.id.tv_back);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        rl_modify_psw = (RelativeLayout) findViewById(R.id.rl_modify_psw);
        rl_security_setting = (RelativeLayout) findViewById(R.id.rl_security_setting);
        rl_exit_login = (RelativeLayout) findViewById(R.id.rl_exit_login);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        //修改密码的点击事件
        rl_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改密码的界面
                Intent intent = new Intent(SettingActivity.this, ModifyPswActivity.class);
                startActivity(intent);
            }
        });
        //设置密保的点击事件
        rl_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置密保界面
                Intent intent = new Intent(SettingActivity.this, FindPswActivity.class);
                intent.putExtra("from", "security");
                startActivity(intent);
            }
        });
        //退出登录的点击事件
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "退出登录成功",
                        Toast.LENGTH_SHORT).show();
                //清除登录状态和登录时的用户名
                UtilsHelper.clearLoginStatus(SettingActivity.this);
                //退出登录成功后把退出成功的状态传递到MainActivity中
                Intent data = new Intent();
                data.putExtra("isLogin", false);
                setResult(RESULT_OK, data);
                SettingActivity.this.finish();
            }
        });
    }
}
