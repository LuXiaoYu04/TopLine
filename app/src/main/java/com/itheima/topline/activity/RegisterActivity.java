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
import com.itheima.topline.R;
import com.itheima.topline.utils.MD5Utils;
import com.itheima.topline.view.SwipeBackLayout;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private SwipeBackLayout layout;
    private EditText et_psw, et_user_name;
    private ImageView iv_show_psw;
    private Button btn_register;
    private String userName, psw;
    private boolean isShowPsw = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        btn_register = (Button) findViewById(R.id.btn_register);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        iv_show_psw = (ImageView) findViewById(R.id.iv_show_psw);
        tv_back.setOnClickListener(this);
        iv_show_psw.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_back) {
            // 返回键的点击事件
            RegisterActivity.this.finish();
        } else if (viewId == R.id.iv_show_psw) {
            // 显示/隐藏密码的点击事件
            psw = et_psw.getText().toString();
            if (isShowPsw) {
                iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                et_psw.setTransformationMethod(PasswordTransformationMethod.getInstance()); // 隐藏密码
                isShowPsw = false;
                if (psw != null) {
                    et_psw.setSelection(psw.length());
                }
            } else {
                iv_show_psw.setImageResource(R.drawable.show_psw_icon);
                et_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); // 显示密码
                isShowPsw = true;
                if (psw != null) {
                    et_psw.setSelection(psw.length());
                }
            }
        } else if (viewId == R.id.btn_register) {
            // 注册按钮的点击事件
            userName = et_user_name.getText().toString().trim();
            psw = et_psw.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(psw)) {
                Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            } else if (isExistUserName(userName)) {
                Toast.makeText(RegisterActivity.this, "此账户名已经存在", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                // 把用户名和密码保存到SharedPreferences中
                saveRegisterInfo(userName, psw);
                // 注册成功后把用户名传递到LoginActivity.java中
                Intent data = new Intent();
                data.putExtra("userName", userName);
                setResult(RESULT_OK, data);
                RegisterActivity.this.finish();
            }
        }
    }
    /**
     * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        if (!TextUtils.isEmpty(spPsw)) {
            has_userName = true;
        }
        return has_userName;
    }
    /**
     * 保存用户名和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String userName, String psw) {
        String md5Psw = MD5Utils.md5(psw);           //把密码用MD5加密
        //loginInfo表示文件名
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        //以用户名为key,密码为value保存到SharedPreferences中
        editor.putString(userName, md5Psw);
        editor.commit();//提交修改
    }
}
