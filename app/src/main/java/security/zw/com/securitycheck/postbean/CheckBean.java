package security.zw.com.securitycheck.postbean;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class CheckBean extends JSONAble {

    public int projectId;
    public int checkType;
    public int checkMode;
    public int result;
    public String image;
    public String ilegalItems;
    public String baseItemrs;
    public String reCheckTime;
    public String respon;
    public String ids;
}
