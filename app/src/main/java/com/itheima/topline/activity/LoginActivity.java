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
import com.itheima.topline.view.SwipeBackLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_psw,et_user_name;
    private TextView tv_quick_register,tv_forget_psw;
    private ImageView iv_show_psw;
    private Button btn_login;
    private boolean isShowPsw=false;
    private String userName,psw,spPsw;
    private TextView tv_main_title,tv_back;
    private RelativeLayout rl_title_bar;
    private SwipeBackLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        tv_main_title= (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        rl_title_bar= (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back= (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        et_user_name= (EditText) findViewById(R.id.et_user_name);
        et_psw= (EditText) findViewById(R.id.et_psw);
        iv_show_psw= (ImageView) findViewById(R.id.iv_show_psw);
        tv_quick_register= (TextView) findViewById(R.id.tv_quick_register);
        tv_forget_psw= (TextView) findViewById(R.id.tv_forget_psw);
        btn_login= (Button) findViewById(R.id.btn_login);
        tv_back.setOnClickListener(this);
        iv_show_psw.setOnClickListener(this);
        tv_quick_register.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_back) {
            LoginActivity.this.finish();
        } else if (viewId == R.id.iv_show_psw) {
            psw = et_psw.getText().toString();
            if (isShowPsw) {
                iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                // 隐藏密码
                et_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isShowPsw = false;
                if (psw != null) {
                    et_psw.setSelection(psw.length());
                }
            } else {
                iv_show_psw.setImageResource(R.drawable.show_psw_icon);
                // 显示密码
                et_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isShowPsw = true;
                if (psw != null) {
                    et_psw.setSelection(psw.length());
                }
            }
        } else if (viewId == R.id.btn_login) {
            userName = et_user_name.getText().toString().trim();
            psw = et_psw.getText().toString().trim();
            String md5Psw = MD5Utils.md5(psw);
            spPsw = readPsw(userName);
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(psw)) {
                Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            } else if (md5Psw.equals(spPsw)) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                // 保存登录状态和登录的用户名
                saveLoginStatus(true, userName);
                // 把登录成功的状态传递到MainActivity中
                Intent data = new Intent();
                data.putExtra("isLogin", true);
                setResult(RESULT_OK, data);
                LoginActivity.this.finish();
                return;
            } else if (!TextUtils.isEmpty(spPsw) && !md5Psw.equals(spPsw)) {
                Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(LoginActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.tv_quick_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1);
        } else if (viewId == R.id.tv_forget_psw) {
            Intent forget = new Intent(LoginActivity.this, FindPswActivity.class);
            startActivity(forget);
        }
    }
    /**
     *从SharedPreferences中根据用户名读取密码
     */
    private String readPsw(String userName){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName, "");
    }
    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();    //获取编辑器
        editor.putBoolean("isLogin", status);          //存入boolean类型的登录状态
        editor.putString("loginUserName", userName); //存入登录时的用户名
        editor.commit();                                   //提交修改
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从注册界面传递过来的用户名
            String userName =data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
