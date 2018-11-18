package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import security.zw.com.securitycheck.MyCheckDetailActivity;
import security.zw.com.securitycheck.MySupervisionProjectListActivity;
import security.zw.com.securitycheck.ProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.RandomCheck;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;


/**
 * Created by wangshu on 17/5/31.
 */


public class RandomCheckAdapter extends RecyclerView.Adapter<RandomCheckAdapter.ProjectViewHolder> {


    private ArrayList<RandomCheck> mData;

    private Activity mContext;

    private int type = -1;

    public RandomCheckAdapter(ArrayList<RandomCheck> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public RandomCheckAdapter(ArrayList<RandomCheck> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public RandomCheckAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_random_check, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView illegal;
        public TextView basic;
        public SimpleDraweeView image1;
        public SimpleDraweeView image2;
        public SimpleDraweeView image3;

        public RelativeLayout photoView;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            illegal = itemView.findViewById(R.id.illegal);
            basic = itemView.findViewById(R.id.basic);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            photoView = itemView.findViewById(R.id.photo_view);
        }
    }

    @Override
    public void onBindViewHolder(RandomCheckAdapter.ProjectViewHolder holder, final int position) {
        final RandomCheck p = mData.get(position);

        holder.illegal.setText(p.illeage);
        holder.basic.setText(p.basic);

        if (!TextUtils.isEmpty(p.image)) {
            holder.photoView.setVisibility(View.VISIBLE);
            String imgs [] = p.image.split(";");

            if (imgs.length == 0) {
                holder.image1.setVisibility(View.INVISIBLE);
                holder.image2.setVisibility(View.INVISIBLE);
                holder.image3.setVisibility(View.INVISIBLE);
            }else if (imgs.length == 1) {
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.INVISIBLE);
                holder.image3.setVisibility(View.INVISIBLE);
            }else if (imgs.length == 2) {
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.INVISIBLE);
            }else if (imgs.length == 3) {
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < imgs.length; i++) {
                if (i == 0) {
                    FrescoImageloader.displayImage(holder.image1, imgs[i]);
                } else if (i == 1) {
                    FrescoImageloader.displayImage(holder.image2, imgs[i]);
                } else if (i == 2) {
                    FrescoImageloader.displayImage(holder.image3, imgs[i]);
                }
            }
        } else {
            holder.photoView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}