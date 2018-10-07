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

import security.zw.com.securitycheck.MyCheckDetailActivity;
import security.zw.com.securitycheck.MySupervisionProjectDetailActivity;
import security.zw.com.securitycheck.MySupervisionProjectListForOneCheckActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.TimeUtils;


/**
 * Created by wangshu on 17/5/31.
 */


public class MySupervisionProjectListAdapter extends RecyclerView.Adapter<MySupervisionProjectListAdapter.ProjectViewHolder> {


    private ArrayList<SupervisionProjectList> mData;

    private Activity mContext;

    private int type = -1;

    public MySupervisionProjectListAdapter(ArrayList<SupervisionProjectList> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public MySupervisionProjectListAdapter(ArrayList<SupervisionProjectList> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public MySupervisionProjectListAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_supervision, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView name;
        public RelativeLayout rel;
        public TextView type;
        public TextView mode;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            type = itemView.findViewById(R.id.type);
            mode = itemView.findViewById(R.id.mode);
        }
    }

    @Override
    public void onBindViewHolder(MySupervisionProjectListAdapter.ProjectViewHolder holder, final int position) {
        final SupervisionProjectList p = mData.get(position);
        holder.name.setText(p.operator + "");
        holder.time.setText(TimeUtils.secToTime(p.createTime));

        if (p.checkType == ProjectDetail.CHECK_TYPE_RANDOM) {
            holder.type.setText("随机检查");
        } else if (p.checkType == ProjectDetail.CHECK_TYPE_COUNT) {
            holder.type.setText("评分检查");
        } else if (p.checkType == ProjectDetail.CHECK_TYPE_EVERY) {
            holder.type.setText("逐项检查");
        } else if (p.checkType == ProjectDetail.CHECK_TYPE_DUST) {
            holder.type.setText("扬尘检查");
        }

        if (p.checkMode == ProjectDetail.CHECK_MODE_SINGLE) {
            holder.mode.setText("单人模式");
        } else {
            holder.mode.setText("多人模式");
        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySupervisionProjectListForOneCheckActivity.launch(view.getContext(), p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}