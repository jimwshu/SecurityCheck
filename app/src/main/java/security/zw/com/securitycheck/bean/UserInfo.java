package security.zw.com.securitycheck.bean;
import org.json.JSONException;
import org.json.JSONObject;

import security.zw.com.securitycheck.utils.json.JSONAble;


/**
 * - 登陆用户
 */

public class UserInfo extends JSONAble {

    public String gtoken;
    public int id;

    public String name;
    public String avatar;

    public String phone;
    public String password;

    /*安监员（副站长）：
    我的项目：分配给我的工程项目列表，点击进入项目详情。
    我的整改：我下达过的整改列表，点击查看呢本次整改信息。
    我的检查：我发起的所有检查的列表，可以分页显示随即检查列表和评分检查列表，评分检查结果根据优良、合格、不合格不同颜色体现。若有多次评分检查自动摘取本季度内最近一次。
    停工复工：我对项目下达停工和已复工状态的显示。（停工复工可以理解为项目的一个独立属性，和业务流程无关）
    录控提醒：另一套湖南省系统的录入提醒，默认每月20日提醒。也是项目的一个独立属性，类似闹钟功能。
    督办项目：站长下达督办的项目列表。是否督办为项目独立属性。
    设备一览：我的项目里使用过的设备一览。
    企业库：企业信息和通讯信息，可以和企业用户进行关联，
    人员库：各企业各项目的人员，包括姓名、性别、岗位、电话、照片，人员同时和企业和项目关联。
    通知：发布和接受通知，有未读红点和高亮提醒


    执法员（副站长）：
    我的整改：移交来进行执法的整改一览
    我的案件：我的立案一览
    督办项目：同安监员
    企业库：同安监员
    人员库：同安监员
    通知：同安监员

    设备员（副站长）：
    设备备案：机械设备的产权备案列表，一台设备对应一次设备备案，标明设备的产权归属，设备备案后对本台设备发备案证。设备产权企业提出的备案申请在此处可以看到。
    使用登记：机械设备的使用登记列表，一台设备对应多次使用登记，但必须一次使用完毕拆除后后才进行下一次，设备在工程项目上使用。设备使用企业提出的使用登记申请在此处可以看到。
    产权变更：设备产权权属变更，设备产权企业提出的变更申请在此处可以看到。
    安装检查：安监站对批准使用的设备进行安装后的安全检查结果列表，界面中可以随机指派检测单位进行检测。
    设备台账：所有使用中设备的列表。
    督办项目：同安监员
    企业库：同安监员
    人员库：同安监员
    通知：同安监员


    技术科（副站长）：
    项目台账：所有在建项目列表，显示风格同安监员。
    整改台账：所有安监员下达整改的列表，显示风格同安监员。
    案件台账：所有在办案件的列表，显示风格同执法员。
    设备台账：所有使用者设备的列表，显示风格同设备员。
    评分检查：本季度所有评分检查的结果，显示风格同安监员。
    督办项目：同安监员
    企业库：同安监员
    人员库：同安监员
    通知：同安监员

    站长：
    项目台账：同技术科（台账里可下达督办）：
    整改台账：同技术科
    案件台账：同技术科
    设备台账：同技术科
    评分检查：同技术科
    督办项目：同技术科
    企业库：同技术科
    人员库：同技术科
    通知：同技术科

    办公员：
    评分检查：同技术科
    督办项目：同技术科
    企业库：同技术科
    人员库：同技术科
    通知：同技术科

    超级管理员：不使用APP，主要在WEB端进行系统维护、权限控制*/



    public int type;

    public UserInfo() {
    }



    public static UserInfo parseUserInfoFromString(String u) {
        try {
            UserInfo userInfo = new UserInfo();
            JSONObject jsonObject = new JSONObject(u);
            userInfo.parseFromJSONObject(jsonObject);
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public String toString() {
        return encodeToJsonObject().toString();
    }

}
