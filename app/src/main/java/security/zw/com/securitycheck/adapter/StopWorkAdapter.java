package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * Created by wangshu on 17/5/31.
 */


public class StopWorkAdapter extends RecyclerView.Adapter<StopWorkAdapter.ProjectViewHolder> {


    private ArrayList<ProjectInfo> mData;

    private Activity mContext;

    private int type = -1;

    public interface onClick {
        public void onClick(int pos);
    }

    private onClick onClick;

    public StopWorkAdapter.onClick getOnClick() {
        return onClick;
    }

    public void setOnClick(StopWorkAdapter.onClick onClick) {
        this.onClick = onClick;
    }

    public StopWorkAdapter(ArrayList<ProjectInfo> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public StopWorkAdapter(ArrayList<ProjectInfo> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public StopWorkAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_remind, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView icon;
        public TextView name;
        public TextView des;
        public RelativeLayout rel;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.circle);
            name = itemView.findViewById(R.id.name);
            des = itemView.findViewById(R.id.des);
            rel = itemView.findViewById(R.id.rel);
        }
    }

    @Override
    public void onBindViewHolder(StopWorkAdapter.ProjectViewHolder holder, final int position) {
        final ProjectInfo p = mData.get(position);
        holder.name.setText(p.name);
        if (p.workState == ProjectInfo.TYPE_STOP) {
            holder.des.setText("停工项目");
            holder.icon.setBackgroundResource(R.drawable.red_circle);
        } else if (p.workState == ProjectInfo.TYPE_START) {
            holder.des.setText("复工项目");
            holder.icon.setBackgroundResource(R.drawable.green_circle);
        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}