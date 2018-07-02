package security.zw.com.securitycheck.postbean;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class PostCheckPersonBean extends JSONAble {

    public int projectId;
    public int checkItemId;
    public int creator;
    public int worker;
    public int type;

}
