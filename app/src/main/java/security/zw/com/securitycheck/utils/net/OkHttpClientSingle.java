package security.zw.com.securitycheck.utils.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by cuiyang on 15/12/22.
 */
public class OkHttpClientSingle {


    private static OkHttpClient.Builder okHttpBuilder;

    private OkHttpClientSingle() {
        okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.retryOnConnectionFailure(true);//设置出现错误是否进行重新连接。
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.addInterceptor(BaseIntecepter.getInstance().getmInteceptor());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpBuilder.addInterceptor(logging);
        /**
         * http://www.jianshu.com/p/16994e49e2f6 设置信任所有https
         */
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static OkHttpClient getInstance() {
        if (okHttpBuilder == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpBuilder == null) {
                    new OkHttpClientSingle();
                }
            }
        }
        return okHttpBuilder.build();
    }
}