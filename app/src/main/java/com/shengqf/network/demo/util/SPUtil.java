package com.shengqf.network.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2018/5/6
 * describe :
 */
//加密
public class SPUtil {

    private SharedPreferences mSP;
    private SharedPreferences.Editor mEditor;

    private static class SingletonHolder {
        private static final SPUtil INSTANCE =
                new SPUtil(ContextUtil.getContext(), "SharedPreferences");
    }

    public static SPUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public SPUtil(Context context, String spName) {
        mSP = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        mEditor = mSP.edit();
        mEditor.apply();
    }

    /**
     * AES128加密
     *
     * @return cipherText base64
     */
    private String encryptPreference(String plainText) {
        return EncryptUtil.getInstance(ContextUtil.getContext()).encrypt(plainText);
    }

    /**
     * AES128解密
     *
     * @return plainText
     */
    private String decryptPreference(String cipherText) {
        return EncryptUtil.getInstance(ContextUtil.getContext()).decrypt(cipherText);
    }

    public void put(String key, @Nullable String value) {
        mEditor.putString(encryptPreference(key), encryptPreference(value)).apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        final String encryptValue = mSP.getString(encryptPreference(key), null);
        return encryptValue == null ? defaultValue : decryptPreference(encryptValue);
    }

    public void put(String key, int value) {
        mEditor.putString(encryptPreference(key), encryptPreference(Integer.toString(value))).apply();
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        final String encryptValue = mSP.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defaultValue;
        }
        return Integer.parseInt(decryptPreference(encryptValue));
    }

    public void put(String key, long value) {
        mEditor.putString(encryptPreference(key), encryptPreference(Long.toString(value))).apply();
    }

    public long getLong(String key) {
        return getLong(key, -1L);
    }

    public long getLong(String key, long defaultValue) {
        final String encryptValue = mSP.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defaultValue;
        }
        return Long.parseLong(decryptPreference(encryptValue));
    }

    public void put(String key, float value) {
        mEditor.putString(encryptPreference(key), encryptPreference(Float.toString(value))).apply();
    }

    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    public float getFloat(String key, float defaultValue) {
        final String encryptValue = mSP.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defaultValue;
        }
        return Float.parseFloat(decryptPreference(encryptValue));
    }

    public void put(String key, boolean value) {
        mEditor.putString(encryptPreference(key), encryptPreference(Boolean.toString(value))).apply();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        final String encryptValue = mSP.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(decryptPreference(encryptValue));
    }

    public void put(String key, Set<String> values) {
        final Set<String> encryptSet = new HashSet<>();
        for (String value : values) {
            encryptSet.add(encryptPreference(value));
        }
        mEditor.putStringSet(encryptPreference(key), encryptSet).apply();
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, @Nullable Set<String> defaultValue) {
        final Set<String> encryptSet = mSP.getStringSet(encryptPreference(key), null);
        if (encryptSet == null) {
            return defaultValue;
        }
        final Set<String> decryptSet = new HashSet<>();
        for (String encryptValue : encryptSet) {
            decryptSet.add(decryptPreference(encryptValue));
        }
        return decryptSet;
    }

    public Map<String, ?> getAll() {
        final Map<String, ?> encryptMap = mSP.getAll();
        final Map<String, String> decryptMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : encryptMap.entrySet()) {
            Object cipherText = entry.getValue();
            if (cipherText != null) {
                decryptMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return decryptMap;
    }

    public void remove(String key) {
        mEditor.remove(encryptPreference(key)).apply();
    }

    public boolean contains(String key) {
        return mSP.contains(encryptPreference(key));
    }

    public void clear() {
        mEditor.clear().apply();
    }


    public void putObject(String key, Object data) {
        if (data != null) {
            put(key, JSON.toJSONString(data));
        } else {
            put(key, "");
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = getString(key);
        if (!TextUtils.isEmpty(json)) {
            return JSON.parseObject(json, clazz);
        } else {
            return null;
        }
    }

    public void putList(String key, Object list) {
        if (list != null) {
            put(key, JSON.toJSONString(list));
        } else {
            put(key, "");
        }
    }

    public <T> List<T> getListFromLocal(String key, Class<T> clazz) {
        String json = getString(key);
        if (!TextUtils.isEmpty(json)) {
            return JSON.parseArray(json, clazz);
        } else {
            return null;
        }
    }

}


