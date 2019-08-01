package com.shengqf.network;

import android.text.TextUtils;

import com.shengqf.network.listener.OnHeaderMapFunction;
import com.shengqf.network.listener.OnNetworkFailListener;
import com.shengqf.network.listener.OnNetworkFinishListener;
import com.shengqf.network.listener.OnNetworkSuccessListener;
import com.shengqf.network.listener.OnParamsMapFunction;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.RxFragment;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class NetworkTask {

    private String mUrl;
    private Map<String, Object> mHeaderMap;
    private Map<String, Object> mParameterMap;
    private String mServerFileName = "files";
    private String[] mFilePathArray;

    private Observable<String> mObservable;
    private Disposable mDisposable;

    private OnNetworkSuccessListener mOnSuccessListener;
    private OnNetworkFailListener mOnFailListener;
    private OnNetworkFinishListener mOnFinishListener;

    private static class Singleton {
        private static final NetworkTask INSTANCE = new NetworkTask();
    }

    private NetworkTask() {

    }

    public static NetworkTask getInstance() {
        return Singleton.INSTANCE;
    }

    public NetworkTask setUrl(String url) {
        mUrl = url;
        //initParameterMap();
        return this;
    }

    public NetworkTask initParameterMap() {
        if (mHeaderMap == null) {
            mHeaderMap = new HashMap<>();
        } else {
            mHeaderMap.clear();
        }
        if (mParameterMap == null) {
            mParameterMap = new HashMap<>();
        } else {
            mParameterMap.clear();
        }
        return this;
    }

    public NetworkTask addHeader(String key, Object value) {
        if (!TextUtils.isEmpty(key)) {
            mHeaderMap.put(key, value == null ? "" : value);
        }
        return this;
    }

    public NetworkTask addParameter(String key, Object value) {
        if (!TextUtils.isEmpty(key)) {
            mParameterMap.put(key, value == null ? "" : value);
        }
        return this;
    }

    public NetworkTask setServerFileName(String fileName) {
        if (null != fileName) {
            mServerFileName = fileName;
        }
        return this;
    }

    public NetworkTask setFilePath(String[] pathArray) {
        if (null != pathArray) {
            this.mFilePathArray = pathArray;
        }
        return this;
    }

    public NetworkTask setOnSuccessListener(OnNetworkSuccessListener onSuccessListener) {
        this.mOnSuccessListener = onSuccessListener;
        return this;
    }

    public NetworkTask setOnFailListener(OnNetworkFailListener onFailListener) {
        this.mOnFailListener = onFailListener;
        return this;
    }

    public NetworkTask setOnFinishListener(OnNetworkFinishListener onFinishListener) {
        this.mOnFinishListener = onFinishListener;
        return this;
    }

    public NetworkTask setMediaType(NetworkConfig.MediaType mediaType) {
        NetworkConfig.getInstance().setMediaType(mediaType);
        return this;
    }

    public <T> void post(T t) {
        mObservable = post();
        mObservable = bindToLifecycle(t);
        subscribe();
    }

    public <T> void postPics(T t) {
        mObservable = postPics();
        mObservable = bindToLifecycle(t);
        subscribe();
    }

    private void subscribe() {
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ObservableTransformer<String, ResultVo>() {
                    @Override
                    public ObservableSource<ResultVo> apply(Observable<String> upstream) {
                        return upstream.map(new Function<String, ResultVo>() {
                            @Override
                            public ResultVo apply(String s) throws Exception {
                                return ParserUtil.parser(s);
                            }
                        });
                    }
                })
                .subscribe(new Observer<ResultVo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(ResultVo resultVo) {
                        if (resultVo == null) {
                            if (mOnFailListener != null) {
                                mOnFailListener.onFail(ParserUtil.PARSER_ERROR, "返回json为空或解析失败");
                            }
                            return;
                        }
                        if (resultVo.isSuccess()) {
                            if (mOnSuccessListener != null) {
                                mOnSuccessListener.onSuccess(resultVo.msg, resultVo.data, resultVo.properties);
                            }
                        } else {
                            if (mOnFailListener != null) {
                                mOnFailListener.onFail(resultVo.code, resultVo.msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != mOnFailListener) {
                            if (e instanceof SocketTimeoutException) {
                                mOnFailListener.onFail(ParserUtil.NET_ERROR, "请求超时");
                            } else if (e instanceof UnknownHostException) {
                                mOnFailListener.onFail(ParserUtil.NET_ERROR, "网络连接不可用");
                            } else {
                                mOnFailListener.onFail(ParserUtil.NET_ERROR, "请求失败");
                            }
                        }
                        if (null != mOnFinishListener) {
                            mOnFinishListener.onFinish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (null != mOnFinishListener) {
                            mOnFinishListener.onFinish();
                        }
                    }
                });
    }

    //绑定Activity或Fragment的生命周期(绑定到具体哪个方法用bindUntilEvent(ActivityEvent.DESTROY))
    private <T> Observable<String> bindToLifecycle(T t) {
        if (t == null) {
            return mObservable;
        }
        if (t instanceof RxActivity) {
            return mObservable.compose(((RxActivity) t).<String>bindToLifecycle());
        } else if (t instanceof RxFragment) {
            return mObservable.compose(((RxFragment) t).<String>bindToLifecycle());
        }
        return mObservable;
    }

    private Observable<String> post() {
        ApiService baseApiService = ServiceManager.getInstance().create(ApiService.class);
        if (NetworkConfig.getInstance().getMediaType() == NetworkConfig.MediaType.JSON) {
            //json方式提交
            return baseApiService.post(mUrl, getHeaderMap(), new Object[]{mParameterMap});
        }
        //form方式提交
        return baseApiService.post(mUrl, getCommonParameter());
    }

    private Observable<String> postPics() {
        Map<String, RequestBody> bodyMap = new HashMap<>();
        ApiService apiService = ServiceManager.getInstance().create(ApiService.class);
        if (null != mFilePathArray && mFilePathArray.length > 0) {
            for (String filePath : mFilePathArray) {
                if (!TextUtils.isEmpty(filePath)) {
                    File file = new File(filePath);
                    RequestBody photo = RequestBody.create(MediaType.parse("image/*"), file);
                    bodyMap.put(mServerFileName + "\"; filename=\"" + file.getName(), photo);
                }
            }
            return apiService.uploadPic(mUrl, bodyMap, getCommonParameter());
        } else {
            return apiService.post(mUrl, getCommonParameter());
        }
    }

    private Map<String, Object> getCommonParameter() {
        OnParamsMapFunction<Map<String, Object>, TreeMap<String, Object>> paramsMapFunction
                = NetworkConfig.getInstance().getOnParamsMapFunction();
        if (paramsMapFunction != null) {
            try {
                return paramsMapFunction.apply(mParameterMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private TreeMap<String, Object> getHeaderMap() {
        OnHeaderMapFunction<Map<String, Object>, Map<String, Object>, TreeMap<String, Object>>
                headerMapFunction = NetworkConfig.getInstance().getOnHeaderMapFunction();
        if (headerMapFunction != null) {
            try {
                return headerMapFunction.apply(mHeaderMap, mParameterMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void cancel() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
