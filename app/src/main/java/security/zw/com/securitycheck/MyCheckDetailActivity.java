package security.zw.com.securitycheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.adapter.MyCheckProjectDetailAdapter;
import security.zw.com.securitycheck.adapter.MyProjectAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.postbean.CheckBean;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class MyCheckDetailActivity extends BaseSystemBarTintActivity implements MyProjectView {


    private ProjectInfo projectInfo;

    public static void launch(Context ctx, ProjectInfo projectInfo) {
        Intent intent = new Intent(ctx, MyCheckDetailActivity.class);
        intent.putExtra("projectInfo", projectInfo);
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

    private ArrayList<MyCheckProjectDetail> data = new ArrayList<MyCheckProjectDetail>();
    protected LinearLayoutManager mManager;
    protected MyCheckProjectDetailAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MyProjectPresenter presenter;

    private ImageView mBack;
    private TextView mType;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        projectInfo = (ProjectInfo) getIntent().getSerializableExtra("projectInfo");
        if (projectInfo == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_my_check_detail_project);
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
        mType.setText("我的检查项目");
        mType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        presenter = new MyProjectPresenter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MyCheckProjectDetailAdapter(data, this, 1);
        mAdapter.setDelete(new MyCheckProjectDetailAdapter.Delete() {
            @Override
            public void delete(int pos) {
                MyCheckProjectDetail myCheckProjectDetail = data.get(pos);
                showWarmDialog(myCheckProjectDetail);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyCheckDetailActivity.this.onRefresh();
            }
        });

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        onRefresh();
    }

    private void showWarmDialog(final MyCheckProjectDetail myCheckProjectDetail) {
        new AlertDialog.Builder(this)
                .setMessage("是否删除随机检查？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteDetail(myCheckProjectDetail);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                setResult(RESULT_OK);
                finish();
            }
        })
                .show();

    }

    Retrofit mRetrofit;
    Constans.GetCheckItemList addCheck;
    Call<String> mCall;
    public void deleteDetail(MyCheckProjectDetail myCheckProjectDetail) {
        isLoading = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetCheckItemList.class);

        mCall = addCheck.deleteDetail(myCheckProjectDetail.id);
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
                                onRefresh();
                            } else {
                                ToastUtil.Long("删除失败，请重试");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Long("删除失败，请重试");
                    }
                } else {
                    ToastUtil.Long("删除失败，请重试");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                ToastUtil.Long("删除失败，请重试");
            }
        });
    }

    private void onRefresh() {
        loadData();
    }

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        presenter.getMyCheckProjectDetailList(projectInfo.id);

    }


    @Override
    public void getListSucc(ArrayList<ProjectInfo> projectInfos, boolean hasMore, int page, int total) {

    }

    @Override
    public void getListFailed(int code, String error) {

    }


    @Override
    public void getProjectSucc(ProjectDetail detail) {

    }

    @Override
    public void getProjectFailed(int code, String error) {

    }

    @Override
    public void getMyCheckProjectListSucc(ArrayList<ProjectInfo> projectInfos, boolean has_more, int page) {

    }

    @Override
    public void getMyCheckProjectListFailed(int code, String error) {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        ToastUtil.Short(error);
    }


    @Override
    public void getMyCheckProjectDetailListSucc(ArrayList<MyCheckProjectDetail> myCheckProjectDetails, boolean has_more) {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);

        if (myCheckProjectDetails.size() > 0) {
            data.clear();
            data.addAll(myCheckProjectDetails);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMyCheckProjectDetailListFailed(int code, String error) {

    }
}
