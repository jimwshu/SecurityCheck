package security.zw.com.securitycheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckPerson;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;
import security.zw.com.securitycheck.zxing.ZxingUtils;

/*扬尘治理详情*/
public class DustDetailActivity extends BaseSystemBarTintActivity {

    public static final int RANDOM_CHECK_REQUEST = 438;

    public int startType;

    public static final String[] CHECK_MODE = new String[]{
            "合格", "不合格"
    };

    public void selectCheckMode() {
        new AlertDialog.Builder(this)
                .setItems(CHECK_MODE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    public static void launch(Context ctx, ProjectDetail p) {
        Intent intent = new Intent(ctx, DustDetailActivity.class);
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
        mType.setText(detail.name);
        mSubmit = findViewById(R.id.submit);
        mSubmit.setText("提交结果");
        mSubmit.setVisibility(View.VISIBLE);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private RelativeLayout name_rel;// 项目名称
    private TextView name_title;
    private TextView name_state;

    private RelativeLayout area_rel;//建筑规模
    private TextView area_title;
    private TextView area_state;


    private RelativeLayout progress_rel;//形象进度
    private TextView progress_title;
    private TextView progress_state;

    private RelativeLayout address_rel;//面积
    private TextView address_title;
    private TextView address_state;


    private RelativeLayout contract;//建设单位
    private TextView contract_name;//建筑单位名称
    private TextView contract_title;//建筑单位名称

    private TextView contract_person;//建筑单位负责人


    private RelativeLayout construction;//施工单位
    private TextView construction_name;//建筑单位名称
    private TextView construction_person;//建筑单位负责人
    private TextView construction_title;//建筑单位名称


    private RelativeLayout monitor;//监管单位
    private TextView monitor_name;//建筑单位名称
    private TextView monitor_person;//建筑单位负// 责人
    private TextView monitor_title;//建筑单位名称

    private TextView check;

    private ProjectDetail detail;


    private RelativeLayout state_1;//围栏施工
    private TextView state_1_title;
    private TextView state_1_state;
    private TextView state_1_choice;

    private RelativeLayout state_2;//施工道路及场地
    private TextView state_2_title;
    private TextView state_2_state;
    private TextView state_2_choice;

    private RelativeLayout state_3;//车辆冲洗
    private TextView state_3_title;
    private TextView state_3_state;
    private TextView state_3_choice;

    private RelativeLayout state_4;//裸土覆盖
    private TextView state_4_title;
    private TextView state_4_state;
    private TextView state_4_choice;

    private RelativeLayout state_5;//喷淋降尘  设备
    private TextView state_5_title;
    private TextView state_5_state;
    private TextView state_5_choice;

    private RelativeLayout state_6;//土方开挖湿法作业
    private TextView state_6_title;
    private TextView state_6_state;
    private TextView state_6_choice;

    private RelativeLayout state_7;//视频监控
    private TextView state_7_title;
    private TextView state_7_state;
    private TextView state_7_choice;

    private RelativeLayout state_8;//扬尘监测
    private TextView state_8_title;
    private TextView state_8_state;
    private TextView state_8_choice;

    private RelativeLayout state_9;//建筑垃圾  建筑材料
    private TextView state_9_title;
    private TextView state_9_state;
    private TextView state_9_choice;

    private RelativeLayout state_10;//"扬尘治理制度及责任人落实情况"
    private TextView state_10_title;
    private TextView state_10_state;
    private TextView state_10_choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_dust_detail);

        detail = (ProjectDetail) getIntent().getSerializableExtra("info");
        if (detail == null) {
            finish();
        }

        initWidget();

    }

    private void initWidget() {
        initBar();
        initView();
    }

    private void initView() {
        check = findViewById(R.id.check);

        name_rel = findViewById(R.id.dust_name);
        name_state = name_rel.findViewById(R.id.state);
        name_title = name_rel.findViewById(R.id.title);
        name_title.setText("项目名称");

        area_rel = findViewById(R.id.dust_area);
        area_state = area_rel.findViewById(R.id.state);
        area_title = area_rel.findViewById(R.id.title);
        area_title.setText("建筑规模");

        progress_rel = findViewById(R.id.dust_progress);
        progress_state = progress_rel.findViewById(R.id.state);
        progress_title = progress_rel.findViewById(R.id.title);
        progress_title.setText("形象进度");

        name_rel = findViewById(R.id.dust_address);
        address_state = address_rel.findViewById(R.id.state);
        address_title = address_rel.findViewById(R.id.title);
        address_title.setText("项目地址");

        contract = findViewById(R.id.contract);
        contract_name = contract.findViewById(R.id.unit);
        contract_title = contract.findViewById(R.id.title);
        contract_person = contract.findViewById(R.id.person);
        contract_title.setText("建设单位");

        contract_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.contract != null) {
                    CompanyDetailActivity.launch(view.getContext(), detail.contract.id, detail.contract.contractCompany);
                }
            }
        });

        contract_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.contract != null) {
                    PersonDetailActivity.launch(view.getContext(), detail.contract.contractLeaderId);
                }
            }
        });

        construction = findViewById(R.id.construction);
        construction_name = construction.findViewById(R.id.unit);
        construction_title = construction.findViewById(R.id.title);

        construction_person = construction.findViewById(R.id.person);
        construction_title.setText("施工单位");

        construction_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.construction != null) {
                    CompanyDetailActivity.launch(view.getContext(), detail.construction.id, detail.construction.constructionCompany);
                }
            }
        });

        construction_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.construction != null) {
                    PersonDetailActivity.launch(view.getContext(), detail.construction.constructionLeaderId);
                }
            }
        });


        monitor = findViewById(R.id.monitor);
        monitor_name = monitor.findViewById(R.id.unit);
        monitor_person = monitor.findViewById(R.id.person);
        monitor_title = monitor.findViewById(R.id.title);
        monitor_title.setText("监理单位");


        monitor_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.monitor != null) {
                    CompanyDetailActivity.launch(view.getContext(), detail.monitor.id, detail.monitor.monitorCompany);
                }
            }
        });

        monitor_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail != null && detail.monitor != null) {
                    PersonDetailActivity.launch(view.getContext(), detail.monitor.projectDirectorId);
                }
            }
        });


        state_1 = findViewById(R.id.state_1);
        state_1_state = state_1.findViewById(R.id.state);
        state_1_title = state_1.findViewById(R.id.title);
        state_1_choice = state_1.findViewById(R.id.choice);
        state_1_title.setText("围栏施工");

        state_2 = findViewById(R.id.state_2);
        state_2_state = state_2.findViewById(R.id.state);
        state_2_title = state_2.findViewById(R.id.title);
        state_2_choice = state_2.findViewById(R.id.choice);
        state_2_title.setText("施工道路及场地");

        state_3 = findViewById(R.id.state_3);
        state_3_state = state_3.findViewById(R.id.state);
        state_3_title = state_3.findViewById(R.id.title);
        state_3_choice = state_3.findViewById(R.id.choice);
        state_3_title.setText("施工道路及场地");



        state_4 = findViewById(R.id.state_4);
        state_4_state = state_4.findViewById(R.id.state);
        state_4_title = state_4.findViewById(R.id.title);
        state_4_choice = state_4.findViewById(R.id.choice);
        state_4_title.setText("施工道路及场地");

        state_5 = findViewById(R.id.state_5);
        state_5_state = state_5.findViewById(R.id.state);
        state_5_title = state_5.findViewById(R.id.title);
        state_5_choice = state_5.findViewById(R.id.choice);
        state_5_title.setText("喷淋降尘设备");

        state_6 = findViewById(R.id.state_6);
        state_6_state = state_6.findViewById(R.id.state);
        state_6_title = state_6.findViewById(R.id.title);
        state_6_choice = state_6.findViewById(R.id.choice);
        state_6_title.setText("土方开挖湿法作业");

        state_7 = findViewById(R.id.state_7);
        state_7_state = state_7.findViewById(R.id.state);
        state_7_title = state_7.findViewById(R.id.title);
        state_7_choice = state_7.findViewById(R.id.choice);
        state_7_title.setText("视频监控");

        state_8 = findViewById(R.id.state_8);
        state_8_state = state_8.findViewById(R.id.state);
        state_8_title = state_8.findViewById(R.id.title);
        state_8_choice = state_8.findViewById(R.id.choice);
        state_8_title.setText("扬尘监测");

        state_9 = findViewById(R.id.state_9);
        state_9_state = state_9.findViewById(R.id.state);
        state_9_title = state_9.findViewById(R.id.title);
        state_9_choice = state_9.findViewById(R.id.choice);
        state_9_title.setText("建筑垃圾建筑材料");

        state_10 = findViewById(R.id.state_10);
        state_10_state = state_10.findViewById(R.id.state);
        state_10_title = state_10.findViewById(R.id.title);
        state_10_choice = state_10.findViewById(R.id.choice);
        state_10_title.setText("扬尘治理制度及责任人落实情况");


    }

    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;



    private void getDetail() {

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

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("获取信息失败");

                        }
                    } else {
                        ToastUtil.Long("获取信息失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("获取信息失败");
                }
            });

        }


    }


    public void showProject(final ProjectDetail detail) {
        this.detail = detail;

        if (detail.contract != null) {
            contract_name.setText(detail.contract.contractCompany);
            contract_person.setText(detail.contract.contractLeader);
        }

        if (detail.construction != null) {
            construction_name.setText(detail.construction.constructionCompany);
            construction_person.setText(detail.construction.constructionLeader);
        }

        if (detail.monitor != null) {
            monitor_name.setText(detail.monitor.monitorCompany);
            monitor_person.setText(detail.monitor.projectDirector);
        }

    }


}
