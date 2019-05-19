package security.zw.com.securitycheck.lowCase;

import com.google.gson.JsonObject;

import android.content.Context;
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
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class MySupervisionProjectActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MySupervisionProjectActivity.class);
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

    private ArrayList<ProjectInfo> data = new ArrayList<ProjectInfo>();
    protected LinearLayoutManager mManager;
    protected MyProjectAdapter mAdapter;
    private SwipeRefreshLayoutBoth mSwipeRefreshLayout;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;

    private int type = 1;// 查询类型

    private ImageView mBack;
    private TextView mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_my_project);
        initWidget();

    }

    private void initWidget() {
        mBack = findViewById(R.id.cancel);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mType = findViewById(R.id.perrmission);
        mType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mType.setText("立案项目");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MyProjectAdapter(data, this, 2);
        mSwipeRefreshLayout = (SwipeRefreshLayoutBoth) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutBoth.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection direction) {
                if (direction == SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection.TOP) {
                    MySupervisionProjectActivity.this.onRefresh();
                } else if (direction == SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection.BOTTOM) {
                    if (!isLoading) {
                        doLoadMore();
                    }
                }
            }
        });

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        onRefresh();
    }


    private void onRefresh() {
        page = 1;
        loadData();
    }


    Retrofit mRetrofit;
    Constans.GetMyProjectList addCheck;
    Call<String> mCall;


    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("size", 10);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.lawCaseProjectList(requestBody);

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
                                JSONObject object = jsonObject.optJSONObject("data");
                                hasMore = jsonObject.optBoolean("has_more");
                                JSONArray data1 = object.optJSONArray("autoProjects");
                                ArrayList<ProjectInfo> arrayList = new ArrayList<>();
                                if (data1 != null && data1.length() > 0) {
                                    for (int i = 0; i < data1.length(); i++) {
                                        JSONObject object1 = data1.optJSONObject(i);
                                        ProjectInfo projectInfo = new ProjectInfo();
                                        projectInfo.parseFromJSONObject(object1);
                                        arrayList.add(projectInfo);
                                    }
                                }

                                if (arrayList.size() > 0) {
                                    if (page > 1) {
                                        data.addAll(arrayList);
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        data.clear();
                                        data.addAll(arrayList);
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
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.Short("数据获取失败");

            }
        });


    }

    private void onLoadMore() {
        if (hasMore) {
            page++;
            loadData();
        } else {
            ToastUtil.Short("没有更多数据了");
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    protected void doLoadMore() {
        int visibleItemCount = mManager.getChildCount(); //获取当前可视的 item 的数量
        int totalItemCount = mManager.getItemCount(); //获取 RecyclerView 中当前所有 item 的数目
        int firstVisibleItemPosition = mManager.findFirstVisibleItemPosition(); //当前可视的第一个item的位置，也就是已经滚动过的item的数量。

        if ((firstVisibleItemPosition + visibleItemCount) >= totalItemCount) {
            onLoadMore();
        } else {  //列表做提前加载，若松手放开进度圈之前，提前加载已经成功，那么列表内容增加了，这时候需要主动让进度圈消失
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }




}
