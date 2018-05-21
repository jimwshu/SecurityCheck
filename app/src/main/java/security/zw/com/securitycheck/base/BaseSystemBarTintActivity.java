package security.zw.com.securitycheck.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.WindowUtils;


public class BaseSystemBarTintActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setBackgroundDrawable(null);


        //设置状态栏
        if (isNeedHideStatusBar()) {  //隐藏状态栏
            hideStatusBar(window);
        } else if (isNeedImmersiveStatusBar()) {  //沉浸式状态栏
            setImmersiveStatusBar(window);
        } //默认普通模式，不特别设置状态栏颜色

        //设置虚拟导航栏
        if (isNeedHideNavigationBar()) {  //隐藏虚拟导航栏
            hideNavigationBar(window);
        } else if (isNeedImmersiveNavigationBar()) {  //沉浸式虚拟导航栏
            setImmersiveNavigationBar(window);
        } else { //默认普通模式，设置虚拟导航栏颜色
            WindowUtils.setNavigationBarColor(window, getResources().getColor(getNavigationBarColor()));
        }
    }


    /**
     * 设置沉浸式状态栏
     */
    protected void setImmersiveStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiFlags);
        }
        WindowUtils.setStatusBarColor(this, window, getResources().getColor(getImmersiveStatusBarColor()), isNotBlurActivity());
    }

    /**
     * 原生5.0以上系统；评论和分享页状态栏表现太奇怪，暂时特殊处理
     */
    protected boolean isNotBlurActivity() {
        return true;
    }


    /*
     * 是否设置沉浸式状态栏
     */
    protected boolean isNeedImmersiveStatusBar() {
        return false;
    }

    /*
    是否需要隐藏状态栏
     */
    protected boolean isNeedHideStatusBar() {
        return false;
    }

    /**
     * 是否需要设置沉浸式虚拟导航栏
     * @return
     */
    protected boolean isNeedImmersiveNavigationBar() {
        return false;
    }

    /**
     * 设置沉浸式导航栏
     */
    protected void setImmersiveNavigationBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(uiFlags);
        }
        WindowUtils.setNavigationBarColor(window, getResources().getColor(getImmersiveNavigationBarColor()), isNotBlurActivity());
    }

    /*
    是否需要隐藏虚拟导航栏
     */
    protected boolean isNeedHideNavigationBar() {
        return false;
    }

    /**
     * 隐藏状态栏
     * @param window
     */
    protected void hideStatusBar(Window window) {
        int flag = 0;
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flag = View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (flag != 0) {
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 隐藏虚拟导航栏
     * @param window
     */
    protected void hideNavigationBar(Window window) {
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

    //设置沉浸式虚拟导航栏颜色
    protected int getImmersiveNavigationBarColor() {
        if (WindowUtils.isXiaoMiMix()) {
            return R.color.black_30_percent_transparent;
        }
        else {
            return R.color.transparent;
        }
    }

    protected int getNavigationBarColor() {
        return R.color.black;
    }

    //设置沉浸式状态栏颜色
    protected int getImmersiveStatusBarColor() {
        return R.color.transparent;
    }

        /**
     * 控制SystemBar是否占位
     * @return true占位并可以通过@getStatusBarColor设置颜色，false不占位。默认占位
     */
    protected boolean isNeedSystemBarTintEnable() {
        return true;
    }
}
