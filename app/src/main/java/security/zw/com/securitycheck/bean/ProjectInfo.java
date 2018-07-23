package security.zw.com.securitycheck.bean;
import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class ProjectInfo extends JSONAble {

    public final static int TYPE_NEED_CHANGE = 1, TYPE_NORMAL = 2, TYPE_FINISHED = 3, TYPE_UNSAFE = 4;

    public final static int TYPE_REMIND = 1, TYPE_UNREMIND = 0;

    public final static int TYPE_STOP = 1, TYPE_START = 0;

    public int state = 0;
    //项目状态 1整改、2正常、3竣工、4 未办开工前安全条件审查



    public int id;
    public String name;
    public String des;
    public int supervise;//是否督办 0：未督办 1督办

    public int recordControl;
    public int workState;

    public ProjectInfo() {
    }


    @Override
    public String toString() {
        return encodeToJsonObject().toString();
    }

}
