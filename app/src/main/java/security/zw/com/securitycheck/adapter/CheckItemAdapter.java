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

public class CheckItemAdapter extends BaseRecyclerViewAdapter<CheckItem,CheckItem,CheckItemViewHolder> {

    private Context ctx;
    private List datas;
    private LayoutInflater mInflater;

    public CheckItemAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public void onBindGroupHolder(CheckItemViewHolder holder, int groupPos, int position, CheckItem groupData) {
        holder.groupTitle.setText(groupData.name);
        if (groupData.isSelected) {
            holder.groupTitle.setTextColor(0xff0f86ff);
            holder.parentRemind.setVisibility(View.VISIBLE);
        } else {
            holder.groupTitle.setTextColor(0xff1a1a1a);
            holder.parentRemind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindChildpHolder(CheckItemViewHolder holder, int groupPos,int childPos,int position, CheckItem childData) {
        holder.childTitle.setText(childData.name);
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_parent,parent,false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_child,parent,false);
    }

    @Override
    public CheckItemViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new CheckItemViewHolder(ctx,view,viewType);
    }
}
