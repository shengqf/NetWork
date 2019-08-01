package com.shengqf.network;

import com.shengqf.network.json.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
class ServiceManager {

    private static final Object LOCK = new Object();
    private static ServiceManager INSTANCE;
    private Retrofit mRetrofit;

    static ServiceManager getInstance() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new ServiceManager();
            }
            return INSTANCE;
        }
    }

    private ServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.retryOnConnectionFailure(true); //连接失败重连
        builder.connectTimeout(NetworkConfig.getInstance().getConnectTimeOut(), TimeUnit.SECONDS);
        builder.writeTimeout(NetworkConfig.getInstance().getWriteTimeOut(), TimeUnit.SECONDS);
        builder.readTimeout(NetworkConfig.getInstance().getReadTimeOut(), TimeUnit.SECONDS);

        if (NetworkConfig.getInstance().isDebug()) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkConfig.getInstance().getHttpUrl())
                .client(builder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }

}
