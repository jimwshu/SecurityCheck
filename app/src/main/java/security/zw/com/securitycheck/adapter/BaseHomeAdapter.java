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
import android.widget.Toast;

import java.util.ArrayList;

import security.zw.com.securitycheck.CompanyActivity;
import security.zw.com.securitycheck.EquipmentListActivity;
import security.zw.com.securitycheck.MyCheckActivity;
import security.zw.com.securitycheck.MyProjectActivity;
import security.zw.com.securitycheck.MySupervisionProjectActivity;
import security.zw.com.securitycheck.NoticeActivity;
import security.zw.com.securitycheck.PersonActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.RemindActivity;
import security.zw.com.securitycheck.StopWorkActivity;
import security.zw.com.securitycheck.allEqupment.AllEquipmentListActivity;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.Item;
import security.zw.com.securitycheck.bean.Person;
import security.zw.com.securitycheck.bean.UserInfo;
import security.zw.com.securitycheck.installEqupment.allEqupment.InstallEquipmentListActivity;
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


    public int HOME_MY_WORKER = 0;         // 我的项目：
    public int HOME_MY_CHANGE = 1;     //我的整改：
    public int HOME_MY_CHECKER = 2;    //我的检查：
    public int HOME_MY_STOP_WORKER = 3;    //停工复工：
    public int HOME_MY_REMINGDER = 5;    //录控提醒：
    public int HOME_MY_Head_WORKER = 4;    //人员库：
    public int HOME_MY_DEVICE = 7;    //设备一览：
    public int HOME_MY_COMPANY = 8;    //企业库：企
    public static int HOME_MY_NOTICE = 6; //通知：发布

    public static int HOME_MY_EQUIPMENT = 9; //设备备案
    public static int HOME_MY_INSTALL = 10; //安装管理
    public static int HOME_MY_UNINSTALL = 19; //拆卸管理

    public static int HOME_MY_RECORD   = 11; //使用登记
    public static int HOME_MY_LOW   = 12; //相关执法
    public static int HOME_MY_FILING   = 13; //产权变更



    public static int HOME_MY_CHECKED   = 14; //复核
    public static int HOME_MY_CASE   = 15; //办案
    public static int HOME_MY_SUPERVISE   = 16; //下达督办

    public static int HOME_MY_CHECKER_COMPANY   = 17; //检测单位一览

    public static int HOME_ALL_EQUPMENT = 18;//所有设备

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
                viewHolder.name.setText("工程项目");
                viewHolder.icon.setImageResource(R.mipmap.t_a);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyProjectActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_CHANGE) {
                viewHolder.name.setText("检查整改");
                viewHolder.icon.setImageResource(R.mipmap.t_b);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MySupervisionProjectActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_CHECKER) {
                viewHolder.name.setText("安全考评");
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
                viewHolder.name.setText("安全管理人员库");
                viewHolder.icon.setImageResource(R.mipmap.t_d);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersonActivity.launch(view.getContext());
                    }
                });
            } else if (item.type == HOME_MY_DEVICE) {
                viewHolder.name.setText("设备产权");
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
                viewHolder.name.setText("安全通知");
                viewHolder.icon.setImageResource(R.mipmap.t_f);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NoticeActivity.launch(view.getContext());
                    }
                });
            }  else if (item.type == HOME_MY_EQUIPMENT) {
                viewHolder.name.setText("设备备案");
                viewHolder.icon.setImageResource(R.mipmap.t_z);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EquipmentListActivity.launch(view.getContext(), 0);
                    }
                });
            }  else if (item.type == HOME_MY_INSTALL) {
                viewHolder.name.setText("安装管理");
                viewHolder.icon.setImageResource(R.mipmap.t_y);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InstallEquipmentListActivity.launch(view.getContext(), 0);
                    }
                });
            }  else if (item.type == HOME_MY_UNINSTALL) {
                viewHolder.name.setText("拆卸管理");
                viewHolder.icon.setImageResource(R.mipmap.t_y);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        security.zw.com.securitycheck.uninstallEqupment.allEqupment.allEqupment.InstallEquipmentListActivity.launch(view.getContext(), 0);
                    }
                });
            } else if (item.type == HOME_MY_RECORD) {
                viewHolder.name.setText("使用登记");
                viewHolder.icon.setImageResource(R.mipmap.t_x);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        security.zw.com.securitycheck.usedEqupment.allEqupment.InstallEquipmentListActivity.launch(view.getContext(), 0);
                    }
                });
            } else if (item.type == HOME_MY_LOW) {
                viewHolder.name.setText("安全执法");
                viewHolder.icon.setImageResource(R.mipmap.t_w);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("相关执法");
                    }
                });
            } else if (item.type == HOME_MY_FILING) {
                viewHolder.name.setText("产权变更");
                viewHolder.icon.setImageResource(R.mipmap.t_v);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        security.zw.com.securitycheck.recordedEqupment.allEqupment.InstallEquipmentListActivity.launch(view.getContext(), 0);
                    }
                });
            } else if (item.type == HOME_MY_CHECKED) {
                viewHolder.name.setText("复核");
                viewHolder.icon.setImageResource(R.mipmap.t_u);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("复核");
                    }
                });
            } else if (item.type == HOME_MY_CASE) {
                viewHolder.name.setText("办案");
                viewHolder.icon.setImageResource(R.mipmap.t_t);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("办案");
                    }
                });
            } else if (item.type == HOME_MY_SUPERVISE) {
                viewHolder.name.setText("领导督办");
                viewHolder.icon.setImageResource(R.mipmap.t_s);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("下达督办");
                    }
                });
            } else if (item.type == HOME_MY_CHECKER_COMPANY) {
                viewHolder.name.setText("检测单位");
                viewHolder.icon.setImageResource(R.mipmap.t_f);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.Short("检测单位");
                    }
                });
            } else if (item.type == HOME_ALL_EQUPMENT) {
                viewHolder.name.setText("已备案设备");
                viewHolder.icon.setImageResource(R.mipmap.t_z);
                viewHolder.rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AllEquipmentListActivity.launch(view.getContext(), 0);
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