package security.zw.com.securitycheck.bean;

import java.util.ArrayList;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/6/13.
 */

public class Company extends JSONAble {
    public int id;
    public String address;
    public String name;
    public String companyName;
    public String enterpriseCategory;


    public String phone;
    public String position;

    public String qualification;

    public String responsiblePerson;
    public int responsiblePersonId;

    public ArrayList<ProjectInfo> projects;
    public ArrayList<Tower> towers;

}
