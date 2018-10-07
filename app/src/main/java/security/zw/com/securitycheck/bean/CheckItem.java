package security.zw.com.securitycheck.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 建筑单位
 */

public class CheckItem extends JSONAble{

    public static final int HAS_ASSIGNED = 1, HAS_NO_ASSIGNED = 0;

    public boolean isSelected = false;

    public int id = -1;
    public int parentId;//上级项目id
    public int projectId;
    public int parentCheckItemId;
    public int checkItemId;

    public String name;//名称
    public int level;
    public int worker;// 获取到检查任务的ID
    public String checker;// 获取到检查任务的ID

    public int assigned;// 是不是暂时有分配了

    public double deserveScore;//应该得的总分
    public double realScore;//实得分
    public String scoreRule;
    public double min;
    public double max;
    public double interval;
    public String type;

    public double score;

    // 是不是已经完成了分配，true是,不能在分配了，false否，还可以分配
    public boolean hasAssigned;

    @SerializedName(value = "childrens", alternate = {"checkItemWithScoreVOs"})
    public ArrayList<CheckItem> childrens;

    public int safetyInspector;


    public String code;
    public boolean haveScored;
    public boolean haveItemChecked;

    public String checkName;


    public boolean isPassed;
    @Override
    public String toString() {
        return "CheckItem{" +
                "isSelected=" + isSelected +
                ", id=" + id +
                ", parentId=" + parentId +
                ", projectId=" + projectId +
                ", parentCheckItemId=" + parentCheckItemId +
                ", checkItemId=" + checkItemId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", worker=" + worker +
                ", checker='" + checker + '\'' +
                ", assigned=" + assigned +
                ", deserveScore=" + deserveScore +
                ", realScore=" + realScore +
                ", scoreRule='" + scoreRule + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", interval=" + interval +
                ", type='" + type + '\'' +
                ", score=" + score +
                ", hasAssigned=" + hasAssigned +
                ", childrens=" + childrens +
                ", safetyInspector=" + safetyInspector +
                ", code='" + code + '\'' +
                ", haveScored=" + haveScored +
                '}';
    }
}


