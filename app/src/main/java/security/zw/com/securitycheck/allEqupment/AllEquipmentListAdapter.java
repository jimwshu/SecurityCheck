package security.zw.com.securitycheck.allEqupment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.EquipmentDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.EquipmentList;
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
        holder.time.setText(p.createTime);
        holder.type.setText(p.companyName);

        holder.status.setVisibility(View.GONE);


        holder.mode.setVisibility(View.INVISIBLE);

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllEquipmentDetailActivity.launch(view.getContext(), p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}