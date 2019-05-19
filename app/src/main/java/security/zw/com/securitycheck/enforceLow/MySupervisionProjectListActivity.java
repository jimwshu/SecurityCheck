package security.zw.com.securitycheck.enforceLow;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckPerson;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

// 执法列表（相对于项目）
public class MySupervisionProjectListActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx, ProjectInfo info) {
        Intent intent = new Intent(ctx, MySupervisionProjectListActivity.class);
        intent.putExtra("info", info);
        ctx.startActivity(intent);
    }


    private ProjectInfo info;
    /*
    * 是否设置沉浸式状态栏
    */
    protected boolean isNeedImmersiveStatusBar() {
        return true;
    }

    protected int getImmersiveStatusBarColor() {
        return R.color.colorPrimary;
    }

    private RecyclerView mRecyclerView;

    private ArrayList<SupervisionProjectList> data = new ArrayList<SupervisionProjectList>();
    protected LinearLayoutManager mManager;
    protected MySupervisionProjectListAdapter mAdapter;

    protected boolean isLoading = false;
    protected boolean hasMore = true;


    private ImageView mBack;
    private TextView mType;
    private TextView mSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        info = (ProjectInfo) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_my_supervision_project_list);
        initWidget();

    }
    private TextView check;

    private void initWidget() {
        mBack = findViewById(R.id.cancel);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        check = findViewById(R.id.check);

        check.setVisibility(View.GONE);
        mType = findViewById(R.id.perrmission);
        mType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mType.setText("执法列表");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MySupervisionProjectListAdapter(data, this);
        mAdapter.setOnClick(new MySupervisionProjectListAdapter.OnClick() {
            @Override
            public void onClick(int pos) {
                selectPermission();
            }
        });
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

    }


    private void onRefresh() {
        loadData();
    }


    Retrofit mRetrofit;
    Constans.GetMyProjectList addCheck;
    Call<String> mCall;
    private boolean get_code = false;


    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("projectId", info.id);
        //todo checkmode
        jsonObject.addProperty("checkMode", 1);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getEnforceLawListForProject(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                                ArrayList<SupervisionProjectList> arrayList = new ArrayList<>();
                                if (jsonObject1 != null) {
                                    JSONArray array = jsonObject1.optJSONArray("nowList");
                                    if (array != null && array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object1 = array.optJSONObject(i);
                                            SupervisionProjectList supervisionProjectList = new Gson().fromJson(object1.toString(), SupervisionProjectList.class);
                                            arrayList.add(supervisionProjectList);
                                        }
                                    }
                                    ArrayList<SupervisionProjectList> arrayList1 = new ArrayList<>();

                                    JSONArray array1 = jsonObject1.optJSONArray("beforeList");
                                    if (array1 != null && array1.length() > 0) {
                                        for (int i = 0; i < array1.length(); i++) {
                                            JSONObject object1 = array1.optJSONObject(i);
                                            SupervisionProjectList supervisionProjectList = new Gson().fromJson(object1.toString(), SupervisionProjectList.class);
                                            arrayList1.add(supervisionProjectList);
                                        }
                                    }


                                    if (arrayList.size() > 0 || arrayList1.size() > 0) {
                                        data.clear();


                                        if (arrayList.size() > 0) {
                                            SupervisionProjectList supervisionProjectList = new SupervisionProjectList();
                                            supervisionProjectList.statusTypeType = 1;
                                            data.add(supervisionProjectList);

                                            data.addAll(arrayList);
                                        }

                                        if (arrayList1.size() > 0) {
                                            SupervisionProjectList supervisionProjectList = new SupervisionProjectList();
                                            supervisionProjectList.statusTypeType = 2;
                                            data.add(supervisionProjectList);

                                            data.addAll(arrayList1);
                                        }
                                        mAdapter.notifyDataSetChanged();

                                    }

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Short("数据获取失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                ToastUtil.Short("数据获取失败");

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    public ArrayList<CheckPerson> persons = new ArrayList<>();

    public int selectedId = -1;
    public String[] personsName;
    private void toShowDepartment() {
        new AlertDialog.Builder(this)
                .setTitle("请选择执法员")
                .setSingleChoiceItems(personsName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedId = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selectPerson();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectedId = -1;
            }
        })
                .show();
    }

    private void selectPerson() {

        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", info.id);
        jsonObject.addProperty("assignTo", persons.get(selectedId).id);
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.enforceLawPersonalAssign(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                ToastUtil.Short("分配成功");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Short("分配失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                ToastUtil.Short("分配失败");

            }
        });


    }


    public void selectPermission() {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            jsonObject.addProperty("projectId", info.id);

            //todo checkmode
            jsonObject.addProperty("checkMode", 1);

            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

            mCall = addCheck.getEnforceLawPersonal(requestBody);
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    get_code = false;
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.has("code")) {
                                int code = jsonObject.optInt("code");
                                if (code == 0) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        persons.clear();
                                        personsName = new String[jsonArray.length()];
                                        selectedId = -1;

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.optJSONObject(i);
                                            CheckPerson checkPerson = new CheckPerson();
                                            checkPerson = SecurityApplication.getGson().fromJson(object.toString(), CheckPerson.class);
                                            persons.add(checkPerson);
                                            personsName[i] = (checkPerson.name);
                                        }

                                        toShowDepartment();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("获取人员失败");

                        }
                    } else {
                        ToastUtil.Long("获取人员失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("获取人员失败");
                }
            });

        }



    }


}
