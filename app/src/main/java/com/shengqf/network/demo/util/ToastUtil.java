package com.shengqf.network.demo.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showLong(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtil.getContext(), "", Toast.LENGTH_LONG);
        }
        if (!TextUtils.isEmpty(msg)) {
            mToast.setText(msg);
            mToast.show();
        }
    }

    @SuppressLint("ShowToast")
    public static void showShort(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtil.getContext(), "", Toast.LENGTH_SHORT);
        }
        if (!TextUtils.isEmpty(msg)) {
            mToast.setText(msg);
            mToast.show();
        }

    }

}
