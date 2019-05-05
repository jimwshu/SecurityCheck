package security.zw.com.securitycheck;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.adapter.CheckCompanyAdapter;
import security.zw.com.securitycheck.adapter.CheckItemAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class CheckCompanyList extends BaseSystemBarTintActivity {

    public static void launch(Activity ctx, int requestCode, SupervisionProjectList list) {
        Intent intent = new Intent(ctx, CheckCompanyList.class);
        intent.putExtra("info", list);
        ctx.startActivityForResult(intent, requestCode);
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

    private SupervisionProjectList list;

    private ImageView mBack;
    private TextView mType;
    private TextView mSubmit;

    private TextView mFinish;

    private int index = -1;

    public void initBar() {
        mBack = findViewById(R.id.cancel);
        mSubmit = findViewById(R.id.submit);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mSubmit.setText("提交检测");
        mType = findViewById(R.id.perrmission);
        mType.setText("检测单位");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.VISIBLE);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index >= 0) {
                    Intent intent = new Intent();
                    intent.putExtra("company", data.get(index));
                    setResult(99, intent);
                    finish();
                } else {
                    ToastUtil.Short("请选择检测单位！");
                }
            }
        });
    }

    ArrayList<Company> data = new ArrayList<>();

    private RecyclerView mRecyclerView;

    protected LinearLayoutManager mManager;
    protected CheckCompanyAdapter mAdapter;
    protected TextView company;

    protected Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }

        setContentView(R.layout.activity_check_company);
        initWidget();
    }

    private void initWidget() {
        initBar();
        initData();
    }

    private ProgressDialog mProgressDialog = null;
    Retrofit mRetrofit;
    Constans.Equipment addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    int num = 0;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            num+=1;
            if (num <= 10) {
                int size = data.size();
                final int newIndex = new Random().nextInt(size);
                index = newIndex;
                company.post(new Runnable() {
                    @Override
                    public void run() {
                        company.setText(data.get(newIndex).name);
                        company.postDelayed(runnable, 500);
                    }
                });
            } else {
                mFinish.setEnabled(true);
            }
        }
    };


    private void showSubmitLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(this, null, "获取资料中，请稍候..", true, true);
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
        list = (SupervisionProjectList) getIntent().getSerializableExtra("info");
        if (list == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
        company = findViewById(R.id.company_name);
        mAdapter = new CheckCompanyAdapter(data, CheckCompanyList.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mFinish = findViewById(R.id.finish_check);
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinish.setEnabled(false);
                num = 0;
                handler.post(runnable);
            }
        });

        getCompay();
    }

    private void getCompay() {

        showSubmitLoading();
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", SecurityApplication.mUser.id);
        jsonObject.addProperty("id", list.id);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.checkCompanyList(requestBody);

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
                                JSONArray object = jsonObject.optJSONArray("data");
                                ArrayList<Company> arrayList = new ArrayList<>();
                                if (object != null && object.length() > 0) {
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject object1 = object.optJSONObject(i);
                                        Company supervisionProjectList = new Gson().fromJson(object1.toString(), Company.class);
                                        arrayList.add(supervisionProjectList);
                                    }
                                }

                                if (arrayList.size() > 0) {
                                    data.clear();
                                    data.addAll(arrayList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                ToastUtil.Long("获取资料失败");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideSubmitLoading();
                        ToastUtil.Long("获取资料失败，请重试");
                    }
                } else {
                    hideSubmitLoading();
                    ToastUtil.Long("获取资料失败，请重试");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
                hideSubmitLoading();
                ToastUtil.Long("获取资料失败，请重试");
            }
        });

    }

}
