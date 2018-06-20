package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.CompanyActivity;
import security.zw.com.securitycheck.MyCheckActivity;
import security.zw.com.securitycheck.MyProjectActivity;
import security.zw.com.securitycheck.MySupervisionProjectActivity;
import security.zw.com.securitycheck.PersonActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.RemindActivity;
import security.zw.com.securitycheck.StopWorkActivity;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.Item;
import security.zw.com.securitycheck.bean.Person;
import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * Created by wangshu on 17/5/31.
 */


public class BaseHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Object> mData;

    private Activity mContext;

    public BaseHomeAdapter(ArrayList<Object> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case 0:
                            return 2;
                    }
                    return 0;
                }
            });

        }
    }


    private int HOME_MY_WORKER = 0;         // 我的项目：
    private int HOME_MY_CHANGE = 1;     //我的整改：
    private int HOME_MY_CHECKER = 8;    //我的检查：
    private int HOME_MY_STOP_WORKER = 2;    //停工复工：
    private int HOME_MY_REMINGDER = 4;    //录控提醒：
    private int HOME_MY_Head_WORKER = 3;    //人员库：
    private int HOME_MY_DEVICE = 6;    //设备一览：
    private int HOME_MY_COMPANY = 7;    //企业库：企
    private int HOME_MY_NOTICE = 5; //通知：发布


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, parent, false);
            return new ItemTabViewHolder(view);
        }
        return null;
    }

    public class ItemTabViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;
        public TextView num;
        public RelativeLayout rel;
        public ItemTabViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            num = itemView.findViewById(R.id.num);
            rel = itemView.findViewById(R.id.rel);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemTabViewHolder) {
            ItemTabViewHolder viewHolder = (ItemTabViewHolder) holder;
            Item item = (Item) mData.get(position);
            if (item.num > 0) {
                viewHolder.num.setText(item.num + "");
                viewHolder.num.setVisibility(View.VISIBLE);
            } else {
                viewHolder.num.setVisibility(View.GONE);
            }
            if (item.type == HOME_MY_WORKER) {
                viewHolder.name.setText("我的项目");
                viewHolder.icon.setImageResource(R.mipmap.t_a);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyProjectActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_CHANGE) {
                viewHolder.name.setText("监督整改");
                viewHolder.icon.setImageResource(R.mipmap.t_b);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MySupervisionProjectActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_CHECKER) {
                viewHolder.name.setText("我的检查");
                viewHolder.icon.setImageResource(R.mipmap.t_i);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyCheckActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_STOP_WORKER) {
                viewHolder.name.setText("停工复工");
                viewHolder.icon.setImageResource(R.mipmap.t_c);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StopWorkActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_REMINGDER) {
                viewHolder.name.setText("录控提醒");
                viewHolder.icon.setImageResource(R.mipmap.t_e);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemindActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_Head_WORKER) {
                viewHolder.name.setText("人员库");
                viewHolder.icon.setImageResource(R.mipmap.t_d);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersonActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_DEVICE) {
                viewHolder.name.setText("设备一览");
                viewHolder.icon.setImageResource(R.mipmap.t_g);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("设备一览");
                    }
                });
            } else if (item.type == HOME_MY_COMPANY) {
                viewHolder.name.setText("企业库");
                viewHolder.icon.setImageResource(R.mipmap.t_h);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CompanyActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_NOTICE) {
                viewHolder.name.setText("通知");
                viewHolder.icon.setImageResource(R.mipmap.t_f);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("通知");
                    }
                });
            }

        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mData.get(position);
        if (o instanceof Item) {
            return 0;
        }
        return -1;
    }


}