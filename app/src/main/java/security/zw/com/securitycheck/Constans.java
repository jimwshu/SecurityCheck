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

        @POST("supervisorDiamond/auth/login")
        public Call<String> login(@Body RequestBody route);

    }

    public interface GetMyProjectList {

        @POST("supervisorDiamond/myProject/list")
        public Call<String> getList(@Body RequestBody route);

        @POST("supervisorDiamond/myProject/{id}")
        public Call<String> getProjectById(@Path("id") int id);

    }

    public interface GetCheckItemList {

        @POST("supervisorDiamond/checkItem/list")
        public Call<String> getCheckItemList();

        @POST("/supervisorDiamond/checkItem/checkBasis")
        public Call<String> getCheckItemDetail(@Body RequestBody route);

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

        @POST("/supervisorDiamond/checkUsers/list")
        public Call<String> getCheckPerson();

        @POST("/supervisorDiamond/projectCheckItem/reviewScoreCheck/detail")
        public Call<String> getScoreCheckDetail(@Body RequestBody route);
    }
}
