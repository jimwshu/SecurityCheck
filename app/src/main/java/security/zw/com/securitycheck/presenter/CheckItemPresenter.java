package security.zw.com.securitycheck.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.model.CheckItemModel;
import security.zw.com.securitycheck.model.MyProjectModel;
import security.zw.com.securitycheck.utils.net.NetworkCallback;
import security.zw.com.securitycheck.view.CheckItemView;
import security.zw.com.securitycheck.view.MyProjectView;


/**
 * Created by wangshu on 17/8/24.
 */

public class CheckItemPresenter implements BasePresenter{
    public CheckItemView checkItemView;
    public CheckItemModel checkItemModel;

    public CheckItemPresenter() {
    }

    public CheckItemPresenter(CheckItemView checkItemView) {
        this.checkItemView = checkItemView;
        this.checkItemModel = new CheckItemModel();
    }


    public void getProjectList() {
        checkItemModel.getList(new NetworkCallback() {
            @Override
            public void onRealFailed(int code, String msg) {
                super.onRealFailed(code, msg);
                checkItemView.getCheckItemFailed(code, msg);
            }

            @Override
            public void onRealSuccess(String date) {
                super.onRealSuccess(date);
                try {
                    JSONObject jsonObject = new JSONObject(date);
                    JSONArray jsonObject1 = jsonObject.optJSONArray("data");
                    ArrayList<CheckItem> checkItems = new ArrayList<>();
                    if (jsonObject1 != null && jsonObject1.length() > 0) {
                        for (int i = 0; i< jsonObject1.length(); i++) {
                            CheckItem checkItem = SecurityApplication.getGson().fromJson(jsonObject1.optJSONObject(i).toString(), CheckItem.class);
                            checkItems.add(checkItem);
                        }
                    }
                    checkItemView.getCheckItemSucc(checkItems);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkItemView.getCheckItemFailed(-1, "数据解析失败");
                }
            }

        });
    }



    @Override
    public void onDestroy() {
        if (checkItemView != null) {
            checkItemView = null;
        }
    }
}
