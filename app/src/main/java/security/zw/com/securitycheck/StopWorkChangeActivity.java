package security.zw.com.securitycheck;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.adapter.StopWorkAdapter;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.Person;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.StopInfo;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.widget.refresh.SwipeRefreshLayoutBoth;


public class StopWorkChangeActivity extends BaseSystemBarTintActivity {


    public static final String[] CHECK_CLASS_1 = new String[]{
            "是", "否"
    };

    public static final String[] CHECK_CLASS_2 = new String[]{
            "节假日", "政策因素", "资金因素","其他"
    };

    public static void launch(Context ctx, ProjectInfo projectInfo) {
        Intent intent = new Intent(ctx, StopWorkChangeActivity.class);
        intent.putExtra("info", projectInfo);
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

    private TextView name_tv;
    private EditText stop_tv;
    private TextView normal_tv;
    private TextView stop;

    private TextView time_tv;
    private TextView time;
    private EditText start_tv;

    private TextView stopOrStart;

    private ProjectInfo info;
    private StopInfo stopInfo;
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        info = (ProjectInfo) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_stop_start_activity);
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
        mType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mType.setText("停复工");
        stop = findViewById(R.id.stop);
        stopOrStart = findViewById(R.id.stopOrStart);
        name_tv = findViewById(R.id.name_tv);
        stop_tv = findViewById(R.id.stop_tv);
        normal_tv = findViewById(R.id.normal_tv);
        time_tv = findViewById(R.id.time_tv);
        time = findViewById(R.id.time);
        start_tv = findViewById(R.id.start_tv);

        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StopWorkChangeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                time_tv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        },
                        2018, 05, 20).show();

            }
        });
        normal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StopWorkChangeActivity.this)
                        .setItems(CHECK_CLASS_1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                normal_tv.setText(CHECK_CLASS_1[which]);
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.select_stop_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StopWorkChangeActivity.this)
                        .setItems(CHECK_CLASS_2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stop_tv.setText(CHECK_CLASS_2[which]);
                            }
                        })
                        .show();
            }
        });

        if (info.workState == ProjectInfo.TYPE_STOP) {
            stopOrStart.setText("复工");
            time.setText("复工时间");
            stop.setText("复工原因");
        } else if (info.workState == ProjectInfo.TYPE_START) {
            stopOrStart.setText("停工");
            time.setText("停工时间");
            stop.setText("停工原因");
        }
        name_tv.setText(info.name);

        stopOrStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDate();
            }
        });

        loadData();
    }

    private void postDate() {
        if (TextUtils.isEmpty(stop_tv.getText().toString())) {
            ToastUtil.Short("停工原因不能为空");
            return;
        }

        if (TextUtils.isEmpty(normal_tv.getText().toString())) {
            ToastUtil.Short("是否正常停工不能为空");
            return;
        }

        if (TextUtils.isEmpty(time_tv.getText().toString())) {
            ToastUtil.Short("停复工时间不能为空");
            return;
        }

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("projectId", info.id);

        if (isAdd) {
            jsonObject.addProperty("id", 0);
        } else {
            jsonObject.addProperty("id", stopInfo.id);
        }

        jsonObject.addProperty("stopReason", stop_tv.getText().toString());
        jsonObject.addProperty("stopTime", time_tv.getText().toString());
        if (TextUtils.equals(normal_tv.getText().toString(), CHECK_CLASS_1[0])) {
            jsonObject.addProperty("normalStop", 0);
        } else {
            jsonObject.addProperty("normalStop", 1);
        }

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);


        if (isAdd) {
            mCall = addCheck.addDetail(requestBody);
        } else {
            mCall = addCheck.updateDetail(requestBody);
        }

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                ToastUtil.Short("修改状态成功");
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Short("修改状态失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                ToastUtil.Short("修改状态失败");
            }
        });


    }

    Retrofit mRetrofit;
    Constans.GetMyProjectList addCheck;
    Call<String> mCall;
    boolean isLoading;

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("projectId", info.id);


        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.stopDetail(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                isLoading = false;

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                StopInfo stopInfo1 = new Gson().fromJson(jsonObject.optJSONObject("data").toString(), StopInfo.class);
                                if (stopInfo1 != null && stopInfo1.id > 0 && stopInfo1.projectId > 0) {
                                    isAdd = false;
                                    stopInfo = stopInfo1;
                                    if (info.workState == ProjectInfo.TYPE_START) {

                                    } else {
                                        stop_tv.setText(stopInfo1.stopReason);
                                        normal_tv.setText(stopInfo1.normalStop == 0 ? CHECK_CLASS_1[0] : CHECK_CLASS_1[1]);
                                        time_tv.setText(stopInfo1.stopTime);
                                    }

                                    //start_tv.setText(stopInfo1.projectName);

                                } else {
                                    isAdd = true;
                                    stopInfo = null;
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Short("数据获取失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                ToastUtil.Short("数据获取失败");
            }
        });


    }

}
