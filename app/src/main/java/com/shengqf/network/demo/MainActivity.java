package com.shengqf.network.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.shengqf.network.NetworkTask;
import com.shengqf.network.listener.OnNetworkFailListener;
import com.shengqf.network.listener.OnNetworkFinishListener;
import com.shengqf.network.listener.OnNetworkSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setClick();
    }

    private void initView() {
        loginBtn = findViewById(R.id.login_btn);
    }

    private void setClick() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        loginBtn.setText("登录中...");
        NetworkTask.getInstance()
                .setUrl("login")
                .initParameterMap()
                .addParameter("username", "15606816762")
                .addParameter("password", "qwerty")
                .setOnSuccessListener(new OnNetworkSuccessListener() {
                    @Override
                    public void onSuccess(String msg, String data, String extra) {

                    }
                })
                .setOnFailListener(new OnNetworkFailListener() {
                    @Override
                    public void onFail(int code, String msg) {

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
