package security.zw.com.securitycheck.postbean;
import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class LoginBean extends JSONAble {

    public String phone;
    public String password;


    public LoginBean() {
    }


}
