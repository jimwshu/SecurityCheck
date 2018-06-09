package security.zw.com.securitycheck;

import com.google.gson.Gson;

import android.text.TextUtils;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.bean.IlegallItem;
import security.zw.com.securitycheck.postbean.BasicBean;
import security.zw.com.securitycheck.postbean.LoginBean;
import security.zw.com.securitycheck.utils.PreferenceUtils;
import security.zw.com.securitycheck.utils.net.NetRequest;

/**
 * Created by wangshu on 17/5/17.
 * - 配置信息管理
 * - 逻辑，一打开APP会去读取本地的，优先SP里的，如果SP里没有，去default_cfg读取
 * - 同时去拉取服务器的数据，拉取到了，替换本地数据，展示服务器的数据
 */

public class ConfigManager {


    /**
     * 请求字符编码
     */
    private static final String CHARSET = "utf-8";

    private static ConfigManager instance = null;

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private ConfigManager() {
    }

    public static ArrayList<IlegallItem> ilegallItems = new ArrayList<>();
    public static String[] ilegallItemsStr;

    public void parseBasicStr() {
        getBasic();
    }


    public void parseBasicStr (String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            if (ilegallItems.size() > 0) {
                ilegallItems.clear();
            }
            if (ilegallItemsStr != null) {
                ilegallItemsStr = null;
            }
            if (jsonArray.length() > 0) {
                ilegallItemsStr = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    IlegallItem ilegallItem = SecurityApplication.getGson().fromJson(jsonArray.optJSONObject(i).toString(), IlegallItem.class);
                    ilegallItems.add(ilegallItem);
                    ilegallItemsStr[i] = ( (i+1) + ":"  + ilegallItem.content);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static final String SP_BASIC = "_SP_BASIC_";
    Retrofit mRetrofit;
    Constans.CheckBasic checkBasic;
    Call<String> mCall;
    private boolean get_code = false;
    public void getBasic() {
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        checkBasic = mRetrofit.create(Constans.CheckBasic.class);

        Gson gson = new Gson();
        BasicBean basicBean = new BasicBean();
        basicBean.type = 1;
        basicBean.userId = SecurityApplication.mUser.id;


        String s = gson.toJson(basicBean);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);
        mCall = checkBasic.getBasic(requestBody);
        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                get_code = false;
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                parseBasicStr(jsonObject.toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
            }
        });
    }

    public interface BasicListenner {
        void getBasicListenner();
    }
    public void getBasic(final BasicListenner listenner) {
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        checkBasic = mRetrofit.create(Constans.CheckBasic.class);

        Gson gson = new Gson();
        BasicBean basicBean = new BasicBean();
        basicBean.type = 1;
        basicBean.userId = SecurityApplication.mUser.id;

        String s = gson.toJson(basicBean);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);
        mCall = checkBasic.getBasic(requestBody);
        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                get_code = false;
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                parseBasicStr(jsonObject.toString());
                                listenner.getBasicListenner();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
            }
        });
    }


}
