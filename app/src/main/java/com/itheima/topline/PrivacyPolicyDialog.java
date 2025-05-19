package com.itheima.topline;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyDialog {

    public interface OnPrivacyAgreedListener {
        void onPrivacyAgreed();
    }

    public static void showPrivacyDialog(final Context context, final OnPrivacyAgreedListener listener) {
        new AlertDialog.Builder(context)
                .setTitle("隐私政策")
                .setMessage("我们承诺保护您的隐私，详情请查看我们的隐私政策。\n\n" +
                        "是否同意？")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户同意隐私政策
                        com.amap.api.maps.MapsInitializer.updatePrivacyAgree(context.getApplicationContext(), true);
                        if (listener != null) {
                            listener.onPrivacyAgreed();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户拒绝隐私政策
                        com.amap.api.maps.MapsInitializer.updatePrivacyAgree(context.getApplicationContext(), false);
                        dialog.dismiss();
                        ((AppCompatActivity) context).finish(); // 关闭应用
                    }
                })
                .setCancelable(false) // 禁止用户点击外部关闭弹窗
                .show();
    }
}