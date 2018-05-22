package security.zw.com.securitycheck.utils.imagepreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.frescoimageview.TouchImageView;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;


/**
 * Created by s1rius on 21/11/2017.
 */

public class BrowseImgFragment extends BrowseBaseFragment {

    SimpleDraweeView mImageView;
    private ImageInfo mImage;

    // Required empty public constructor
    public BrowseImgFragment() {
    }

    /**
     * @param picUrl PicUrl 1.
     * @return A new instance of fragment BrowseImgFragment.
     */
    public static BrowseImgFragment newInstance(ImageInfo picUrl) {
        BrowseImgFragment f = new BrowseImgFragment();
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
            mImage = (ImageInfo) arguments.getSerializable("img");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenet_browse_img, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = (SimpleDraweeView) view.findViewById(R.id.image);
        FrescoImageloader.displayImage(mImageView, mImage.getImageUrl());
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
