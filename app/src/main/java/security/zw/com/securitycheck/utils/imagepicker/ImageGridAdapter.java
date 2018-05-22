package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.QBImageView;
import security.zw.com.securitycheck.utils.image.FrescoImageloader;
import security.zw.com.securitycheck.utils.imagepicker.recyclerview.CursorRecyclerViewAdapter;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class ImageGridAdapter extends CursorRecyclerViewAdapter {

    int resizeWidth;

    public ImageGridAdapter(Context context, Cursor cursor, OnMediaChoooseListener listener) {
        super(context, cursor);
        this.mOnMediaCheckedListener = listener;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        resizeWidth = (int) ((metrics.widthPixels - 5 * 4 * metrics.density) / 4);
    }

    OnMediaChoooseListener mOnMediaCheckedListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_picker, null);
        if (viewType == 0) {
            return new CameraHolder(view);
        }
        return new ImageHolder(view);
    }

    @Override
    protected int getItemViewType(int position, Cursor cursor) {
        ImageInfo imageInfo = ImageInfo.valueOf(cursor);
        if (imageInfo.id == ImageInfoLoader.ITEM_ID_CAPTURE) {
            return 0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        final ImageInfo imageInfo = ImageInfo.valueOf(cursor);
        if (viewHolder instanceof ImageHolder) {
            final ImageHolder holder = (ImageHolder) viewHolder;
            holder.image.setChecked(mOnMediaCheckedListener.isMediaSelected(imageInfo));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnMediaCheckedListener.goPreview(null, imageInfo);
                }
            });
            holder.image.setOnCheckedChangeListener(new CheckedImageView.OnCheckedChangeListener() {
                @Override
                public void onCheckedChange(CheckedImageView view, boolean checked, boolean isFromTouch) {

                    if (isFromTouch) { // isFromTouch 只有触摸事件才需要处理
                        if (view.isChecked()) {
                            if (!mOnMediaCheckedListener.isMaxCount() && !mOnMediaCheckedListener.isMediaSelected(imageInfo)) {
                                if (imageInfo.isOverSize()) {
                                    view.setChecked(false);
                                    ToastUtil.Long( String.format("图片不能大于%s", imageInfo.getMaxSize()));
                                } else {
                                    mOnMediaCheckedListener.select(imageInfo);
                                }
                            } else {
                                view.setChecked(false); // 不能选择，就回退
                                ToastUtil.Long(String.format("最多只能选取%s张图片哦", mOnMediaCheckedListener.getMaxChooseCount()));
                            }
                        } else {
                            mOnMediaCheckedListener.unSelect(imageInfo);
                        }
                    }
                }
            });
            displayFileImage(holder.image, imageInfo.getImageUrl(), imageInfo.mediaFormat);
        }
    }

    private void displayFileImage(QBImageView simpleDraweeView, String uri, MediaFormat format) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(resizeWidth, resizeWidth))
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
        simpleDraweeView.setTypeImageResouce(MediaFormat.getFormatTagImage(format));
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        CheckedImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_folder_image);
        }
    }

    private class CameraHolder extends RecyclerView.ViewHolder {
        CheckedImageView image;

        public CameraHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_folder_image);
            image.setChecked(false);
            image.setCheckable(false);
            image.setImageResource(R.mipmap.image_picker_camera);
            image.setBackgroundColor(0xff444444);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mOnMediaCheckedListener.isMaxCount()) {
                        mOnMediaCheckedListener.startCamera();
                    } else {
                        ToastUtil.Long(String.format("最多只能选取%s张图片哦", mOnMediaCheckedListener.getMaxChooseCount()));
                    }
                }
            });
        }
    }

    public interface OnMediaChoooseListener {
        public boolean isMediaSelected(ImageInfo imageInfo);
        public int indexOfSelect(ImageInfo imageInfo);
        public boolean isMaxCount();
        public int getMaxChooseCount();
        public void startCamera();
        public void goPreview(ImageFolderInfo folderInfo, ImageInfo imageInfo);
        void select(ImageInfo imageInfo);
        void unSelect(ImageInfo imageInfo);
    }
}
