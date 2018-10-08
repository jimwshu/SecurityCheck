package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.CheckItemActivity;
import security.zw.com.securitycheck.CheckItemForMoreActivity;
import security.zw.com.securitycheck.PersonDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.RandomCheckActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.Notice;
import security.zw.com.securitycheck.bean.Person;
import security.zw.com.securitycheck.bean.ProjectDetail;


/**
 * Created by wangshu on 17/5/31.
 */


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.CompanyViewHolder> {


    private ArrayList<Notice> mData;

    private Activity mContext;

    private int type = -1;

    public NoticeAdapter(ArrayList<Notice> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public NoticeAdapter(ArrayList<Notice> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public NoticeAdapter.CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_person, parent, false);
        return new CompanyViewHolder(view);
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RelativeLayout rel;
        public TextView job;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            job = itemView.findViewById(R.id.job);
        }
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.CompanyViewHolder holder, final int position) {
        final Notice person = mData.get(position);
        holder.name.setText(person.name);
        if (person.type == 1) {
            holder.job.setText("随机检查");
        } else if (person.type == 2) {
            holder.job.setText("评分检查");
        } else if (person.type == 3) {
            holder.job.setText("逐项检查");
        }
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectDetail projectDetail = new ProjectDetail();
                projectDetail.id = person.id;
                projectDetail.check_type = person.type;
                projectDetail.name = person.name;
                projectDetail.check_mode = person.mode;
                if (projectDetail.check_type == 2) {
                    CheckItemForMoreActivity.launch(view.getContext(), projectDetail);
                } else if (projectDetail.check_type == 1) {
                    RandomCheckActivity.launch(view.getContext(), projectDetail);
                } else if (projectDetail.check_type == 3){
                    CheckItemActivity.launch(view.getContext(), projectDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}