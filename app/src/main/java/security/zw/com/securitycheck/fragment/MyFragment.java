package security.zw.com.securitycheck.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import security.zw.com.securitycheck.LoginActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;


/**
 * Created by wangshu on 17/5/19.
 */

public class MyFragment extends BaseStatisticsFragment {

    protected View view;

    private TextView logout;

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
    private RelativeLayout actionBar;

    private void initBar(View view) {
        actionBar = view.findViewById(R.id.action_bar);
        actionBar.setBackgroundColor(0xff0F86FF);
        barLeft = (LinearLayout) view.findViewById(R.id.bar_left);
        barBack = (ImageView) view.findViewById(R.id.bar_back);
        barClose = (ImageView) view.findViewById(R.id.bar_close);
        barTitle = (TextView) view.findViewById(R.id.bar_title);
        barRightRel = view.findViewById(R.id.bar_right_rel);
        barButton = view.findViewById(R.id.checked);
        barButton.setVisibility(View.GONE);
        barRightRel.setVisibility(View.GONE);
        barLeft.setVisibility(View.GONE);
        barClose.setVisibility(View.GONE);
        barBack.setVisibility(View.GONE);
        barTitle.setText("个人中心");


    }


    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_my_info, container, false);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecurityApplication.getInstance().removeUserInfo();
                SecurityApplication.mUser = null;
                getActivity().finish();
                LoginActivity.launch(view.getContext());
            }
        });
        initBar(view);
    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
