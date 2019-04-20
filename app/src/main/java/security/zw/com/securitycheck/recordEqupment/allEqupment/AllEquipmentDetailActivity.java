package security.zw.com.securitycheck.recordEqupment.allEqupment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.EquipmentDetail;
import security.zw.com.securitycheck.bean.EquipmentList;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class AllEquipmentDetailActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx, EquipmentList p) {
        Intent intent = new Intent(ctx, AllEquipmentDetailActivity.class);
        intent.putExtra("info", p);
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
        mType.setText("设备详情");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setText("生成二维码");
        mSubmit.setVisibility(View.GONE);

    }


    public EquipmentList info;
    private EquipmentDetail detail;


    private RelativeLayout equip_detail;// 是否督办
    private TextView equip_detail_title;
    private TextView equip_detail_state;
    private ImageView bar_next;
    private ImageView map;


    private RelativeLayout equip_detail_1;// 是否督办
    private TextView equip_detail_title_1;
    private TextView equip_detail_state_1;
    private ImageView bar_next_1;
    private ImageView map_1;

    private RelativeLayout equip_detail_2;// 是否督办
    private TextView equip_detail_title_2;
    private TextView equip_detail_state_2;
    private ImageView bar_next_2;
    private ImageView map_2;


    private RelativeLayout equip_detail_3;// 是否督办
    private TextView equip_detail_title_3;
    private TextView equip_detail_state_3;
    private ImageView bar_next_3;
    private ImageView map_3;


    private RelativeLayout equip_detail_4;// 是否督办
    private TextView equip_detail_title_4;
    private TextView equip_detail_state_4;
    private ImageView bar_next_4;
    private ImageView map_4;

    private RelativeLayout equip_detail_5;// 是否督办
    private TextView equip_detail_title_5;
    private TextView equip_detail_state_5;
    private ImageView bar_next_5;
    private ImageView map_5;

    private RelativeLayout equip_detail_6;// 是否督办
    private TextView equip_detail_title_6;
    private TextView equip_detail_state_6;
    private ImageView bar_next_6;
    private ImageView map_6;

    private RelativeLayout equip_detail_7;// 是否督办
    private TextView equip_detail_title_7;
    private TextView equip_detail_state_7;
    private ImageView bar_next_7;
    private ImageView map_7;

    private RelativeLayout equip_detail_8;// 是否督办
    private TextView equip_detail_title_8;
    private TextView equip_detail_state_8;
    private ImageView bar_next_8;
    private ImageView map_8;


    private RelativeLayout equip_detail_9;// 是否督办
    private TextView equip_detail_title_9;
    private TextView equip_detail_state_9;
    private ImageView bar_next_9;
    private ImageView map_9;

    private RelativeLayout equip_detail_11;// 是否督办
    private TextView equip_detail_title_11;
    private TextView equip_detail_state_11;
    private ImageView bar_next_11;
    private ImageView map_11;

    private RelativeLayout equip_detail_12;// 是否督办
    private TextView equip_detail_title_12;
    private TextView equip_detail_state_12;
    private ImageView bar_next_12;
    private ImageView map_12;

    private RelativeLayout equip_detail_10;// 是否督办
    private TextView equip_detail_title_10;
    private TextView equip_detail_state_10;
    private ImageView bar_next_10;
    private ImageView map_10;

    private RelativeLayout equip_detail_13;// 是否督办
    private TextView equip_detail_title_13;
    private TextView equip_detail_state_13;
    private ImageView bar_next_13;
    private ImageView map_13;

    private RelativeLayout equip_detail_14;// 是否督办
    private TextView equip_detail_title_14;
    private TextView equip_detail_state_14;
    private ImageView bar_next_14;
    private ImageView map_14;

    private RelativeLayout equip_detail_15;// 是否督办
    private TextView equip_detail_title_15;
    private TextView equip_detail_state_15;
    private ImageView bar_next_15;
    private ImageView map_15;

    private RelativeLayout equip_detail_16;// 是否督办
    private TextView equip_detail_title_16;
    private TextView equip_detail_state_16;
    private ImageView bar_next_16;
    private ImageView map_16;

    private RelativeLayout equip_detail_17;// 是否督办
    private TextView equip_detail_title_17;
    private TextView equip_detail_state_17;
    private ImageView bar_next_17;
    private ImageView map_17;

    private RelativeLayout equip_detail_18;// 是否督办
    private TextView equip_detail_title_18;
    private TextView equip_detail_state_18;
    private ImageView bar_next_18;
    private ImageView map_18;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_equipme_uesd_detail);

        info = (EquipmentList) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
        }

        initWidget();

    }

    private void initWidget() {
        initBar();
        initView();
    }

    private void initView() {


        equip_detail = findViewById(R.id.detail);
        equip_detail_title = equip_detail.findViewById(R.id.title);
        equip_detail_state = equip_detail.findViewById(R.id.state);
        bar_next = equip_detail.findViewById(R.id.bar_next);
        map = equip_detail.findViewById(R.id.map);
        bar_next.setVisibility(View.GONE);
        map.setVisibility(View.GONE);
        equip_detail_title.setText("设备类别");



        equip_detail_1 = findViewById(R.id.detail_1);
        equip_detail_title_1 = equip_detail_1.findViewById(R.id.title);
        equip_detail_state_1 = equip_detail_1.findViewById(R.id.state);
        bar_next_1 = equip_detail_1.findViewById(R.id.bar_next);
        map_1 = equip_detail_1.findViewById(R.id.map);
        bar_next_1.setVisibility(View.GONE);
        map_1.setVisibility(View.GONE);
        equip_detail_title_1.setText("所在项目名称");

        equip_detail_2 = findViewById(R.id.detail_2);
        equip_detail_title_2 = equip_detail_2.findViewById(R.id.title);
        equip_detail_state_2 = equip_detail_2.findViewById(R.id.state);
        bar_next_2 = equip_detail_2.findViewById(R.id.bar_next);
        map_2 = equip_detail_2.findViewById(R.id.map);
        bar_next_2.setVisibility(View.GONE);
        map_2.setVisibility(View.GONE);
        equip_detail_title_2.setText("建设单位");

        equip_detail_3 = findViewById(R.id.detail_3);
        equip_detail_title_3 = equip_detail_3.findViewById(R.id.title);
        equip_detail_state_3 = equip_detail_3.findViewById(R.id.state);
        bar_next_3 = equip_detail_3.findViewById(R.id.bar_next);
        map_3 = equip_detail_3.findViewById(R.id.map);
        bar_next_3.setVisibility(View.GONE);
        map_3.setVisibility(View.GONE);
        equip_detail_title_3.setText("施工单位");

        equip_detail_4 = findViewById(R.id.detail_4);
        equip_detail_title_4 = equip_detail_4.findViewById(R.id.title);
        equip_detail_state_4 = equip_detail_4.findViewById(R.id.state);
        bar_next_4 = equip_detail_4.findViewById(R.id.bar_next);
        map_4 = equip_detail_4.findViewById(R.id.map);
        bar_next_4.setVisibility(View.GONE);
        map_4.setVisibility(View.GONE);
        equip_detail_title_4.setText("监理单位");

        equip_detail_5 = findViewById(R.id.detail_5);
        equip_detail_title_5 = equip_detail_5.findViewById(R.id.title);
        equip_detail_state_5 = equip_detail_5.findViewById(R.id.state);
        bar_next_5 = equip_detail_5.findViewById(R.id.bar_next);
        map_5 = equip_detail_5.findViewById(R.id.map);
        bar_next_5.setVisibility(View.GONE);
        map_5.setVisibility(View.GONE);
        equip_detail_title_5.setText("安拆单位");

        equip_detail_6 = findViewById(R.id.detail_6);
        equip_detail_title_6 = equip_detail_6.findViewById(R.id.title);
        equip_detail_state_6 = equip_detail_6.findViewById(R.id.state);
        bar_next_6 = equip_detail_6.findViewById(R.id.bar_next);
        map_6 = equip_detail_6.findViewById(R.id.map);
        bar_next_6.setVisibility(View.GONE);
        map_6.setVisibility(View.GONE);
        equip_detail_title_6.setText("产权单位");

        equip_detail_7 = findViewById(R.id.detail_7);
        equip_detail_title_7 = equip_detail_7.findViewById(R.id.title);
        equip_detail_state_7 = equip_detail_7.findViewById(R.id.state);
        bar_next_7 = equip_detail_7.findViewById(R.id.bar_next);
        map_7 = equip_detail_7.findViewById(R.id.map);
        bar_next_7.setVisibility(View.GONE);
        map_7.setVisibility(View.GONE);
        equip_detail_title_7.setText("检测单位");

        equip_detail_8 = findViewById(R.id.detail_8);
        equip_detail_title_8 = equip_detail_8.findViewById(R.id.title);
        equip_detail_state_8 = equip_detail_8.findViewById(R.id.state);
        bar_next_8 = equip_detail_8.findViewById(R.id.bar_next);
        map_8 = equip_detail_8.findViewById(R.id.map);
        bar_next_8.setVisibility(View.GONE);
        map_8.setVisibility(View.GONE);
        equip_detail_title_8.setText("产权备案情况");

        equip_detail_9 = findViewById(R.id.detail_9);
        equip_detail_title_9 = equip_detail_9.findViewById(R.id.title);
        equip_detail_state_9 = equip_detail_9.findViewById(R.id.state);
        bar_next_9 = equip_detail_9.findViewById(R.id.bar_next);
        map_9 = equip_detail_9.findViewById(R.id.map);
        bar_next_9.setVisibility(View.GONE);
        map_9.setVisibility(View.GONE);
        equip_detail_title_9.setText("检验检测情况");

        equip_detail_10 = findViewById(R.id.detail_10);
        equip_detail_title_10 = equip_detail_10.findViewById(R.id.title);
        equip_detail_state_10 = equip_detail_10.findViewById(R.id.state);
        bar_next_10 = equip_detail_10.findViewById(R.id.bar_next);
        map_10 = equip_detail_10.findViewById(R.id.map);
        bar_next_10.setVisibility(View.GONE);
        map_10.setVisibility(View.GONE);
        equip_detail_title_10.setText("使用登记情况");

        equip_detail_11 = findViewById(R.id.detail_11);
        equip_detail_title_11 = equip_detail_11.findViewById(R.id.title);
        equip_detail_state_11 = equip_detail_11.findViewById(R.id.state);
        bar_next_11 = equip_detail_11.findViewById(R.id.bar_next);
        map_11 = equip_detail_11.findViewById(R.id.map);
        bar_next_11.setVisibility(View.GONE);
        map_11.setVisibility(View.GONE);
        equip_detail_title_11.setText("设备运行状况");

        equip_detail_12 = findViewById(R.id.detail_12);
        equip_detail_title_12 = equip_detail_12.findViewById(R.id.title);
        equip_detail_state_12 = equip_detail_12.findViewById(R.id.state);
        bar_next_12 = equip_detail_12.findViewById(R.id.bar_next);
        map_12 = equip_detail_12.findViewById(R.id.map);
        bar_next_12.setVisibility(View.GONE);
        map_12.setVisibility(View.GONE);
        equip_detail_title_12.setText("出产厂家");

        equip_detail_13 = findViewById(R.id.detail_13);
        equip_detail_title_13 = equip_detail_13.findViewById(R.id.title);
        equip_detail_state_13 = equip_detail_13.findViewById(R.id.state);
        bar_next_13 = equip_detail_13.findViewById(R.id.bar_next);
        map_13 = equip_detail_13.findViewById(R.id.map);
        bar_next_13.setVisibility(View.GONE);
        map_13.setVisibility(View.GONE);
        equip_detail_title_13.setText("出产编号");

        equip_detail_14 = findViewById(R.id.detail_14);
        equip_detail_title_14 = equip_detail_14.findViewById(R.id.title);
        equip_detail_state_14 = equip_detail_14.findViewById(R.id.state);
        bar_next_14 = equip_detail_14.findViewById(R.id.bar_next);
        map_14 = equip_detail_14.findViewById(R.id.map);
        bar_next_14.setVisibility(View.GONE);
        map_14.setVisibility(View.GONE);
        equip_detail_title_14.setText("出产时间");

        equip_detail_15 = findViewById(R.id.detail_15);
        equip_detail_title_15 = equip_detail_15.findViewById(R.id.title);
        equip_detail_state_15 = equip_detail_15.findViewById(R.id.state);
        bar_next_15 = equip_detail_15.findViewById(R.id.bar_next);
        map_15 = equip_detail_15.findViewById(R.id.map);
        bar_next_15.setVisibility(View.GONE);
        map_15.setVisibility(View.GONE);
        equip_detail_title_15.setText("安装时间");

        equip_detail_16 = findViewById(R.id.detail_16);
        equip_detail_title_16 = equip_detail_16.findViewById(R.id.title);
        equip_detail_state_16 = equip_detail_16.findViewById(R.id.state);
        bar_next_16 = equip_detail_16.findViewById(R.id.bar_next);
        map_16 = equip_detail_16.findViewById(R.id.map);
        bar_next_16.setVisibility(View.GONE);
        map_16.setVisibility(View.GONE);
        equip_detail_title_16.setText("检测时间");

        equip_detail_17 = findViewById(R.id.detail_17);
        equip_detail_title_17 = equip_detail_17.findViewById(R.id.title);
        equip_detail_state_17 = equip_detail_17.findViewById(R.id.state);
        bar_next_17 = equip_detail_17.findViewById(R.id.bar_next);
        map_17 = equip_detail_17.findViewById(R.id.map);
        bar_next_17.setVisibility(View.GONE);
        map_17.setVisibility(View.GONE);
        equip_detail_title_17.setText("备案时间");

        equip_detail_18 = findViewById(R.id.detail_18);
        equip_detail_title_18 = equip_detail_18.findViewById(R.id.title);
        equip_detail_state_18 = equip_detail_18.findViewById(R.id.state);
        bar_next_18 = equip_detail_18.findViewById(R.id.bar_next);
        map_18 = equip_detail_18.findViewById(R.id.map);
        bar_next_18.setVisibility(View.GONE);
        map_18.setVisibility(View.GONE);
        equip_detail_title_18.setText("备注");
    }


    Retrofit mRetrofit;
    Constans.Equipment addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.Equipment.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("equipmentId", info.equipmentId);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getUsedDetail(requestBody);

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

                                JSONObject object = jsonObject.optJSONObject("data");
                                if (object != null) {
                                    EquipmentDetail supervisionProjectList = new Gson().fromJson(object.toString(), EquipmentDetail.class);
                                    if (supervisionProjectList != null) {
                                        equip_detail_state.setText(supervisionProjectList.deviceType);
                                        equip_detail_state_1.setText(supervisionProjectList.projectName);
                                        equip_detail_state_2.setText(supervisionProjectList.constructCompany);
                                        equip_detail_state_3.setText(supervisionProjectList.developmentCompany);
                                        equip_detail_state_4.setText(supervisionProjectList.monitorCompany);
                                        equip_detail_state_5.setText(supervisionProjectList.installCompany);

                                        equip_detail_state_6.setText(supervisionProjectList.propertyCompany);
                                        equip_detail_state_7.setText(supervisionProjectList.checkCompany);
                                        equip_detail_state_8.setText(supervisionProjectList.recordCode);
                                        equip_detail_state_9.setText(supervisionProjectList.checkState);


                                        equip_detail_state_10.setText(supervisionProjectList.useCode);
                                        equip_detail_state_11.setText(supervisionProjectList.equipmentRunState);

                                        equip_detail_state_12.setText(supervisionProjectList.productCompany);
                                        equip_detail_state_13.setText(supervisionProjectList.productCode);
                                        equip_detail_state_14.setText(supervisionProjectList.outCompanyDate);
                                        equip_detail_state_15.setText(supervisionProjectList.installDate);
                                        equip_detail_state_16.setText(supervisionProjectList.checkDate);
                                        equip_detail_state_17.setText(supervisionProjectList.recordDate);
                                        equip_detail_state_18.setText(supervisionProjectList.remark);


                                    }
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

    private void onRefresh() {
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }




}
