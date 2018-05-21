package security.zw.com.securitycheck.utils.net;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.base.BaseActivity;
import security.zw.com.securitycheck.bean.UserInfo;

/**
 * Created by luohong on 16/5/9.
 */
public class NetworkCallback {
    public void onPreExecute() {

    }

    public Map<String, String> getHeaders(){
        return null;
    }

    public Map<String, String> getParams() {
        return null;
    }

    public void onSuccess(String date) {
        try {
            JSONObject jsonObject = new JSONObject(date);
            if (jsonObject.has("code")) {
                int code = jsonObject.optInt("code");
                if (code == 0) {
                    onRealSuccess(date);
                } else{
                    onFailed(code, jsonObject.optString("msg"));
                }
            } else {
                onFailed(-1, "数据错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onFailed(-1, "出错了");
        }
    }

    public void onFinished() {

    }

    public void onRealSuccess(String data) {

    }

    public void onFailed(int code, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "出错了";
        }

        onRealFailed(code, msg);
    }

    public void onFailed(Object t) {


        try {
            if (!NetworkUtils.getInstance().isNetworkAvailable()) {
                onFailed(-1, "网络无法连接，请检查后再试");
             } else {
                if (t == null) {
                    onFailed(-1, "出错了");
                } else if (t instanceof ConnectException) {
                    onFailed(-1, "网络无法连接，请检查后再试");
                } else if (t instanceof UnknownHostException) {
                    onFailed(-1, "网络无法连接，请检查后再试");
                } else if (t instanceof SocketTimeoutException){
                    onFailed(-1, "连接超时，请检查后再试");
                } else if (t instanceof Throwable) {
                    Throwable throwable = (Throwable) t;
                    if (TextUtils.isEmpty(throwable.getMessage())) {
                        onFailed(-1, throwable.getMessage());
                    } else {
                        onFailed(-1, "出错了");
                    }

                } else {
                    onFailed(-1, "出错了");
                }
            }
        } catch (Exception e) {
            onFailed(-1, "出错了");
            e.printStackTrace();
        }

    }

    public void onRealFailed(int code, String msg) {

    }
}
