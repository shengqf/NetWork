package com.shengqf.network.demo.util;

import android.annotation.SuppressLint;
import android.content.Context;


public class ContextUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private ContextUtil() {
        throw new UnsupportedOperationException("ContextUtil is private ，can't be instantiate...");
    }

    public static void init(Context context) {
        ContextUtil.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("you should init ContextUtil first");
    }

}
