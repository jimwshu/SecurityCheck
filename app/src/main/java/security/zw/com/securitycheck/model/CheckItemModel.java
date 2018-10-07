package security.zw.com.securitycheck.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.postbean.CheckItemDetailBean;
import security.zw.com.securitycheck.postbean.MyProjectBean;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.net.NetworkCallback;

/**
 * Created by admin on 16/3/1.
 *
 *  登陆逻辑
 */
public class CheckItemModel implements BaseModel {


    private Context mContext;
    private Call<String> mCall;
    private boolean isLogin = false;

    public CheckItemModel() {
        mContext = SecurityApplication.mContext;
    }


    public void getList(int projectId,int check_type, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("projectId", projectId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            jsonObject.addProperty("checkMode", 1);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
            Constans.GetCheckItemList getCheckItemList = mRetrofit.create(Constans.GetCheckItemList.class);

            if (check_type == 3) {
                mCall = getCheckItemList.getCheckItemListForType3(requestBody);
            } else if (check_type == 4) {

                mCall = getCheckItemList.getCheckItemListForType4(requestBody);
            } else {
                mCall = getCheckItemList.getCheckItemList(requestBody);
            }

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


    public void getCheckItemDetail(int projectId, int checkItemId, int checkmode, int check_type, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetCheckItemList getCheckItemList = mRetrofit.create(Constans.GetCheckItemList.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("projectId", projectId);
            jsonObject.addProperty("checkItemId", checkItemId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);


            if (check_type == 3) {
                if (checkmode > 0) {
                    jsonObject.addProperty("checkMode", checkmode);
                }
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
                mCall = getCheckItemList.getCheckItemDetailForType3(requestBody);
            } else {
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
                mCall = getCheckItemList.getCheckItemDetail(requestBody);
            }

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


    public void getFilter(int projectId,int check_type, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetCheckItemList getCheckItemList = mRetrofit.create(Constans.GetCheckItemList.class);


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("projectId", projectId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);

            if (check_type == 3) {
                jsonObject.addProperty("checkMode",2);
                String s = jsonObject.toString();
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);
                mCall = getCheckItemList.getFilterForType3(requestBody);
            } else {
                String s = jsonObject.toString();
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);
                mCall = getCheckItemList.getFilter(requestBody);
            }
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

    public void getCheckScoreList(int projectId, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetCheckItemList getCheckItemList = mRetrofit.create(Constans.GetCheckItemList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", projectId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);

            mCall = getCheckItemList.getCheckScoreList(requestBody);
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


    public void getCheckScoreItemDetail(int projectId, int checkItemId, final NetworkCallback callback){

        if (!isLogin) {
            isLogin = true;
            Retrofit mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            Constans.GetCheckItemList getCheckItemList = mRetrofit.create(Constans.GetCheckItemList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", projectId);
            jsonObject.addProperty("checkItemId", checkItemId);
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);

            mCall = getCheckItemList.getCheckScoreItemDetail(requestBody);
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
