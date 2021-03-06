package security.zw.com.securitycheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by wangshu on 17/5/17.
 */

public class Constans {
    public static String DOMAIN = "http://47.107.236.231:7083/";

    public static String Login = DOMAIN + "supervisorDiamond/auth/login";

    public interface GetSmsService {

        @POST("/supervisorDiamond/auth/login")
        public Call<String> login(@Body RequestBody route);


        @POST("/supervisorDiamond/message/getMessageCount")
        public Call<String> getMessageCount(@Body RequestBody route);

        @POST("/supervisorDiamond/message/projectList")
        public Call<String> getTaskList(@Body RequestBody route);

    }

    public interface GetMyProjectList {

        @POST("/supervisorDiamond/myProject/list")
        public Call<String> getList(@Body RequestBody route);

        @POST("/supervisorDiamond/myProject/{id}")
        public Call<String> getProjectById(@Path("id") int id);

        @POST("/supervisorDiamond/myCheck/projectList")
        public Call<String> getMyCheckProjectList(@Body RequestBody route);

        // 我的检查项目的总评分记录列表
        @POST("/supervisorDiamond/projectEvaluation/list")
        public Call<String> getEvaluationCheckProjectList(@Body RequestBody route);

        // 监督整改列表，项目为单位
        @POST("/supervisorDiamond/supervisionReform/projectList")
        public Call<String> getSupervisionList(@Body RequestBody route);

        // 项目为单位下的列表
        @POST("/supervisorDiamond/supervisionReform/checkList")
        public Call<String> getSupervisionListForProject(@Body RequestBody route);

        // 项目为单位的单次整改列表
        @POST("/supervisorDiamond/supervisionReform/projectCheckItems")
        public Call<String> getSupervisionListForOneCheck(@Body RequestBody route);



        // 整改列表详情
        @POST("/supervisorDiamond/supervisionReform/detail")
        public Call<String> getSupervisionListDetailForProject(@Body RequestBody route);

        // 移交执法 or 整改合格
        @POST("/supervisorDiamond/supervisionReform/updateStatus")
        public Call<String> updateSupervisionListDetailForProject(@Body RequestBody route);

        // 录控提醒
        @POST("/supervisorDiamond/myProject/list")
        public Call<String> getRemind(@Body RequestBody route);

        // 录控提醒
        @POST("/supervisorDiamond/projectStateManager/detail")
        public Call<String> stopDetail(@Body RequestBody route);

        // 录控提醒
        @POST("/supervisorDiamond/projectStateManager/add")
        public Call<String> addDetail(@Body RequestBody route);

        // 录控提醒
        @POST("/supervisorDiamond/projectStateManager/update")
        public Call<String> updateDetail(@Body RequestBody route);

        // 录控提醒
        @POST("/supervisorDiamond/recordControl/update")
        public Call<String> changeRemind(@Body RequestBody route);


        // 执法项目列表
        @POST("/supervisorDiamond/enforceLaw/projectList")
        public Call<String> enforceLawProjectList(@Body RequestBody route);

        // 项目为单位下的执法列表
        @POST("/supervisorDiamond/enforceLaw/enforceLawList")
        public Call<String> getEnforceLawListForProject(@Body RequestBody route);

        // 获取执法人员
        @POST("/supervisorDiamond/enforceLaw/personnel")
        public Call<String> getEnforceLawPersonal(@Body RequestBody route);

        // 分配执法
        @POST("/supervisorDiamond/enforceLaw/assign")
        public Call<String> enforceLawPersonalAssign(@Body RequestBody route);

        // 执法详情
        @POST("/supervisorDiamond/enforceLaw/enforceLawDetail")
        public Call<String> enforceLawDetail(@Body RequestBody route);

        // 处理执法
        @POST("/supervisorDiamond/enforceLaw/dealDetail")
        public Call<String> enforceLawDetailDeal(@Body RequestBody route);


        // 立案项目列表
        @POST("/supervisorDiamond/lawCase/projectList")
        public Call<String> lawCaseProjectList(@Body RequestBody route);


        // 项目为单位下的立案列表
        @POST("/supervisorDiamond/lawCase/caseList")
        public Call<String> getLawCaseListForProject(@Body RequestBody route);
    }

    public interface GetCheckItemList {

