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
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.utils.LogUtils;


/**
 * Created by wangshu on 17/5/31.
 */


public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.ProjectViewHolder> {

    public interface OnClick {
        void onClick(int pos);
    }

    public OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    private ArrayList<ProjectInfo> mData;

    private Activity mContext;

    private int type = -1;

    public MyProjectAdapter(ArrayList<ProjectInfo> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public MyProjectAdapter(ArrayList<ProjectInfo> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public MyProjectAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView icon;
        public TextView name;
        public TextView des;
        public ImageView check;
        public RelativeLayout rel;
        public TextView changeStatus;
        public ProjectViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.circle);
            name = itemView.findViewById(R.id.name);
            des = itemView.findViewById(R.id.des);
            check = itemView.findViewById(R.id.remind);
            rel = itemView.findViewById(R.id.rel);
            changeStatus = itemView.findViewById(R.id.change_status);
        }
    }

    @Override
    public void onBindViewHolder(MyProjectAdapter.ProjectViewHolder holder, final int position) {
        final ProjectInfo p = mData.get(position);
        holder.name.setText(p.name);
        if (p.state == ProjectInfo.TYPE_NEED_CHANGE) {
            holder.des.setText("需要整改项目");
            holder.icon.setBackgroundResource(R.drawable.red_circle);
        } else if (p.state == ProjectInfo.TYPE_NORMAL) {
            holder.des.setText("正常项目");
            holder.icon.setBackgroundResource(R.drawable.green_circle);
        } else if (p.state == ProjectInfo.TYPE_FINISHED) {
            holder.des.setText("竣工项目");
            holder.icon.setBackgroundResource(R.drawable.blue_circle);
        } else if (p.state == ProjectInfo.TYPE_UNSAFE) {
            holder.des.setText("未办安全收监手续项目");
            holder.icon.setBackgroundResource(R.drawable.grey_circle);
        }

        if (type == -1) {
            if (SecurityApplication.mUser.type == 1) {
                holder.changeStatus.setVisibility(View.VISIBLE);
            } else {
                holder.changeStatus.setVisibility(View.GONE);
            }
        } else {
            holder.changeStatus.setVisibility(View.GONE);
        }


        holder.changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClick(position);
                }
            }
        });

        if (p.supervise == 0) {
            holder.check.setVisibility(View.INVISIBLE);
        } else if (p.supervise == 1) {
            holder.check.setVisibility(View.VISIBLE);
        }
        if (type == -1) {
            holder.rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProjectDetailActivity.launch(view.getContext(), p);
                }
            });
        } else if (type == 1) {
            holder.rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyCheckDetailActivity.launch(view.getContext(), p);
                }
            });
        }  else if (type == 2) {
            holder.rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MySupervisionProjectListActivity.launch(view.getContext(), p);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}