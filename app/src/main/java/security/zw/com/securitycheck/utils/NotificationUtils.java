package security.zw.com.securitycheck.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yrsx on 16/8/16.
 */

public class NotificationUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    private NotificationUtils() {
    }

    /**
     * 检测"系统设置->应用->your app->显示通知"的check box 是否选中,默认为选中。
     * 是否允许弹出通知(包括:通知栏、系统Toast,这个设置是系统设置->应用->yourapp->显示通知), 只在API 大于19上有效, 默认返回true
     *
     * @param context
     * @return
     */
    public final static boolean isNotificationAllow(Context context) {

        if (context == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        Context appContext = context.getApplicationContext();
        AppOpsManager mAppOps = (AppOpsManager) appContext.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = appContext.getApplicationInfo();
        String pkg = appContext.getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
}
