package security.zw.com.securitycheck.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import drawthink.expandablerecyclerview.holder.BaseViewHolder;
import security.zw.com.securitycheck.R;

public class CheckItemViewHolder extends BaseViewHolder {

    public TextView groupTitle;
    public View parentRemind;

    public TextView childTitle;

    /**
     *  初始化你的View(这里包括GroupView,和childView)
     */
    public CheckItemViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx,itemView, viewType);
        groupTitle = itemView.findViewById(R.id.parent_title);
        childTitle = itemView.findViewById(R.id.child_title);
        parentRemind = itemView.findViewById(R.id.parent_remind);
    }

    /**
     * @return 返回你的GroupView 布局文件中根节点的ID
     */
    @Override
    public int getGroupViewResId() {
        return R.id.parent;
    }

    /**
     * @return 返回你的ChildView 布局文件中根节点的ID
     */
    @Override
    public int getChildViewResId() {
        return R.id.child;
    }

}
