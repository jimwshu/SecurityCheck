package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情
 */

public class EquipmentDetail extends JSONAble {

    public String demand;//要求
    public String docDir;//目录
    public String docUrl;//图片Url
    public int num;
    public boolean pass;
}
