package security.zw.com.securitycheck.receive;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.PreferenceUtils;

public class PushProcessor {

    public static final String PUSH = "push";
    public static final String PUSH_TOKEN = "push_token";
    public static final String PUSH_BIND = "push_bind";


    public static void toBindPush() {
        final String pushToken = PreferenceUtils.instance().getString(PUSH_TOKEN, "");
        toBindPush(pushToken);
    }

    public static void toBindPush(final String pushToken) {
        toBindPush(pushToken, false);
    }

    public static void toBindPush(final boolean isPushUserBind) {
        final String pushToken = PreferenceUtils.instance().getString(PUSH_TOKEN, "");
        toBindPush(pushToken, isPushUserBind);
    }

    public static void toBindPush(final String pushToken, final boolean isPushUserBind) {
        if (TextUtils.isEmpty(pushToken)) {
            LogUtils.e(PUSH, "push token is null");
            return;
        }

        String url = "";


    }

    public static void onBindSuccess(String pushToken, boolean isPushUserBind) {
        PreferenceUtils.instance().putString(PUSH_BIND, pushToken);
    }


    public static void processPush(Context context, String msg, String extra) {

    }

    public static void processPushClicked(Context context, String msg) {

    }




    private static String getNotificationTitle(Context context, JSONObject extraJson) {
        String title = null;
        if (extraJson != null) {
            title = extraJson.optString("title");
        }
        if (TextUtils.isEmpty(title) || "null".equalsIgnoreCase(title)) {
            title = context.getResources().getString(R.string.app_name);
        }
        return title;
    }
}
