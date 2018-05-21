package security.zw.com.securitycheck.utils.toast;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yrsx on 16/8/3.
 */
public class SystemToastReflection extends SystemToast {

    static final int LONG_DELAY = 3500; // 3.5 seconds
    static final int SHORT_DELAY = 2000; // 2 seconds
    private final Handler mHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            cancel();
        }
    };
    private boolean mInited = false;
    private Method mShowMethod;
    private Object mTN;

    protected SystemToastReflection() {
    }

    public SystemToastReflection(Context context) {
        mToast = new Toast(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application} or {@link
     *                 android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    public static SystemToastReflection makeText(Context context, CharSequence text, int duration) {
        SystemToastReflection systemToast = new SystemToastReflection();
        systemToast.mToast = Toast.makeText(context, text, duration);
        return systemToast;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application} or {@link
     *                 android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static SystemToastReflection makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Show with reflection
     */
    @Override
    public void show() {
        try {
            initedReflection();
            if (mTN == null) {
                return;
            }
            Field field = mTN.getClass().getDeclaredField("mNextView");
            field.setAccessible(true);
            field.set(mTN, getView());
            if (mShowMethod != null) {
                mShowMethod.invoke(mTN, (Object[]) null);
            }
            mHandler.postDelayed(mHideRunnable, mToast.getDuration() == Toast.LENGTH_SHORT ? SHORT_DELAY : LONG_DELAY);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void initedReflection() throws Throwable {
        if (mInited) {
            return;
        }
        Field field = Toast.class.getDeclaredField("mTN");
        field.setAccessible(true);
        mTN = field.get(mToast);
        mShowMethod = mTN.getClass().getDeclaredMethod("show", (Class[]) null);
        mInited = true;
    }
}
