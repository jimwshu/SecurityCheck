package security.zw.com.securitycheck.utils.toast;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import java.util.List;

/**
 * Created by yrsx on 16/6/24.
 * <ol> Difference
 * <li>Use {@link SystemToast} will not be shown when user close notification permission in System Settings;</>
 * <li>{@link SystemToast#show()} calls NotificationManagerService#enqueueToast to show toast one by one,
 * but {@link CustomToast#show()} will not.</li>
 * </ol>
 */
public class ToastManager {
    private ToastManager() {
    }

    /**
     * Make a custom toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link AbstractToast#LENGTH_SHORT} or
     *                 {@link AbstractToast#LENGTH_LONG}
     */
    public static AbstractToast makeText(Context context, CharSequence text, int duration) {
        return makeText(context, text, duration, true);
    }

    /**
     * Make a custom toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link AbstractToast#LENGTH_SHORT} or
     *                 {@link AbstractToast#LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static AbstractToast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Make a standard or custom toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link AbstractToast#LENGTH_SHORT} or
     *                 {@link AbstractToast#LENGTH_LONG}
     * @param custom
     */
    public static AbstractToast makeText(Context context, CharSequence text, int duration, boolean custom) {
        return (custom && preV25()) ? SystemToastReflection.makeText(context, text, duration) : SystemToast.makeText(context, text, duration);
    }

    /**
     * Pre Android 7.1
     */
    private static boolean preV25() {
        return Build.VERSION.SDK_INT < 25;
    }

    /**
     * Show custom by default.
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void show(Context context, CharSequence text, int duration) {
        show(context, text, duration, true);
    }

    /**
     * Only show in foreground by default.
     *
     * @param context
     * @param text
     * @param duration
     * @param custom
     */
    public static void show(Context context, CharSequence text, int duration, boolean custom) {
        show(context, text, duration, custom, true);
    }

    /**
     * Show toast.
     *
     * @param context
     * @param text
     * @param duration
     * @param custom
     * @param onlyForeground
     */
    public static void show(Context context, CharSequence text, int duration, boolean custom, boolean onlyForeground) {
        boolean show = true;
        if (onlyForeground) {
            show = isRunningForeground(context);
        }
        if (show) {
            AbstractToast toast = makeText(context, text, duration, custom);
            toast.show();
        }
    }

    /**
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        if (context == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                ComponentName cn = tasks.get(0).topActivity;
                if (cn != null) {
                    String currentPackageName = cn.getPackageName();
                    if (context.getPackageName().equals(currentPackageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
