package security.zw.com.securitycheck.widget.imagepreview;

import android.app.Activity;
import android.support.v4.app.Fragment;


/**
 * 媒体浏览Fragment的基类
 */
public abstract class BrowseBaseFragment extends Fragment {

    public MediaClickListener mMediaClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof MediaClickListener)) {
            throw new IllegalArgumentException(activity.getClass().getSimpleName() + " must implement MediaClickListener");
        }
        mMediaClickListener = (MediaClickListener) activity;
    }

    public interface MediaClickListener {
        void onMediaClick();
    }

}