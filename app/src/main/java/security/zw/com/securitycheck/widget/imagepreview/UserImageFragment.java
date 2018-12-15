package security.zw.com.securitycheck.widget.imagepreview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.frescoimageview.TouchImageView;


/**
 * Created by s1rius on 21/11/2017.
 */

public class UserImageFragment extends BrowseBaseFragment {

    private TouchImageView mImageView;
    private String url;

    // Required empty public constructor
    public UserImageFragment() {
    }

    /**
     * @param picUrl PicUrl 1.
     * @return A new instance of fragment BrowseImgFragment.
     */
    public static UserImageFragment newInstance(String picUrl) {
        UserImageFragment f = new UserImageFragment();
        Bundle b = new Bundle();
        b.putSerializable("img", picUrl);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = (String) arguments.getString("img");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenet_browse_img, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = (TouchImageView) view.findViewById(R.id.image);
        if (!TextUtils.isEmpty(url)) {
            mImageView.setImageURI(Uri.parse(url));
        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaClickListener != null) {
                    mMediaClickListener.onMediaClick();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
