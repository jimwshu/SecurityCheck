package security.zw.com.securitycheck.enforceLow;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
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
import security.zw.com.securitycheck.CheckCompanyList;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.Base64Img;
import security.zw.com.securitycheck.utils.DeviceUtils;
import security.zw.com.securitycheck.utils.ImageUtils;
import security.zw.com.securitycheck.utils.WindowUtils;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

// 整改列表（相对于单次检查）
public class MySupervisionProjectListForOneCheckActivity extends BaseSystemBarTintActivity {

    public Company company;

    public static void launch(Context ctx, SupervisionProjectList info) {
        Intent intent = new Intent(ctx, MySupervisionProjectListForOneCheckActivity.class);
        intent.putExtra("info", info);
        ctx.startActivity(intent);
    }


    private SupervisionProjectList info;
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

    private ArrayList<SupervisionProjectList> data = new ArrayList<SupervisionProjectList>();
    protected LinearLayoutManager mManager;
    protected MySupervisionListForOneCheckAdapter mAdapter;

    private int page = 1;
    protected boolean isLoading = false;
    protected boolean hasMore = true;


    private ImageView mBack;
    private TextView mType;
    private TextView mSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        info = (SupervisionProjectList) getIntent().getSerializableExtra("info");
        if (info == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_my_force_low_project_list);
        initWidget();

    }
    private TextView check;
    private TextView change;


    private void initWidget() {
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


        addImageViews[0] = (SimpleDraweeView) findViewById(R.id.image4);
        addImageViews[1] = (SimpleDraweeView) findViewById(R.id.image5);
        addImageViews[2] = (SimpleDraweeView) findViewById(R.id.image6);

        imageDeleteBtns[0] = findViewById(R.id.imageDelete4);
        imageDeleteBtns[1] = findViewById(R.id.imageDelete5);
        imageDeleteBtns[2] = findViewById(R.id.imageDelete6);

        View.OnClickListener addImageClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageCount = imagePaths.size();
                if (imageCount == 0 || (imageCount < addImageViews.length && v == addImageViews[imageCount])) {
                    startMediaPicker();
                }
            }
        };

        for (int i = 0; i < addImageViews.length; i++) {
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


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MySupervisionListForOneCheckAdapter(data, this);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

    }


    private void onRefresh() {
        loadData();
    }


    Retrofit mRetrofit;
    Constans.GetMyProjectList addCheck;
    Call<String> mCall;


    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;

        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);
        jsonObject.addProperty("id", info.id);
        jsonObject.addProperty("checkMode", info.checkMode);
        jsonObject.addProperty("checkType", info.checkType);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.enforceLawDetail(requestBody);

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
                                JSONArray object = jsonObject.optJSONArray("data");
                                ArrayList<SupervisionProjectList> arrayList = new ArrayList<>();
                                if (object != null && object.length() > 0) {
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject object1 = object.optJSONObject(i);
                                        SupervisionProjectList supervisionProjectList = new Gson().fromJson(object1.toString(), SupervisionProjectList.class);
                                        arrayList.add(supervisionProjectList);
                                    }
                                }

                                if (arrayList.size() > 0) {
                                    data.clear();
                                    data.addAll(arrayList);
                                    mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    private void postResult(int status) {
        if (!isLoading) {
            isLoading = true;
            mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
            addCheck = mRetrofit.create(Constans.GetMyProjectList.class);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", SecurityApplication.mUser.id);
            jsonObject.addProperty("id", info.id);
            jsonObject.addProperty("status", status);

            jsonObject.addProperty("checkMode", info.checkMode);
            jsonObject.addProperty("checkType", info.checkType);

            if (images != null && images.length > 0) {
                String [] strings = new String[imagePaths.size()];

                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < imagePaths.size(); i++) {
                    jsonArray.add(images[i]);
                }
                jsonObject.add("interviewRecord", jsonArray);
            }

            String s = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);
            mCall = addCheck.enforceLawDetailDeal(requestBody);
            mCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    isLoading = false;
                    hideSubmitLoading();
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.has("code")) {
                                int code = jsonObject.optInt("code");
                                if (code == 0) {
                                    company = null;
                                    ToastUtil.Long("修改状态成功");
                                    loadData();
                                } else {
                                    ToastUtil.Long("修改状态失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.Long("修改状态失败");

                        } catch (Exception e) {
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
                    isLoading = false;
                    ToastUtil.Long("修改状态失败");
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
                        if (new File(i.getFilePath(MySupervisionProjectListForOneCheckActivity.this)).exists()) {
                            imagePaths.add(i);
                        }
                    }
                }
                resetImageViews();
            }
        }
    }


    String images [] = new String[]{};
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
                    images = new String[]{};
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
            images = new String[imagePaths.size()];
            showSubmitLoading();
            for (int i = 0; i < imagePaths.size(); i++) {
                ImageInfo imageInfo = imagePaths.get(i);
                if (imageInfo.status == ImageInfo.STATUS_NET) {
                    images[i] = (imageInfo.url);
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
                            images[j] = (output);
                            if (j == imagePaths.size() - 1) {
                                complete = true;
                                postResult(status);
                            }
                        }
                    }).execute(new Uri[]{uri});
                }
            }
        } else {
            postResult(status);
        }


        return "";
    }




    private static class Compress extends AsyncTask<Uri, Void, String> {
        final static int MAX = 720;
        final Compress.ICompress iCompress;


        Compress(Compress.ICompress iCompress) {
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
