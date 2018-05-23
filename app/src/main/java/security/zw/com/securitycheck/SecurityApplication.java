package security.zw.com.securitycheck;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;

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
        loadUserInfoFromLocalPreference();
        // 初始化bugly，设置渠道
        String channel = DeviceUtils.getChannel();
        boolean isDebug = BuildConfig.DEBUG || "dev".equals(channel);

        Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));
        if (isDebug) {
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        }

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
}
