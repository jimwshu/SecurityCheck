package security.zw.com.securitycheck.bean;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 项目详情
 */

public class ProjectDetail extends JSONAble {

    public int id;

    public String safetyInspector;
    public String structure;//
    public String area;//面积
    public String cost;//造价
    public String state;
    public String name;
    public String district;
    public String address;
    public String imageProgress;


    public Construction construction;
    public Contract contract;
    public Monitor monitor;
    public Location location;

    public int supervise;// 是否督办

    public int check_type;//随机检查，评分检查
    public static final int CHECK_TYPE_RANDOM = 1, CHECK_TYPE_COUNT = 2;

    public int check_mode;//单人模式，多人模式
    public static final int CHECK_MODE_SINGLE = 1, CHECK_MODE_MORE = 2;

    public String ids;
}
