package com.shengqf.network.demo;

import android.app.Application;

import com.shengqf.network.NetworkConfig;
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
public class NetworkHelper {

    static void init(Application application){
        NetworkConfig.getInstance()
                .setDebug(BuildConfig.DEBUG)
                .setHttpUrl(BuildConfig.httpUrl)
                .setMediaType(NetworkConfig.MediaType.JSON)
                .setSuccessCode(200)
                .setConnectTimeOut(15)
                .setReadTimeOut(15)
                .setWriteTimeOut(15)
                .setOnHeaderMapFunction(new OnHeaderMapFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>>() {
                    @Override
                    public TreeMap<String, Object> apply(Map<String, Object> headerMap, Map<String, Object> paramsMap) throws Exception {
                        return getHeaderMap(headerMap,paramsMap);
                    }
                })
                .setOnParamsMapFunction(new OnParamsMapFunction<Map<String, Object>, TreeMap<String, Object>>() {
                    @Override
                    public TreeMap<String, Object> apply(Map<String, Object> paramsMap) throws Exception {
                        return getParamsMap(paramsMap);
                    }
                })
                .setOnSSOListener(new OnSSOListener() {
                    @Override
                    public void singleSignOn() {

                    }
                });
    }

    private static TreeMap<String, Object> getParamsMap(Map<String, Object> paramsMap) {
        return null;
    }

    private static TreeMap<String, Object> getHeaderMap(Map<String, Object> headerMap, Map<String, Object> paramsMap) {
        return null;
    }

}
