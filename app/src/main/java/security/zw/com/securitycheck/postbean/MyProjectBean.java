package security.zw.com.securitycheck.postbean;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class MyProjectBean extends JSONAble {

    public int type;
    public int userId;
    public int page;
    public int size;
    public String name;
    public MyProjectBean() {
    }


}
