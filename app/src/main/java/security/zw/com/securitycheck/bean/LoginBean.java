package security.zw.com.securitycheck.bean;
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



    public static LoginBean parseUserInfoFromString(String u) {
        try {
            LoginBean userInfo = new LoginBean();
            JSONObject jsonObject = new JSONObject(u);
            userInfo.parseFromJSONObject(jsonObject);
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public String toString() {
        return encodeToJsonObject().toString();
    }

}
