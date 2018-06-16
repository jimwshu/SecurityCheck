package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情
 */

public class SupervisionProjecDetail extends JSONAble {

    public int id;

    public String projectName;

    public long createTime;

    public long reCheckTime;

    public String ilegalContent;

    public String ilegalBasis;

    public String code;

    public int status;//状态 0 整改中 1整改合格

    public int supervise;

    public String ilegalCompany;

    public String images;

}
