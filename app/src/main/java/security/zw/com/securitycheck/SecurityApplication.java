package security.zw.com.securitycheck;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.utils.DeviceUtils;
import security.zw.com.securitycheck.utils.PreferenceUtils;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.image.ImagePipelineConfigUtils;


/**
 * Created by wangshu on 17/5/17.
 */

public class SecurityApplication extends Application {

    public static final String APP_ID = "", APP_KEY = "", TAG = "SECURITY";

    public static Context mContext;
    // 当前活动用户
    public static UserInfo mUser;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private static SecurityApplication instance;

    // 单例模式中获取唯一的MyApplication实例
    public static SecurityApplication getInstance() {
        if (null == instance) {
            instance = new SecurityApplication();
        }
        return instance;
    }

    public Context getAppContext() {
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this.getApplicationContext();
        if (shouldInit()) {
            init();
        }
    }

    /**
     * 从本地加载用户信息
     */
    public void loadUserInfoFromLocalPreference() {
        String localUserData = PreferenceUtils.instance().getString("loginUser", "");
        if (!TextUtils.isEmpty(localUserData)) {
            mUser = UserInfo.parseUserInfoFromString(localUserData);
        }/* else {
            mUser = new UserInfo();
            mUser.id = 1;
            mUser.name = "sb";
            mUser.type = 1;
        }*/
    }

    public void updateUserInfo(UserInfo userInfo) {

    }

    public void saveUserInfo() {
        PreferenceUtils.instance().putString("loginUser", mUser.toString());
    }

    public void removeUserInfo() {
        //SPHelper.remove("loginUser");
        PreferenceUtils.instance().removeKey("loginUser");
    }

    public void init() {
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        loadUserInfoFromLocalPreference();
        // 初始化bugly，设置渠道
        String channel = DeviceUtils.getChannel();
        boolean isDebug = BuildConfig.DEBUG || "dev".equals(channel);

        Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));
        if (isDebug) {
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }
        ConfigManager.getInstance().parseBasicStr();

        MiPushClient.registerPush(this, APP_ID, APP_KEY);

        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if (processInfos != null) {
            String mainProcessName = mContext.getPackageName();
            int myPid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        FrescoImageloader.clearAllMemoryCaches();
    }

    private static Gson mGson;

    public static Gson getGson() {
        if (mGson == null) {
            ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return clazz == Field.class || clazz == Method.class;
                }
            };
            mGson = new GsonBuilder()
                    .addSerializationExclusionStrategy(exclusionStrategy)
                    .addDeserializationExclusionStrategy(exclusionStrategy)
                    .create();
        }
        return mGson;
    }

    public static <T> T fromJson(String json, Class<T> type) {
        T t = null;
        try {
            t = getGson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
