package security.zw.com.securitycheck.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import security.zw.com.securitycheck.R;


/**
 * Created by wangshu on 17/5/19.
 */

public class MyFragment extends BaseStatisticsFragment {

    protected View view;



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

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_my_info, container, false);

    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