        @POST("/supervisorDiamond/checkItem/list")
        public Call<String> getCheckItemList(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/listItemCheckFilter")
        public Call<String> getCheckItemListForType3(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/dustList")
        public Call<String> getCheckItemListForType4(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/checkBasis")
        public Call<String> getCheckItemDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/itemCheckBasis")
        public Call<String> getCheckItemDetailForType3(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/listFilter")
        public Call<String> getFilter(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/listItemCheckFilter")
        public Call<String> getFilterForType3(@Body RequestBody route);


        @POST("/supervisorDiamond/myCheck/scoreList")
        public Call<String> getCheckScoreList(@Body RequestBody route);

        @POST("/supervisorDiamond/myCheck/scoreDetail")
        public Call<String> getCheckScoreItemDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/projectTag/remove/{id}")
        public Call<String> deleteDetail(@Path("id") int id);

    }


    public interface CheckBasic {
        @POST("/supervisorDiamond/legalItem/ilegalItems")
        public Call<String> getBasic(@Body RequestBody route);
    }

    public interface Company {
        @POST("/supervisorDiamond/company/list")
        public Call<String> getCompanyList(@Body RequestBody route);

        @POST("/supervisorDiamond/company/detail")
        public Call<String> getCompanyDetail(@Body RequestBody route);
    }

    public interface Person {
        @POST("/supervisorDiamond/companyUser/list")
        public Call<String> getPersonList(@Body RequestBody route);

        @POST("/supervisorDiamond/companyUser/detail")
        public Call<String> getPersonDetail(@Body RequestBody route);
    }


    public interface AddCheck {
        @POST("/supervisorDiamond/projectCheckItem/reviewRandomCheck/add")
        public Call<String> addRandomCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/add")
        public Call<String> addScoreCheck(@Body RequestBody route);


        @POST("/supervisorDiamond/projectCheckItem/dustCheck/add")
        public Call<String> addDustCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/dustCheck/update")
        public Call<String> updateDustCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/update")
        public Call<String> updateScoreCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/itemByItemCheck/add")
        public Call<String> addEveryCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/itemByItemCheck/update")
        public Call<String> updateEveryCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/checkUsers/list")
        public Call<String> getCheckPerson();

        @POST("/supervisorDiamond/departments/list")
        public Call<String> getDepartments();

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/detailById")
        public Call<String> getScoreCheckDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewRandomCheck/detail")
        public Call<String> getRandomCheckDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/dustCheck/detailById")
        public Call<String> getDustCheckDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/assign")
        public Call<String> postCheckPerson(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/assign/update")
        public Call<String> updateCheckCheckPerson(@Body RequestBody route);

        @POST("/supervisorDiamond/projectTag/add")
        public Call<String> finishCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/assign/judge")
        public Call<String> finishCheckPerson(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewRandomCheck/list")
        public Call<String> getRandomCheckList(@Body RequestBody route);


        @POST("/supervisorDiamond/myProject/modify/superiseStatus")
        public Call<String> toChangeStatus(@Body RequestBody route);
    }


    public interface Equipment {
        @POST("/supervisorDiamond/equipment/record")
        public Call<String> getEquipmentRecord(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/uninstalled")
        public Call<String> getEquipmentUninstall(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/unInstallDoc")
        public Call<String> getEquipmentUninstallDoc(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/unInstallDocAudit")
        public Call<String> getEquipmentUninstallDocAudit(@Body RequestBody route);


        @POST("/supervisorDiamond/equipment/installed")
        public Call<String> getEquipmentInstall(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/installDoc")
        public Call<String> getEquipmentInstallDoc(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/installDocAudit")
        public Call<String> getEquipmentInstallAudit(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/used")
        public Call<String> getEquipmentUsed(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/usedDoc")
        public Call<String> getEquipmentUsedDoc(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/usedDocAudit")
        public Call<String> getEquipmentUsedDocAudit(@Body RequestBody route);



        @POST("/supervisorDiamond/equipment/changeUsed")
        public Call<String> changeUsed(@Body RequestBody route);


        @POST("/supervisorDiamond/equipment/recordDoc")
        public Call<String> getEquipmentDoc(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/recordDocAudit")
        public Call<String> recordDocAudit(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/list")
        public Call<String> getEquipmentList(@Body RequestBody route);

        @POST("/supervisorDiamond/users/resetPassword")
        public Call<String> resetPassword(@Body RequestBody route);


        @POST("/supervisorDiamond/equipmentProperty/alreadyRecords")
        public Call<String> getAlreadyRecordsList(@Body RequestBody route);

        @POST("/supervisorDiamond/equipmentProperty/detail")
        public Call<String> getAlreadyRecordsListDetail(@Body RequestBody route);


        @POST("/supervisorDiamond/equipment/usedInProjects")
        public Call<String> usedInProjects(@Body RequestBody route);

        @POST("/supervisorDiamond/equipment/usedDetail")
        public Call<String> getUsedDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/company/checkCompanyList")
        public Call<String> checkCompanyList(@Body RequestBody route);

    }


}
