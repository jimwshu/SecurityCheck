package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.CompanyDetailActivity;
import security.zw.com.securitycheck.PersonDetailActivity;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.bean.Company;
import security.zw.com.securitycheck.bean.Person;


/**
 * Created by wangshu on 17/5/31.
 */


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.CompanyViewHolder> {


    private ArrayList<Person> mData;

    private Activity mContext;

    private int type = -1;

    public PersonAdapter(ArrayList<Person> mData, Activity mActivity) {
        this.mData = mData;
        this.mContext = mActivity;
    }

    public PersonAdapter(ArrayList<Person> mData, Activity mActivity, int type) {
        this.mData = mData;
        this.mContext = mActivity;
        this.type = type;
    }

    @Override
    public PersonAdapter.CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_person, parent, false);
        return new CompanyViewHolder(view);
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RelativeLayout rel;
        public TextView job;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rel = itemView.findViewById(R.id.rel);
            job = itemView.findViewById(R.id.job);
        }
    }

    @Override
    public void onBindViewHolder(PersonAdapter.CompanyViewHolder holder, final int position) {
        final Person person = mData.get(position);
        holder.name.setText(person.name);
        holder.job.setText(person.position);
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonDetailActivity.launch(view.getContext(), person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}