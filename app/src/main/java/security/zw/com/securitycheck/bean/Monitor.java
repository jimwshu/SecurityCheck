package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 监理单位信息
 */

public class Monitor extends JSONAble {

    public int id;
    public String monitorCompany;//单位名称
    public String projectDirector;//单位负责人
    public int projectDirectorId;
 
}
