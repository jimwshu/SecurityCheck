package security.zw.com.securitycheck.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.utils.systembartint.SystemBarTintManager;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.KITKAT;

public class WindowUtils {
	
	public static int getScreenWidth() {
        return SecurityApplication.getInstance().getAppContext().getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int getScreenHeight() {
	    return SecurityApplication.getInstance().getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

	//Android 4.2以后算上虚拟导航栏的高度
	public static int getScreenExactHeight() {
		if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
			Context context = SecurityApplication.getInstance().getAppContext();
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			windowMgr.getDefaultDisplay().getRealMetrics(dm);
			return dm.heightPixels;
		} else {
			return SecurityApplication.getInstance().getAppContext().getResources().getDisplayMetrics().heightPixels;
		}
	}

	public static int getScreenExactWidth() {
		if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
			Context context = SecurityApplication.getInstance().getAppContext();
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			windowMgr.getDefaultDisplay().getRealMetrics(dm);
			return dm.widthPixels;
		} else {
			return SecurityApplication.getInstance().getAppContext().getResources().getDisplayMetrics().widthPixels;
		}
	}
	public static int dp2Px(int dp) {
        Resources res = SecurityApplication.getInstance().getAppContext().getResources();
        return Math.round(res.getDisplayMetrics().density * dp);
    }

	public static int getStatusBarHeight() {
		int result = 0;
		Resources res = SecurityApplication.getInstance().getAppContext().getResources();
		int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = res.getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static int getNavigationBarHeight() {
		Resources res = SecurityApplication.getInstance().getAppContext().getResources();
		int resId = res.getIdentifier("navigation_bar_height", "dimen", "android");
		if(resId > 0) {
			int navigationHeight = res.getDimensionPixelSize(resId);
			if (navigationHeight > 0) {
				return navigationHeight;
			}
		}
		return 0;
	}

	public static void setTransparentNavigationBar(Activity activity, boolean keybordHide) {
		if (keybordHide) {
			setTransparentNavigationBar(activity);
		} else {
			setNonTransparentNavigationBar(activity);
		}
	}

	public static void setTransparentNavigationBar(Activity activity) {
		if (activity != null) {
			Window window = activity.getWindow();
			if (window != null) {
                addSupportTransparentNavigationBar( window);
            }
		}
	}

	/**
	 * 隐藏虚拟导航栏
	 * @param window
	 */
	public static void hideNavigationBar(Window window) {
		int flag = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				flag |= View.SYSTEM_UI_FLAG_IMMERSIVE;
			}
		}
		if (flag != 0) {
			window.getDecorView().setSystemUiVisibility(flag);
		}
	}

	public static void addSupportTransparentNavigationBar(Window window) {
		if (window != null) {
			if (isSupportForTransparentStatusBar()) {
				if (!isXiaoMiMix()) {
					setTransparentNavigationBar(window);
				} else {
					setBlackTranslucentNavigationBar(window);
				}
			}
		}
	}

	public static void setNonTransparentNavigationBar(Activity activity) {
		if (activity != null) {
			Window window = activity.getWindow();
			if (isSupportForTransparentStatusBar()) {
//				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				if (!isXiaoMiMix()) {
					setBlackNavigationBar(activity, window);
				} else {
					setWhiteNavigationBar(activity, window);
				}
			}
		}
	}

	/**
	 * @return 是否支持透明状态栏
	 */
	public static boolean isSupportForTransparentStatusBar() {
		return Build.VERSION.SDK_INT >= KITKAT;
	}

	/**
	 * @return 是否支持透明状态栏
	 */
	public static boolean isSupportForSetNavigationColor() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	public static boolean isXiaoMiMix() {
		return "MIX".equals(DeviceUtils.getDeviceModel());
	}

	public static void setBlackNavigationBar(Activity activity, Window window) {
		setNavigationBarColor(window, activity.getResources().getColor(R.color.black));
	}

	public static void setWhiteNavigationBar(Activity activity, Window window) {
		setNavigationBarColor(window, activity.getResources().getColor(R.color.white));
	}

	public static void setBlackTranslucentNavigationBar(Window window) {
		setNavigationBarColor(window,SecurityApplication.getInstance().getAppContext().getResources().getColor(R.color.black_30_percent_transparent));
	}

	public static void setTransparentNavigationBar(Window window) {
		setNavigationBarColor(window, SecurityApplication.getInstance().getAppContext().getResources().getColor(R.color.transparent));
	}

    /**设置普通模式的状态栏
     * @param window
     */
    public static void setStatusBarColor(Activity activity, Window window, int color, boolean isNotBlurActivity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNotBlurActivity) {
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        } else if (isSupportForTransparentStatusBar()){
            window.addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

    public static void setStatusBarColor(Activity activity, Window window, int color) {
        setStatusBarColor(activity, window, color, true);
    }


    /**
     * 设置虚拟导航栏颜色
     * @param window
     */
    public static void setNavigationBarColor(Window window, int color, boolean isNotBlurActivity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNotBlurActivity) {
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setNavigationBarColor(color);
        }
    }

    public static void fullscreen(Window window) {
        int flag = 0;
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                flag |= View.SYSTEM_UI_FLAG_IMMERSIVE;
            }
        }
        if (flag != 0) {
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 设置虚拟导航栏颜色
     * @param window
     */
    public static void setNavigationBarColor(Window window, int color) {
        setNavigationBarColor(window, color, true);
    }

	// 将px值转换为sp值
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	// 将sp值转换为px值
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	// 将px值转换为dip或dp值
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
