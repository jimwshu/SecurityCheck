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

import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.RandomCheckActivity;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.CheckItemDetail;
import security.zw.com.securitycheck.bean.DecreaseItem;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.Score;
import security.zw.com.securitycheck.bean.ScoreItem;


/**
 * Created by wangshu on 17/5/31.
 */


public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Object> mData;

    private Activity mContext;

    public ScoreAdapter(ArrayList<Object> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SCORE_TOTAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_total, parent, false);
            return new StoreTotalItem(view);
        } else if (viewType == TYPE_SCORE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_item, parent, false);
            return new StoreItem(view);
        }
        return null;

    }

    public class StoreTotalItem extends RecyclerView.ViewHolder {

        public TextView totalScore;

        public StoreTotalItem(View itemView) {
            super(itemView);
            totalScore = itemView.findViewById(R.id.score);
        }
    }

    public class StoreItem extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView score;
        public TextView recheck;

        public StoreItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            score = itemView.findViewById(R.id.decrease_score);
            recheck = itemView.findViewById(R.id.recheck);
        }
    }


    public static final int TYPE_SCORE_TOTAL = 1;
    public static final int TYPE_SCORE_ITEM = 2;



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StoreTotalItem) {
            StoreTotalItem totalItem = (StoreTotalItem) holder;
            CheckItemDetail checkItemDetail = (CheckItemDetail) mData.get(position);
            totalItem.totalScore.setText(checkItemDetail.deserveScore + "");
        } else if (holder instanceof StoreItem) {
            final DecreaseItem item = (DecreaseItem) mData.get(position);
            StoreItem storeItem = (StoreItem) holder;

            storeItem.score.setText(item.score + "");
            storeItem.title.setText(item.name);
            storeItem.recheck.setVisibility(item.score != 0 ? View.VISIBLE : View.GONE);
            storeItem.recheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListenner != null) {
                        clickListenner.onClick(position, item);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mData.get(position);
        if (o instanceof CheckItemDetail) {
            return TYPE_SCORE_TOTAL;
        } else if (o instanceof DecreaseItem) {
            return TYPE_SCORE_ITEM;
        }
        return super.getItemViewType(position);
    }

    public interface RecheckClickListenner {
        void onClick(int pos, DecreaseItem item);
    }

    private RecheckClickListenner clickListenner;

    public RecheckClickListenner getClickListenner() {
        return clickListenner;
    }

    public void setClickListenner(RecheckClickListenner clickListenner) {
        this.clickListenner = clickListenner;
    }
}