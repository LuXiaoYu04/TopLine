package com.itheima.topline;

import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.itheima.topline.fragment.HomeFragment;
import com.itheima.topline.fragment.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private TextView tv_main_title;
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_main_title = (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("首页");
        rl_title_bar = (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_home) {
                    viewPager.setCurrentItem(0,false);
                }
                if (checkedId == R.id.rb_count){
                    viewPager.setCurrentItem(1,false);
                }
                if (checkedId == R.id.rb_video){
                    viewPager.setCurrentItem(2,false);
                }
                if (checkedId == R.id.rb_me){
                    viewPager.setCurrentItem(3,false);
                }
            }
        });
        viewPager = (ViewPager)findViewById(R.id.viewPager);


        HomeFragment homeFragment = new HomeFragment();
        List<Fragment> alFragment = new ArrayList<>();
        alFragment.add(homeFragment);

        // ViewPager 设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        viewPager.setCurrentItem(0); // ViewPager 显示第一个 Fragment


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    radioGroup.check(R.id.rb_home);
                    tv_main_title.setText("首页");
                    rl_title_bar.setVisibility(View.VISIBLE);
                }
                if (position == 1) {
                    radioGroup.check(R.id.rb_count);
                    tv_main_title.setText("统计");
                    rl_title_bar.setVisibility(View.VISIBLE);
                }
                if (position == 2) {
                    radioGroup.check(R.id.rb_video);
                    tv_main_title.setText("视频");
                    rl_title_bar.setVisibility(View.VISIBLE);
                }
                if (position == 3) {
                    radioGroup.check(R.id.rb_me);
                    tv_main_title.setText("我的");
                    rl_title_bar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }protected long execTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - execTime > 2000){
                Toast.makeText(MainActivity.this, "再按一次退出黑马头条", Toast.LENGTH_SHORT).show();
                execTime = System.currentTimeMillis();
            }else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}