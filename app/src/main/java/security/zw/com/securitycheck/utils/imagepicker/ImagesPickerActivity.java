package security.zw.com.securitycheck.utils.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.base.BaseActivity;
import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.utils.FileUtils;
import security.zw.com.securitycheck.utils.imagepicker.recyclerview.CursorRecyclerViewAdapter;
import security.zw.com.securitycheck.utils.imagepicker.recyclerview.ItemClickSupport;
import security.zw.com.securitycheck.utils.imagepreview.ImagePreviewActivity;

/**
 * 图片选择
 */
public class ImagesPickerActivity extends BaseActivity implements ImageGridAdapter.OnMediaChoooseListener {
    public static final String PATH_KEY = "paths";
    public static final String COUNT_KEY = "count";
    public static final String CHECKED_ARRAY_KEY = "checkedArray";
    public static final int KEY_PRIVIEW = 99;
    public static final int KEY_DIRECTOR_RETURN = 999;
    public static final int KEY_CAMARE = 1999;
    public static int maxCount = 6;
    private FolderListAdapter folderAdapter;
    private ArrayList<ImageInfo> checkedArray = new ArrayList<ImageInfo>();
    private RecyclerView folderView;
    private View folderMask;
    private TextView folderName;
    private TextView folderCount;
    private TextView select_num;
    private RelativeLayout bottom_layout;
    private TextView picker_preview;
    private Animation inAnim;
    private Animation outAnim;
    private Animation maskInAnim;
    private Animation maskOutAnim;
    private boolean showFolder = false;
    private int imageWidth = 0;
    BlackProgressDialog dialog;

    ImageFolderController imageFolderController = new ImageFolderController();

    /**
     * For memory case.
     */
    private int from;//1为来自群，2来自chat，默认来自糗友圈发帖
    private Uri mCameraFilePath;

    private ImageFolderInfo mCurrentFolder;
    private ImagePickFragment currentFragment;

