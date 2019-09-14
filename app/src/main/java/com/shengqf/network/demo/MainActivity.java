package com.shengqf.network.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.shengqf.network.NetworkConfig;
import com.shengqf.network.NetworkTask;
import com.shengqf.network.demo.model.LoginUserVo;
import com.shengqf.network.demo.util.MD5;
import com.shengqf.network.demo.util.SPUtil;
import com.shengqf.network.demo.util.ToastUtil;
import com.shengqf.network.listener.OnNetworkFailListener;
import com.shengqf.network.listener.OnNetworkFinishListener;
import com.shengqf.network.listener.OnNetworkSuccessListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class MainActivity extends RxAppCompatActivity {

    private Button loginBtn, recordBtn, httpsBtn;
    private NetworkTask mLoginTask, mQueryTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setClick();
    }

    private void initView() {
        loginBtn = findViewById(R.id.login_btn);
        recordBtn = findViewById(R.id.record_btn);
        httpsBtn = findViewById(R.id.https_btn);
    }

    private void setClick() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRecordList();
            }
        });

        httpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query12306();
            }
        });
    }

    private void query12306() {
        httpsBtn.setText("查询中...");
        new NetworkTask().setUrl("https://kyfw.cer12306.cn/")
                .setOnSuccessListener(new OnNetworkSuccessListener() {
                    @Override
                    public void onSuccess(String msg, String data, String extra) {
                        ToastUtil.showShort("查询成功");
                    }
                })
                .setOnFailListener(new OnNetworkFailListener() {
                    @Override
                    public void onFail(int code, String msg) {
                        ToastUtil.showLong(msg);
                    }
                })
                .setOnFinishListener(new OnNetworkFinishListener() {
                    @Override
                    public void onFinish() {
                        httpsBtn.setText("https请求");
                    }
                })
                .post(this);
    }

    private void queryRecordList() {
        recordBtn.setText("申请记录查询中...");
        if (mQueryTask == null) {
            mQueryTask = new NetworkTask();
        }
        mQueryTask.setUrl("auth/copyApply/getList")
                .setMediaType(NetworkConfig.MediaType.JSON)
                .addParameter("pageNo", 1)
                .addParameter("pageSize", 20)
                .setOnSuccessListener(new OnNetworkSuccessListener() {
                    @Override
                    public void onSuccess(String msg, String data, String extra) {
                        ToastUtil.showShort("查询成功");
                    }
                })
                .setOnFailListener(new OnNetworkFailListener() {
                    @Override
                    public void onFail(int code, String msg) {
                        ToastUtil.showLong(msg);
                    }
                })
                .setOnFinishListener(new OnNetworkFinishListener() {
                    @Override
                    public void onFinish() {
                        recordBtn.setText("申请记录查询");
                    }
                })
                .post(this);
    }

    private void login() {
        loginBtn.setText("登录中...");
        if (mLoginTask == null) {
            mLoginTask = new NetworkTask();
        }
        mLoginTask.setUrl("login")
                .setMediaType(NetworkConfig.MediaType.FORM)
                .addParameter("username", "15606816762")
                .addParameter("password", MD5.getMD5("qwerty"))
                .setOnSuccessListener(new OnNetworkSuccessListener() {
                    @Override
                    public void onSuccess(String msg, String data, String extra) {
                        LoginUserVo loginUserVo = JSON.parseObject(data, LoginUserVo.class);
                        if (loginUserVo != null) {
                            SPUtil.getInstance().put("token", loginUserVo.token);
                            SPUtil.getInstance().put("sn", loginUserVo.sn);
                            ToastUtil.showShort("登录成功");
                        }
                    }
                })
                .setOnFailListener(new OnNetworkFailListener() {
                    @Override
                    public void onFail(int code, String msg) {
                        ToastUtil.showLong(msg);
                    }
                })
                .setOnFinishListener(new OnNetworkFinishListener() {
                    @Override
                    public void onFinish() {
                        loginBtn.setText("登录");
                    }
                })
                .post(this);
    }
}
