package security.zw.com.securitycheck;

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

import java.util.ArrayList;

import security.zw.com.securitycheck.adapter.MyProjectAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class MyCheckActivity extends BaseSystemBarTintActivity implements MyProjectView {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MyCheckActivity.class);
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

    private MyProjectPresenter presenter;

    private ImageView mBack;
    private TextView mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_my_check_project);
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
        mAdapter = new MyProjectAdapter(data, this, 1);
        mSwipeRefreshLayout = (SwipeRefreshLayoutBoth) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutBoth.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection direction) {
                if (direction == SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection.TOP) {
                    MyCheckActivity.this.onRefresh();
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

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        presenter.getMyCheckProjectList(page);

    }

    private void onLoadMore() {
        if (hasMore) {
            page++;
            loadData();
        } else {
            ToastUtil.Short( "没有更多数据了");
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
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        this.hasMore = hasMore;
        this.page = page;

        if (projectInfos.size() > 0) {
            if (page > 1) {
                data.addAll(projectInfos);
                mAdapter.notifyDataSetChanged();
            } else {
                data.clear();
                data.addAll(projectInfos);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getMyCheckProjectListFailed(int code, String error) {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        ToastUtil.Short(error);
    }

    @Override
    public void getMyCheckProjectDetailListSucc(ArrayList<MyCheckProjectDetail> myCheckProjectDetails, boolean has_more) {

    }

    @Override
    public void getMyCheckProjectDetailListFailed(int code, String error) {

    }
}
