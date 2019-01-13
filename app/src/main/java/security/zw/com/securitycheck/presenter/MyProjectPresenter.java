package security.zw.com.securitycheck.presenter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
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


    public void getProjectList(final int type,final int page, final String name, final int departId) {
        myProjectModel.getList(type, page, name,departId, new NetworkCallback() {
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
                    int total = object.optInt("total");
                    JSONArray data = object.optJSONArray("projects");
                    ArrayList<ProjectInfo> arrayList = new ArrayList<>();
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object1 = data.optJSONObject(i);
                            ProjectInfo projectInfo = new ProjectInfo();
                            projectInfo.parseFromJSONObject(object1);
                            arrayList.add(projectInfo);
                        }
                        myProjectView.getListSucc(arrayList, jsonObject.optBoolean("has_more"), page, total);
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


    public void getMyCheckProjectList(final int page) {
        myProjectModel.getMyCheckProjectList(page, new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                myProjectView.getMyCheckProjectListFailed(code, msg);
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
                        myProjectView.getMyCheckProjectListSucc(arrayList, jsonObject.optBoolean("has_more"), page);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    myProjectView.getMyCheckProjectListFailed(-1, "数据解析失败");
                }
            }

        });
    }

    public void getMyCheckProjectDetailList(final int projectId) {
        myProjectModel.getMyCheckProjectDetailList(projectId, new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                myProjectView.getMyCheckProjectDetailListFailed(code, msg);
            }

            @Override
            public void onRealSuccess(String date) {
                super.onRealSuccess(date);
                try {
                    JSONObject jsonObject = new JSONObject(date);
                    JSONArray object = jsonObject.optJSONArray("data");
                    ArrayList<MyCheckProjectDetail> details = new ArrayList<>();
                    if (object != null && object.length() > 0) {
                        for (int i = 0; i < object.length(); i++) {
                            JSONObject object1 = object.optJSONObject(i);
                            MyCheckProjectDetail detail = new Gson().fromJson(object1.toString(), MyCheckProjectDetail.class);
                            details.add(detail);
                        }
                        myProjectView.getMyCheckProjectDetailListSucc(details, jsonObject.optBoolean("has_more"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    myProjectView.getMyCheckProjectDetailListFailed(-1, "数据解析失败");
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
