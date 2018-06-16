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


        // 整改列表详情
        @POST("/supervisorDiamond/supervisionReform/detail")
        public Call<String> getSupervisionListDetailForProject(@Body RequestBody route);

        // 移交执法 or 整改合格
        @POST("/supervisorDiamond/supervisionReform/updateStatus")
        public Call<String> updateSupervisionListDetailForProject(@Body RequestBody route);

    }

    public interface GetCheckItemList {

        @POST("/supervisorDiamond/checkItem/list")
        public Call<String> getCheckItemList();

        @POST("/supervisorDiamond/projectCheckItem/superviseModification/subList")
        public Call<String> getCheckItemDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/checkItem/listFilter")
        public Call<String> getFilter(@Body RequestBody route);

        @POST("/supervisorDiamond/myCheck/scoreList")
        public Call<String> getCheckScoreList(@Body RequestBody route);

        @POST("/supervisorDiamond/myCheck/scoreDetail")
        public Call<String> getCheckScoreItemDetail(@Body RequestBody route);

    }


    public interface CheckBasic {
        @POST("/supervisorDiamond/legalItem/ilegalItems")
        public Call<String> getBasic(@Body RequestBody route);
    }


    public interface AddCheck {
        @POST("/supervisorDiamond/projectCheckItem/reviewRandomCheck/add")
        public Call<String> addRandomCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/add")
        public Call<String> addScoreCheck(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/update\n")
        public Call<String> updateScoreCheck(@Body RequestBody route);


        @POST("/supervisorDiamond/checkUsers/list")
        public Call<String> getCheckPerson();

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/detail")
        public Call<String> getScoreCheckDetail(@Body RequestBody route);

        @POST("/supervisorDiamond/projectCheckItem/reviewRandomCheck/detail")
        public Call<String> getRandomCheckDetail(@Body RequestBody route);


        @POST("/supervisorDiamond/projectCheckItem/assign")
        public Call<String> postCheckPerson(@Body RequestBody route);

        @POST("/supervisorDiamond/projectTag/add")
        public Call<String> finishCheck(@Body RequestBody route);

    }
}
