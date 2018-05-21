package security.zw.com.securitycheck.utils.net;

import android.content.Context;
import android.text.TextUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.SecurityApplication;


public class NetRequest {


    private static final String TAG = NetRequest.class.getSimpleName();

    private static NetRequest instance_;
    private Context mContext;

    public Retrofit getmRetrofit() {
        return mRetrofit;
    }

    private Retrofit mRetrofit;

    public NetRequest(Context context) {
        this.mContext = context;
    }

    public synchronized static NetRequest getInstance() {
        Context applicationContext = SecurityApplication.getInstance().mContext;
        if (instance_ == null || instance_.mContext != applicationContext) {
            instance_ = new NetRequest(applicationContext);
        }
        return instance_;
    }

    /**
     * @param replaceHost 替换的host（不同的baseUrl）
     * @return
     */
    public NetRequest init(String replaceHost) {

        OkHttpClient ok = OkHttpClientSingle.getInstance();
        if (!TextUtils.isEmpty(replaceHost)) {
            HostSelectionInterceptor hostSelectionInterceptor = new HostSelectionInterceptor();
            hostSelectionInterceptor.setHost(replaceHost);
            hostSelectionInterceptor.setScheme("https");
            ok = ok.newBuilder().addInterceptor(hostSelectionInterceptor).build();
        }

       mRetrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(Constans.DOMAIN)//主机地址
                .client(ok)
                .build();

        return this;
    }

}
