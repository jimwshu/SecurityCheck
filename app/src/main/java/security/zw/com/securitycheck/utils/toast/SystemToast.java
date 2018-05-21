package security.zw.com.securitycheck.utils.toast;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

/**
 * Created by yrsx on 16/6/24.
 */
public class SystemToast extends AbstractToast {
    protected Toast mToast;

    protected SystemToast() {

    }

    public SystemToast(Context context) {
        mToast = new Toast(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
    public static SystemToast makeText(Context context, CharSequence text, int duration) {
        SystemToast systemToast = new SystemToast();
        systemToast.mToast = Toast.makeText(context, text, duration);
        return systemToast;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static SystemToast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    @Override
    public void show() {
        mToast.show();
    }

    @Override
    public void cancel() {
        mToast.cancel();
    }

    @Override
    public View getView() {
        return mToast.getView();
    }

    @Override
    public void setView(View view) {
        mToast.setView(view);
    }

    @Override
    public int getDuration() {
        return mToast.getDuration();
    }

    @Override
    public void setDuration(int duration) {
        mToast.setDuration(duration);
    }

    @Override
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
    }

    @Override
    public float getHorizontalMargin() {
        return mToast.getHorizontalMargin();
    }

    @Override
    public float getVerticalMargin() {
        return mToast.getVerticalMargin();
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    @Override
    public int getGravity() {
        return mToast.getGravity();
    }

    @Override
    public int getXOffset() {
        return mToast.getXOffset();
    }

    @Override
    public int getYOffset() {
        return mToast.getYOffset();
    }

    @Override
    public void setText(@StringRes int resId) {
        mToast.setText(resId);
    }

    @Override
    public void setText(CharSequence s) {
        mToast.setText(s);
    }
}