    public static void launch(Context context, int count) {
        Intent intent = new Intent(context, ImagesPickerActivity.class);
        intent.putExtra(COUNT_KEY, count);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static Intent prepareIntent(Activity activity, int count) {
        Intent intent = new Intent(activity, ImagesPickerActivity.class);
        intent.putExtra(COUNT_KEY, count);
        return intent;
    }

    public static Intent prepareIntent(Activity activity, int count, ArrayList<ImageInfo> checkedArray) {
        Intent intent = new Intent(activity, ImagesPickerActivity.class);
        intent.putExtra(COUNT_KEY, count);
        intent.putExtra(CHECKED_ARRAY_KEY, checkedArray);
        return intent;
    }

    protected String getCustomTitle() {
        return "图片选择";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_picker_copy;
    }

    @Override
    protected void initData() {
        initFolders();
    }

    protected void init() {
        maxCount = getIntent().getIntExtra(COUNT_KEY, 0);
        from = getIntent().getIntExtra("KEY_PICK_IAMGE", 0);
        ArrayList<ImageInfo> intentCheck = (ArrayList<ImageInfo>) getIntent().getSerializableExtra(CHECKED_ARRAY_KEY);
        if (intentCheck != null && intentCheck.size() > 0) {
            checkedArray.addAll(intentCheck);
        }
        if (maxCount <= 0) {
            finish();
        }
        initAnim();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = (int) ((metrics.widthPixels - 5 * 4 * metrics.density) / 4);
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        init();
        folderAdapter = new FolderListAdapter(this, null);

        folderView = findViewById(R.id.image_folder_list);
        folderMask = findViewById(R.id.image_folder_list_mask);
        folderView.setLayoutManager(new LinearLayoutManager(ImagesPickerActivity.this));
        folderView.setAdapter(folderAdapter);
        ItemClickSupport.addTo(folderView).setOnItemClickListener(folderItemClickListener);

        folderName = (TextView) findViewById(R.id.picker_name);
        folderCount = (TextView) findViewById(R.id.picker_count);
        if (from != 0) {
            folderCount.setText("发送");
        }

        select_num = (TextView) findViewById(R.id.select_num);
        select_num.setText(checkedArray.size() + "");

        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = checkedArray.size();
                String paths[] = new String[len];
                for (int i = 0; i < len; i++) {
                    paths[i] = checkedArray.get(i).url;
                }
                Intent intent = new Intent();
                intent.putExtra(PATH_KEY, checkedArray);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        picker_preview = (TextView) findViewById(R.id.picker_preview);
        picker_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (checkedArray.size() > 0) {
            picker_preview.setClickable(true);
            picker_preview.setTextColor(0xffffffff);
        } else {
            picker_preview.setClickable(false);
            picker_preview.setTextColor(0xffededed);
        }

        folderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFolder) {
                    hideFolder();
                } else {
                    showFolder();
                }
            }
        });
        dialog = new BlackProgressDialog(this);
    }

    private void initAnim() {
        final int duration = 300;
        inAnim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_SELF, 0);
        inAnim.setDuration(duration);
        outAnim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                folderView.setVisibility(View.GONE);
                folderMask.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        outAnim.setDuration(duration);
        maskInAnim = new AlphaAnimation(0, 1);
        maskInAnim.setDuration(duration);
        maskOutAnim = new AlphaAnimation(1, 0);
        maskOutAnim.setDuration(duration);
    }

    private void showFolder() {
        showFolder = true;
        folderView.setVisibility(View.VISIBLE);
        folderMask.setVisibility(View.VISIBLE);

        folderView.startAnimation(inAnim);
        folderMask.startAnimation(maskInAnim);
        folderMask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        hideFolder();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });
    }

    private void hideFolder() {
        showFolder = false;
        folderView.startAnimation(outAnim);
        folderMask.startAnimation(maskOutAnim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, 1, 1, "取消");
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                /*int len = checkedArray.size();
                String paths[] = new String[len];
                for (int i = 0; i < len; i++) {
                    paths[i] = checkedArray.get(i).path;
                }
                Intent intent = new Intent();
                intent.putExtra(PATH_KEY, paths);
                setResult(RESULT_OK, intent);*/
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFolders() {
        imageFolderController.onCreate(this, new ImageFolderController.ImageFolderCallback() {
            @Override
            public void onFilesLoad(Cursor cursor) {
                cursor.moveToFirst();
                ImageFolderInfo folderInfo = ImageFolderInfo.valueOf(cursor);
                if (folderInfo != null) {
                    selectFolder(folderInfo);
                }

                folderAdapter.swapCursor(cursor);
            }

            @Override
            public void onReset() {
                folderAdapter.swapCursor(null);
            }
        });
        imageFolderController.load();
    }

    private void selectFolder(ImageFolderInfo info) {
        mCurrentFolder = info;
        currentFragment = ImagePickFragment.newInstance(info);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, currentFragment)
                .commitNowAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == KEY_CAMARE && resultCode == RESULT_OK) {
            dialog.show();
            FileUtils.notifyFileChanged(ImagesPickerActivity.this, new File(mCameraFilePath.getPath()));
            ImageInfo cameraInfo = new ImageInfo(mCameraFilePath.toString());
            checkedArray.add(cameraInfo);

            folderView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageFolderController.restart();
                    currentFragment.refreshData();
                    dialog.dismiss();
                }
            }, 300);

            refreshCheckState();
        }
        /*
        todo start activity 返回
        if (resultCode == RESULT_OK) {
            dialog.show();
            FileUtils.notifyFileChanged(ImagesPickerActivity.this, new File(mCameraFilePath.getPath()));
            ImageInfo cameraInfo = new ImageInfo(mCameraFilePath.toString());
            checkedArray.add(cameraInfo);

            folderView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageFolderController.restart();
                    currentFragment.refreshData();
                    dialog.dismiss();
                }
            }, 300);

        }*/



        // 从ImagePreviewActivity点击完成返回来的，直接关闭页面，返回paths
        if (resultCode == KEY_DIRECTOR_RETURN) {
            ArrayList<ImageInfo> paths = (ArrayList<ImageInfo>) data.getSerializableExtra(ImagesPickerActivity.PATH_KEY);
            Intent intent = new Intent();
            intent.putExtra(PATH_KEY, paths);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (requestCode == KEY_PRIVIEW && resultCode == KEY_PRIVIEW) {
                checkedArray = (ArrayList<ImageInfo>) data.getSerializableExtra("checkedArray");
            }
            select_num.setText(checkedArray.size() + "");
            if (checkedArray.size() > 0) {
                picker_preview.setClickable(true);
                picker_preview.setTextColor(0xffffffff);
            } else {
                picker_preview.setClickable(false);
                picker_preview.setTextColor(0xffededed);
            }
            currentFragment.notifyDataChange();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshCheckState() {
        if (isFinishing()) {
            return;
        }
        select_num.setText(checkedArray.size() + "");
    }

    @Override
    protected void onDestroy() {
        imageFolderController.onDestory();
        super.onDestroy();

    }

    @Override
    public boolean isMediaSelected(ImageInfo imageInfo) {
        return checkedArray.contains(imageInfo);
    }

    @Override
    public int indexOfSelect(ImageInfo imageInfo) {
        return checkedArray.indexOf(imageInfo);
    }

    @Override
    public boolean isMaxCount() {
        return checkedArray.size() >= maxCount;
    }

    @Override
    public int getMaxChooseCount() {
        return maxCount;
    }

    /**
     * 调用手机相机
     */
    public void startCamera() {
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraFilePath = Uri.fromFile(getCameraOutputPath(this));
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFilePath);
        startActivityForResult(getImageByCamera, KEY_CAMARE);
    }



    @Override
    public void goPreview(ImageFolderInfo folderInfo, ImageInfo imageInfo) {
        // todo preview activity
        ImagePreviewActivity.launchForResult(this, checkedArray, folderInfo, imageInfo, KEY_PRIVIEW);
    }

    @Override
    public void select(ImageInfo imageInfo) {
        checkedArray.add(imageInfo);
        select_num.setText(checkedArray.size() + "");
    }

    @Override
    public void unSelect(ImageInfo imageInfo) {
        checkedArray.remove(imageInfo);
        select_num.setText(checkedArray.size() + "");

    }

    /**
     * 获取相机照完后的图片路径
     */
    private File getCameraOutputPath(Context context) {
        return new File(FileUtils.getExternalDCIMDir(context), "security" + System.currentTimeMillis() + ".jpg");
    }

    private void displayFileImage(SimpleDraweeView simpleDraweeView, String uri) {
        displayFileImage(simpleDraweeView, uri, null);
    }

    private void displayFileImage(SimpleDraweeView simpleDraweeView, String uri, MediaFormat format) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(imageWidth, imageWidth))
                .setImageDecodeOptions(new ImageDecodeOptionsBuilder()
                        .setForceStaticImage(true)
                        .build())
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        simpleDraweeView.setController(controller);
    }

    private class FolderListAdapter extends CursorRecyclerViewAdapter<FolderHolder> {

        public FolderListAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        protected int getItemViewType(int position, Cursor cursor) {
            return 0;
        }


        @Override
        public void onBindViewHolder(FolderHolder viewHolder, Cursor cursor) {
            ImageFolderInfo folderInfo = ImageFolderInfo.valueOf(cursor);
            viewHolder.name.setText(folderInfo.getName());
            viewHolder.count.setText(folderInfo.count + "张");
            displayFileImage(viewHolder.image, Uri.fromFile(new File(folderInfo.photoPath)).toString());
            viewHolder.setFolderInfo(folderInfo);
        }

        @Override
        public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= getLayoutInflater().inflate(R.layout.item_image_folder, parent, false);
            return new FolderHolder(view);
        }
    }

    private class FolderHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView name;
        TextView count;
        ImageFolderInfo folderInfo;

        public FolderHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image_folder_image);
            name = (TextView) itemView.findViewById(R.id.image_folder_name);
            count = (TextView) itemView.findViewById(R.id.image_folder_count);

        }

        public void setFolderInfo(ImageFolderInfo folder) {
            this.folderInfo = folder;
        }
    }

    ItemClickSupport.OnItemClickListener folderItemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            hideFolder();
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            if (holder instanceof FolderHolder) {
                selectFolder(((FolderHolder) holder).folderInfo);
            }
        }
    };

}
