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

import security.zw.com.securitycheck.CheckItemForMyCheckDetailActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.utils.TimeUtils;


/**
 * Created by wangshu on 17/5/31.
 */


public class MyCheckProjectDetailAdapter extends RecyclerView.Adapter<MyCheckProjectDetailAdapter.ProjectDetailViewHolder> {


    private ArrayList<MyCheckProjectDetail> mData;

    private Activity mContext;

    private int type = -1;

    public MyCheckProjectDetailAdapter(ArrayList<MyCheckProjectDetail> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public MyCheckProjectDetailAdapter(ArrayList<MyCheckProjectDetail> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public MyCheckProjectDetailAdapter.ProjectDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_check_project_detail, parent, false);
        return new ProjectDetailViewHolder(view);
    }

    public class ProjectDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView circle;
        public TextView time;
        public TextView name;
        public TextView score;
        public TextView check_result;
        public RelativeLayout rel;

        public ProjectDetailViewHolder(View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            check_result = itemView.findViewById(R.id.check_result);
            rel = itemView.findViewById(R.id.rel);
        }
    }

    @Override
    public void onBindViewHolder(MyCheckProjectDetailAdapter.ProjectDetailViewHolder holder, final int position) {
        final MyCheckProjectDetail myCheckProjectDetail = mData.get(position);

        holder.name.setText(myCheckProjectDetail.projectName);
        holder.time.setText(TimeUtils.secToTime(myCheckProjectDetail.createTime));

        holder.score.setText(myCheckProjectDetail.score + "");

        if (myCheckProjectDetail.rank == 1) {
            holder.check_result.setText("合格");
            holder.circle.setBackgroundResource(R.drawable.green_circle);

        } else if (myCheckProjectDetail.rank == 2) {
            holder.check_result.setText("不合格（不需整改）");
            holder.circle.setBackgroundResource(R.drawable.red_circle);

        } else if (myCheckProjectDetail.rank == 3) {
            holder.check_result.setText("不合格（需要整改）");
            holder.circle.setBackgroundResource(R.drawable.red_circle);

        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckItemForMyCheckDetailActivity.launch(view.getContext(), myCheckProjectDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}