package com.itheima.topline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.itheima.heartlayout.HeartLayout;
import com.itheima.topline.R;
import com.itheima.topline.bean.ConstellationBean;
import com.itheima.topline.utils.Constant;
import com.itheima.topline.utils.DBUtils;
import com.itheima.topline.utils.JsonParse;
import com.itheima.topline.view.SwipeBackLayout;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConstellationActivity extends AppCompatActivity {
    private TextView tv_back, tv_switch;
    private SwipeBackLayout layout;
    private ImageView iv_head, iv_icon;
    private TextView tv_name, tv_date, tv_info;
    private RatingBar rb_whole, rb_love, rb_career, rb_money;
    private TextView tv_whole, tv_love, tv_career, tv_money, tv_health;
    private OkHttpClient okHttpClient;
    public static final int MSG_CONSTELLATION_OK = 1;//获取星座数据
    private MHandler mHandler;
    private Random mRandom;
    private Timer mTimer;
    private HeartLayout mHeartLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_constellation);
        mHandler = new MHandler();
        okHttpClient = new OkHttpClient();
        getData();
        init();
    }
    private void init() {
        mTimer = new Timer();
        mRandom = new Random();
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_switch = (TextView) findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("切换");
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_info = (TextView) findViewById(R.id.tv_info);
        rb_whole = (RatingBar) findViewById(R.id.rb_whole);
        rb_love = (RatingBar) findViewById(R.id.rb_love);
        rb_career = (RatingBar) findViewById(R.id.rb_career);
        rb_money = (RatingBar) findViewById(R.id.rb_money);
        tv_whole = (TextView) findViewById(R.id.tv_whole);
        tv_love = (TextView) findViewById(R.id.tv_love);
        tv_career = (TextView) findViewById(R.id.tv_career);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_health = (TextView) findViewById(R.id.tv_health);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstellationActivity.this.finish();
            }
        });
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstellationActivity.this, ChooseConstellationActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHeartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mHeartLayout.addHeart(randomColor());
                    }
                });
            }
        }, 500, 200);
    }
    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255),
                mRandom.nextInt(255));
    }
    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_CONSTELLATION_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        List<ConstellationBean> list = JsonParse.getInstance().
                                getConstellaList(result);
                        if (list != null) {
                            if (list.size() > 0) {
                                //保存数据到数据库
                                DBUtils.getInstance(ConstellationActivity.this).
                                        saveConstellationInfo(list);
                                ConstellationBean bean = DBUtils.getInstance(
                                        ConstellationActivity.this).getConstellationInfo(1);
                                setData(bean);
                            }
                        }
                    }
                    break;
            }
        }
    }
    private void setData(ConstellationBean bean) {
        tv_name.setText(bean.getName());
        Glide
                .with(ConstellationActivity.this)
                .load(bean.getHead())
                .error(R.mipmap.ic_launcher)
                .into(iv_head);
        Glide
                .with(ConstellationActivity.this)
                .load(bean.getIcon())
                .error(R.mipmap.ic_launcher)
                .into(iv_icon);
        tv_date.setText(bean.getDate());
        tv_info.setText(bean.getInfo());
        rb_whole.setRating(bean.getWhole());
        rb_love.setRating(bean.getLove());
        rb_career.setRating(bean.getCareer());
        rb_money.setRating(bean.getMoney());
        tv_whole.setText(bean.getWhole_info());
        tv_love.setText(bean.getLove_info());
        tv_career.setText(bean.getCareer_info());
        tv_money.setText(bean.getMoney_info());
        tv_health.setText(bean.getHealth_info());
    }
    private void getData() {
        Request request = new Request.Builder().url(Constant.WEB_SITE +
                Constant.REQUEST_CONSTELLATION_URL).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_CONSTELLATION_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException arg1) {
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int id = data.getIntExtra("id", 0);
            ConstellationBean bean = DBUtils.getInstance(ConstellationActivity.this).
                    getConstellationInfo(id);
            setData(bean);
        }
    }
}
