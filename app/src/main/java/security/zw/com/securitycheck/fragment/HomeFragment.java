package security.zw.com.securitycheck.fragment;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.LoginActivity;
import security.zw.com.securitycheck.MapActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.adapter.BaseHomeAdapter;
import security.zw.com.securitycheck.bean.Item;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;
import security.zw.com.securitycheck.zxing.ScanActivity;


/**
 * Created by wangshu on 17/5/19.
 */

public class HomeFragment extends BaseStatisticsFragment {


    private RecyclerView mRecyclerView;
    private ArrayList<Object> data = new ArrayList<Object>();
    protected GridLayoutManager mGridLayoutManager;
    protected BaseHomeAdapter mHomeAdapter;
    protected View view;

    protected TextView name;
    protected TextView type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            initView(inflater, container);
        } else {
            ViewParent parent = view.getParent();

            if (parent != null) {
                ((ViewGroup) parent).removeView(view);
            }
        }

        return view;
    }

    private LinearLayout barLeft;
    private ImageView barBack;
    private ImageView barClose;
    private TextView barTitle;
    private ImageView barButton;
    private RelativeLayout barRightRel;

    private void initBar(View view) {
        barLeft = (LinearLayout) view.findViewById(R.id.bar_left);
        barBack = (ImageView) view.findViewById(R.id.bar_back);
        barClose = (ImageView) view.findViewById(R.id.bar_close);
        barTitle = (TextView) view.findViewById(R.id.bar_title);
        barRightRel = view.findViewById(R.id.bar_right_rel);
        barButton = view.findViewById(R.id.checked);
        barButton.setVisibility(View.VISIBLE);
        barRightRel.setVisibility(View.VISIBLE);
        barLeft.setVisibility(View.VISIBLE);
        barClose.setVisibility(View.VISIBLE);
        barBack.setVisibility(View.GONE);
        barTitle.setText("监督宝");

        barButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity.launch(view.getContext(), "衡阳");
            }
        });

        barClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(getActivity())
                        .setOrientationLocked(false)
                        .setCaptureActivity(ScanActivity.class)// 设置自定义的activity是ScanActivity
                        .initiateScan(); // 初始化扫描
            }
        });
    }

    public void initView(LayoutInflater inflater, ViewGroup container) {
        if (SecurityApplication.mUser != null) {
            for (int i = 0; i < 9; i++) {
                Item item = new Item();
                item.type = i;
                data.add(item);
            }
        } else {
            System.exit(0);
            return;
        }
        view = inflater.inflate(R.layout.layout_home_fragment, container, false);
        //actionBar = (RelativeLayout) view.findViewById(R.id.action_bar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mHomeAdapter = new BaseHomeAdapter(data, getActivity());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 6, GridLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mHomeAdapter);
        mRecyclerView.setHasFixedSize(true);

        initBar(view);
        name = view.findViewById(R.id.name);
        type = view.findViewById(R.id.type);

        if (SecurityApplication.mUser != null) {
            name.setText(SecurityApplication.mUser.name);
            type.setText(SecurityApplication.mUser.getTypeName() + "");
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getMessageCount();
    }


    Retrofit mRetrofit;
    Constans.GetSmsService addCheck;
    Call<String> mCall;
    public void getMessageCount() {
        mRetrofit = NetRequest.getInstance().init("").getmRetrofit();
        addCheck = mRetrofit.create(Constans.GetSmsService.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", SecurityApplication.mUser.id);

        String s = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), s);

        mCall = addCheck.getMessageCount(requestBody);

        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("code")) {
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                int count = jsonObject.optInt("data");

                                for (int i = 0; i < data.size(); i++) {
                                    Item item = (Item) data.get(i);
                                    if (item.type == BaseHomeAdapter.HOME_MY_NOTICE) {
                                        item.num = count;
                                        break;
                                    }
                                }

                                if (mHomeAdapter != null) {
                                    mHomeAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
