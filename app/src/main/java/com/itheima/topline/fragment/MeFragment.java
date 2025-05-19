package com.itheima.topline.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.itheima.topline.R;
import com.itheima.topline.activity.CalendarActivity;
import com.itheima.topline.activity.CollectionActivity;
import com.itheima.topline.activity.ConstellationActivity;
import com.itheima.topline.activity.LoginActivity;
import com.itheima.topline.activity.MapActivity;
import com.itheima.topline.activity.ScrawActivity;
import com.itheima.topline.activity.SettingActivity;
import com.itheima.topline.activity.UserInfoActivity;
import com.itheima.topline.receiver.UpdateUserInfoReceiver;
import com.itheima.topline.utils.DBUtils;
import com.itheima.topline.utils.UtilsHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_calendar, ll_constellation, ll_scraw, ll_map;
    private RelativeLayout rl_collection, rl_setting;
    private CircleImageView iv_avatar;
    private View view;
    private UpdateUserInfoReceiver updateUserInfoReceiver;
    private IntentFilter filter;
    private boolean isLogin = false;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_calendar = (LinearLayout) view.findViewById(R.id.ll_calendar);
        ll_constellation = (LinearLayout) view.findViewById(R.id.ll_constellation);
        ll_scraw = (LinearLayout) view.findViewById(R.id.ll_scraw);
        ll_map = (LinearLayout) view.findViewById(R.id.ll_map);
        rl_collection = (RelativeLayout) view.findViewById(R.id.rl_collection);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tool_bar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ToolbarTitle);
        isLogin = UtilsHelper.readLoginStatus(getActivity());
        setLoginParams(isLogin);
        setListener();
        receiver();
    }

    private void receiver() {
        updateUserInfoReceiver = new UpdateUserInfoReceiver(
                new UpdateUserInfoReceiver.BaseOnReceiveMsgListener() {
                    @Override
                    public void onReceiveMsg(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO.equals(action)) {
                            String type = intent.getStringExtra(UpdateUserInfoReceiver.INTENT_TYPE.TYPE_NAME);
                            if (UpdateUserInfoReceiver.INTENT_TYPE.UPDATE_HEAD.equals(type)) { // 更新头像
                                String head = intent.getStringExtra("head");
                                Bitmap bt = BitmapFactory.decodeFile(head);
                                if (bt != null) {
                                    Drawable drawable = new BitmapDrawable(bt);
                                    iv_avatar.setImageDrawable(drawable);
                                } else {
                                    iv_avatar.setImageResource(R.drawable.default_head);
                                }
                            }
                        }
                    }
                });

        filter = new IntentFilter(UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 及以上版本需要指定 RECEIVER_EXPORTED 或 RECEIVER_NOT_EXPORTED
            getActivity().registerReceiver(updateUserInfoReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    private void setListener() {
        ll_calendar.setOnClickListener(this);
        ll_constellation.setOnClickListener(this);
        ll_scraw.setOnClickListener(this);
        ll_map.setOnClickListener(this);
        rl_collection.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateUserInfoReceiver != null) {
            getActivity().unregisterReceiver(updateUserInfoReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_calendar) {
            Intent calendarIntent = new Intent(getActivity(), CalendarActivity.class);
            startActivity(calendarIntent);
        } else if (viewId == R.id.ll_constellation) {
            Intent constellIntent = new Intent(getActivity(), ConstellationActivity.class);
            startActivity(constellIntent);
        } else if (viewId == R.id.ll_scraw) {
            Intent scarwIntent = new Intent(getActivity(), ScrawActivity.class);
            startActivity(scarwIntent);
        } else if (viewId == R.id.ll_map) {
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);
            startActivity(mapIntent);
        } else if (viewId == R.id.rl_collection) {
            if (isLogin) {
                Intent collection = new Intent(getActivity(), CollectionActivity.class);
                startActivity(collection);
            } else {
                Toast.makeText(getActivity(), "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.rl_setting) {
            if (isLogin) {
                // 跳转到设置界面
                Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(settingIntent, 1);
            } else {
                Toast.makeText(getActivity(), "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.iv_avatar) {
            if (isLogin) {
                // 跳转到个人资料界面
                Intent userinfo = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(userinfo);
            } else {
                // 跳转到登录界面
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(login, 1);
            }
        }
    }

    /**
     * 登录成功后设置我的界面
     */
    public void setLoginParams(boolean isLogin) {
        if (isLogin) {
            String userName = UtilsHelper.readLoginUserName(getActivity());
            collapsingToolbarLayout.setTitle(userName);
            String head = DBUtils.getInstance(getActivity()).getUserHead(userName);
            Bitmap bt = BitmapFactory.decodeFile(head);
            if (bt != null) {
                Drawable drawable = new BitmapDrawable(bt); // 转换成 drawable
                iv_avatar.setImageDrawable(drawable);
            } else {
                iv_avatar.setImageResource(R.drawable.default_head);
            }
        } else {
            iv_avatar.setImageResource(R.drawable.default_head);
            collapsingToolbarLayout.setTitle("点击登录");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            boolean isLogin = data.getBooleanExtra("isLogin", false);
            setLoginParams(isLogin);
            this.isLogin = isLogin;
        }
    }
}