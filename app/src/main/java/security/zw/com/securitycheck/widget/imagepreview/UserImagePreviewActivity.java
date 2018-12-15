package security.zw.com.securitycheck.widget.imagepreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.model.Gallery;


/**
 * Created by wangshu on 15/11/12.
 */
public class UserImagePreviewActivity extends BaseSystemBarTintActivity implements BrowseBaseFragment.MediaClickListener, ViewPager.OnPageChangeListener {

    protected ViewPager mViewPager;
    protected PagerAdapter mAdapter;

    protected ArrayList<Gallery> mItems;
    protected int mPosition = 0;//当前所在位置

    protected int getLayoutId() {
        return R.layout.activity_user_image_preview;
    }

    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.priview_viewpager);
        mViewPager.addOnPageChangeListener(this);
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
    }

    protected void initData() {
        mItems = (ArrayList<Gallery>) getIntent().getSerializableExtra("infos");
        mPosition = getIntent().getIntExtra("pos", 0);

        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        if (mItems.size() == 0) {
            finish();
            return;
        }

        mAdapter = new ImagePreviewAdapter(UserImagePreviewActivity.this, getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onMediaClick() {
        finish(); // 点击图片关闭
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    protected boolean isNeedImmersiveStatusBar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class ImagePreviewAdapter extends FragmentStatePagerAdapter {

        final LayoutInflater inflater;

        ImagePreviewAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Fragment getItem(int position) {
            Gallery imageInfo = mItems.get(position);
            return getImageFragment(imageInfo);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }

    protected UserImageFragment getImageFragment(Gallery imageInfo) {
        return UserImageFragment.newInstance(imageInfo.gallery_url);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public static void launch(Context context, ArrayList<Gallery> galleries, int pos) {
        Intent intent = new Intent(context, UserImagePreviewActivity.class);
        intent.putExtra("infos", galleries);
        intent.putExtra("pos", pos);
        context.startActivity(intent);
    }
}
