package security.zw.com.securitycheck.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * 列表适配器的基类，负责初始化基础控件
 *
 * @author mamx
 * @version 1.0
 * @date 2012-3-22
 */
public abstract class DefaultAdapter<T> extends BaseAdapter {

    protected ListView _mListView;

    protected ArrayList<T> _dataSource;

    protected final Activity _mContext;

    protected LayoutInflater _mInflater;

    public DefaultAdapter(ArrayList<T> dataSource, Activity mContext) {
        super();
        _dataSource = dataSource;
        _mContext = mContext;
        _mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return _dataSource.size();
    }

    @Override
    public T getItem(int position) {
        if (_dataSource != null && _dataSource.size() > 0)
            return _dataSource.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearImageCache() {

    }

    public void onDestroy() {
    }
}
