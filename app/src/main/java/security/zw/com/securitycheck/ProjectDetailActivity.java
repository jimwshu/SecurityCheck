package security.zw.com.securitycheck;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.CheckPerson;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.postbean.CheckBean;
import security.zw.com.securitycheck.presenter.MyProjectPresenter;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.view.MyProjectView;


public class ProjectDetailActivity extends BaseSystemBarTintActivity implements MyProjectView {

    public static final int RANDOM_CHECK_REQUEST = 438;

    public int check_type = -1;
    public int check_mode = -1;

    public static final String[] CHECK_CLASS = new String[]{
            "随机检查", "评分检查"
    };

    public static final String[] CHECK_MODE = new String[]{
            "单人模式", "多人模式"
    };


    public void selectCheckClass() {
        new AlertDialog.Builder(this)
                .setItems(CHECK_CLASS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (check_type != which + 1) {
                            check_type = which + 1;
                            check_class_state.setText(CHECK_CLASS[which]);
                        }
                    }
                })
                .show();
    }

    public void selectCheckMode() {
        new AlertDialog.Builder(this)
                .setItems(CHECK_MODE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (check_mode != which + 1) {
                            check_mode = which + 1;
                            check_mode_state.setText(CHECK_MODE[which]);
                        }
                    }
                })
                .show();
    }


    public static void launch(Context ctx, ProjectInfo p) {
        Intent intent = new Intent(ctx, ProjectDetailActivity.class);
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
        mType.setText(info.name);
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }



    public ProjectInfo info;
    private RelativeLayout is_checked;// 是否督办
    private TextView is_checked_title;
    private TextView is_checked_state;
    private ImageView bar_next_1;
    private ImageView map_1;

    private RelativeLayout safer;//安检员
    private TextView safer_title;
    private TextView safer_state;
    private ImageView bar_next_2;
    private ImageView map_2;

    private RelativeLayout construct;//结构
    private TextView construct_title;
    private TextView construct_state;
    private ImageView bar_next_3;
    private ImageView map_3;



    private RelativeLayout area;//面积
    private TextView area_title;
    private TextView area_state;
    private ImageView bar_next_4;
    private ImageView map_4;


    private RelativeLayout cost;//造价
    private TextView cost_title;
    private TextView cost_state;
    private ImageView bar_next_5;
    private ImageView map_5;


    private RelativeLayout state;//状态
    private TextView state_title;
    private TextView state_state;
    private ImageView bar_next_6;
    private ImageView map_6;


    private RelativeLayout image;//形象进度
    private TextView image_title;
    private TextView image_state;
    private ImageView bar_next_7;
    private ImageView map_7;


    private RelativeLayout district;//所属片区
    private TextView district_title;
    private TextView district_state;
    private ImageView bar_next_8;
    private ImageView map_8;


    private RelativeLayout address;//地址
    private TextView address_title;
    private TextView address_state;
    private ImageView bar_next_9;
    private ImageView map_9;

    private RelativeLayout check_class;//地址
    private TextView check_class_title;
    private TextView check_class_state;
    private ImageView bar_next_10;
    private ImageView map_10;


    private RelativeLayout check_mode_rel;//地址
    private TextView check_mode_title;
    private TextView check_mode_state;
    private ImageView bar_next_11;
    private ImageView map_11;



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
    private MyProjectPresenter presenter;
    private ProjectDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_project_detail);

        info = (ProjectInfo) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
        }
        initWidget();

        presenter = new MyProjectPresenter(this);
        presenter.getProjecById(info.id);
    }

    private void initWidget() {
        initBar();
        initView();
    }

    private void initView() {
        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_type == -1) {
                    ToastUtil.Short("请选择检查类别");
                    return;
                }
                if (check_mode == -1) {
                    ToastUtil.Short("请选择检查模式");
                    return;
                }

                detail.check_type = check_type;
                detail.check_mode = check_mode;

                // 单人检查 并且是 随机模式
                if (detail.check_mode == ProjectDetail.CHECK_MODE_SINGLE) {
                    if (detail.check_type == ProjectDetail.CHECK_TYPE_RANDOM) {
                        RandomCheckActivity.launch(ProjectDetailActivity.this, detail, null, RANDOM_CHECK_REQUEST);
                    } else if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT) {
                        CheckItemActivity.launch(view.getContext(), detail);
                    }
                } else if (detail.check_mode == ProjectDetail.CHECK_MODE_MORE){
                    if (detail.check_type == ProjectDetail.CHECK_TYPE_RANDOM) {
                        getCheckPerson();
                    } else if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT) {
                        CheckItemForMoreActivity.launch(ProjectDetailActivity.this, detail);
                    }
                }



            }
        });

        is_checked = findViewById(R.id.is_checked);
        is_checked_title = is_checked.findViewById(R.id.title);
        is_checked_state = is_checked.findViewById(R.id.state);
        bar_next_1 = is_checked.findViewById(R.id.bar_next);
        map_1 = is_checked.findViewById(R.id.map);
        bar_next_1.setVisibility(View.GONE);
        map_1.setVisibility(View.GONE);
        is_checked_title.setText("是否督办");

        safer = findViewById(R.id.safer);
        safer_title = safer.findViewById(R.id.title);
        safer_state = safer.findViewById(R.id.state);
        safer_title.setText("安监员");

        bar_next_2 = safer.findViewById(R.id.bar_next);
        map_2 = safer.findViewById(R.id.map);
        bar_next_2.setVisibility(View.GONE);
        map_2.setVisibility(View.GONE);

        construct = findViewById(R.id.construct);
        construct_title = construct.findViewById(R.id.title);
        construct_state = construct.findViewById(R.id.state);
        construct_title.setText("结构");
        bar_next_3 = construct.findViewById(R.id.bar_next);
        map_3 = construct.findViewById(R.id.map);
        bar_next_3.setVisibility(View.GONE);
        map_3.setVisibility(View.GONE);


        area = findViewById(R.id.area);
        area_title = area.findViewById(R.id.title);
        area_state = area.findViewById(R.id.state);
        area_title.setText("面积");
        bar_next_4 = area.findViewById(R.id.bar_next);
        map_4 = area.findViewById(R.id.map);
        bar_next_4.setVisibility(View.GONE);
        map_4.setVisibility(View.GONE);


        cost = findViewById(R.id.cost);
        cost_title = cost.findViewById(R.id.title);
        cost_state = cost.findViewById(R.id.state);
        cost_title.setText("造价");
        bar_next_5 = cost.findViewById(R.id.bar_next);
        map_5 = cost.findViewById(R.id.map);
        bar_next_5.setVisibility(View.GONE);
        map_5.setVisibility(View.GONE);



        state = findViewById(R.id.state_s);
        state_title = state.findViewById(R.id.title);
        state_state = state.findViewById(R.id.state);
        state_title.setText("状态");

        bar_next_6 = state.findViewById(R.id.bar_next);
        map_6 = state.findViewById(R.id.map);
        bar_next_6.setVisibility(View.GONE);
        map_6.setVisibility(View.GONE);



        image = findViewById(R.id.image);
        image_title = image.findViewById(R.id.title);
        image_state = image.findViewById(R.id.state);
        image_title.setText("形象进度");

        bar_next_7 = image.findViewById(R.id.bar_next);
        map_7 = image.findViewById(R.id.map);
        bar_next_7.setVisibility(View.GONE);
        map_7.setVisibility(View.GONE);


        district = findViewById(R.id.district);
        district_title = district.findViewById(R.id.title);
        district_state = district.findViewById(R.id.state);
        district_title.setText("所属片区");

        bar_next_8 = district.findViewById(R.id.bar_next);
        map_8 = district.findViewById(R.id.map);
        bar_next_8.setVisibility(View.GONE);
        map_8.setVisibility(View.GONE);


        address = findViewById(R.id.address);
        address_title = address.findViewById(R.id.title);
        address_state = address.findViewById(R.id.state);
        address_title.setText("地址");

        bar_next_9 = address.findViewById(R.id.bar_next);
        map_9 = address.findViewById(R.id.map);
        bar_next_9.setVisibility(View.VISIBLE);
        map_9.setVisibility(View.VISIBLE);



        contract = findViewById(R.id.contract);
        contract_name = contract.findViewById(R.id.unit);
        contract_title = contract.findViewById(R.id.title);
        contract_person = contract.findViewById(R.id.person);
        contract_title.setText("建设单位");


        construction = findViewById(R.id.construction);
        construction_name = construction.findViewById(R.id.unit);
        construction_title = construction.findViewById(R.id.title);

        construction_person = construction.findViewById(R.id.person);
        construction_title.setText("施工单位");


        monitor = findViewById(R.id.monitor);
        monitor_name = monitor.findViewById(R.id.unit);
        monitor_person = monitor.findViewById(R.id.person);
        monitor_title = monitor.findViewById(R.id.title);
        monitor_title.setText("监理单位");

        check_class = findViewById(R.id.check_class);//地址
        check_class_title = check_class.findViewById(R.id.title);
        check_class_state = check_class.findViewById(R.id.state);

        check_class_title.setText("检查类别");
        bar_next_10 = check_class.findViewById(R.id.bar_next);
        map_10 = check_class.findViewById(R.id.map);
        bar_next_10.setVisibility(View.VISIBLE);
        map_10.setVisibility(View.GONE);


        check_mode_rel = findViewById(R.id.check_mode);//地址
        check_mode_title = check_mode_rel.findViewById(R.id.title);
        check_mode_state = check_mode_rel.findViewById(R.id.state);

        check_mode_title.setText("检查模式");

        bar_next_11 = check_mode_rel.findViewById(R.id.bar_next);
        map_11 = check_mode_rel.findViewById(R.id.map);
        bar_next_11.setVisibility(View.VISIBLE);
        map_11.setVisibility(View.GONE);

        check_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCheckClass();
            }
        });

        check_mode_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCheckMode();
            }
        });
    }

    public ArrayList<CheckPerson> persons = new ArrayList<>();
    public String[] personsName;
    public boolean selected[];

    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    private void getCheckPerson() {

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
                                        selected = new boolean[jsonArray.length()];

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.optJSONObject(i);
                                            CheckPerson checkPerson = new CheckPerson();
                                            checkPerson = SecurityApplication.getGson().fromJson(object.toString(), CheckPerson.class);
                                            persons.add(checkPerson);
                                            personsName[i] = (checkPerson.name);
                                            selected[i] = false;
                                        }

                                        toShowChcekPerson();
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

    private void toShowChcekPerson() {
        new AlertDialog.Builder(this)
                .setTitle("请选择人员")
                .setMultiChoiceItems(personsName, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        stringBuilder.append(persons.get(i).id + ",");
                    }
                }

                if (stringBuilder.length() > 0) {
                    String s = stringBuilder.substring(0, stringBuilder.length()-1).toString();
                    detail.assistPersonIds = s;
                }

                if (detail.check_type == ProjectDetail.CHECK_TYPE_RANDOM) {
                    RandomCheckActivity.launch(ProjectDetailActivity.this, detail, null, RANDOM_CHECK_REQUEST);
                }

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .show();
    }


    @Override
    public void getListSucc(ArrayList<ProjectInfo> projectInfos, boolean hasMore, int page) {

    }

    @Override
    public void getListFailed(int code, String error) {

    }

    @Override
    public void getProjectSucc(ProjectDetail detail) {
        this.detail = detail;
        if (detail.supervise == 0) {
            is_checked_state.setTextColor(0xffd0021b);
            is_checked_state.setText("是");
        } else {
            is_checked_state.setTextColor(0xffd0021b);
            is_checked_state.setText("否");
        }

        safer_state.setText(detail.safetyInspector);
        construct_state.setText(detail.structure);
        area_state.setText(detail.area);
        cost_state.setText(detail.cost);

        image_state.setText(detail.imageProgress);

        district_state.setText(detail.district);
        state_state.setText(detail.state);
        address_state.setText(detail.address);

        contract_name.setText(detail.contract.contractCompany);
        contract_person.setText(detail.contract.contractLeader);

        construction_name.setText(detail.construction.constructionCompany);
        construction_person.setText(detail.construction.constructionLeader);

        monitor_name.setText(detail.monitor.monitorCompany);
        monitor_person.setText(detail.monitor.projectDirector);

    }

    @Override
    public void getProjectFailed(int code, String error) {
        ToastUtil.Long(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RANDOM_CHECK_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
            return;
        }
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
}
