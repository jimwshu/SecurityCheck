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

import org.json.JSONArray;
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
import security.zw.com.securitycheck.adapter.CheckItemForMoreAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.CheckPerson;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.postbean.PostCheckPersonBean;
import security.zw.com.securitycheck.presenter.CheckItemPresenter;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.CheckItemView;


public class CheckItemForMoreActivity extends BaseSystemBarTintActivity implements OnRecyclerViewListener.OnItemClickListener, OnRecyclerViewListener.OnItemLongClickListener, CheckItemView{


    private ProjectDetail detail;

    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, CheckItemForMoreActivity.class);
        ctx.startActivity(intent);
    }

    public static void launch(Context ctx, ProjectDetail detail) {
        Intent intent = new Intent(ctx, CheckItemForMoreActivity.class);
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
        mType.setText("新建评分检查");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }


    private RecyclerView mRecyclerView;

    private ArrayList<RecyclerViewData> data = new ArrayList<>();
    protected LinearLayoutManager mManager;
    protected CheckItemForMoreAdapter mAdapter;

    protected CheckItemPresenter presenter;

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
        detail = (ProjectDetail) getIntent().getSerializableExtra("detail");
        if (detail == null) {
            finish();
            return;
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CheckItemForMoreAdapter( this,data);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        presenter.getFilter(detail.id);
    }


    @Override
    public void onGroupItemClick(int position, int groupPosition, View view) {

        RecyclerViewData ds = data.get(groupPosition);
        CheckItem checkItem1 = (CheckItem) ds.getGroupData();
        if (checkItem1.assigned == CheckItem.HAS_ASSIGNED) {

            boolean hasChildren = false;
            if (groupPosition >= 0) {
                itemSelect = groupPosition;
                for (int i = 0; i < data.size(); i++) {
                    RecyclerViewData d = data.get(i);
                    CheckItem checkItem = (CheckItem) d.getGroupData();

                    if (i == itemSelect && checkItem.childrens != null && checkItem.childrens.size() > 0) {
                        hasChildren = true;
                    }

                    if (i != itemSelect) {
                        checkItem.isSelected = false;
                    } else {
                        checkItem.isSelected = true;
                    }
                }
                mAdapter.notifyRecyclerViewData();

                // 如果分配给了自己，跳转到评分页面
                if (checkItem1.worker == SecurityApplication.mUser.id) {
                    ScoreActivity.launch(CheckItemForMoreActivity.this, detail, checkItem1);
                } else {
                    if (!hasChildren) {
                        ToastUtil.Long("该项目已经分配");
                    }
                }
            }

        } else {
            boolean hasChildren = false;
            if (groupPosition >= 0) {
                itemSelect = groupPosition;
                for (int i = 0; i < data.size(); i++) {
                    RecyclerViewData d = data.get(i);
                    CheckItem checkItem = (CheckItem) d.getGroupData();

                    if (i == itemSelect && checkItem.childrens != null && checkItem.childrens.size() > 0) {
                        hasChildren = true;
                    }

                    if (i != itemSelect) {
                        checkItem.isSelected = false;
                    } else {
                        checkItem.isSelected = true;
                    }
                }
                mAdapter.notifyRecyclerViewData();
            }

            if (!hasChildren) {
                getCheckPerson(checkItem1);
            }
        }
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
        RecyclerViewData d = data.get(groupPosition);
        CheckItem checkItem = (CheckItem) d.getChild(childPosition);

        if (checkItem.assigned == CheckItem.HAS_ASSIGNED) {
            if (checkItem.worker == SecurityApplication.mUser.id) {
                ScoreActivity.launch(CheckItemForMoreActivity.this, detail, checkItem);
            } else {
                ToastUtil.Long("该项目已经分配");
            }
            return;
        } else {
            // todo 分配项目
            getCheckPerson(checkItem);

        }

        //ScoreActivity.launch(CheckItemActivity.this, detail, checkItem);
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



    private void toShowChcekPerson(final CheckItem checkItem) {
        new AlertDialog.Builder(this)
                .setTitle("请为该项目选择协助检查人员")
                .setSingleChoiceItems(personsName, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selected) {
                        itemSelect = selected;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (itemSelect >= 0) {
                    postCheckPerson(checkItem, itemSelect);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                itemSelect = -1;
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                itemSelect = -1;
            }
        })
                .show();
    }

    public ArrayList<CheckPerson> persons = new ArrayList<>();
    public String[] personsName;
    public int itemSelect;

    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;


    private void getCheckPerson(final CheckItem checkItem) {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.AddCheck.class);
            mCall = addCheck.getCheckPerson();
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
                                        itemSelect = -1;

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.optJSONObject(i);
                                            CheckPerson checkPerson = SecurityApplication.getGson().fromJson(object.toString(), CheckPerson.class);
                                            persons.add(checkPerson);
                                            personsName[i] = (checkPerson.name);
                                        }

                                        toShowChcekPerson(checkItem);
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


    private void postCheckPerson(final CheckItem checkItem, int select) {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.AddCheck.class);

            PostCheckPersonBean postCheckPersonBean = new PostCheckPersonBean();
            postCheckPersonBean.checkItemId = checkItem.id;
            postCheckPersonBean.creator = SecurityApplication.mUser.id;
            postCheckPersonBean.projectId = detail.id;
            postCheckPersonBean.worker = select;

            String s = SecurityApplication.getGson().toJson(postCheckPersonBean);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),s);

            mCall = addCheck.postCheckPerson(requestBody);
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
                                    ToastUtil.Long("分配成功");
                                    presenter.getFilter(detail.id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("分配失败");

                        }
                    } else {
                        ToastUtil.Long("分配失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("分配失败");
                }
            });

        }


    }

}
