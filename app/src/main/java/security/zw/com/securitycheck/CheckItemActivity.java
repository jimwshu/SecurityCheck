package security.zw.com.securitycheck;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import drawthink.expandablerecyclerview.listener.OnRecyclerViewListener;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.adapter.CheckItemAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.CheckItemView;


public class CheckItemActivity extends BaseSystemBarTintActivity implements OnRecyclerViewListener.OnItemClickListener, OnRecyclerViewListener.OnItemLongClickListener, CheckItemView{


    ArrayList<Integer> integerArrayList = new ArrayList<>();

    private ProjectDetail detail;

    public static void launch(Context ctx, ProjectDetail detail) {
        Intent intent = new Intent(ctx, CheckItemActivity.class);
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
        if (detail.check_type == ProjectDetail.CHECK_TYPE_DUST) {
            mType.setText("扬尘治理");
        } else {
            mType.setText("新建评分检查");
        }
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }


    private RecyclerView mRecyclerView;

    private ArrayList<RecyclerViewData> data = new ArrayList<>();
    protected LinearLayoutManager mManager;
    protected CheckItemAdapter mAdapter;

    protected CheckItemPresenter presenter;

    public int select = -1;
    protected TextView finish_check;

    protected RelativeLayout check_for_dust;
    protected TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        detail = (ProjectDetail) getIntent().getSerializableExtra("detail");
        if (detail == null) {
            finish();
            return;
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
    private ProgressDialog mProgressDialog = null;
    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    private void showSubmitLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(this, null, "发布中，请稍候..", true, true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCall != null && mCall.isExecuted()) {
                    mCall.cancel();
                }
            }
        });
    }

    private void hideSubmitLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void initData() {

        check_for_dust = findViewById(R.id.check_for_dust);
        pass = findViewById(R.id.pass);
        finish_check = findViewById(R.id.finish_check);
        if (detail.check_type == ProjectDetail.CHECK_TYPE_DUST) {
            check_for_dust.setVisibility(View.VISIBLE);
            mAdapter = new CheckItemAdapter( this,data, 4);
            finish_check.setVisibility(View.GONE);
        } else {
            check_for_dust.setVisibility(View.GONE);
            mAdapter = new CheckItemAdapter( this,data);
            finish_check.setVisibility(View.VISIBLE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        presenter.getProjectList(detail.id, detail.check_type);

        finish_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFinishDialog();
            }
        });
    }
    public void showFinishDialog() {

        new AlertDialog.Builder(this)
                .setMessage("是否结束本项目的检查？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        postFinish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        })
                .show();
    }

    private void postFinish() {
        showSubmitLoading();
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.AddCheck.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("projectId", detail.id);
        jsonObject.addProperty("creator", SecurityApplication.mUser.id);
        jsonObject.addProperty("checkType", detail.check_type);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.finishCheck(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                get_code = false;
                hideSubmitLoading();
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                ToastUtil.Long("项目结束检查已提交");
                                setResult(111);
                                finish();
                            } else {
                                ToastUtil.Long("项目结束检查失败");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideSubmitLoading();
                        ToastUtil.Long("项目结束检查提交失败，请重试");
                    }
                } else {
                    hideSubmitLoading();
                    ToastUtil.Long("项目结束检查提交失败，请重试");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
                hideSubmitLoading();
                ToastUtil.Long("项目结束检查提交失败，请重试");
            }
        });

    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {


        boolean hasChildren = false;
        if (groupPosition >= 0) {
            select = groupPosition;
            for (int i = 0; i < data.size(); i++) {
                RecyclerViewData d = data.get(i);
                CheckItem checkItem = (CheckItem) d.getGroupData();
                if (i != select) {
                    checkItem.isSelected = false;
                } else {
                    checkItem.isSelected = true;
                    if (checkItem.childrens != null && checkItem.childrens.size() > 0) {
                        hasChildren = true;
                    }
                }
            }
            mAdapter.notifyRecyclerViewData();
        }
        if (!hasChildren) {
            RecyclerViewData d = data.get(groupPosition);
            CheckItem checkItem = (CheckItem) d.getGroupData();

            if (detail.check_type == 3) {
                ScoreForMoreActivity.launch(CheckItemActivity.this, detail, checkItem, 1);
            } else if (detail.check_type == ProjectDetail.CHECK_TYPE_DUST) {
                // 直接去评分页面
                DustCheckActivity.launch(CheckItemActivity.this, detail, checkItem, 4);
            } else {
                ScoreActivity.launch(CheckItemActivity.this, detail, checkItem);
            }
        }

    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        RecyclerViewData d = data.get(groupPosition);
        CheckItem checkItem = (CheckItem) d.getChild(childPosition);

        if (integerArrayList.contains(checkItem.id)) {
            ScoreForMoreActivity.launch(CheckItemActivity.this, detail, checkItem, 2);
        } else {
            ScoreActivity.launch(CheckItemActivity.this, detail, checkItem);
        }

        //RandomCheckActivity.launch(this, detail, checkItem);
    }

    @Override
    public void onGroupItemLongClick(int position, int groupPosition, View view) {

    }

    @Override
    public void onChildItemLongClick(int position, int groupPosition, int childPosition, View view) {

    }

    @Override
    public void getCheckItemSucc(ArrayList<CheckItem> items) {

        if (data.size() > 0) {
            data.clear();
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 111) {
            setResult(111);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getProjectList(detail.id, detail.check_type);
    }
}
