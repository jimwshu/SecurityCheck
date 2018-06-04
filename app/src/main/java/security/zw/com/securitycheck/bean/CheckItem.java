package security.zw.com.securitycheck.bean;

import java.util.ArrayList;

import security.zw.com.securitycheck.utils.json.JSONAble;

/**
 * Created by wangshu on 18/5/26.
 * 建筑单位
 */

public class CheckItem extends JSONAble{
    public boolean isSelected = false;
    public CheckItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int id;
    public int parentId;//上级项目id
    public String name;//名称
    public int level;

    public ArrayList<CheckItem> childrens;

}
