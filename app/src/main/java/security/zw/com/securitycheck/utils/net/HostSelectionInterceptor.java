package security.zw.com.securitycheck.utils.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * This example uses an OkHttp interceptor to change the target hostname dynamically at runtime.
 * Typically this would be used to implement client-side load balancing or to use the webserver
 * that's nearest geographically.
 */
public class HostSelectionInterceptor implements Interceptor {

    private volatile String host;

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    private volatile String scheme;

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isHttps() {
        return scheme.equals("https");
    }

    public boolean isHttp() {
        return scheme.equals("http");
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = this.host;
        String scheme = this.scheme;
        if (host != null && (isHttp() || isHttps())) {
            HttpUrl newUrl = request.url().newBuilder()
                    .host(host)
                    .scheme(scheme)
                    .build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}