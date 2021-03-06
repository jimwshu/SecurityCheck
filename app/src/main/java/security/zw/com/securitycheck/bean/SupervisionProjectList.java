package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情
 */

public class SupervisionProjectList extends JSONAble {
    public int statusTypeType = 0;

    public int id;

    public String projectName;

    public long createTime;

    public String operator;

    public String code;

    public int status = -1;//状态 0 整改中 1整改合格  （1）	（整改中、整改合格、移交执法）；

    public String checkItemName;

    public int checkType;
    public int checkMode;

    public String personLiable;

    public int rank;

    public double score;

    public String ilegalContent;
}
