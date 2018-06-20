package security.zw.com.securitycheck.bean;
import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class StopInfo extends JSONAble {

    public int id;
    public int projectId;
    public String projectName;
    public String stopReason;
    public int normalStop;//是否督办 0：未督办 1督办

    public String stopTime;
    public String applyTime;
    public String approveTime;
    public String safeInspector;

    public StopInfo() {
    }


}
