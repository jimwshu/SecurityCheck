package security.zw.com.securitycheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.adapter.MyCheckProjectDetailAdapter;
import security.zw.com.securitycheck.adapter.MyProjectAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
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
    public void getListSucc(ArrayList<ProjectInfo> projectInfos, boolean hasMore, int page) {

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
