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
import security.zw.com.securitycheck.MySupervisionProjectListActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * Created by wangshu on 17/5/31.
 */


public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ProjectViewHolder> {


    private ArrayList<ProjectInfo> mData;

    private Activity mContext;

    private int type = -1;

    public interface onClick {
        public void onClick(int pos);
    }

    private onClick onClick;

    public RemindAdapter.onClick getOnClick() {
        return onClick;
    }

    public void setOnClick(RemindAdapter.onClick onClick) {
        this.onClick = onClick;
    }

    public RemindAdapter(ArrayList<ProjectInfo> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public RemindAdapter(ArrayList<ProjectInfo> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public RemindAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(RemindAdapter.ProjectViewHolder holder, final int position) {
        final ProjectInfo p = mData.get(position);
        holder.name.setText(p.name);
        if (p.recordControl == ProjectInfo.TYPE_REMIND) {
            holder.des.setText("已录入");
            holder.icon.setBackgroundResource(R.drawable.green_circle);
        } else if (p.recordControl == ProjectInfo.TYPE_UNREMIND) {
            holder.des.setText("点击录入");
            holder.icon.setBackgroundResource(R.drawable.red_circle);
        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p.recordControl == ProjectInfo.TYPE_REMIND) {
                    ToastUtil.Short("已录入");
                } else if (p.recordControl == ProjectInfo.TYPE_UNREMIND) {
                    if (onClick != null) {
                        onClick.onClick(position);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}