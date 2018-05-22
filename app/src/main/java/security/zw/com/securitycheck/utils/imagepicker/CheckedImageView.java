package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;
import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.QBImageView;

/**
 * 带勾选功能的ImageView
 */
public class CheckedImageView extends QBImageView implements Checkable {
    private boolean isChecked = false;
    private boolean isCheckable = true;
    private Drawable checkedDrawable;
    private Drawable uncheckedDrawable;
    private float space;

    private boolean isTouchCheck = false;

    private OnCheckedChangeListener listener;

    public CheckedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CheckedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckedImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        checkedDrawable = getResources().getDrawable(R.mipmap.image_picker_checked);
        uncheckedDrawable = getResources().getDrawable(R.mipmap.image_picker_unchecked);
        space = getResources().getDisplayMetrics().density * 6;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isCheckable) {
            if (isChecked) {
                if (checkedDrawable != null) {
                    int width = checkedDrawable.getIntrinsicWidth();
                    int height = checkedDrawable.getIntrinsicHeight();
                    int left = (int) (getWidth() - getPaddingRight() - space - width);
                    int top = (int) (getPaddingTop() + space);
                    checkedDrawable.setBounds(left, top, left + width, top + height);
                    checkedDrawable.draw(canvas);
                }
            } else {
                if (uncheckedDrawable != null) {
                    int width = uncheckedDrawable.getIntrinsicWidth();
                    int height = uncheckedDrawable.getIntrinsicHeight();
                    int left = (int) (getWidth() - getPaddingRight() - space - width);
                    int top = (int) (getPaddingTop() + space);
                    uncheckedDrawable.setBounds(left, top, left + width, top + height);
                    uncheckedDrawable.draw(canvas);
                }
            }
        }
    }

    private int getCheckWidth() {
        int width = 0;
        if (checkedDrawable != null) {
            width = checkedDrawable.getIntrinsicWidth();
        }
        if (uncheckedDrawable != null) {
            width = Math.max(width, uncheckedDrawable.getIntrinsicWidth());
        }
        return width;
    }

    private int getCheckHeight() {
        int height = 0;
        if (checkedDrawable != null) {
            height = checkedDrawable.getIntrinsicHeight();
        }
        if (uncheckedDrawable != null) {
            height = Math.max(height, uncheckedDrawable.getIntrinsicHeight());
        }
        return height;
    }

    public void setCheckable(boolean enable) {
        isCheckable = enable;
        postInvalidate();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (!isCheckable) {
            return;
        }
        if (isChecked != checked) {
            postInvalidate();
        }
        isChecked = checked;
        if (listener != null) {
            listener.onCheckedChange(this, isChecked, false);
        }
    }

    @Override
    public void toggle() {
        toggle(false);
    }

    private void toggle(boolean isFromTouch) {
        if (!isCheckable) {
            return;
        }
        isChecked = !isChecked;
        if (listener != null) {
            listener.onCheckedChange(this, isChecked, isFromTouch);
        }
        postInvalidate();
    }

    public void setCheckedDrawable(Drawable drawable) {
        checkedDrawable = drawable;
        postInvalidate();
    }

    public void setUnCheckedDrawable(Drawable drawable) {
        checkedDrawable = drawable;
        postInvalidate();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float ex = event.getX();
        final float ey = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchCheck = ex >= getWidth() - getPaddingRight() - space - getCheckWidth() &&
                        ey <= getPaddingTop() + space + getCheckHeight();
                break;
            case MotionEvent.ACTION_UP:
                if (isTouchCheck) {
                    if (ex >= getWidth() - getPaddingRight() - space - getCheckWidth() &&
                            ey <= getPaddingTop() + space + getCheckHeight()) {
                        if (listener != null) {
                            toggle(true);
                        }
                    }
                }
                break;
        }
        return isTouchCheck || super.onTouchEvent(event);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(CheckedImageView view, boolean checked, boolean isFromTouch);
    }

}
