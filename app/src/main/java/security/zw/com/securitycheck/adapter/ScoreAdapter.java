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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mData.get(position);
        if (o instanceof Score) {
            return TYPE_SCORE_TOTAL;
        } else if (o instanceof ScoreItem) {
            return TYPE_SCORE_ITEM;
        }
        return super.getItemViewType(position);
    }
}