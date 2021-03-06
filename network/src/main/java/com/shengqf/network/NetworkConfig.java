package com.shengqf.network;


import com.shengqf.network.listener.OnHeaderMapTransformFunction;
import com.shengqf.network.listener.OnParamsMapTransformFunction;
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
    private String mHostName = "";//（https请求时设置）服务器的IP
    private int mCertificateRes;//（https请求时设置）数字证书资源文件（服务端配置证书，客户端在请求服务端时验证服务器的证书）
    private MediaType mMediaType = MediaType.FORM;//post请求默认是form表单提交方式
    private int mSuccessCode = 200;//请求成功码
    private long mConnectTimeOut = 15L;//单位秒
    private long mWriteTimeOut = 15L;//单位秒
    private long mReadTimeOut = 15L;//单位秒
    private OnHeaderMapTransformFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>> mOnHeaderMapTransformFunction;
    private OnParamsMapTransformFunction<Map<String, Object>, TreeMap<String, Object>> mOnParamsMapTransformFunction;
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

    public NetworkConfig setHostName(String hostName) {
        this.mHostName = hostName;
        return this;
    }

    public String getHostName() {
        return mHostName;
    }

    public NetworkConfig setCertificateRes(int certificateRes) {
        this.mCertificateRes = certificateRes;
        return this;
    }

    public int getCertificateRes() {
        return mCertificateRes;
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

    public NetworkConfig setOnHeaderMapTransformFunction(OnHeaderMapTransformFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>> function) {
        this.mOnHeaderMapTransformFunction = function;
        return this;
    }

    public OnHeaderMapTransformFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>> getOnHeaderMapFunction() {
        return mOnHeaderMapTransformFunction;
    }

    public NetworkConfig setOnParamsMapTransformFunction(OnParamsMapTransformFunction<Map<String, Object>, TreeMap<String, Object>> function) {
        this.mOnParamsMapTransformFunction = function;
        return this;
    }

    public OnParamsMapTransformFunction<Map<String, Object>, TreeMap<String, Object>> getOnParamsMapFunction() {
        return mOnParamsMapTransformFunction;
    }

    public NetworkConfig setOnSSOListener(OnSSOListener onSSOListener) {
        this.mOnSSOListener = onSSOListener;
        return this;
    }

    public OnSSOListener getOnSSOListener() {
        return mOnSSOListener;
    }


    public enum MediaType {
        FORM, JSON
    }
}
