package security.zw.com.securitycheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;
import security.zw.com.securitycheck.adapter.CheckItemAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.view.CheckItemView;


public class CheckItemForMyCheckDetailActivity extends BaseSystemBarTintActivity implements OnRecyclerViewListener.OnItemClickListener, OnRecyclerViewListener.OnItemLongClickListener, CheckItemView{

    ArrayList<Integer> integerArrayList = new ArrayList<>();

    private MyCheckProjectDetail detail;

    public static void launch(Context ctx, MyCheckProjectDetail detail) {
        Intent intent = new Intent(ctx, CheckItemForMyCheckDetailActivity.class);
        intent.putExtra("detail", detail);
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
        mType.setText("评分检查");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }


    private RecyclerView mRecyclerView;

    private ArrayList<RecyclerViewData> data = new ArrayList<>();
    protected LinearLayoutManager mManager;
    protected CheckItemAdapter mAdapter;

    protected CheckItemPresenter presenter;

    public int select = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }

        integerArrayList.add(656);
        integerArrayList.add(714);
        integerArrayList.add(774);
        integerArrayList.add(829);
        integerArrayList.add(134);
        integerArrayList.add(135);
        integerArrayList.add(136);
        integerArrayList.add(137);
        integerArrayList.add(138);
        integerArrayList.add(139);
        integerArrayList.add(140);
        integerArrayList.add(937);

        setContentView(R.layout.activity_check_item);
        presenter = new CheckItemPresenter(this);
        initWidget();
    }

    private void initWidget() {
        initBar();
        initData();
    }

    private void initData() {
        detail = (MyCheckProjectDetail) getIntent().getSerializableExtra("detail");
        if (detail == null) {
            finish();
            return;
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CheckItemAdapter( this,data, 1);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        presenter.getCheckScoreList(detail.id);
    }


    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {


        boolean hasChildren = false;
        if (groupPosition >= 0) {
            select = groupPosition;
            CheckItem newCheckItem = null;
            for (int i = 0; i < data.size(); i++) {
                RecyclerViewData d = data.get(i);
                CheckItem checkItem = (CheckItem) d.getGroupData();

                if (i == select) {
                    newCheckItem = checkItem;
                }

                if (i != select) {
                    checkItem.isSelected = false;
                } else {
                    checkItem.isSelected = true;
                }
            }
            if (newCheckItem != null && newCheckItem.childrens != null && newCheckItem.childrens.size() > 0) {
                hasChildren = true;
            }
            mAdapter.notifyRecyclerViewData();
        }

        if (!hasChildren) {
            RecyclerViewData d = data.get(groupPosition);
            CheckItem checkItem = (CheckItem) d.getGroupData();
            ScoreForMyCheckDetailActivity.launch(CheckItemForMyCheckDetailActivity.this, detail, checkItem);
        }

    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        RecyclerViewData d = data.get(groupPosition);
        CheckItem checkItem = (CheckItem) d.getChild(childPosition);

        if (integerArrayList.contains(checkItem.id)) {
            ScoreForMoreActivity.launch(CheckItemForMyCheckDetailActivity.this, detail, checkItem, 3);
        } else {
            ScoreForMyCheckDetailActivity.launch(CheckItemForMyCheckDetailActivity.this, detail, checkItem);
        }

    }

    @Override
    public void onGroupItemLongClick(int position, int groupPosition, View view) {

    }

    @Override
    public void onChildItemLongClick(int position, int groupPosition, int childPosition, View view) {

    }

    @Override
    public void getCheckItemSucc(ArrayList<CheckItem> items) {
        for (int i = 0; i< items.size(); i++) {
            CheckItem checkItem = items.get(i);
            ArrayList<CheckItem> checkItems = null;
            if (checkItem.childrens != null && checkItem.childrens.size()> 0) {
                checkItems = checkItem.childrens;
            } else {
                checkItems = new ArrayList<>();
            }
            data.add(new RecyclerViewData(checkItem, checkItems, false));
        }
        mAdapter.notifyRecyclerViewData();
    }

    @Override
    public void getCheckItemFailed(int code, String error) {

    }


    @Override
    public void getCheckItemDetailSucc(CheckItem detail) {

    }

    @Override
    public void getCheckItemDetailFailed(int code, String error) {

    }
}
