package security.zw.com.securitycheck.bean;

import java.util.ArrayList;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 建筑单位
 */

public class CheckItemDetail extends JSONAble {

    public int projectId;
    public int checkItemId;


    public String parentName;
    public int mainItemId;
    public double deserveScore;
    public double realScore;
    public String name;
    public String type;

    public ArrayList<DecreaseItem> decreaseItems;

}
