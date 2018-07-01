package security.zw.com.securitycheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;
import security.zw.com.securitycheck.adapter.ScoreAdapter;
import security.zw.com.securitycheck.adapter.ScoreListAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.CheckItemView;


public class ScoreForMoreActivity extends BaseSystemBarTintActivity implements CheckItemView, OnRecyclerViewListener.OnItemClickListener {

    public static final int REQUEST_SCORE_CHECK = 439;

    public static void launch(Activity ctx, ProjectDetail projectDetail, CheckItem item) {
        Intent intent = new Intent(ctx, ScoreForMoreActivity.class);
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
        mType.setText("评分检查10" + " " + projectDetail.name);
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
        setContentView(R.layout.activity_score_for_more);
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

    /*private TextView score;
    private TextView title;*/
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CheckItemPresenter presenter;
    private ScoreListAdapter adapter;
    private ArrayList<RecyclerViewData> data = new ArrayList<>();
    private CheckItem detail;

    public int select = -1;


    private void initWidget() {
        initBar();

     /*   score = findViewById(R.id.score);
        title = findViewById(R.id.title);*/
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.refresher);

        adapter = new ScoreListAdapter(this, data);
        adapter.setOnItemClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getCheckItemDetail(projectDetail.id, item.id);
            }
        });

        presenter.getCheckItemDetail(projectDetail.id, item.id);

    }


    @Override
    public void getCheckItemSucc(ArrayList<CheckItem> items) {

    }

    @Override
    public void getCheckItemFailed(int code, String error) {

    }

    @Override
    public void getCheckItemDetailSucc(CheckItem detail) {


        if (data.size() > 0) {
            data.clear();
        }
        for (int i = 0; i < detail.childrens.size(); i++) {
            CheckItem checkItem = detail.childrens.get(i);
            ArrayList<CheckItem> checkItems = null;
            if (checkItem.childrens != null && checkItem.childrens.size() > 0) {
                checkItems = new ArrayList<>();
            } else {
                checkItems = new ArrayList<>();
            }
            data.add(new RecyclerViewData(checkItem, checkItems, false));
        }
        adapter.notifyRecyclerViewData();
        mSwipeRefreshLayout.setRefreshing(false);
        this.detail = detail;
     /*   score.setText("实得分： " + detail.realScore + "分");
        title.setText(detail.name);*/
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
                presenter.getCheckItemDetail(projectDetail.id, item.id);
            } else if (resultCode == 111) {
                setResult(111);
                finish();
            }
        }
    }


    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        RecyclerViewData d = data.get(groupPosition);
        CheckItem checkItem = (CheckItem) d.getGroupData();
        ScoreActivity.launch(ScoreForMoreActivity.this, projectDetail, checkItem);
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {

    }
}
