package com.shengqf.network.listener;


import io.reactivex.functions.BiFunction;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public interface OnHeaderMapFunction<T1, T2, R> extends BiFunction<T1, T2, R> {

}

