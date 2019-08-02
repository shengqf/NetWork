package com.shengqf.network.demo.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;


public class DeviceUtil {
    
    private static String deviceID = null;
    private static final String INSTALLATION = "INSTALLATION";

    //获取设备id
    public static String getDeviceId(Context context){
        if (deviceID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()){
                    writeInstallationFile(installation);
                }
                deviceID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return deviceID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    /**
     * 获取设备名称
     */
    public static String getDeviceName(){
        StringBuilder sb=new StringBuilder();
        if(!TextUtils.isEmpty(android.os.Build.BRAND)){
            sb.append(android.os.Build.BRAND);
        }
        if(!TextUtils.isEmpty(android.os.Build.MODEL)){
            sb.append(android.os.Build.MODEL);
        }
        return sb.toString();
    }

    /**
     * 获取当前的版本信息
     */
    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
    
}
