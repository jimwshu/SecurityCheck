package security.zw.com.securitycheck.utils.imagepreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.base.BaseActivity;
import security.zw.com.securitycheck.utils.imagepicker.ImageFolderInfo;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfo;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfoController;
import security.zw.com.securitycheck.utils.imagepicker.ImageInfoLoader;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

/**
 * Created by wangshu on 15/11/12.
 */
public class ImagePreviewActivity extends BaseActivity implements BrowseBaseFragment.MediaClickListener, ViewPager.OnPageChangeListener {

    @Override
    protected boolean isNeedImmersiveStatusBar() {
        return false;
    }

    public final static String KEY_CHECKED_ARRAY = "_checked_array_";//已经选取的pic list
    public final static String KEY_POSITION = "_pic_position_";//当前所在位置
    public final static String KEY_PIC_ALL = "_pic_all_";//所有的pic list
    private static final String KEY_FOLDER = "_pic_folder_";
    private static final String KEY_CURRENT_IMAGE = "_pic_info";

    private ViewPager priview_viewpager;
    private ArrayList<ImageInfo> checkedArray = new ArrayList<ImageInfo>();
    private ArrayList<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
    // private int nowPosition = 0;//当前所在位置
    private PagerAdapter pagerAdapter;
    private int currentPosition = -1;
    private ImageInfo imageInfo = null;

    ImageInfoController imageInfoController = new ImageInfoController();


    private RelativeLayout bottom_layout;
    private TextView select_num;
    private TextView sent_textview;

    private int from;


    public static void launchForResult(Activity context, ArrayList<ImageInfo> checkedArray, ArrayList<ImageInfo> allPic, int position, int reqeustCode) {
        Intent intent = new Intent();
        intent.setClass(context, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.KEY_CHECKED_ARRAY, checkedArray);
        intent.putExtra(ImagePreviewActivity.KEY_PIC_ALL, allPic);
        intent.putExtra(ImagePreviewActivity.KEY_POSITION, position);
        context.startActivityForResult(intent, reqeustCode);
    }

    public static void launchForResult(Activity activity, ArrayList<ImageInfo> checkedArray, ImageFolderInfo folder, ImageInfo currentImage, int reqeustCode) {
        Intent intent = new Intent();
        intent.setClass(activity, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.KEY_CHECKED_ARRAY, checkedArray);
        intent.putExtra(ImagePreviewActivity.KEY_FOLDER, folder);
        intent.putExtra(ImagePreviewActivity.KEY_CURRENT_IMAGE, currentImage);
        activity.startActivityForResult(intent, reqeustCode);
    }


    protected String getCustomTitle() {
        return (currentPosition + 1) + "/" + (imageInfos == null ? "..." : imageInfos.size() + "");
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        boolean value = handleIntent(getIntent());
        if (!value) {
            finish();
            return;
        }
        initWidget();
        initListener();
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select_icon:
                if (checkedArray.contains(imageInfo)) {
                    checkedArray.remove(imageInfo);
                    select_num.setText(checkedArray.size() + "");
                } else {
                    if (checkedArray.size() < ImagesPickerActivity.maxCount) {
                        if (!imageInfo.isOverSize()) {
                            checkedArray.add(imageInfo);
                        } else {
                            ToastUtil.Long( String.format("图片不能大于%s"));
                        }
                        select_num.setText(checkedArray.size() + "");
                    } else {
                        ToastUtil.Long(String.format("最多只能选取%s张图片哦", ImagesPickerActivity.maxCount));

                    }
                }
                break;
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("checkedArray", checkedArray);
                setResult(ImagesPickerActivity.KEY_PRIVIEW, intent);

                this.finish();
                break;

            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        imageInfoController.onDestroy();
        super.onDestroy();
    }

