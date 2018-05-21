package security.zw.com.securitycheck.model;

import com.google.gson.Gson;

import android.content.Context;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.LoginBean;
import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.net.NetworkCallback;

/**
 * Created by admin on 16/3/1.
 *
 *  登陆逻辑
 */
public class LoginModel implements BaseModel {


    private Context mContext;
    private Call<String> mCall;
    private boolean isLogin = false;

    public LoginModel() {
        mContext = SecurityApplication.mContext;
    }


    public void login(String phone, String code, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetSmsService getSmsService = mRetrofit.create(Constans.GetSmsService.class);

            Gson gson = new Gson();
            LoginBean loginBean = new LoginBean();
            loginBean.phone = phone;
            loginBean.password = code;

            String s = gson.toJson(loginBean);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);

            mCall = getSmsService.login(requestBody);
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    isLogin = false;
                    int code = response.code();
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSuccess(response.body().toString());
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailed(code, response.message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    isLogin = false;
                    t.printStackTrace();
                    if (callback != null) {
                        callback.onFailed(t);
                    }
                }
            });
        }
    }
    

}
