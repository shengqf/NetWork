package com.shengqf.network;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public interface ApiService {


    @GET
    Observable<ResponseBody> get(
            @Url String url,
            @QueryMap Map<String, Object> map);

    @GET
    Observable<ResponseBody> get(
            @Url String url,
            @HeaderMap Map<String, String> headerMap,
            @QueryMap Map<String, Object> map);

    /**
     * 入参form形式的post请求
     */
    @FormUrlEncoded
    @POST
    Observable<String> post(
            @Url String url,
            @FieldMap Map<String, Object> map);

    /**
     * 入参json格式的post请求
     */
    @POST
    Observable<String> post(
            @Url String url,
            @HeaderMap TreeMap<String, Object> headerMap,
            @Body Object body);

    /**
     * 上传图片
     */
    @Multipart
    @POST
    Observable<String> uploadPic(
            @Url String url,
            @PartMap Map<String, RequestBody> files,
            @QueryMap Map<String, Object> maps);

}
