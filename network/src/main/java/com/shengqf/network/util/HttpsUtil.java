package com.shengqf.network.util;

import com.shengqf.network.NetworkConfig;
import com.shengqf.network.util.NetWokContextUtil;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/8/22
 * describe :
 * <p>
 * 参考：
 * https://blog.csdn.net/qq_20521573/article/details/79233793
 * https://www.jianshu.com/p/2bb5317f05d3
 */
public class HttpsUtil {

    /**
     * 单项认证
     */
    public static void setSSLSocketFactory(OkHttpClient.Builder builder) {
        try {
            KeyStore keyStore = getKeyStore();
            if (keyStore == null) {
                return;
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            X509TrustManager x509TrustManager = getX509TrustManager(trustManagerFactory);
            if (x509TrustManager == null) {
                return;
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            builder.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KeyStore getKeyStore() {
        try {
            InputStream certificateInputString = NetWokContextUtil.getContext().getResources()
                    .openRawResource(NetworkConfig.getInstance().getCertificateRes());
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = String.valueOf(System.currentTimeMillis());
            keyStore.setCertificateEntry(certificateAlias,
                    certificateFactory.generateCertificate(certificateInputString));
            certificateInputString.close();
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager getX509TrustManager(TrustManagerFactory trustManagerFactory) {
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            return null;
        }
        return (X509TrustManager) trustManagers[0];
    }

}
