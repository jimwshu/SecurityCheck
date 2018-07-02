package security.zw.com.securitycheck;

import android.app.Activity;
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

import security.zw.com.securitycheck.adapter.ScoreAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.CheckItemView;


public class ScoreActivity extends BaseSystemBarTintActivity implements CheckItemView {

    public static final int REQUEST_SCORE_CHECK = 439;

    public static void launch(Activity ctx, ProjectDetail projectDetail, CheckItem item) {
        Intent intent = new Intent(ctx, ScoreActivity.class);
        intent.putExtra("detail", projectDetail);
        intent.putExtra("item", item);
        ctx.startActivityForResult(intent, 111);
    }



    private ProjectDetail projectDetail;
    private CheckItem item;


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
        mType.setText("评分检查6");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_score);
        presenter = new CheckItemPresenter(this);
        initIntent();
        initWidget();

    }

    private void initIntent() {
        projectDetail = (ProjectDetail) getIntent().getSerializableExtra("detail");
        if (projectDetail == null) {
            finish();
            return;
        }

        item = (CheckItem) getIntent().getSerializableExtra("item");
        if (item == null) {
            finish();
            return;
        }
    }

    private TextView score;
    private TextView title;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CheckItemPresenter presenter;
    private ScoreAdapter adapter;
    private ArrayList<Object> mData = new ArrayList<>();
    private CheckItem detail;
    private void initWidget() {
        initBar();

        score = findViewById(R.id.score);
        title = findViewById(R.id.title);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.refresher);

        adapter = new ScoreAdapter(mData, this);
        adapter.setClickListenner(new ScoreAdapter.RecheckClickListenner() {
            @Override
            public void onClick(int pos, CheckItem item) {
                ScoreCheckActivity.launch(ScoreActivity.this, projectDetail, item, REQUEST_SCORE_CHECK);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (projectDetail.check_mode == ProjectDetail.CHECK_MODE_MORE) {
                    presenter.getCheckItemDetail(projectDetail.id,item.checkItemId);
                } else {
                    presenter.getCheckItemDetail(projectDetail.id,item.id);
                }
            }
        });
        if (projectDetail.check_mode == ProjectDetail.CHECK_MODE_MORE) {
            presenter.getCheckItemDetail(projectDetail.id,item.checkItemId);
        } else {
            presenter.getCheckItemDetail(projectDetail.id,item.id);
        }

    }


    @Override
    public void getCheckItemSucc(ArrayList<CheckItem> items) {

    }

    @Override
    public void getCheckItemFailed(int code, String error) {

    }

    @Override
    public void getCheckItemDetailSucc(CheckItem detail) {
        mSwipeRefreshLayout.setRefreshing(false);
        this.detail = detail;
        mData.clear();
        mData.add(detail);
        mData.addAll(detail.childrens);
        adapter.notifyDataSetChanged();

        score.setText("实得分： " + detail.realScore + "分");
        title.setText(detail.name);
    }

    @Override
    public void getCheckItemDetailFailed(int code, String error) {
        mSwipeRefreshLayout.setRefreshing(false);
        ToastUtil.Long(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCORE_CHECK) {
            if (resultCode == RESULT_OK) {
                if (projectDetail.check_mode == ProjectDetail.CHECK_MODE_MORE) {
                    presenter.getCheckItemDetail(projectDetail.id,item.checkItemId);
                } else {
                    presenter.getCheckItemDetail(projectDetail.id,item.id);
                }
            } else if (resultCode == 111) {
                setResult(111);
                finish();
            }
        }
    }
}
