package security.zw.com.securitycheck.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import security.zw.com.securitycheck.base.BaseActivity;


public class KeyBoardUtils {

    private boolean mKeyboardHide = true;
    private int mNavigationHeight;
    private int mPreviousKeyboardHeight;
    private int mNavigationHideHeight;

    private OnKeyboardHiddenChangedListener mListener;

    public static void hideSoftInput(final Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        final View currentFocusView = ((Activity) context).getCurrentFocus();
        if (currentFocusView != null) {
            final IBinder windowToken = currentFocusView.getWindowToken();
            if (inputMethodManager != null && windowToken != null) {
                try {
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void toggleSoftInput(final Context context) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftInput(final Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        final View currentFocusView = ((Activity) context).getCurrentFocus();
        if (inputMethodManager != null && currentFocusView != null)
            try {
                inputMethodManager.showSoftInput(currentFocusView, 0);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
    }

    public void setOnKeyboadHiddenChangedListener(final BaseActivity activity, OnKeyboardHiddenChangedListener listener) {
        mListener = listener;
        if (activity != null && listener != null && DeviceUtils.getSystemSDKInt() > 18) {
            mListener.attach(this);
            mNavigationHeight = WindowUtils.getNavigationBarHeight();
            // API 18 Android 4.3 adjustResize的和adjustPan一样导致计算键盘高度和是否收起或展开不正确

            final View decorView = activity.getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    // 在其他页面弹出键盘会影响布局
                    if (!activity.isOnResume || activity.isFinishing()) return;

                    Rect rect = new Rect();
                    decorView.getWindowVisibleDisplayFrame(rect);
                    int displayHeight = rect.bottom;
                    int height = decorView.getHeight();
                    int keyboardHeight = Math.abs(height - displayHeight);

                    if (mPreviousKeyboardHeight != keyboardHeight) {
                        final boolean hide = (double) (displayHeight - rect.top) / height > 0.8; //表示是否弹出了软键盘
                        // 部分手机上虚拟按键的高度计算
                        if (hide) {  //如果没有弹出软键盘，多出来的高度就是虚拟按键的高度
                            mNavigationHideHeight = keyboardHeight;
                        } else { //如果弹出了软键盘，需要判断是否弹出或隐藏了虚拟按键
                            if (isVirutalNavigationChanged(mPreviousKeyboardHeight - keyboardHeight)) {
                                mNavigationHideHeight = keyboardHeight < mPreviousKeyboardHeight ? 0 : mNavigationHeight;
                            }
                        }

                        mListener.onKeyboardHiddenChanged(keyboardHeight, hide);
                        mPreviousKeyboardHeight = keyboardHeight;
                    }
                }
            });
        }
    }

    public boolean isVirutalNavigationChanged(int difHeight) {
        return Math.abs(difHeight) == mNavigationHeight;
    }

    public int getNavigationHideHeight() {
        return mNavigationHideHeight;
    }

    public int getPreviousKeyboardHeight() {
        return mPreviousKeyboardHeight;
    }

    public boolean isKeyboardHide() {
        return mKeyboardHide;
    }

    public static KeyBoardUtils build() {
        return new KeyBoardUtils();
    }

    public abstract static class OnKeyboardHiddenChangedListener {
        private KeyBoardUtils mKeyBoardUtils;

        public void onKeyboardHiddenChanged(int keyboardHeight, boolean hide) {
            if (isVirutalNavigationChanged(keyboardHeight - getPreviousKeyboardHeight())) {
                onNavigationBarChanged(keyboardHeight);
            } else {
                onSoftKeyboardHiddenChanged(keyboardHeight, hide);
            }
        }

        public abstract void onNavigationBarChanged(int keyboardHeight);

        public abstract void onSoftKeyboardHiddenChanged(int keyboardHeight, boolean hide);

        public void attach(KeyBoardUtils utils) {
            mKeyBoardUtils = utils;
        }

        public boolean isVirutalNavigationChanged(int difHeight) {
            return mKeyBoardUtils.isVirutalNavigationChanged(difHeight);
        }

        public int getNavigationHideHeight() {
            return mKeyBoardUtils.getNavigationHideHeight();
        }

        public int getPreviousKeyboardHeight() {
            return mKeyBoardUtils.getPreviousKeyboardHeight();
        }

        public boolean isKeyboardHide() {
            return mKeyBoardUtils.isKeyboardHide();
        }
    }
}
