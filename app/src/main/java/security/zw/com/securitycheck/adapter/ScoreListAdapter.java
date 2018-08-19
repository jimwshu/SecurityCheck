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
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.CheckItem;

public class ScoreListAdapter extends BaseRecyclerViewAdapter<CheckItem, CheckItem, ScoreListViewHolder> {

    private Context ctx;
    private List datas;
    private LayoutInflater mInflater;

    public ScoreListAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public void onBindGroupHolder(ScoreListViewHolder holder, int groupPos, int position, CheckItem groupData) {
        holder.groupTitle.setText(groupData.name);
        if (groupData.isSelected) {
            holder.groupTitle.setTextColor(0xff0f86ff);
            holder.parentRemind.setVisibility(View.GONE);
        } else {
            holder.groupTitle.setTextColor(0xff1a1a1a);
            holder.parentRemind.setVisibility(View.GONE);
        }

        if (groupData.haveScored) {
            holder.groupTitle.setTextColor(0xffff0000);
        }
        if (groupData.childrens != null && groupData.childrens.size() == 0) {
            if (groupData.assigned == CheckItem.HAS_ASSIGNED) {
                holder.parentHasWorker.setVisibility(View.VISIBLE);
                if (groupData.worker == SecurityApplication.mUser.id) {
                    holder.parentHasWorker.setText("去评分");
                } else {
                    if (TextUtils.isEmpty(groupData.checker)) {
                        holder.parentHasWorker.setText("已分配");
                    } else {
                        holder.parentHasWorker.setText(groupData.checker);
                    }
                }
            } else {
                holder.parentHasWorker.setVisibility(View.GONE);
                holder.parentHasWorker.setText("未分配");
            }
        } else if (groupData.childrens != null && groupData.childrens.size() > 0) {
            holder.parentHasWorker.setVisibility(View.GONE);
        } else {
            if (groupData.assigned == CheckItem.HAS_ASSIGNED) {
                holder.parentHasWorker.setVisibility(View.GONE);
                if (groupData.worker == SecurityApplication.mUser.id) {
                    holder.parentHasWorker.setText("去评分");
                } else {
                    if (TextUtils.isEmpty(groupData.checker)) {
                        holder.parentHasWorker.setText("已分配");
                    } else {
                        holder.parentHasWorker.setText(groupData.checker);
                    }
                }
            } else {
                holder.parentHasWorker.setVisibility(View.GONE);
                holder.parentHasWorker.setText("未分配");
            }
        }
    }

    @Override
    public void onBindChildpHolder(ScoreListViewHolder holder, int groupPos, int childPos, int position, CheckItem childData) {
        holder.childTitle.setText(childData.name);


        if (childData.haveScored) {
            holder.childTitle.setTextColor(0xffff0000);
        } else {
            holder.childTitle.setTextColor(0xff1a1a1a);
        }


        if (childData.assigned == CheckItem.HAS_ASSIGNED) {
            holder.childHasWorker.setVisibility(View.GONE);
            if (childData.worker == SecurityApplication.mUser.id) {
                holder.childHasWorker.setText("去评分");
            } else {
                if (TextUtils.isEmpty(childData.checker)) {
                    holder.childHasWorker.setText("已分配");
                } else {
                    holder.childHasWorker.setText(childData.checker);
                }
            }
        } else {
            holder.childHasWorker.setVisibility(View.GONE);
            holder.childHasWorker.setText("未分配");
        }

    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_for_more_parent, parent, false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.check_item_for_more_child, parent, false);
    }

    @Override
    public ScoreListViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new ScoreListViewHolder(ctx, view, viewType);
    }
}
