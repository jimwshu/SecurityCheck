package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

    private int type = -1;

    public ScoreAdapter(ArrayList<Object> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public ScoreAdapter(ArrayList<Object> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
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
        public RelativeLayout relativeLayout;
        public StoreTotalItem(View itemView) {
            super(itemView);
            totalScore = itemView.findViewById(R.id.score);
            relativeLayout = itemView.findViewById(R.id.rel);
        }
    }

    public class StoreItem extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView score;
        public TextView recheck;
        public RelativeLayout rel;

        public StoreItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            score = itemView.findViewById(R.id.decrease_score);
            recheck = itemView.findViewById(R.id.recheck);
            rel = itemView.findViewById(R.id.rel);
        }
    }


    public static final int TYPE_SCORE_TOTAL = 1;
    public static final int TYPE_SCORE_ITEM = 2;



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StoreTotalItem) {
            StoreTotalItem totalItem = (StoreTotalItem) holder;
            if (type == 2) {
                totalItem.relativeLayout.setVisibility(View.GONE);
            } else {
                totalItem.relativeLayout.setVisibility(View.VISIBLE);
                CheckItem checkItem = (CheckItem) mData.get(position);
                totalItem.totalScore.setText("应得分： " + checkItem.deserveScore + "分");
            }
        } else if (holder instanceof StoreItem) {
            final CheckItem item = (CheckItem) mData.get(position);
            StoreItem storeItem = (StoreItem) holder;

            SpannableString ss = new SpannableString("扣减分： " + item.realScore + "");
            ss.setSpan(new ForegroundColorSpan(0xff92969c), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(0xffd0021b), 5, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            storeItem.score.setText(ss);
            if (type == -1) {
                storeItem.title.setText(item.name + " " + "（" + item.scoreRule+ "）");
                storeItem.score.setVisibility(View.VISIBLE);
                storeItem.recheck.setVisibility(View.VISIBLE);
                storeItem.recheck.setVisibility(item.realScore < 0 ? View.VISIBLE : View.GONE);
            } else if (type == 1){
                storeItem.title.setText(item.name + " " + "（" + item.scoreRule+ "）");
                storeItem.score.setVisibility(View.VISIBLE);
                storeItem.recheck.setVisibility(View.GONE);
            } else if (type == 2) {
                storeItem.title.setText(item.name);
                storeItem.score.setVisibility(View.INVISIBLE);
                storeItem.recheck.setVisibility(View.INVISIBLE);
            }
            storeItem.rel.setOnClickListener(new View.OnClickListener() {
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

        if (position == 0) {
            return TYPE_SCORE_TOTAL;
        } else {
            return TYPE_SCORE_ITEM;
        }
    }

    public interface RecheckClickListenner {
        void onClick(int pos, CheckItem item);
    }

    private RecheckClickListenner clickListenner;

    public RecheckClickListenner getClickListenner() {
        return clickListenner;
    }

    public void setClickListenner(RecheckClickListenner clickListenner) {
        this.clickListenner = clickListenner;
    }
}