package security.zw.com.securitycheck.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.postbean.LoginBean;
import security.zw.com.securitycheck.postbean.MyProjectBean;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.net.NetworkCallback;

/**
 * Created by admin on 16/3/1.
 *
 *  登陆逻辑
 */
public class MyProjectModel implements BaseModel {


    private Context mContext;
    private Call<String> mCall;
    private boolean isLogin = false;

    public MyProjectModel() {
        mContext = SecurityApplication.mContext;
    }


    public void getList(int type, int page, String name, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetMyProjectList getSmsService = mRetrofit.create(Constans.GetMyProjectList.class);


            Gson gson = new Gson();
            MyProjectBean loginBean = new MyProjectBean();
            loginBean.userId = SecurityApplication.mUser.id;
            loginBean.type = type;
            loginBean.page = page;
            loginBean.size = 10;
            loginBean.name = name;
            String s = gson.toJson(loginBean);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);


            mCall = getSmsService.getList(requestBody);
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

    public void getProjectById(int id, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetMyProjectList getSmsService = mRetrofit.create(Constans.GetMyProjectList.class);


            mCall = getSmsService.getProjectById(id);
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


    public void getMyCheckProjectList(int page, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetMyProjectList getSmsService = mRetrofit.create(Constans.GetMyProjectList.class);


            Gson gson = new Gson();
            MyProjectBean loginBean = new MyProjectBean();
            loginBean.userId = SecurityApplication.mUser.id;
            loginBean.page = page;
            loginBean.size = 10;
            String s = gson.toJson(loginBean);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);


            mCall = getSmsService.getMyCheckProjectList(requestBody);
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


    public void getMyCheckProjectDetailList(int projectId, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetMyProjectList getSmsService = mRetrofit.create(Constans.GetMyProjectList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("projectId", projectId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            String s =jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);


            mCall = getSmsService.getEvaluationCheckProjectList(requestBody);
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
