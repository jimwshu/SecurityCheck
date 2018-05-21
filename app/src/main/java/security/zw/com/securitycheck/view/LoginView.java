package security.zw.com.securitycheck.view;

/**
 * Created by wangshu on 17/8/24.
 */

public interface LoginView {

    void loginSucc();

    void loginFailed(int code, String error);

}
