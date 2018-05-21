package security.zw.com.securitycheck.utils.net;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.utils.DeviceUtils;


public class BaseIntecepter {


    private static BaseIntecepter instance_;
    private Context mContext;

    private AddHeaderInterceptor mInteceptor;


    public BaseIntecepter(Context context) {
        this.mContext = context;
        this.mInteceptor = new AddHeaderInterceptor();
    }

    public synchronized static BaseIntecepter getInstance() {
        Context applicationContext = SecurityApplication.getInstance().mContext;
        if (instance_ == null || instance_.mContext != applicationContext) {
            instance_ = new BaseIntecepter(applicationContext);
        }
        return instance_;
    }

    public AddHeaderInterceptor getmInteceptor() {
        return mInteceptor;
    }

    public class AddHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request =  chain.request();

            if (SecurityApplication.mUser != null) {
                request = request.newBuilder()
                        .addHeader("chn", DeviceUtils.getChannel())
                        .addHeader("ver", DeviceUtils.getAppVersion())
                        .addHeader("verCode", DeviceUtils.getAPPVersionCode() + "")
                        .addHeader("Sys", "android" + "_" + DeviceUtils.getSystemVersion())
                        .addHeader("mod", DeviceUtils.getDeviceModel())
                        .addHeader("app_name", "security")
                        .addHeader("id", SecurityApplication.mUser.id + "")
                        .build();
            } else {
                request = request.newBuilder()
                        .addHeader("chn",  DeviceUtils.getChannel())
                        .addHeader("ver", DeviceUtils.getAppVersion())
                        .addHeader("verCode", DeviceUtils.getAPPVersionCode() + "")
                        .addHeader("Sys", "android" + "_" + DeviceUtils.getSystemVersion())
                        .addHeader("mod", DeviceUtils.getDeviceModel())
                        .addHeader("app_name", "security")
                        .build();
            }

            return chain.proceed(request);
        }
    }


}
