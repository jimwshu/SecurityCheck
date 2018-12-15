package security.zw.com.securitycheck.installEqupment.allEqupment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import security.zw.com.securitycheck.bean.EquipmentDetail;
import security.zw.com.securitycheck.bean.EquipmentList;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class InstallEquipmentDetailActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx, EquipmentList p) {
        Intent intent = new Intent(ctx, InstallEquipmentDetailActivity.class);
        intent.putExtra("info", p);
        ctx.startActivity(intent);
    }


    /*
     * 是否设置沉浸式状态栏
     */
    protected boolean isNeedImmersiveStatusBar() {
        return true;
    }

    protected int getImmersiveStatusBarColor() {
        return R.color.colorPrimary;
    }


    private ImageView mBack;
    private TextView mType;
    private TextView mSubmit;

    public void initBar() {
        mBack = findViewById(R.id.cancel);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mType = findViewById(R.id.perrmission);
        mType.setText("安装设备详情");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setText("生成二维码");
        mSubmit.setVisibility(View.GONE);

    }


    public EquipmentList info;
    private EquipmentDetail detail;

    private RecyclerView mRecyclerView;

    private ArrayList<EquipmentDetail> data = new ArrayList<EquipmentDetail>();
    protected LinearLayoutManager mManager;
    protected InstallEquipmentDetailAdapter mAdapter;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView checkNo;
    private TextView checkYes;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_equipment_detail);

        info = (EquipmentList) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
        }

        initWidget();

    }

    private void initWidget() {
        initBar();
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.edittext);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InstallEquipmentDetailActivity.this.onRefresh();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new InstallEquipmentDetailAdapter(data, this);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        checkNo = findViewById(R.id.check_no);
        checkYes = findViewById(R.id.check_yes);

        checkNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtil.Short("请输入拒绝申请理由");
                } else {
                    setYseOrNo(false);
                }
            }
        });

        checkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setYseOrNo(true);
            }
        });

    }


    Retrofit mRetrofit;
    Constans.Equipment addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("equipmentId", info.equipmentId);
        jsonObject.addProperty("recordId", info.recordId);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getEquipmentInstall(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                JSONArray object = jsonObject.optJSONArray("data");
                                ArrayList<EquipmentDetail> arrayList = new ArrayList<>();
                                if (object != null && object.length() > 0) {
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject object1 = object.optJSONObject(i);
                                        EquipmentDetail supervisionProjectList = new Gson().fromJson(object1.toString(), EquipmentDetail.class);
                                        arrayList.add(supervisionProjectList);
                                    }
                                }

                                if (arrayList.size() > 0) {
                                    data.clear();
                                    data.addAll(arrayList);
                                    mAdapter.notifyDataSetChanged();
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
                mSwipeRefreshLayout.setRefreshing(false);

                ToastUtil.Short("数据获取失败");

            }
        });


    }

    private void onRefresh() {
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    private void setYseOrNo(boolean yes) {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("equipmentId", info.equipmentId);
        jsonObject.addProperty("recordId", info.recordId);
        jsonObject.addProperty("accept", yes);

        if (!yes) {
            jsonObject.addProperty("reason", editText.getText().toString());
        }
        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.recordDocAudit(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                ToastUtil.Short("数据提交成功");
                                onRefresh();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Short("数据提交失败");
                    }
                } else {
                    ToastUtil.Short("数据提交失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);

                ToastUtil.Short("数据提交失败");

            }
        });


    }
}
