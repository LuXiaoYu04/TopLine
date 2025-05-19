package com.itheima.topline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

/**
 * 更新用户信息的广播接收器
 */
public class UpdateUserInfoReceiver extends BroadcastReceiver {

    /**
     * 广播动作
     */
    public interface ACTION {
        String UPDATE_USERINFO = "update_userinfo";
    }

    /**
     * 广播 Intent 类型
     */
    public interface INTENT_TYPE {
        String TYPE_NAME = "intent_name";
        String UPDATE_HEAD = "update_head"; // 更新头像
    }

    private BaseOnReceiveMsgListener onReceiveMsgListener;

    public UpdateUserInfoReceiver(BaseOnReceiveMsgListener onReceiveMsgListener) {
        this.onReceiveMsgListener = onReceiveMsgListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onReceiveMsgListener != null) {
            onReceiveMsgListener.onReceiveMsg(context, intent);
        }
    }

    /**
     * 广播接收器的回调接口
     */
    public interface BaseOnReceiveMsgListener {
        void onReceiveMsg(Context context, Intent intent);
    }

    /**
     * 注册广播接收器
     *
     * @param context 上下文
     */
    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(ACTION.UPDATE_USERINFO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 及以上版本需要指定 RECEIVER_EXPORTED 或 RECEIVER_NOT_EXPORTED
            context.registerReceiver(this, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    /**
     * 取消注册广播接收器
     *
     * @param context 上下文
     */
    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(this);
    }
}