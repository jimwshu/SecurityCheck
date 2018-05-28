package security.zw.com.securitycheck.bean;
import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class ProjectInfo extends JSONAble {

    public final static int TYPE_NEED_CHANGE = 1, TYPE_NORMAL = 2, TYPE_FINISHED = 3, TYPE_UNSAFE = 4;

    public int state = 0;
    //项目状态 1整改、2正常、3竣工、4 未办开工前安全条件审查



    public int id;
    public String name;
    public String des;
    public int supervise;//是否督办 0：未督办 1督办

    public ProjectInfo() {
    }



    public static ProjectInfo parseUserInfoFromString(String u) {
        try {
            ProjectInfo userInfo = new ProjectInfo();
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
