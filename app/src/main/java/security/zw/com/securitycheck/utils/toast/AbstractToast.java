package security.zw.com.securitycheck.utils.toast;

import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by yrsx on 16/6/24.
 */
public abstract class AbstractToast {
    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     *
     * @see #setDuration
     */
    public final static int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     *
     * @see #setDuration
     */
    public final static int LENGTH_LONG = 1;

    /**
     * Show the view for the specified duration.
     */
    public abstract void show();

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public abstract void cancel();

    /**
     * Return the view.
     *
     * @see #setView
     */
    public abstract View getView();

    /**
     * Set the view to show.
     *
     * @see #getView
     */
    public abstract void setView(View view);

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    public abstract int getDuration();


    /**
     * Set how long to show the view for.
     *
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     */
    public abstract void setDuration(int duration);

    /**
     * Set the margins of the view.
     *
     * @param horizontalMargin The horizontal margin, in percentage of the
     *                         container width, between the container's edges and the
     *                         notification
     * @param verticalMargin   The vertical margin, in percentage of the
     *                         container height, between the container's edges and the
     *                         notification
     */
    public abstract void setMargin(float horizontalMargin, float verticalMargin);

    /**
     * Return the horizontal margin.
     */
    public abstract float getHorizontalMargin();

    /**
     * Return the vertical margin.
     */
    public abstract float getVerticalMargin();

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @see android.view.Gravity
     * @see #getGravity
     */
    public abstract void setGravity(int gravity, int xOffset, int yOffset);

    /**
     * Get the location at which the notification should appear on the screen.
     *
     * @see android.view.Gravity
     * @see #getGravity
     */
    public abstract int getGravity();

    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    public abstract int getXOffset();

    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    public abstract int getYOffset();

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param resId The new text for the Toast.
     */
    public abstract void setText(@StringRes int resId);

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param s The new text for the Toast.
     */
    public abstract void setText(CharSequence s);
}