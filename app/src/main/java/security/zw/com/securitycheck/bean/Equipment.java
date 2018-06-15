package security.zw.com.securitycheck.bean;

import java.util.ArrayList;

/**
 * Created by wangshu on 18/6/13.
 *
 * 设备表
 */

public class Equipment {
    public int id;
    public String name;


    // 单位
    public Contract contract;


    public String format;

    // 出厂编号和厂家
    public String seriesNumber;

    public Construction construction;

    public String people;
    public String mechanic;

    public String phone;

    public String remarkTime;

    public String remarkLimitTime;

    public String remark;

    //todo 使用登记
}
