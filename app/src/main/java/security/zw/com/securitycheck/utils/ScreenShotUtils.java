package security.zw.com.securitycheck.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.TextureView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import security.zw.com.securitycheck.base.BaseActivity;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * Created by chandler on 16/12/21.
 */

public class ScreenShotUtils {
    //视图截屏
    public static void takeWindowScreeShot(BaseActivity activity) {
        if (activity != null && !activity.isFinishing()) {
            View view = activity.getWindow().getDecorView();
            takeViewScreeShot(activity, view);
        }
    }

    //控件截屏
    public static void takeViewScreeShot(BaseActivity activity, View view) {
        if (activity != null && !activity.isFinishing()) {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            view.setDrawingCacheEnabled(false);
            saveBitmap(activity, bitmap);
        }
    }

    //带视频页面的截屏
    public static void takeLiveRoomScreeShot(final BaseActivity activity, TextureView player) {
        if (activity != null && !activity.isFinishing()) {
            View layout = activity.getWindow().getDecorView();
            layout.setDrawingCacheEnabled(true);
            player.setDrawingCacheEnabled(true);
            final Bitmap bitmap = getLiveRoomScreenShot(layout, player);
            layout.setDrawingCacheEnabled(false);
            player.setDrawingCacheEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveBitmap(activity, bitmap);
                }
            }).start();
        }
    }

    public static void saveBitmap(BaseActivity activity, Bitmap bitmap) {
        if (activity != null && !activity.isFinishing() && bitmap != null) {
            Date now = new Date();
            String formatDate = DateFormat.format("yyyyMMddhhmmss", now).toString();


            try {
                String filename = formatDate + ".png";
                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Remix/Image/" + filename;
                File imageFile = new File(mPath);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                activity.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.Long("截屏成功");
                    }
                });
                FileUtils.notifyMediaScannerScanFile(activity, imageFile);
            } catch (Throwable e) {
                // Several error may come out with file handling or OOM
                activity.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.Long("截屏失败");
                    }
                });
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getLiveRoomScreenShot(View layout, TextureView player) {
        Bitmap screenshot = null;
        try {
            layout.setAlpha(0);
            Bitmap layoutBmp = layout.getDrawingCache();
            Bitmap playerBmp = player.getBitmap();
            if (layoutBmp != null && playerBmp != null) {
                screenshot = Bitmap.createBitmap(layoutBmp.getWidth(), layoutBmp.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                canvas.drawBitmap(playerBmp, (layout.getWidth() - playerBmp.getWidth()) / 2, (layout.getHeight() - playerBmp.getHeight()) / 2, new Paint());
                canvas.drawBitmap(layoutBmp, 0, 0, new Paint());
                canvas.save();
                canvas.restore();
                layout.setAlpha(1);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        return screenshot;
    }

}
