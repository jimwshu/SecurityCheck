package security.zw.com.securitycheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.CheckItem;

public class CheckItemForMoreAdapter extends BaseRecyclerViewAdapter<CheckItem,CheckItem,CheckItemForMoreViewHolder> {

    private Context ctx;
    private List datas;
    private LayoutInflater mInflater;

    public CheckItemForMoreAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public void onBindGroupHolder(CheckItemForMoreViewHolder holder, int groupPos, int position, CheckItem groupData) {
        holder.groupTitle.setText(groupData.name);
        if (groupData.isSelected) {
            holder.groupTitle.setTextColor(0xff0f86ff);
            holder.parentRemind.setVisibility(View.VISIBLE);
        } else {
            holder.groupTitle.setTextColor(0xff1a1a1a);
            holder.parentRemind.setVisibility(View.GONE);
        }

        if ( groupData.childrens != null && groupData.childrens.size() == 0) {
            if (groupData.assigned == CheckItem.HAS_ASSIGNED) {
                holder.parentHasWorker.setVisibility(View.VISIBLE);
                holder.parentHasWorker.setText("已分配");
            } else {
                holder.parentHasWorker.setVisibility(View.VISIBLE);
                holder.parentHasWorker.setText("未分配");
            }
        } else if (groupData.childrens != null && groupData.childrens.size() > 0) {
            holder.parentHasWorker.setVisibility(View.GONE);
        } else {
            if (groupData.assigned == CheckItem.HAS_ASSIGNED) {
                holder.parentHasWorker.setVisibility(View.VISIBLE);
                holder.parentHasWorker.setText("已分配");
            } else {
                holder.parentHasWorker.setVisibility(View.VISIBLE);
                holder.parentHasWorker.setText("未分配");
            }
        }
    }

    @Override
    public void onBindChildpHolder(CheckItemForMoreViewHolder holder, int groupPos,int childPos,int position, CheckItem childData) {
        holder.childTitle.setText(childData.name);
        if (childData.assigned == CheckItem.HAS_ASSIGNED) {
            holder.childHasWorker.setVisibility(View.VISIBLE);
            holder.childHasWorker.setText("已分配");
        } else {
            holder.childHasWorker.setVisibility(View.VISIBLE);
            holder.childHasWorker.setText("未分配");
        }

    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_for_more_parent,parent,false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_for_more_child,parent,false);
    }

    @Override
    public CheckItemForMoreViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new CheckItemForMoreViewHolder(ctx,view,viewType);
    }
}
