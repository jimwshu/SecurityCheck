package security.zw.com.securitycheck.recordEqupment.allEqupment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.EquipmentList;
import security.zw.com.securitycheck.usedEqupment.allEqupment.InstallEquipmentDetailActivity;
import security.zw.com.securitycheck.utils.TimeUtils;


/**
 * Created by wangshu on 17/5/31.
 */


public class AllEquipmentListAdapter extends RecyclerView.Adapter<AllEquipmentListAdapter.ProjectViewHolder> {


    private ArrayList<EquipmentList> mData;

    private Activity mContext;

    private int type = -1;

    public AllEquipmentListAdapter(ArrayList<EquipmentList> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public AllEquipmentListAdapter(ArrayList<EquipmentList> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public AllEquipmentListAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_equipment_list, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView name;
        public RelativeLayout rel;
        public TextView type;
        public TextView mode;
        public TextView status;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            type = itemView.findViewById(R.id.type);
            mode = itemView.findViewById(R.id.mode);
            status = itemView.findViewById(R.id.status);
        }
    }

    @Override
    public void onBindViewHolder(AllEquipmentListAdapter.ProjectViewHolder holder, final int position) {
        final EquipmentList p = mData.get(position);

        holder.name.setText(p.equipmentName + "");
        holder.time.setText(TimeUtils.secToTime(p.applyTime));
        holder.type.setText(p.recordCompany);

        holder.status.setVisibility(View.GONE);


        if (p.state == 0) {

        } else if (p.state == 1) {
            holder.mode.setText("设备备案");
        } else if (p.state == 2) {
            holder.mode.setText("使用记录");
        } else if (p.state == 3) {
            holder.mode.setText("安装告知");
        }else if (p.state == 4) {
            holder.mode.setText("拆卸告知");
        }else if (p.state == 5) {
            holder.mode.setText("设备变更");
        }
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllEquipmentList2Activity.launch(view.getContext(), 2, p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}