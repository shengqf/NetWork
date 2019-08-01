package com.shengqf.network;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
class ResultVo {

    String data;
    String properties;
    String msg;
    int code;

    boolean isSuccess() {
        return code == NetworkConfig.getInstance().getSuccessCode();
    }

}
