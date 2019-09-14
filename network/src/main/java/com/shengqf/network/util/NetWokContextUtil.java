package com.shengqf.network.util;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/9/14
 * describe :
 */
public class NetWokContextUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private NetWokContextUtil() {
        throw new UnsupportedOperationException("NetWokContextUtil is private ，can't be instantiate...");
    }

    public static void init(Context context) {
        NetWokContextUtil.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("you should init NetWokContextUtil first");
    }
}
