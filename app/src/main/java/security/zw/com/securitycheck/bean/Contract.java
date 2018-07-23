package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 建筑单位
 */

public class Contract extends JSONAble {

    public int id;
    public String contractCompany;//单位名称
    public String contractLeader;//单位负责人
    public int contractLeaderId;

}
