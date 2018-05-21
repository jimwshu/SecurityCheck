package security.zw.com.securitycheck.utils.toast;


import security.zw.com.securitycheck.SecurityApplication;

/**
 * Created by power on 14-2-13.
 */
public class ToastUtil {

    public static void Long(String s){
        ToastManager.show(SecurityApplication.getInstance().getAppContext(), s, AbstractToast.LENGTH_LONG, true, true);
    }

    public static void Long(int sid){
        String s = SecurityApplication.getInstance().getAppContext().getString(sid);
        Long(s);
    }

    public static void Short(String s){
        ToastManager.show(SecurityApplication.getInstance().getAppContext(), s, AbstractToast.LENGTH_SHORT, true, true);
    }

    public static void Short(int sid){
        String s = SecurityApplication.getInstance().getAppContext().getString(sid);
        Short(s);
    }

}
