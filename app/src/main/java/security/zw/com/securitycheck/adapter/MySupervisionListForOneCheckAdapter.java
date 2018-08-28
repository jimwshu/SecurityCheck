package security.zw.com.securitycheck.adapter;

import com.google.gson.JsonObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.zw.com.securitycheck.CheckItemForMyCheckDetailActivity;
import security.zw.com.securitycheck.Constans;
import security.zw.com.securitycheck.MySupervisionProjectDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;
import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.MyCheckProjectDetail;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.bean.SupervisionProjectList;
import security.zw.com.securitycheck.utils.TimeUtils;
import security.zw.com.securitycheck.utils.net.NetRequest;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * Created by wangshu on 17/5/31.
 */


public class MySupervisionListForOneCheckAdapter extends RecyclerView.Adapter<MySupervisionListForOneCheckAdapter.ProjectDetailViewHolder> {


    private ArrayList<SupervisionProjectList> mData;

    private Activity mContext;

    private int type = -1;

    public interface ChangeListener {
        void change(int pos);
    }

    public ChangeListener changeListener;

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public MySupervisionListForOneCheckAdapter(ArrayList<SupervisionProjectList> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public MySupervisionListForOneCheckAdapter(ArrayList<SupervisionProjectList> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public MySupervisionListForOneCheckAdapter.ProjectDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_supervision_list_for, parent, false);
        return new ProjectDetailViewHolder(view);
    }

    public class ProjectDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView circle;
        public TextView time;
        public TextView name;
        public TextView score;
        public TextView change;
        public TextView check_result;

        public RelativeLayout rel;

        public ProjectDetailViewHolder(View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.circle);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            check_result = itemView.findViewById(R.id.check_result);
            rel = itemView.findViewById(R.id.rel);
            change = itemView.findViewById(R.id.change);
        }
    }

    @Override
    public void onBindViewHolder(MySupervisionListForOneCheckAdapter.ProjectDetailViewHolder holder, final int position) {
        final SupervisionProjectList myCheckProjectDetail = mData.get(position);

        holder.name.setText(myCheckProjectDetail.projectName);
        holder.time.setText(TimeUtils.secToTime(myCheckProjectDetail.createTime));

        if (myCheckProjectDetail.checkType == ProjectDetail.CHECK_TYPE_COUNT) {
            holder.score.setText("评分检查");
        } else if (myCheckProjectDetail.checkType == ProjectDetail.CHECK_TYPE_RANDOM) {
            holder.score.setText("随机检查");
        } else if (myCheckProjectDetail.checkType == ProjectDetail.CHECK_TYPE_EVERY){
            holder.score.setText("逐项检查");
        }

        holder.check_result.setText("不合格");
        holder.circle.setBackgroundResource(R.drawable.red_circle);

        /*if (myCheckProjectDetail.rank == 1) {
            holder.check_result.setText("合格");
            holder.circle.setBackgroundResource(R.drawable.green_circle);

        } else if (myCheckProjectDetail.rank == 2) {
            holder.check_result.setText("不合格（不需整改）");
            holder.circle.setBackgroundResource(R.drawable.red_circle);

        } else if (myCheckProjectDetail.rank == 3) {
            holder.check_result.setText("不合格（需要整改）");
            holder.circle.setBackgroundResource(R.drawable.red_circle);

        }*/
        holder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeListener != null) {
                    changeListener.change(position);
                }
            }
        });

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySupervisionProjectDetailActivity.launch(view.getContext(), myCheckProjectDetail);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}