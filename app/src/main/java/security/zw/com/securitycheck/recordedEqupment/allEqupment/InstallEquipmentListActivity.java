package security.zw.com.securitycheck.recordedEqupment.allEqupment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import security.zw.com.securitycheck.bean.EquipmentList;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

// 整改列表（相对于项目）
public class InstallEquipmentListActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx, int type) {
        Intent intent = new Intent(ctx, InstallEquipmentListActivity.class);
        intent.putExtra("type", type);
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

    private RecyclerView mRecyclerView;

    private ArrayList<EquipmentList> data = new ArrayList<EquipmentList>();
    protected LinearLayoutManager mManager;
    protected InstallEquipmentListAdapter mAdapter;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        setContentView(R.layout.activity_equpment_list);
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
        mType.setText("产权变更");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new InstallEquipmentListAdapter(data, this);
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InstallEquipmentListActivity.this.onRefresh();
            }
        });
    }


    private void onRefresh() {
        loadData();
    }


    Retrofit mRetrofit;
    Constans.Equipment addCheck;
    Call<String> mCall;


    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.changeUsed(requestBody);

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
                                ArrayList<EquipmentList> arrayList = new ArrayList<>();
                                if (object != null && object.length() > 0) {
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject object1 = object.optJSONObject(i);
                                        EquipmentList supervisionProjectList = new Gson().fromJson(object1.toString(), EquipmentList.class);
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
                ToastUtil.Short("数据获取失败");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