    public boolean handleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            boolean isBootClassLoader = intent.getExtras().getClassLoader().getClass().getSimpleName().contains("BootClassLoader");
            if (isBootClassLoader) {
                intent.setExtrasClassLoader(ImagePreviewActivity.class.getClassLoader());
            }
        }
        imageInfos.clear();
        if (intent.hasExtra(KEY_PIC_ALL)) {
            imageInfos.addAll((ArrayList<ImageInfo>) intent.getSerializableExtra(KEY_PIC_ALL));
        }
        if (intent.hasExtra(KEY_FOLDER)) {
            ImageFolderInfo folder = (ImageFolderInfo) intent.getSerializableExtra(KEY_FOLDER);
            if (folder != null) {
                imageInfoController.onCreate(this, new ImageInfoController.ImageInfoLoadCallbacks() {
                    @Override
                    public void onImageLoad(Cursor cursor) {
                        imageInfos.clear();
                        while (cursor.moveToNext()) {
                            ImageInfo imageInfo = ImageInfo.valueOf(cursor);
                            if (imageInfo.id != ImageInfoLoader.ITEM_ID_CAPTURE) {
                                imageInfos.add(imageInfo);
                            }
                        }
                        pagerAdapter.notifyDataSetChanged();
                        if (imageInfo != null) {
                            currentPosition = imageInfos.indexOf(imageInfo);
                            priview_viewpager.setCurrentItem(currentPosition, false);
                        }

                    }

                    @Override
                    public void onAlbumReset() {

                    }
                }, folder.getId());
                imageInfoController.loadImage();
            }
        }

        checkedArray = (ArrayList<ImageInfo>) intent.getSerializableExtra(KEY_CHECKED_ARRAY);
        currentPosition = intent.getIntExtra(KEY_POSITION, -1);
        if (currentPosition < 0) {
            currentPosition = 0;
            imageInfo = (ImageInfo) intent.getSerializableExtra(KEY_CURRENT_IMAGE);
        } else if (imageInfos != null) {
            if (currentPosition >= imageInfos.size()) {
                currentPosition = imageInfos.size() - 1;
                imageInfo = imageInfos.get(currentPosition);
            } else {
                imageInfo = imageInfos.get(currentPosition);
            }

        }
        if (imageInfo == null) {
            return false;
        }
        from = intent.getIntExtra("KEY_PICK_IAMGE", 0);
        return true;
    }

    public void initWidget() {
        priview_viewpager = (ViewPager) findViewById(R.id.priview_viewpager);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        select_num = (TextView) findViewById(R.id.select_num);
        select_num.setText(checkedArray.size() + "");
        sent_textview = (TextView) findViewById(R.id.sent_textview);
        sent_textview.setText("确定");

    }

    public void initListener() {
        pagerAdapter = new ImagePreviewAdapter(ImagePreviewActivity.this, getSupportFragmentManager());
        priview_viewpager.setAdapter(pagerAdapter);
        priview_viewpager.setCurrentItem(currentPosition);
        priview_viewpager.setOnPageChangeListener(this);
        bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ImagesPickerActivity.PATH_KEY, checkedArray);
                setResult(ImagesPickerActivity.KEY_DIRECTOR_RETURN, intent);
                finish();

            }
        });
        initBar();
        onPageSelected(currentPosition);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("checkedArray", checkedArray);
        setResult(ImagesPickerActivity.KEY_PRIVIEW, intent);
        super.onBackPressed();
    }

    @Override
    public void onMediaClick() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (imageInfos.size() > position) {
            currentPosition = position;
            imageInfo = imageInfos.get(position);
            check();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ImagePreviewAdapter extends FragmentStatePagerAdapter {

        final LayoutInflater inflater;

        ImagePreviewAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return imageInfos.size();
        }

        @Override
        public Fragment getItem(int position) {
            ImageInfo imageInfo = imageInfos.get(position);
            switch (imageInfo.mediaFormat) {
                default:
                    BrowseImgFragment f = new BrowseImgFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("img", imageInfo);
                    f.setArguments(b);
                    return f;
            }
        }

    }

    private void initBar() {
        barLeft = (LinearLayout) findViewById(R.id.bar_left);
        barBack = (ImageView) findViewById(R.id.bar_back);
        barClose = (ImageView) findViewById(R.id.bar_close);
        barTitle = (TextView) findViewById(R.id.bar_title);
        barLeft.setVisibility(View.VISIBLE);
        barBack.setVisibility(View.VISIBLE);
        barClose.setVisibility(View.GONE);
        barTitle.setText("图片选择");

        barBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("checkedArray", checkedArray);
                setResult(ImagesPickerActivity.KEY_PRIVIEW, intent);
                finish();
            }
        });
        barRight = (RelativeLayout) findViewById(R.id.bar_right_rel);
        checked = (ImageView) findViewById(R.id.checked);
        cancel = (TextView) findViewById(R.id.cancel);

        barRight.setVisibility(View.VISIBLE);
        checked.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);

        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedArray.contains(imageInfo)) {
                    checkedArray.remove(imageInfo);
                    select_num.setText(checkedArray.size() + "");
                    check();
                } else {
                    if (checkedArray.size() < ImagesPickerActivity.maxCount) {
                        checkedArray.add(imageInfo);
                        select_num.setText(checkedArray.size() + "");
                        check();
                    } else {
                        ToastUtil.Short(String.format("最多只能选取%s张图片哦", ImagesPickerActivity.maxCount));
                    }
                }
            }
        });
        check();

    }

    private void check() {
        if (imageInfos != null && checkedArray.contains(imageInfo)) {
            checked.setImageResource(R.mipmap.image_picker_checked);
        } else {
            checked.setImageResource(R.mipmap.image_picker_unchecked);
        }
    }

    private LinearLayout barLeft;
    private ImageView barBack;
    private ImageView barClose;
    private TextView barTitle;
    private RelativeLayout barRight;
    private ImageView checked;
    private TextView cancel;
}
