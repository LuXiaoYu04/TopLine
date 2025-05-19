package com.itheima.topline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itheima.topline.R;
import com.itheima.topline.adapter.ChooseConstellaListAdapter;
import com.itheima.topline.bean.ConstellationBean;
import com.itheima.topline.utils.Constant;
import com.itheima.topline.utils.JsonParse;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseConstellationActivity extends AppCompatActivity {
    private ImageView iv_close;
    private ChooseConstellaListAdapter adapter;
    private OkHttpClient okHttpClient;
    public static final int MSG_CHOOSE_CONSTELLATION_OK = 1;//获取星座数据
    private MHandler mHandler;
    private RecyclerView recyclerView;
    private List<ConstellationBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_constellation);
        mHandler = new MHandler();
        okHttpClient = new OkHttpClient();
        getData();
        init();
    }
    private void init() {
        iv_close = (ImageView) findViewById(R.id.iv_close);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //这里用线性显示 类似于	ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChooseConstellaListAdapter(ChooseConstellationActivity.this);
        recyclerView.setAdapter(adapter);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseConstellationActivity.this.finish();
            }
        });
        adapter.setOnItemClickListener(new ChooseConstellaListAdapter.
                OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", list.get(position).getId());
                setResult(RESULT_OK, intent);
                ChooseConstellationActivity.this.finish();
            }
        });
    }
    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_CHOOSE_CONSTELLATION_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        list = JsonParse.getInstance().getConstellaList(result);
                        if (list != null) {
                            if (list.size() > 0) {
                                adapter.setData(list);
                            }
                        }
                    }
                    break;
            }
        }
    }
    private void getData() {
        Request request = new Request.Builder().url(Constant.WEB_SITE +
                Constant.REQUEST_CHOOSE_CONSTELLATION_LIST_URL).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_CHOOSE_CONSTELLATION_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException arg1) {
            }
        });
    }
}
