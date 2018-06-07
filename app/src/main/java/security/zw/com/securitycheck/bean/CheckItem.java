package security.zw.com.securitycheck.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 建筑单位
 */

public class CheckItem extends JSONAble{
    public boolean isSelected = false;

    public int id;
    public int parentId;//上级项目id
    public int projectId;
    public int parentCheckItemId;
    public int checkItemId;

    public String name;//名称
    public int level;

    public double deserveScore;//应该得的总分
    public double realScore;//实得分
    public String scoreRule;
    public double min;
    public double max;
    public String type;

    @SerializedName(value = "childrens", alternate = {"checkItemWithScoreVOs"})
    public ArrayList<CheckItem> childrens;

}
