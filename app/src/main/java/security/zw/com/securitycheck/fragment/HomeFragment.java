package security.zw.com.securitycheck.fragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.adapter.BaseHomeAdapter;


/**
 * Created by wangshu on 17/5/19.
 */

public class HomeFragment extends BaseStatisticsFragment{


    private RecyclerView mRecyclerView;
    private ArrayList<Object> data = new ArrayList<Object>();
    protected GridLayoutManager mGridLayoutManager;
    protected BaseHomeAdapter mHomeAdapter;
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

    public void initView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.layout_home_fragment, container, false);
        //actionBar = (RelativeLayout) view.findViewById(R.id.action_bar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mHomeAdapter = new BaseHomeAdapter(data, getActivity());
        mGridLayoutManager = new GridLayoutManager(getActivity(), 6, GridLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mHomeAdapter);
        mRecyclerView.setHasFixedSize(true);


    }


}
