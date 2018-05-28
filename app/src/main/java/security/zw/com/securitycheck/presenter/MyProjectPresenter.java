package security.zw.com.securitycheck.presenter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.model.LoginModel;
import security.zw.com.securitycheck.model.MyProjectModel;
import security.zw.com.securitycheck.utils.net.NetworkCallback;
import security.zw.com.securitycheck.view.LoginView;
import security.zw.com.securitycheck.view.MyProjectView;


/**
 * Created by wangshu on 17/8/24.
 */

public class MyProjectPresenter implements BasePresenter{
    public MyProjectView myProjectView;
    public MyProjectModel myProjectModel;

    public MyProjectPresenter() {
    }

    public MyProjectPresenter(MyProjectView myProjectView) {
        this.myProjectView = myProjectView;
        this.myProjectModel = new MyProjectModel();
    }


    public void getProjectList(final int type,final int page) {
        myProjectModel.getList(type, page, new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                myProjectView.getListFailed(code, msg);
            }

            @Override
            public void onRealSuccess(String date) {
                super.onRealSuccess(date);
                try {
                    JSONObject jsonObject = new JSONObject(date);
                    JSONObject object = jsonObject.optJSONObject("data");
                    JSONArray data = object.optJSONArray("projects");
                    ArrayList<ProjectInfo> arrayList = new ArrayList<>();
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object1 = data.optJSONObject(i);
                            ProjectInfo projectInfo = new ProjectInfo();
                            projectInfo.parseFromJSONObject(object1);
                            arrayList.add(projectInfo);
                        }
                        myProjectView.getListSucc(arrayList, jsonObject.optBoolean("has_more"), page);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    myProjectView.getListFailed(-1, "数据解析失败");
                }
            }

        });
    }


    public void getProjecById(int id) {
        myProjectModel.getProjectById(id, new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                myProjectView.getProjectFailed(code, msg);
            }

            @Override
            public void onRealSuccess(String date) {
                super.onRealSuccess(date);
                try {
                    JSONObject jsonObject = new JSONObject(date);
                    JSONObject data = jsonObject.optJSONObject("data");

                    ProjectDetail p = SecurityApplication.getGson().fromJson(data.toString(), ProjectDetail.class);

                    myProjectView.getProjectSucc(p);

                } catch (JSONException e) {
                    e.printStackTrace();
                    myProjectView.getProjectFailed(-1, "数据解析失败");
                }
            }

        });
    }


    @Override
    public void onDestroy() {
        if (myProjectView != null) {
            myProjectView = null;
        }
    }
}