package com.shengqf.network;


import com.shengqf.network.listener.OnHeaderMapFunction;
import com.shengqf.network.listener.OnParamsMapFunction;
import com.shengqf.network.listener.OnSSOListener;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class NetworkConfig {

    private Boolean mIsDebug = true; //是否是调试模式，调试模式才打印日志
    private String mHttpUrl = ""; //接口请求地址，"/"结尾
    private MediaType mMediaType = MediaType.FORM;//post请求默认是form表单提交方式
    private int mSuccessCode = 200;//请求成功码
    private long mConnectTimeOut = 15L;//单位秒
    private long mWriteTimeOut = 15L;//单位秒
    private long mReadTimeOut = 15L;//单位秒
    private OnHeaderMapFunction<Map<String, Object>,Map<String, Object>, TreeMap<String, Object>> mOnHeaderMapFunction;
    private OnParamsMapFunction<Map<String, Object>, TreeMap<String, Object>> mOnParamsMapFunction;
    private OnSSOListener mOnSSOListener;//单设备登录

    private static final Object LOCK = new Object();
    private static NetworkConfig INSTANCE;

    private NetworkConfig() {
    }

    public static NetworkConfig getInstance() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new NetworkConfig();
            }
            return INSTANCE;
        }
    }

    public NetworkConfig setDebug(boolean isDebug) {
        this.mIsDebug = isDebug;
        return this;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public NetworkConfig setHttpUrl(String httpUrl) {
        this.mHttpUrl = httpUrl;
        return this;
    }

    public String getHttpUrl() {
        return mHttpUrl;
    }

    public NetworkConfig setMediaType(MediaType mediaType) {
        this.mMediaType = mediaType;
        return this;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public NetworkConfig setSuccessCode(int successCode) {
        this.mSuccessCode = successCode;
        return this;
    }

    public int getSuccessCode() {
        return mSuccessCode;
    }

    public NetworkConfig setConnectTimeOut(long connectTimeOut) {
        this.mConnectTimeOut = connectTimeOut;
        return this;
    }

    public long getConnectTimeOut() {
        return mConnectTimeOut;
    }

    public NetworkConfig setReadTimeOut(long readTimeOut) {
        this.mReadTimeOut = readTimeOut;
        return this;
    }

    public long getReadTimeOut() {
        return mReadTimeOut;
    }

    public NetworkConfig setWriteTimeOut(long writeTimeOut) {
        this.mWriteTimeOut = writeTimeOut;
        return this;
    }

    public long getWriteTimeOut() {
        return mWriteTimeOut;
    }

    public NetworkConfig setOnHeaderMapFunction(OnHeaderMapFunction<Map<String, Object>,Map<String, Object>, TreeMap<String, Object>> function) {
        this.mOnHeaderMapFunction = function;
        return this;
    }

    public OnHeaderMapFunction<Map<String, Object>,Map<String, Object>, TreeMap<String, Object>> getOnHeaderMapFunction() {
        return mOnHeaderMapFunction;
    }

    public NetworkConfig setOnParamsMapFunction(OnParamsMapFunction<Map<String, Object>, TreeMap<String, Object>> function) {
        this.mOnParamsMapFunction = function;
        return this;
    }

    public OnParamsMapFunction<Map<String, Object>, TreeMap<String, Object>> getOnParamsMapFunction() {
        return mOnParamsMapFunction;
    }

    public NetworkConfig setOnSSOListener(OnSSOListener onSSOListener){
        this.mOnSSOListener = onSSOListener;
        return this;
    }

    public OnSSOListener getOnSSOListener(){
        return mOnSSOListener;
    }


    public static enum MediaType {
        FORM, JSON
    }
}
