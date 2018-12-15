package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Random;

import security.zw.com.securitycheck.EquipmentDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.EquipmentDetail;
import security.zw.com.securitycheck.bean.EquipmentList;
import security.zw.com.securitycheck.model.Gallery;
import security.zw.com.securitycheck.widget.imagepreview.UserImagePreviewActivity;


/**
 * Created by wangshu on 17/5/31.
 */


public class EquipmentDetailAdapter extends RecyclerView.Adapter<EquipmentDetailAdapter.ProjectViewHolder> {


    private ArrayList<EquipmentDetail> mData;
    private EquipmentList equipmentList;
    private Activity mContext;

    private int type = -1;

    public EquipmentDetailAdapter(ArrayList<EquipmentDetail> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public EquipmentDetailAdapter(ArrayList<EquipmentDetail> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public EquipmentDetailAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_equipment_detail, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView name;
        public RelativeLayout rel;
        public TextView type;
        public TextView mode;
        public SimpleDraweeView image;
        public TextView check;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            type = itemView.findViewById(R.id.type);
            mode = itemView.findViewById(R.id.mode);
            image = itemView.findViewById(R.id.image1);
            check = itemView.findViewById(R.id.check_detail_tv);
        }
    }

    @Override
    public void onBindViewHolder(EquipmentDetailAdapter.ProjectViewHolder holder, final int position) {
        final EquipmentDetail p = mData.get(position);
        holder.name.setText(p.docDir + "");
        holder.mode.setText(p.demand + "");

        if (!TextUtils.isEmpty(p.docUrl)) {
            holder.image.setImageURI(Uri.parse(p.docUrl));
        }

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gallery gallery = new Gallery();
                gallery.id = new Random().nextLong();
                gallery.status = Gallery.STATUS_NETWORK;
                gallery.gallery_url = p.docUrl;
                ArrayList<Gallery> galleries = new ArrayList<>();
                galleries.add(gallery);

                launch(v.getContext(), galleries, 0);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gallery gallery = new Gallery();
                gallery.id = new Random().nextLong();
                gallery.status = Gallery.STATUS_NETWORK;
                gallery.gallery_url = p.docUrl;
                ArrayList<Gallery> galleries = new ArrayList<>();
                galleries.add(gallery);

                launch(v.getContext(), galleries, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static void launch(Context context, ArrayList<Gallery> galleries, int pos) {
        Intent intent = new Intent(context, UserImagePreviewActivity.class);
        intent.putExtra("infos", galleries);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }
}