package security.zw.com.securitycheck.utils.imagepicker.recyclerview;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private static final boolean DEBUG = true;
    private static final String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private boolean enabled = true;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mVisibleThreshold = -1;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    private boolean mIsOrientationHelperVertical;
    private OrientationHelper mOrientationHelper;



    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener() {
    }

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerOnScrollListener(int visibleThreshold) {
        this.mVisibleThreshold = visibleThreshold;
    }

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager, int visibleThreshold) {
        this.mLayoutManager = layoutManager;
        this.mVisibleThreshold = visibleThreshold;
    }


    private int findFirstVisibleItemPosition(RecyclerView recyclerView) {
        final View child = findOneVisibleChild(0, mLayoutManager.getChildCount(), false, true);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    private int findLastVisibleItemPosition(RecyclerView recyclerView) {
        final View child = findOneVisibleChild(recyclerView.getChildCount() - 1, -1, false, true);
        return child == null ? RecyclerView.NO_POSITION : recyclerView.getChildAdapterPosition(child);
    }

    private View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                                     boolean acceptPartiallyVisible) {
        if (mLayoutManager.canScrollVertically() != mIsOrientationHelperVertical
                || mOrientationHelper == null) {
            mIsOrientationHelperVertical = mLayoutManager.canScrollVertically();
            mOrientationHelper = mIsOrientationHelperVertical
                    ? OrientationHelper.createVerticalHelper(mLayoutManager)
                    : OrientationHelper.createHorizontalHelper(mLayoutManager);
        }

        final int start = mOrientationHelper.getStartAfterPadding();
        final int end = mOrientationHelper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = mLayoutManager.getChildAt(i);
            if (child != null) {
                final int childStart = mOrientationHelper.getDecoratedStart(child);
                final int childEnd = mOrientationHelper.getDecoratedEnd(child);
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child;
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child;
                        }
                    } else {
                        return child;
                    }
                }
            }
        }
        return partiallyVisible;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (enabled) {
            if (mLayoutManager == null)
                mLayoutManager = recyclerView.getLayoutManager();

            int footerItemCount = 0;

            if (mVisibleThreshold == -1)
                mVisibleThreshold = findLastVisibleItemPosition(recyclerView) - findFirstVisibleItemPosition(recyclerView) - footerItemCount;

            mVisibleItemCount = recyclerView.getChildCount() - footerItemCount;
            mTotalItemCount = mLayoutManager.getItemCount() - footerItemCount;
            mFirstVisibleItem = findFirstVisibleItemPosition(recyclerView);

            if (mLoading) {
                if (mTotalItemCount > mPreviousTotal) {
                    mLoading = false;
                    mPreviousTotal = mTotalItemCount;
                    if (DEBUG) Log.i(TAG, "not loading more");
                }
            }
            if (!mLoading && (mTotalItemCount - mVisibleItemCount)
                    <= (mFirstVisibleItem + mVisibleThreshold)) {

                if (DEBUG) Log.i(TAG, "onLoadMore() total = " + mTotalItemCount + "visibcount = " + mVisibleItemCount);
                onLoadMore();

                mLoading = true;
            }
        }
    }

    public EndlessRecyclerOnScrollListener enable() {
        enabled = true;
        return this;
    }

    public EndlessRecyclerOnScrollListener disable() {
        enabled = false;
        return this;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public int getTotalItemCount() {
        return mTotalItemCount;
    }

    public int getFirstVisibleItem() {
        return mFirstVisibleItem;
    }

    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    public void reset() {
        mPreviousTotal = 0;
        mLoading = false;
    }

    public abstract void onLoadMore();
}