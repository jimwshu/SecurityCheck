package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;



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
                        case 1:
                        case 2:
                        case 3:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                            return 6;

                        case 5:
                        case 7:
                            return 3;
                    }
                    return 0;
                }
            });

        }
    }


    public int HOME_BANNNER = 0; // 首页banner列表
    private int HOME_WINNER_INFO = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }



}