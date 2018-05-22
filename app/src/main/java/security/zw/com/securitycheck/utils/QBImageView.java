package security.zw.com.securitycheck.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import security.zw.com.securitycheck.R;


/**
 * 列表带gif，长图等标签的图片
 *
 * Created by s1rius on 20/11/2017.
 */
public class QBImageView extends SimpleDraweeView {

    public static final int IMAGE_UNROLL_BOTTOM = 1;
    public static final int IMAGE_UNROLL_TOP_BOTTOM = 2;

    private Drawable mTypeDrawable;
    private Drawable mBottomDrawable;
    private Drawable mTopDrawable;
    private int mUnrollType;
    private int mTypeLeftMargin;
    private int mTypeTopMargin;
    private int mTypeRightMargin;
    private int mTypeBottomMargin;
    private int mUnrollTopMargin;
    private int mUnrollBottomMargin;
    private int mTypeGravity = Gravity.LEFT | Gravity.TOP;

    public QBImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context, null, 0);
    }

    public QBImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QBImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QBImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mTypeLeftMargin = dp2px(context, 4);
        mTypeTopMargin = dp2px(context, 4);
        mUnrollTopMargin = dp2px(context, 2);
        mUnrollBottomMargin = dp2px(context, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTypeDrawable != null) {
            switch (mTypeGravity) {
                case (Gravity.LEFT | Gravity.TOP):
                    mTypeDrawable.setBounds(mTypeLeftMargin,
                            mTypeTopMargin,
                            mTypeLeftMargin + mTypeDrawable.getIntrinsicWidth(),
                            mTypeTopMargin + mTypeDrawable.getIntrinsicHeight());
                    break;
                case (Gravity.RIGHT | Gravity.BOTTOM):
                    int width = getMeasuredWidth();
                    int height = getMeasuredHeight();
                    int left = width - mTypeDrawable.getIntrinsicWidth() - mTypeRightMargin;
                    int top = height - mTypeDrawable.getIntrinsicHeight() - mTypeBottomMargin;
                    mTypeDrawable.setBounds(
                            left,
                            top,
                            mTypeDrawable.getIntrinsicWidth(),
                            mTypeDrawable.getIntrinsicHeight());
                    break;
            }
            mTypeDrawable.draw(canvas);
        }

        if (mBottomDrawable != null && mUnrollType == IMAGE_UNROLL_BOTTOM) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int left = width / 2 - mBottomDrawable.getIntrinsicWidth();
            int top = height - mBottomDrawable.getIntrinsicHeight() - mUnrollBottomMargin;
            mBottomDrawable.setBounds(
                    left,
                    top,
                    left + mBottomDrawable.getIntrinsicWidth(),
                    top + mBottomDrawable.getIntrinsicHeight());
            mBottomDrawable.draw(canvas);
        }
        if (mTopDrawable != null && mBottomDrawable != null && mUnrollType == IMAGE_UNROLL_TOP_BOTTOM) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int left = width / 2 - mBottomDrawable.getIntrinsicWidth();
            int top = height - mBottomDrawable.getIntrinsicHeight() - mUnrollBottomMargin;
            mBottomDrawable.setBounds(
                    left,
                    top,
                    left + mBottomDrawable.getIntrinsicWidth(),
                    top + mBottomDrawable.getIntrinsicHeight());
            mBottomDrawable.draw(canvas);
            mTopDrawable.setBounds(left, mUnrollTopMargin, left + mTopDrawable.getIntrinsicWidth(), mUnrollTopMargin + mTopDrawable.getIntrinsicHeight());
            mTopDrawable.draw(canvas);
        }

    }

    public void setTypeImageResouce(int drawableResouce) {
        if (drawableResouce == 0) {
            mTypeDrawable = null;
        } else {
            mTypeDrawable = getResources().getDrawable(drawableResouce);
        }

        invalidate();
    }

    public void showImageUnrollTag(int imageUnrollTag) {
        mUnrollType = imageUnrollTag;
        if (mUnrollType == IMAGE_UNROLL_BOTTOM) {
            mBottomDrawable = getResources().getDrawable(R.mipmap.ic_unrolled_arrow_down);
        } else if (mUnrollType == IMAGE_UNROLL_TOP_BOTTOM) {
            mBottomDrawable = getResources().getDrawable(R.mipmap.ic_unrolled_arrow_down);
            mTopDrawable = getResources().getDrawable(R.mipmap.ic_unrolled_arrow_up);
        } else {
            mTopDrawable = null;
            mBottomDrawable = null;
        }
        invalidate();
    }
}
