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
    public static String DOMAIN = "http://39.106.107.203:7083/";

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
    }
}
