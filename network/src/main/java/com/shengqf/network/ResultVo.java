package com.shengqf.network;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class ResultVo {

    public String data;
    public String properties;
    public String msg;
    public int code;

    public boolean isSuccess() {
        return code == NetworkConfig.getInstance().getSuccessCode();
    }

}
