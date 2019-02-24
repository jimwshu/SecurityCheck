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


    public String manufactureLicense = "";
    public String lastCheckTime = "";
    public String specificationModel = "";
    public String recordTime = "";
    public String lastValidTime = "";
    public String manufacturingNum= "";
    public String equipmentName= "";
    public String recordUnit= "";
    public String outManufactureTime= "";
    public String recordNum= "";
    public String recordValidTime= "";
    public String manufacturer= "";


}
