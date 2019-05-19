package security.zw.com.securitycheck.enforceLow;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.ScoreCheckActivity;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.SupervisionProjecDetail;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.Base64Img;
import security.zw.com.securitycheck.utils.DeviceUtils;
import security.zw.com.securitycheck.utils.ImageUtils;
import security.zw.com.securitycheck.utils.TimeUtils;
import security.zw.com.securitycheck.utils.WindowUtils;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class MySupervisionProjectDetailActivity extends BaseSystemBarTintActivity {




    public static void launch(Context ctx, SupervisionProjectList p) {
        Intent intent = new Intent(ctx, MySupervisionProjectDetailActivity.class);
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
        mType.setText("执法详情");
        mSubmit = findViewById(R.id.submit);
        mSubmit.setVisibility(View.GONE);
    }



    public SupervisionProjectList info;
    public int checkType;


    private RelativeLayout is_checked;// 整改名称
    private TextView is_checked_title;
    private TextView is_checked_state;
    private ImageView bar_next_1;
    private ImageView map_1;

    private RelativeLayout safer;//整改单编号
    private TextView safer_title;
    private TextView safer_state;
    private ImageView bar_next_2;
    private ImageView map_2;

    private RelativeLayout construct;//是否督办
    private TextView construct_title;
    private TextView construct_state;
    private ImageView bar_next_3;
    private ImageView map_3;



    private RelativeLayout area;//违法主体
    private TextView area_title;
    private TextView area_state;
    private ImageView bar_next_4;
    private ImageView map_4;


    private RelativeLayout cost;//下单时间
    private TextView cost_title;
    private TextView cost_state;
    private ImageView bar_next_5;
    private ImageView map_5;


    private RelativeLayout state;//整改期限
    private TextView state_title;
    private TextView state_state;
    private ImageView bar_next_6;
    private ImageView map_6;


    private RelativeLayout image;//违法内容
    private TextView image_title;
    private TextView image_state;
    private ImageView bar_next_7;
    private ImageView map_7;


    private RelativeLayout district;//违法依据
    private TextView district_title;
    private TextView district_state;
    private ImageView bar_next_8;
    private ImageView map_8;


    private RelativeLayout address;//整改状态
    private TextView address_title;
    private TextView address_state;
    private ImageView bar_next_9;
    private ImageView map_9;

    private RelativeLayout check_class;//违法照片
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
    private TextView change;

    private SimpleDraweeView[] imageViews = new SimpleDraweeView[3];

    private RelativeLayout photo_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_my_enforce_low_detail);

        info = (SupervisionProjectList) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
        }
        checkType = info.checkType;
        initWidget();

    }

    private void initWidget() {
        initBar();
        initView();
    }

    private void initView() {
        check = findViewById(R.id.check);
        change = findViewById(R.id.change);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImages(1);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImages(2);

            }
        });
        photo_view = findViewById(R.id.photo_view);
        if (checkType == 3) {
            photo_view.setVisibility(View.GONE);
        } else {
            photo_view.setVisibility(View.VISIBLE);
        }
        imageViews[0] = (SimpleDraweeView) findViewById(R.id.image1);
        imageViews[1] = (SimpleDraweeView) findViewById(R.id.image2);
        imageViews[2] = (SimpleDraweeView) findViewById(R.id.image3);


        addImageViews[0] = (SimpleDraweeView) findViewById(R.id.image4);
        addImageViews[1] = (SimpleDraweeView) findViewById(R.id.image5);
        addImageViews[2] = (SimpleDraweeView) findViewById(R.id.image6);

        imageDeleteBtns[0] = findViewById(R.id.imageDelete4);
        imageDeleteBtns[1] = findViewById(R.id.imageDelete5);
        imageDeleteBtns[2] = findViewById(R.id.imageDelete6);

        is_checked = findViewById(R.id.is_checked);
        is_checked_title = is_checked.findViewById(R.id.title);
        is_checked_state = is_checked.findViewById(R.id.state);
        bar_next_1 = is_checked.findViewById(R.id.bar_next);
        map_1 = is_checked.findViewById(R.id.map);
        bar_next_1.setVisibility(View.GONE);
        map_1.setVisibility(View.GONE);
        is_checked_title.setText("执法名称");

        safer = findViewById(R.id.safer);
        safer_title = safer.findViewById(R.id.title);
        safer_state = safer.findViewById(R.id.state);
        safer_title.setText("执法编号");

        bar_next_2 = safer.findViewById(R.id.bar_next);
        map_2 = safer.findViewById(R.id.map);
        bar_next_2.setVisibility(View.GONE);
        map_2.setVisibility(View.GONE);

        construct = findViewById(R.id.construct);
        construct_title = construct.findViewById(R.id.title);
        construct_state = construct.findViewById(R.id.state);
        construct_title.setText("是否督办");
        bar_next_3 = construct.findViewById(R.id.bar_next);
        map_3 = construct.findViewById(R.id.map);
        bar_next_3.setVisibility(View.GONE);
        map_3.setVisibility(View.GONE);


        area = findViewById(R.id.area);
        area_title = area.findViewById(R.id.title);
        area_state = area.findViewById(R.id.state);
        area_title.setText("违法主体");
        bar_next_4 = area.findViewById(R.id.bar_next);
        map_4 = area.findViewById(R.id.map);
        bar_next_4.setVisibility(View.GONE);
        map_4.setVisibility(View.GONE);


        cost = findViewById(R.id.cost);
        cost_title = cost.findViewById(R.id.title);
        cost_state = cost.findViewById(R.id.state);
        cost_title.setText("下达时间");
        bar_next_5 = cost.findViewById(R.id.bar_next);
        map_5 = cost.findViewById(R.id.map);
        bar_next_5.setVisibility(View.GONE);
        map_5.setVisibility(View.GONE);



        state = findViewById(R.id.state_s);
        state_title = state.findViewById(R.id.title);
        state_state = state.findViewById(R.id.state);
        state_title.setText("整改期限");

        bar_next_6 = state.findViewById(R.id.bar_next);
        map_6 = state.findViewById(R.id.map);
        bar_next_6.setVisibility(View.GONE);
        map_6.setVisibility(View.GONE);



        image = findViewById(R.id.image);
        image_title = image.findViewById(R.id.title);
        image_state = image.findViewById(R.id.state);
        image_title.setText("违法内容");

        bar_next_7 = image.findViewById(R.id.bar_next);
        map_7 = image.findViewById(R.id.map);
        bar_next_7.setVisibility(View.GONE);
        map_7.setVisibility(View.GONE);


        district = findViewById(R.id.district);
        district_title = district.findViewById(R.id.title);
        district_state = district.findViewById(R.id.state);
        district_title.setText("违法依据");

        bar_next_8 = district.findViewById(R.id.bar_next);
        map_8 = district.findViewById(R.id.map);
        bar_next_8.setVisibility(View.GONE);
        map_8.setVisibility(View.GONE);

        if (checkType == 3) {
            district.setVisibility(View.GONE);
        } else {
            district.setVisibility(View.VISIBLE);
        }

        address = findViewById(R.id.address);
        address_title = address.findViewById(R.id.title);
        address_state = address.findViewById(R.id.state);
        address_title.setText("整改状态");

        bar_next_9 = address.findViewById(R.id.bar_next);
        map_9 = address.findViewById(R.id.map);
        bar_next_9.setVisibility(View.GONE);
        map_9.setVisibility(View.GONE);


        contract = findViewById(R.id.contract);
        contract_name = contract.findViewById(R.id.unit);
        contract_title = contract.findViewById(R.id.title);
        contract_person = contract.findViewById(R.id.person);
        contract_title.setText("建设单位简介");


        construction = findViewById(R.id.construction);
        construction_name = construction.findViewById(R.id.unit);
        construction_title = construction.findViewById(R.id.title);

        construction_person = construction.findViewById(R.id.person);
        construction_title.setText("施工单位简介");


        monitor = findViewById(R.id.monitor);
        monitor_name = monitor.findViewById(R.id.unit);
        monitor_person = monitor.findViewById(R.id.person);
        monitor_title = contract.findViewById(R.id.title);
        monitor_title.setText("监理单位简介");

        check_class = findViewById(R.id.check_class);//地址
        check_class_title = check_class.findViewById(R.id.title);
        check_class_state = check_class.findViewById(R.id.state);

        check_class_title.setText("违法照片");
        bar_next_10 = check_class.findViewById(R.id.bar_next);
        map_10 = check_class.findViewById(R.id.map);
        bar_next_10.setVisibility(View.GONE);
        map_10.setVisibility(View.GONE);


        check_mode_rel = findViewById(R.id.check_mode);//地址
        check_mode_title = check_mode_rel.findViewById(R.id.title);
        check_mode_state = check_mode_rel.findViewById(R.id.state);

        check_mode_title.setText("检查模式");

        bar_next_11 = check_mode_rel.findViewById(R.id.bar_next);
        map_11 = check_mode_rel.findViewById(R.id.map);
        bar_next_11.setVisibility(View.VISIBLE);
        map_11.setVisibility(View.GONE);
        check_mode_rel.setVisibility(View.GONE);



        View.OnClickListener addImageClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageCount = imagePaths.size();
                if (imageCount == 0 || (imageCount < addImageViews.length && v == addImageViews[imageCount])) {
                    startMediaPicker();
                }
            }
        };

        for (int i = 0; i < imageViews.length; i++) {
            addImageViews[i].setOnClickListener(addImageClick);
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

        loadData();
    }


    /**
     * @param status 1合格，2不合格
     */
    private void postResult(int status) {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            jsonObject.addProperty("id", supervisionProjecDetail.id);
            jsonObject.addProperty("status", status);

            // todo images
            if (!TextUtils.isEmpty(images)) {
                jsonObject.addProperty("interviewRecord", images.toString().substring(0, images.length() - 1));
            }


            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
            mCall = addCheck.enforceLawDetailDeal(requestBody);
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    hideSubmitLoading();
                    get_code = false;
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.has("code")) {
                                int code = jsonObject.optInt("code");
                                if (code == 0) {
                                    ToastUtil.Long("修改状态成功");
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("修改状态失败");

                        }
                    } else {
                        ToastUtil.Long("修改状态失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hideSubmitLoading();
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("修改状态失败");
                }
            });

        }


    }


    SupervisionProjecDetail supervisionProjecDetail;
    Retrofit mRetrofit;
    Constans.GetMyProjectList addCheck;
    Call<String> mCall;
    private boolean get_code = false;

    private void loadData() {

        if (!get_code) {
            get_code = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            jsonObject.addProperty("id", info.id);
            //todo checkmode
            jsonObject.addProperty("checkMode", info.checkMode);
            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
            mCall = addCheck.enforceLawDetail(requestBody);
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
                                    supervisionProjecDetail = new Gson().fromJson(jsonObject.optString("data"), SupervisionProjecDetail.class);
                                    is_checked_state.setText(supervisionProjecDetail.projectName);
                                    safer_state.setText(supervisionProjecDetail.code + "");

                                    construct_state.setText(supervisionProjecDetail.supervise > 0 ? "已监督" : "未监督");
                                    area_state.setText(supervisionProjecDetail.ilegalCompany);
                                    cost_state.setText(TimeUtils.secToTime(supervisionProjecDetail.createTime));
                                    state_state.setText(TimeUtils.secToTime(supervisionProjecDetail.reCheckTime));
                                    image_state.setText(supervisionProjecDetail.ilegalContent);
                                    district_state.setText(supervisionProjecDetail.ilegalBasis);

                                    address_state.setText(supervisionProjecDetail.status > 0 ? "整改合格" : "整改中");
                                    if (!TextUtils.isEmpty(supervisionProjecDetail.images)) {
                                        String imgs [] = supervisionProjecDetail.images.split(";");
                                        for (int i = 0; i < imgs.length; i++) {
                                            FrescoImageloader.displayImage(imageViews[i], imgs[i]);
                                            imageViews[i].setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("获取数据失败");

                        }
                    } else {
                        ToastUtil.Long("获取数据失败");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    get_code = false;
                    ToastUtil.Long("获取数据失败");
                }
            });

        }


    }


    private ArrayList<ImageInfo> imagePaths = new ArrayList<>();
    private SimpleDraweeView[] addImageViews = new SimpleDraweeView[3];
    private View[] imageDeleteBtns = new View[3];


    private void resetImageViews() {

        int imageCount = imagePaths.size();
        for (int i = 0; i < imageCount; i++) {
            FrescoImageloader.displayImage(addImageViews[i], imagePaths.get(i).getImageUrl());
            addImageViews[i].setVisibility(View.VISIBLE);
            imageDeleteBtns[i].setVisibility(View.VISIBLE);
        }

        if (imageCount < addImageViews.length) {
            FrescoImageloader.displayImage(addImageViews[imageCount], "", R.mipmap.image_add);
            addImageViews[imageCount].setVisibility(View.VISIBLE);
            imageDeleteBtns[imageCount].setVisibility(View.GONE);
        }
        for (int i = imageCount + 1; i < addImageViews.length; i++) {
            addImageViews[i].setVisibility(View.GONE);
            imageDeleteBtns[i].setVisibility(View.GONE);
        }
    }

    private void deleteImage(int pos) {
        imagePaths.remove(pos);
        resetImageViews();
    }

    private void startMediaPicker() {
        Intent intent = ImagesPickerActivity.prepareIntent(this, 3 - imagePaths.size());
        startActivityForResult(intent, 699);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 699 && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                ArrayList<ImageInfo> paths = (ArrayList<ImageInfo>) data.getSerializableExtra(ImagesPickerActivity.PATH_KEY);
                if (paths != null && paths.size() > 0) {
                    for (ImageInfo i : paths) {
                        if (new File(i.getFilePath(MySupervisionProjectDetailActivity.this)).exists()) {
                            imagePaths.add(i);
                        }
                    }
                }
                resetImageViews();
            }
        }
    }


    StringBuilder images = new StringBuilder();
    boolean complete = false;

    private ProgressDialog mProgressDialog = null;

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



    private String compressImages(final int status) {
        if (imagePaths.size() > 0) {
            showSubmitLoading();
            for (int i = 0; i < imagePaths.size(); i++) {
                ImageInfo imageInfo = imagePaths.get(i);
                if (imageInfo.status == ImageInfo.STATUS_NET) {
                    images.append(imageInfo.url);
                    images.append(";");
                    if (i == imagePaths.size() - 1) {
                        complete = true;
                        postResult(status);
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
                                postResult(status);
                            }
                        }
                    }).execute(new Uri[]{uri});
                }
            }
        } else {
            ToastUtil.Short("请选择图片！");
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

}
