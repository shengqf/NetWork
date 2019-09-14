package com.shengqf.network.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shengqf.network.NetworkConfig;
import com.shengqf.network.ResultVo;
import com.shengqf.network.listener.OnSSOListener;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/1
 * describe :
 */
public class ParserUtil {

    //网络错误
    public static final int NET_ERROR = -1;
    //返回json为空或解析失败
    public static final int PARSER_ERROR = -2;

    private static final String CODE = "code";
    private static final String DATA = "data";
    private static final String BODY = "body";
    private static final String MSG = "msg";
    private static final String MESSAGE = "message";
    private static final String PROPERTIES = "properties";

    private static int code = -88;

    public static ResultVo parser(String json) {
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject == null) {
            return null;
        }
        if (jsonObject.containsKey(CODE)) {
            code = jsonObject.getIntValue(CODE);
        }

        ResultVo resultVo = new ResultVo();
        resultVo.code = code;

        if (code == -500) {//单设备登录
            OnSSOListener onSSOListener = NetworkConfig.getInstance().getOnSSOListener();
            if (onSSOListener != null) {
                onSSOListener.singleSignOn();
            }
            return resultVo;
        }

        if (resultVo.isSuccess()) {//请求成功
            //data
            if (jsonObject.containsKey(DATA)) {
                resultVo.data = jsonObject.getString(DATA);
                if (resultVo.data.equals("{}")) {
                    resultVo.data = null;
                }
            } else if (jsonObject.containsKey(BODY)) {
                resultVo.data = jsonObject.getString(BODY);
                if (resultVo.data.equals("{}")) {
                    resultVo.data = null;
                }
            } else {
                resultVo.msg = "数据为空";
            }

            //message
            if (jsonObject.containsKey(MSG) && !jsonObject.getString(MSG).isEmpty()) {
                resultVo.msg = jsonObject.getString(MSG);
            } else if (jsonObject.containsKey(MESSAGE) && !jsonObject.getString(MESSAGE).isEmpty()) {
                resultVo.msg = jsonObject.getString(MESSAGE);
            }

            //properties
            if (jsonObject.containsKey(PROPERTIES)) {
                resultVo.properties = jsonObject.getString(PROPERTIES);
            }

            return resultVo;
        } else {//请求失败
            if (jsonObject.containsKey(MSG)) {
                resultVo.msg = jsonObject.getString(MSG);
            } else if (jsonObject.containsKey(MESSAGE)) {
                resultVo.msg = jsonObject.getString(MESSAGE);
            }
            return resultVo;
        }
    }

}
