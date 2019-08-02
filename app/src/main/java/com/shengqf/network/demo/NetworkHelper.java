package com.shengqf.network.demo;

import android.app.Application;

import com.shengqf.network.NetworkConfig;
import com.shengqf.network.demo.util.ContextUtil;
import com.shengqf.network.demo.util.DeviceUtil;
import com.shengqf.network.demo.util.MD5;
import com.shengqf.network.demo.util.SPUtil;
import com.shengqf.network.demo.util.ToastUtil;
import com.shengqf.network.listener.OnHeaderMapTransformFunction;
import com.shengqf.network.listener.OnParamsMapTransformFunction;
import com.shengqf.network.listener.OnSSOListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class NetworkHelper {

    static void init(Application application) {
        NetworkConfig.getInstance()
                .setDebug(BuildConfig.DEBUG)
                .setHttpUrl(BuildConfig.httpUrl)
                .setMediaType(NetworkConfig.MediaType.JSON)
                .setSuccessCode(200)
                .setConnectTimeOut(15)
                .setReadTimeOut(15)
                .setWriteTimeOut(15)
                .setOnHeaderMapTransformFunction(new OnHeaderMapTransformFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>>() {
                    @Override
                    public TreeMap<String, Object> apply(Map<String, Object> headerMap, Map<String, Object> paramsMap) throws Exception {
                        return getNewHeaderMap(headerMap, paramsMap);
                    }
                })
                .setOnParamsMapTransformFunction(new OnParamsMapTransformFunction<Map<String, Object>, TreeMap<String, Object>>() {
                    @Override
                    public TreeMap<String, Object> apply(Map<String, Object> paramsMap) throws Exception {
                        return getNewParamsMap(paramsMap);
                    }
                })
                .setOnSSOListener(new OnSSOListener() {
                    @Override
                    public void singleSignOn() {
                        ToastUtil.showShort("你的账号在别的设备登录了，请重新登录");
                    }
                });
    }


    private static TreeMap<String, Object> getNewHeaderMap(Map<String, Object> headerMap, Map<String, Object> paramsMap) {
        String timestamp = System.currentTimeMillis() + "";
        String device = DeviceUtil.getDeviceId(ContextUtil.getContext());
        String token = SPUtil.getInstance().getString("token");
        String sn = SPUtil.getInstance().getString("sn");
        String utype = "1";

        TreeMap<String, Object> signMap = new TreeMap<>();
        if (paramsMap != null) {
            signMap.putAll(paramsMap);
        }
        signMap.put("timestamp", timestamp);
        signMap.put("device", device);
        signMap.put("token", token);
        signMap.put("sn", sn);
        signMap.put("utype", utype);

        String sign;
        StringBuilder sb = new StringBuilder();
        for (String key : signMap.keySet()) {
            Object value = signMap.get(key);
            sb.append(value);
        }
        sign = MD5.getMD5(sb.toString());

        TreeMap<String, Object> newHeaderMap = new TreeMap<>();
        try {
            newHeaderMap.put("sign", sign);
            newHeaderMap.put("timestamp", timestamp);
            newHeaderMap.put("utype", URLEncoder.encode(utype, "utf-8"));
            newHeaderMap.put("device", URLEncoder.encode(device, "utf-8"));
            newHeaderMap.put("token", URLEncoder.encode(token, "utf-8"));
            newHeaderMap.put("sn", URLEncoder.encode(sn, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newHeaderMap;
    }

    private static TreeMap<String, Object> getNewParamsMap(Map<String, Object> paramsMap) {
        String timestamp = System.currentTimeMillis() + "";
        String device = DeviceUtil.getDeviceId(ContextUtil.getContext());
        String token = SPUtil.getInstance().getString("token");
        String sn = SPUtil.getInstance().getString("sn");
        String utype = "1";

        TreeMap<String, Object> signMap = new TreeMap<>();
        if (paramsMap != null) {
            signMap.putAll(paramsMap);
        }
        signMap.put("timestamp", timestamp);
        signMap.put("device", device);
        signMap.put("token", token);
        signMap.put("sn", sn);
        signMap.put("utype", utype);


        String sign;
        StringBuilder sb = new StringBuilder();
        for (String key : signMap.keySet()) {
            Object value = signMap.get(key);
            sb.append(value);
        }
        sign = MD5.getMD5(sb.toString());

        TreeMap<String, Object> newParamsMap = new TreeMap<>();
        try {
            newParamsMap.put("sign", sign);
            newParamsMap.put("timestamp", timestamp);
            newParamsMap.put("utype", URLEncoder.encode(utype, "utf-8"));
            newParamsMap.put("device", URLEncoder.encode(device, "utf-8"));
            newParamsMap.put("token", URLEncoder.encode(token, "utf-8"));
            newParamsMap.put("sn", URLEncoder.encode(sn, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (paramsMap != null) {
            newParamsMap.putAll(paramsMap);
        }
        return newParamsMap;
    }

}
