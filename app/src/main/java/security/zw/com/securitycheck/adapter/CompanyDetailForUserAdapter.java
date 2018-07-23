package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.CompanyDetailActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;


/**
 * Created by wangshu on 17/5/31.
 */


public class CompanyDetailForUserAdapter extends RecyclerView.Adapter<CompanyDetailForUserAdapter.CompanyViewHolder> {


    private ArrayList<ProjectInfo> mData;

    private Activity mContext;

    private int type = -1;

    public CompanyDetailForUserAdapter(ArrayList<ProjectInfo> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public CompanyDetailForUserAdapter(ArrayList<ProjectInfo> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public CompanyDetailForUserAdapter.CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_company_for_company, parent, false);
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
    public void onBindViewHolder(CompanyDetailForUserAdapter.CompanyViewHolder holder, final int position) {
        final ProjectInfo company = mData.get(position);
        holder.name.setText((position + 1) + " " + company.name);
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectDetailActivity.launch(view.getContext(), company, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}