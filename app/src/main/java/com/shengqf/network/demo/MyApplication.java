package com.shengqf.network.demo;

import android.app.Application;

import com.shengqf.network.demo.util.ContextUtil;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.init(this);
        NetworkHelper.init(this);
    }
}
