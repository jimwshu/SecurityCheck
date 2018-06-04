package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/23.
 */

public class IlegallItem extends JSONAble {
    public int id;//类型
    public String name;
    public String content;
    public int type;//1.违法，2惩罚
}
