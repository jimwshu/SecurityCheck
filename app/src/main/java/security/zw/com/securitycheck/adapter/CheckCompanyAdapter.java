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

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.Company;


/**
 * Created by wangshu on 17/5/31.
 */


public class CheckCompanyAdapter extends RecyclerView.Adapter<CheckCompanyAdapter.CompanyViewHolder> {


    private ArrayList<Company> mData;

    private Activity mContext;

    public CheckCompanyAdapter(ArrayList<Company> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }


    @Override
    public CheckCompanyAdapter.CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RelativeLayout rel;
        public ImageView bar_next;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            bar_next = itemView.findViewById(R.id.bar_next);
        }
    }

    @Override
    public void onBindViewHolder(CheckCompanyAdapter.CompanyViewHolder holder, final int position) {
        final Company company = mData.get(position);
        holder.name.setText((position + 1) + " " + company.name);
        holder.bar_next.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}