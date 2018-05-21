package security.zw.com.securitycheck.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.model.LoginModel;
import security.zw.com.securitycheck.utils.net.NetworkCallback;
import security.zw.com.securitycheck.view.LoginView;


/**
 * Created by wangshu on 17/8/24.
 */

public class LoginPresenter implements BasePresenter{
    public LoginView loginView;
    public LoginModel loginModel;

    public LoginPresenter() {
    }

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }


    public void login(String phone, String code) {
        loginModel.login(phone, code, new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                loginView.loginFailed(code, msg);
            }

            @Override
            public void onRealSuccess(String date) {
                super.onRealSuccess(date);
                loginView.loginSucc();
            }

        });
    }



    @Override
    public void onDestroy() {
        if (loginView != null) {
            loginView = null;
        }
    }
}
