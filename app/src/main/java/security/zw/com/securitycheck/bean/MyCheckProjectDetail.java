package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情
 */

public class MyCheckProjectDetail extends JSONAble {

    public int id;

    public int projectId;

    public String projectName;

    public double score;

    public int rank;

    public long createTime;
}
