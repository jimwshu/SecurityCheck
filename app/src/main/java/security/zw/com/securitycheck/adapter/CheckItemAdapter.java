package security.zw.com.securitycheck.adapter;

import android.content.Context;
import android.text.TextUtils;
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
    private int type = -1;

    public CheckItemAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
    }

    public CheckItemAdapter(Context ctx, List<RecyclerViewData> datas, int type) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
        this.type = type;
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

        if (type == 1) {
            if (TextUtils.isEmpty(groupData.checker)) {
                holder.parentCheckName.setVisibility(View.GONE);
            } else {
                holder.parentCheckName.setVisibility(View.VISIBLE);
                holder.parentCheckName.setText(groupData.checker);
            }

            holder.parentCheckScore.setVisibility(View.VISIBLE);
            holder.parentCheckScore.setText(groupData.score + "");

        } else if (type == -1) {
            holder.parentCheckName.setVisibility(View.GONE);
            holder.parentCheckScore.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBindChildpHolder(CheckItemViewHolder holder, int groupPos,int childPos,int position, CheckItem childData) {
        holder.childTitle.setText(childData.name);
        if (type == 1) {
            if (TextUtils.isEmpty(childData.checker)) {
                holder.childCheckName.setVisibility(View.GONE);
            } else {
                holder.childCheckName.setVisibility(View.VISIBLE);
                holder.childCheckName.setText(childData.checker);
            }
            holder.childCheckScore.setVisibility(View.VISIBLE);
            holder.childCheckScore.setText(childData.score + "");


        } else if (type == -1) {
            holder.childCheckName.setVisibility(View.GONE);
            holder.childCheckScore.setVisibility(View.GONE);

        }
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
