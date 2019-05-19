package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情 && 立案详情 && 执法详情
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


    public String administrativeLitigationFile; // 行政诉讼情况
    public String administrativeReviewFile; // 行政复议情况

    public String buildUnit;// 立案单位
    public String caseDescription; // 立案描述
    public String constructUnit; // 施工单位

    public String punishmentBase;//处罚依据
    public String relateFile; //相关附件

    public String result;//处理结果
    public String source;//案源
    public String unitAddress;//单位地址
    public String unitLeader;//单位负责人
    public String unitLeaderPhone;//单位负责人电话
    public String unitLeaderPosition;//单位负责人职务
    public String unitPhone;//单位电话

    public int userId;//单位负责人id

}
