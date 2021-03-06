package security.zw.com.securitycheck.lowCase;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.TimeUtils;


/**
 * Created by wangshu on 17/5/31.
 */


public class MySupervisionProjectListAdapter extends RecyclerView.Adapter<MySupervisionProjectListAdapter.ProjectViewHolder> {


    private ArrayList<SupervisionProjectList> mData;

    private Activity mContext;

    private int type = -1;

    public interface OnClick {
        void onClick(int pos);
    }

    public OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_enforce_low, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView name;
        public RelativeLayout rel;
        public TextView type;
        public TextView mode;
        public TextView assign;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            type = itemView.findViewById(R.id.type);
            mode = itemView.findViewById(R.id.mode);
            assign = itemView.findViewById(R.id.assign);
        }
    }

    @Override
    public void onBindViewHolder(MySupervisionProjectListAdapter.ProjectViewHolder holder, final int position) {
        final SupervisionProjectList p = mData.get(position);


        if (p.statusTypeType > 0) {
            holder.name.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
            holder.mode.setVisibility(View.GONE);
            holder.time.setText(p.statusTypeType == 1 ? "本周内" : "一周前");
            holder.time.setTextColor(0xffff0000);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.VISIBLE);
            holder.mode.setVisibility(View.VISIBLE);
            holder.time.setTextColor(0xff92969c);

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
                    MySupervisionProjectDetailActivity.launch(view.getContext(), p);
                }
            });

            holder.assign.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}