package security.zw.com.securitycheck;

import com.google.gson.Gson;
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
import security.zw.com.securitycheck.adapter.NoticeAdapter;
import security.zw.com.securitycheck.adapter.PersonAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.Notice;
import security.zw.com.securitycheck.bean.Person;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class NoticeActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, NoticeActivity.class);
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

    private ArrayList<Notice> data = new ArrayList<Notice>();
    protected LinearLayoutManager mManager;
    protected NoticeAdapter mAdapter;
    private SwipeRefreshLayoutBoth mSwipeRefreshLayout;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;


    private ImageView mBack;
    private TextView mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_company_list);
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
        mType.setText("通知");
        mType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new NoticeAdapter(data, this, 1);
        mSwipeRefreshLayout = (SwipeRefreshLayoutBoth) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutBoth.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection direction) {
                if (direction == SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection.TOP) {
                    NoticeActivity.this.onRefresh();
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


    Retrofit mRetrofit;
    Constans.GetSmsService addCheck;
    Call<String> mCall;


    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetSmsService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("size", 10);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getTaskList(requestBody);

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
                                hasMore = jsonObject.optBoolean("has_more");
                                ArrayList<Notice> arrayList = new ArrayList<>();
                                if (object != null && object.length() > 0) {
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject object1 = object.optJSONObject(i);
                                        Notice company = new Gson().fromJson(object1.toString(), Notice.class);
                                        company.mode = ProjectDetail.CHECK_MODE_MORE;
                                        arrayList.add(company);
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

    private void onRefresh() {
        page = 1;
        loadData();
    }


}
