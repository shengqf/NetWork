package com.shengqf.network.demo.interceptor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengqf.network.demo.util.ContextUtil;
import com.shengqf.network.demo.util.DeviceUtil;
import com.shengqf.network.demo.util.MD5;
import com.shengqf.network.demo.util.SPUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe : 拦截请求，添加参数
 * 使用：
 * OkHttpClient.Builder builder = new OkHttpClient.Builder();
 * ParamsInterceptor paramsInterceptor = new ParamsInterceptor();
 * if(paramsInterceptor){
 *     builder.addInterceptor(paramsInterceptor);
 * }
 *
 */
public class ParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Map<String, String> paramsMap = null;

        Request request = chain.request();
        RequestBody requestBody = request.body();

        if (requestBody != null) {
            //获取请求体
            String requestParams = null;
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                Charset charset = mediaType.charset(Charset.forName("UTF-8"));
                if (charset != null) {
                    //读取原请求参数内容
                    requestParams = buffer.readString(charset);
                    //请求体转为map
                    paramsMap = JSONObject.parseObject(requestParams, Map.class);
                }
            }

            if (paramsMap != null && !paramsMap.isEmpty()) {
                Request.Builder newRequestBuilder = getNewRequestBuilder(paramsMap, request);
                RequestBody newBody = RequestBody.create(mediaType, "[" + requestParams + "]");
                newRequestBuilder.post(newBody);
                return chain.proceed(newRequestBuilder.build());
            }
        }


        return null;
    }

    /**
     * 参数签名（互联网医院josn请求签名方法）
     */
    private Request.Builder getNewRequestBuilder(Map<String, String> paramsMap, Request request) {
        String timestamp = System.currentTimeMillis() + "";
        String device = DeviceUtil.getDeviceId(ContextUtil.getContext());
        String token = SPUtil.getInstance().getString("token");
        String sn = SPUtil.getInstance().getString("sn");
        String utype = "1";//居民端-1，医生端-2

        TreeMap<String, Object> signMap = new TreeMap<>();
        signMap.put("timestamp", timestamp);
        signMap.put("device", device);
        signMap.put("token", token);
        signMap.put("sn", sn);
        signMap.put("utype", utype);
        if (paramsMap != null) {
            signMap.putAll(paramsMap);
        }

        String sign;
        StringBuilder sb = new StringBuilder();
        for (String key : signMap.keySet()) {
            try {
                String value = (String) signMap.get(key);
                sb.append(value);
            } catch (ClassCastException e) {
                Object object = signMap.get(key);
                sb.append(JSON.toJSONString(object));
            }
        }
        sign = MD5.getMD5(sb.toString());

        Request.Builder requestBuilder = request.newBuilder();
        try {
            requestBuilder.addHeader("sign", sign);
            requestBuilder.addHeader("sn", URLEncoder.encode(sn, "utf-8"));
            requestBuilder.addHeader("token", URLEncoder.encode(token, "utf-8"));
            requestBuilder.addHeader("utype", utype);
            requestBuilder.addHeader("device", URLEncoder.encode(device, "utf-8"));
            requestBuilder.addHeader("timestamp", timestamp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return requestBuilder;
        }
        return requestBuilder;
    }
}
