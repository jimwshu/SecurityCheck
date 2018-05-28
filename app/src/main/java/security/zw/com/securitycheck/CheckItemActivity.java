package security.zw.com.securitycheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;
import security.zw.com.securitycheck.adapter.CheckItemAdapter;
import security.zw.com.securitycheck.adapter.MyProjectAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.CheckItemView;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class CheckItemActivity extends BaseSystemBarTintActivity implements OnRecyclerViewListener.OnItemClickListener, OnRecyclerViewListener.OnItemLongClickListener, CheckItemView{




    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, CheckItemActivity.class);
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
        mType.setText("新建检查");
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
        setContentView(R.layout.activity_check_item);
        presenter = new CheckItemPresenter(this);
        initWidget();
    }

    private void initWidget() {
        initBar();
        initData();
    }

    private void initData() {
        /*List<CheckItem> bean1 = new ArrayList<>();
        List<CheckItem> bean2 = new ArrayList<>();
        List<CheckItem> bean3 = new ArrayList<>();
        // 每个子列表长度可以不相同
        bean1.add(new CheckItem(1,"Dog"));
        bean2.add(new CheckItem(2, "Cat"));
        bean3.add(new CheckItem(3, "Bird"));

        CheckItem checkItem = new CheckItem(4, "DOG BABA");
        CheckItem checkItem1 = new CheckItem(5, "CAT BABA");
        CheckItem checkItem2 = new CheckItem(6, "BIRD BABA");
        CheckItem checkItem3 = new CheckItem(7, "BIRD BABA");

        data.add(new RecyclerViewData(checkItem, bean1, true));
        data.add(new RecyclerViewData(checkItem1, bean2, true));
        data.add(new RecyclerViewData(checkItem2, bean3, true));
        data.add(new RecyclerViewData(checkItem3, new ArrayList(), false));*/


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CheckItemAdapter( this,data);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        presenter.getProjectList();
    }


    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {
        ToastUtil.Long("group item:" + position + " " + groupPosition);
        if (groupPosition >= 0 && groupPosition != select) {
            select = groupPosition;
            for (int i = 0; i < data.size(); i++) {
                RecyclerViewData d = data.get(i);
                CheckItem checkItem = (CheckItem) d.getGroupData();
                if (i != select) {
                    checkItem.isSelected = false;
                } else {
                    checkItem.isSelected = true;
                }
            }
            mAdapter.notifyRecyclerViewData();
        }


    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        ToastUtil.Long("child item:" + position + " " + groupPosition + " " + childPosition);

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
}
