package security.zw.com.securitycheck;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.postbean.CheckBean;
import security.zw.com.securitycheck.postbean.CheckItemDetailBean;
import security.zw.com.securitycheck.utils.Base64Img;
import security.zw.com.securitycheck.utils.DeviceUtils;
import security.zw.com.securitycheck.utils.ImageUtils;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.WindowUtils;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

/**
 * 内容跟RandomCheckActivity一样
 */
public class ScoreCheckActivity extends BaseSystemBarTintActivity {

    private ProjectDetail detail;//对应的项目
    private CheckItem checkItem;// 对应的检查类别
    private ArrayList<ImageInfo> imagePaths = new ArrayList<>();


    private int type;// 选择的违法内容

    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, ScoreCheckActivity.class);
        ctx.startActivity(intent);
    }


    public static void launch(Activity ctx, ProjectDetail detail, CheckItem checkItem, int requestCode) {
        Intent intent = new Intent(ctx, ScoreCheckActivity.class);
        intent.putExtra("detail", detail);
        if (checkItem != null) {
            intent.putExtra("check", checkItem);
        }
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
        if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
            mType.setText("逐项检查");
        } else {
            mType.setText("评分检查");
        }
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        if (ConfigManager.ilegallItems.size() == 0) {
            ConfigManager.getInstance().getBasic();
        }
        setContentView(R.layout.activity_obtain_evidence);
        initWidget();
        if (checkItem.id > 0) {
            loadData();
            recheck_rel.setVisibility(View.GONE);
        } else {
            recheck_rel.setVisibility(View.GONE);
        }

    }


    private RelativeLayout check_result_rel;
    private RelativeLayout illegal_rel;
    private RelativeLayout basic_rel;
    private RelativeLayout respon_rel;
    private RadioGroup checkResult;
    private RadioButton fit;
    private RadioButton unfit;
    private RadioButton unfit2;
    private TextView score_des;


    private View imagesContainer;
    private View photoView;
    private SimpleDraweeView[] imageViews = new SimpleDraweeView[3];
    private View[] imageDeleteBtns = new View[3];

    private EditText illegel;
    private EditText basic;

    private ImageView decrease;
    private TextView count;
    private ImageView increase;

    public int cCount;

    private EditText respon;
    private TextView recheck;
    private RelativeLayout recheck_rel;
    private Button confirm;
    private RelativeLayout count_rel;

    private ProgressDialog mProgressDialog = null;

    private boolean isTwe = false;
    private double min;
    private double max;
    private double unit;


    private boolean isCancel() {
        return mProgressDialog == null || !mProgressDialog.isShowing();
    }

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
                    images = new StringBuilder();
                    complete = false;
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


    private void initWidget() {
        initIntent();
        initBar();

        imagesContainer = findViewById(R.id.images_container);
        imageViews[0] = (SimpleDraweeView) findViewById(R.id.image1);
        imageViews[1] = (SimpleDraweeView) findViewById(R.id.image2);
        imageViews[2] = (SimpleDraweeView) findViewById(R.id.image3);
        imageDeleteBtns[0] = findViewById(R.id.imageDelete1);
        imageDeleteBtns[1] = findViewById(R.id.imageDelete2);
        imageDeleteBtns[2] = findViewById(R.id.imageDelete3);
        photoView = findViewById(R.id.photo_view);
        if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
            photoView.setVisibility(View.GONE);
        }

        checkResult = findViewById(R.id.check_result_group);
        fit = findViewById(R.id.check_result_fit);
        unfit = findViewById(R.id.check_result_unfit);
        unfit2 = findViewById(R.id.check_result_unfit_2);



        check_result_rel = findViewById(R.id.check_result_rel);
        count_rel = findViewById(R.id.count_rel);

        if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
            check_result_rel.setVisibility(View.VISIBLE);
            count_rel.setVisibility(View.GONE);
            unfit.setVisibility(View.VISIBLE);

        } else if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT){
            check_result_rel.setVisibility(View.GONE);
            count_rel.setVisibility(View.VISIBLE);
        }
        score_des = findViewById(R.id.score_des);
        illegal_rel = findViewById(R.id.illegal_rel);
        illegal_rel.setVisibility(View.GONE);

        basic_rel = findViewById(R.id.basic_rel);
        basic_rel.setVisibility(View.GONE);

        illegel = findViewById(R.id.illegal);
        basic = findViewById(R.id.basic);


        decrease = findViewById(R.id.decrease);
        increase = findViewById(R.id.increase);
        count = findViewById(R.id.count);

        if (checkItem != null) {
            score_des.setText(checkItem.min + "_" + checkItem.max);
        }

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (min == max) {
                    if (cCount == 0) {
                        cCount = (int) min;
                    }
                } else {
                    if (cCount == 0) {
                        cCount = (int) max;
                    } else {
                        if (cCount - unit >= min) {
                            cCount -= unit;
                        }
                    }

                }
                count.setText("" + cCount);
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (min == max) {
                    if (cCount == max) {
                        cCount = 0;
                    }
                } else {

                    if (cCount == max) {
                        cCount = 0;
                    } else {
                        if (cCount + unit <= max) {
                            cCount += unit;
                        }
                    }

                }
                count.setText("" + cCount);
            }
        });


        respon_rel = findViewById(R.id.respon_rel);
        respon_rel.setVisibility(View.GONE);
        respon = findViewById(R.id.respon);
        recheck = findViewById(R.id.recheck);
        recheck_rel = findViewById(R.id.recheck_rel);
        recheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ScoreCheckActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                recheck.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        },
                        2018, 8, 9).show();

            }
        });
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        initListenner();
    }

    private void submit() {

        if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT || detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {

            if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
                if (!unfit.isChecked()) {
                    ToastUtil.Short("请选择不合格");
                    return;
                }
            }

            addCheck();
        }


    }

    private void initListenner() {

        findViewById(R.id.select_illegal_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConfigManager.ilegallItems.size() > 0) {
                    selectIlegall();
                } else {
                    ToastUtil.Long("正在同步数据，请稍后选择");
                    ConfigManager.getInstance().getBasic(new ConfigManager.BasicListenner() {
                        @Override
                        public void getBasicListenner() {
                            ToastUtil.Short("同步数据完成");
                        }
                    });
                }
            }
        });

        View.OnClickListener addImageClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageCount = imagePaths.size();
                if (imageCount == 0 || (imageCount < imageViews.length && v == imageViews[imageCount])) {
                    startMediaPicker();
                }
            }
        };

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setOnClickListener(addImageClick);
            final int index = i;
            imageDeleteBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int imageCount = imagePaths.size();
                    if (index < imageCount) {
                        deleteImage(index);
                    }
                }
            });
        }
        resetImageViews();
    }

    private void selectIlegall() {
        new AlertDialog.Builder(this)
                .setItems(ConfigManager.ilegallItemsStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type != which + 1) {
                            type = which + 1;
                            illegel.setText(ConfigManager.ilegallItems.get(which).content);
                            basic.setText(ConfigManager.ilegallItems.get(which).name);
                            illegel.setSelection(illegel.getText().length());
                            basic.setSelection(basic.getText().length());
                        }
                    }
                })
                .show();

    }

    private void startMediaPicker() {
        Intent intent = ImagesPickerActivity.prepareIntent(this, 3 - imagePaths.size());
        startActivityForResult(intent, 699);
    }


    private void initIntent() {
        detail = (ProjectDetail) getIntent().getSerializableExtra("detail");
        if (detail == null) {
            finish();
            return;
        }
        checkItem = (CheckItem) getIntent().getSerializableExtra("check");
        if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT) {
            cCount = (int) checkItem.realScore;
            if (checkItem.max == checkItem.min && checkItem.max == -2) {
                isTwe = true;
                min = -10d;
                max = -2d;
                unit = 2;
            } else {
                isTwe = false;
                min = checkItem.min;
                max = checkItem.max;
                unit = 1;
            }

        }
    }


    private void resetImageViews() {

        int imageCount = imagePaths.size();
        for (int i = 0; i < imageCount; i++) {
            FrescoImageloader.displayImage(imageViews[i], imagePaths.get(i).getImageUrl());
            imageViews[i].setVisibility(View.VISIBLE);
            imageDeleteBtns[i].setVisibility(View.VISIBLE);
        }

        if (imageCount < imageViews.length) {
            FrescoImageloader.displayImage(imageViews[imageCount], "", R.mipmap.image_add);
            imageViews[imageCount].setVisibility(View.VISIBLE);
            imageDeleteBtns[imageCount].setVisibility(View.GONE);
        }
        for (int i = imageCount + 1; i < imageViews.length; i++) {
            imageViews[i].setVisibility(View.GONE);
            imageDeleteBtns[i].setVisibility(View.GONE);
        }
    }

    private void deleteImage(int pos) {
        imagePaths.remove(pos);
        resetImageViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 699 && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                ArrayList<ImageInfo> paths = (ArrayList<ImageInfo>) data.getSerializableExtra(ImagesPickerActivity.PATH_KEY);
                if (paths != null && paths.size() > 0) {
                    for (ImageInfo i : paths) {
                        if (new File(i.getFilePath(ScoreCheckActivity.this)).exists()) {
                            imagePaths.add(i);
                        }
                    }
                }
                resetImageViews();
            }
        }
    }


    Retrofit mRetrofit;
    Constans.AddCheck addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    public void addCheck() {
        showSubmitLoading();


        compressImages();


    }

    private void loadData() {
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.AddCheck.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", checkItem.id);
        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getScoreCheckDetail(requestBody);
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
                                JSONObject object = jsonObject.optJSONObject("data");
                                final CheckBean checkBean = SecurityApplication.getGson().fromJson(object.toString(), CheckBean.class);

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (checkBean.result == 1) {
                                            fit.setChecked(true);
                                            unfit.setChecked(false);
                                            unfit2.setChecked(false);
                                        } else if (checkBean.result == 2) {
                                            fit.setChecked(false);
                                            unfit.setChecked(true);
                                            unfit2.setChecked(false);
                                        } else if (checkBean.result == 3) {
                                            fit.setChecked(false);
                                            unfit.setChecked(false);
                                            unfit2.setChecked(true);
                                        }

                                        illegel.setText(checkBean.ilegalItems);
                                        basic.setText(checkBean.baseItemrs);

                                        count.setText("" + checkBean.score);
                                        respon.setText(checkBean.personLiable);
                                        recheck.setText("");
                                        if (checkItem != null) {
                                            score_des.setText(checkItem.min + "_" + checkItem.max);
                                        }
                                        if (!TextUtils.isEmpty(checkBean.image)) {
                                            String[] imgs = checkBean.image.split(";");
                                            if (imgs.length > 0) {
                                                imagePaths.clear();
                                                for (int i = 0; i < (imgs.length > 3 ? 3 : imgs.length); i++) {
                                                    String url = imgs[i];
                                                    ImageInfo imageInfo = new ImageInfo();
                                                    imageInfo.status = ImageInfo.STATUS_NET;
                                                    imageInfo.url = url;
                                                    imagePaths.add(imageInfo);
                                                }
                                                if (imagePaths.size() > 0) {
                                                    resetImageViews();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.Long("获取评分信息失败，请重试");
                    }
                } else {
                    ToastUtil.Long("获取评分信息失败，请重试");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
                ToastUtil.Long("获取评分信息失败，请重试");
            }
        });

    }

    public void addRandomCheck() {
        get_code = true;
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.AddCheck.class);

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("projectId", detail.id);
        jsonObject.addProperty("checkItemId", checkItem.checkItemId);
        jsonObject.addProperty("checkMode", detail.check_mode);
        jsonObject.addProperty("checkType", detail.check_type);
        if (!TextUtils.isEmpty(illegel.getText().toString())) {
            jsonObject.addProperty("illegalItems", illegel.getText().toString());
        }

        if (!TextUtils.isEmpty(basic.getText().toString())) {
            jsonObject.addProperty("baseItemrs", basic.getText().toString());
        }

        if (!TextUtils.isEmpty(recheck.getText().toString())) {
            jsonObject.addProperty("reCheckTime", recheck.getText().toString());
        }

        if (!TextUtils.isEmpty(respon.getText().toString())) {
            jsonObject.addProperty("personLiable", respon.getText().toString());
        }

        // type:1是随机检查，type：2是评分检查  type：3 是 逐项检查？
        if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
            jsonObject.addProperty("type", 3);

            if (fit.isChecked()) {
                jsonObject.addProperty("result", 1);
            }

            if (unfit.isChecked()) {
                jsonObject.addProperty("result", 2);
            }

            if (unfit2.isChecked()) {
                jsonObject.addProperty("result", 3);
            }

        } else if (detail.check_type == ProjectDetail.CHECK_TYPE_COUNT){
            jsonObject.addProperty("type", 2);
            jsonObject.addProperty("score", Double.parseDouble(count.getText().toString()));
        }

        if (!TextUtils.isEmpty(images)) {
            jsonObject.addProperty("image", images.toString().substring(0, images.length() - 1));
        }

        if (checkItem.id > 0) {
            jsonObject.addProperty("id", checkItem.id);
        }
        /*Gson gson = new Gson();
        CheckBean basicBean = new CheckBean();
        basicBean.userId = SecurityApplication.mUser.id;
        basicBean.projectId = detail.id;
        basicBean.checkItemId = checkItem.checkItemId;
        basicBean.checkMode = detail.check_mode;
        basicBean.checkType = detail.check_type;
        basicBean.ilegalItems = illegel.getText().toString();
        basicBean.baseItemrs = basic.getText().toString();

        basicBean.reCheckTime = recheck.getText().toString();
        basicBean.personLiable = respon.getText().toString();
        basicBean.score = Double.parseDouble(count.getText().toString());

        basicBean.id = checkItem.id;

        if (!TextUtils.isEmpty(images)) {
            basicBean.image = images.toString().substring(0, images.length() - 1);
        }

        String s = gson.toJson(basicBean);*/
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        if (checkItem.id > 0) {
            if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
                mCall = addCheck.updateEveryCheck(requestBody);
            } else {
                mCall = addCheck.updateScoreCheck(requestBody);
            }

        } else {
            if (detail.check_type == ProjectDetail.CHECK_TYPE_EVERY) {
                mCall = addCheck.addEveryCheck(requestBody);
            } else {
                mCall = addCheck.addScoreCheck(requestBody);
            }
        }
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
                                hideSubmitLoading();
                                ToastUtil.Long("评分成功");
                                setResult(RESULT_OK);
                                finish();
                                /*if (detail.check_mode == ProjectDetail.CHECK_MODE_MORE) {
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    showFinishDialog();
                                }*/
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setResult(RESULT_CANCELED);

                        ToastUtil.Long("评分失败");
                        hideSubmitLoading();
                        complete = false;
                        images = new StringBuilder();

                    }
                } else {
                    ToastUtil.Long("评分失败");
                    hideSubmitLoading();
                    complete = false;
                    images = new StringBuilder();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
                ToastUtil.Long("评分失败");
                hideSubmitLoading();
                complete = false;
                images = new StringBuilder();
            }
        });
    }

    public void showFinishDialog() {

        new AlertDialog.Builder(this)
                .setMessage("是否结束本项目的检查？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        postFinish();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                setResult(RESULT_OK);
                finish();
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
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                hideSubmitLoading();
                                ToastUtil.Long("项目结束检查已提交");
                                setResult(111);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideSubmitLoading();
                        showRetryDialog();
                    }
                } else {
                    hideSubmitLoading();
                    showRetryDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                get_code = false;
                hideSubmitLoading();
                showRetryDialog();
            }
        });

    }

    private void showRetryDialog() {
        hideSubmitLoading();
        new AlertDialog.Builder(this)
                .setMessage("项目结束检查提交失败，请重试")
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postFinish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .show();
    }

    StringBuilder images = new StringBuilder();
    boolean complete = false;

    private String compressImages() {

        if (imagePaths.size() == 0) {
            addRandomCheck();
        } else {
            for (int i = 0; i < imagePaths.size(); i++) {
                ImageInfo imageInfo = imagePaths.get(i);
                if (imageInfo.status == ImageInfo.STATUS_NET) {
                    images.append(imageInfo.url);
                    images.append(";");
                    if (i == imagePaths.size() - 1) {
                        complete = true;
                        addRandomCheck();
                    }
                } else if (imageInfo.status == ImageInfo.STATUS_LOCAL) {
                    final Uri uri = Uri.parse(imagePaths.get(i).url);
                    final int j = i;
                    new Compress(new Compress.ICompress() {
                        @Override
                        public void onDone(String output) {
                            images.append(output);
                            images.append(";");
                            if (j == imagePaths.size() - 1) {
                                complete = true;
                                addRandomCheck();
                            }
                        }
                    }).execute(new Uri[]{uri});
                }
            }
        }


        return "";
    }


    private static class Compress extends AsyncTask<Uri, Void, String> {
        final static int MAX = 720;
        final ICompress iCompress;


        Compress(ICompress iCompress) {
            this.iCompress = iCompress;
        }

        static int[] getProperSize(int width, int height) {
            int[] outter = new int[2];
            if (width <= MAX && height <= MAX) {
                outter[0] = width;
                outter[1] = height;
            } else {
                final float ratio = (float) width / height;
                if (ratio > 1.0f) {
                    outter[0] = MAX;
                    outter[1] = Math.round(MAX / ratio);
                } else {
                    outter[1] = MAX;
                    outter[0] = Math.round(MAX * ratio);
                }
            }
            return outter;
        }

        static void compress(String input, String output) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            File file = new File(input);
            if (!file.exists()) {
                return;
            }
            BitmapFactory.decodeFile(input, options);
            final int oriWidth = options.outWidth;
            final int oriHeight = options.outHeight;
            // 糗友圈长图不压缩
            final int[] properSize = getProperSize(oriWidth, oriHeight);


            options.inJustDecodeBounds = false;
            options.inSampleSize = ImageUtils.calculateInSampleSize(options, WindowUtils.getScreenWidth(), WindowUtils.getScreenHeight());
            Bitmap src = BitmapFactory.decodeFile(input, options);
            Bitmap bitmap = Bitmap.createScaledBitmap(src, properSize[0], properSize[1], true);
            if (src != bitmap) {
                src.recycle();
                src = null;
            }
            File send = new File(output);
            File parent = send.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            try {
                FileOutputStream out = new FileOutputStream(send);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        @Override
        protected String doInBackground(Uri... params) {
            String outPath = DeviceUtils.getSDPath() + "/security/send/" + System.currentTimeMillis() + ".jpg";
            Uri input = params[0];

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            File file = new File(input.getPath());
            if (!file.exists()) {
                return input.getPath();
            } else {
                BitmapFactory.decodeFile(input.getPath(), options);
            }
            final int oriWidth = options.outWidth;
            final int oriHeight = options.outHeight;

            compress(input.getPath(), outPath);
            Uri uri = Uri.parse(outPath);
            String b = Base64Img.GetImageStrFromPath(uri.getPath());
            return b;
        }

        @Override
        protected void onPostExecute(String s) {
            if (iCompress != null) {
                iCompress.onDone(s);
            }
        }

        interface ICompress {
            void onDone(String output);
        }
    }


    @Override
    public void finish() {
        super.finish();
    }
}
