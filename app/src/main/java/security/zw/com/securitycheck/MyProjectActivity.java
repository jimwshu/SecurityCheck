package security.zw.com.securitycheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.adapter.MyProjectAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckPerson;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class MyProjectActivity extends BaseSystemBarTintActivity implements MyProjectView {

    public static final String[] ACCESS_PERMISSION = new String[]{
            "我的项目", "监督整改","开工复工","督办项目"
    };

    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    public void selectPermission() {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.AddCheck.class);
            mCall = addCheck.getDepartments();
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    get_code = false;
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.has("code")) {
                                int code = jsonObject.optInt("code");
                                if (code == 0) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        persons.clear();
                                        personsName = new String[jsonArray.length()];
                                        selectedId = -1;

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.optJSONObject(i);
                                            CheckPerson checkPerson = new CheckPerson();
                                            checkPerson = SecurityApplication.getGson().fromJson(object.toString(), CheckPerson.class);
                                            persons.add(checkPerson);
                                            personsName[i] = (checkPerson.name);
                                        }

                                        toShowDepartment();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("获取检查人员失败");

                        }
                    } else {
                        ToastUtil.Long("获取检查人员失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("获取检查人员失败");
                }
            });

        }



    }


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MyProjectActivity.class);
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

    private int type = 1;// 查询类型

    private ImageView mBack;
    private TextView mType;
    private TextView total;
    private EditText search;
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
        search = findViewById(R.id.et_search);
        search.setVisibility(View.VISIBLE);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH  ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String content = search.getText().toString();
                    presenter.getProjectList(type, page, content, selectedId != -1 ? persons.get(selectedId).id : -1);
                    return true;
                }
                return false;
            }
        });
        total = findViewById(R.id.total);
        total.setVisibility(View.VISIBLE);
        mBack = findViewById(R.id.cancel);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mType = findViewById(R.id.perrmission);
        mType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPermission();
            }
        });

        presenter = new MyProjectPresenter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MyProjectAdapter(data, this);
        mSwipeRefreshLayout = (SwipeRefreshLayoutBoth) findViewById(R.id.refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutBoth.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection direction) {
                if (direction == SwipeRefreshLayoutBoth.SwipeRefreshLayoutDirection.TOP) {
                    MyProjectActivity.this.onRefresh();
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
        presenter.getProjectList(type, page, search.getText().toString(), selectedId != -1 ? persons.get(selectedId).id : -1);

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
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        this.hasMore = hasMore;
        this.page = page;

        this.total.setText("共有项目：" + total);

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
    public void getListFailed(int code, String error) {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
        ToastUtil.Short(error);
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

    }

    @Override
    public void getMyCheckProjectDetailListSucc(ArrayList<MyCheckProjectDetail> myCheckProjectDetails, boolean has_more) {

    }

    @Override
    public void getMyCheckProjectDetailListFailed(int code, String error) {

    }

    public ArrayList<CheckPerson> persons = new ArrayList<>();
    public String[] personsName;
    public int selectedId = -1;

    private void toShowDepartment() {
        new AlertDialog.Builder(this)
                .setTitle("请选择部门")
                .setSingleChoiceItems(personsName, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedId = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onRefresh();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectedId = -1;
            }
        })
                .show();
    }

}
