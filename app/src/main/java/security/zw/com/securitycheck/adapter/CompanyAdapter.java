package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.CompanyDetailActivity;
import security.zw.com.securitycheck.MyCheckDetailActivity;
import security.zw.com.securitycheck.MySupervisionProjectListActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.ProjectInfo;


/**
 * Created by wangshu on 17/5/31.
 */


public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {


    private ArrayList<Company> mData;

    private Activity mContext;

    private int type = -1;

    public CompanyAdapter(ArrayList<Company> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public CompanyAdapter(ArrayList<Company> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public CompanyAdapter.CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RelativeLayout rel;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
        }
    }

    @Override
    public void onBindViewHolder(CompanyAdapter.CompanyViewHolder holder, final int position) {
        final Company company = mData.get(position);
        holder.name.setText((position + 1) + " " + company.name);
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompanyDetailActivity.launch(view.getContext(), company);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}