package security.zw.com.securitycheck.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


public class ImageUtils {

    public static byte[] bmpToByteArray(final Bitmap bitmap, final boolean needRecycle) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 80, 80), null);
            if (needRecycle)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 85, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                e.printStackTrace();
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }


    public static byte[] bmpToByteArray(final Bitmap bitmap, int maxLength, final boolean needRecycle) {
        double maxWidth = Math.sqrt(maxLength / 2);

        Bitmap localBitmap = Bitmap.createBitmap((int)maxWidth, (int)maxWidth, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, (int)maxWidth, (int)maxWidth), null);
            if (needRecycle)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 85, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                e.printStackTrace();
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }

    /**
     * 图片旋转
     *
     * @param src    原图
     * @param rotate 旋转角度
     */
    public static Bitmap picRotate(Bitmap src, int rotate) {

        // 获取图片的原始的大小
        int width = src.getWidth();
        int height = src.getHeight();

        int newWidth = width;
        int newHeight = height;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 创建操作图片的用的Matrix对象
        android.graphics.Matrix matrix = new android.graphics.Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        matrix.postRotate(rotate);

        // 创建一个新的图片
        android.graphics.Bitmap resizeBitmap = android.graphics.Bitmap.createBitmap(src, 0, 0,
                width, height, matrix, true);

        return resizeBitmap;

    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * 图片圆角
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap watermark(Bitmap src, Bitmap watermark, int targetW, int targetH) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap newb = null;
        // float scale = 1;
        float sx = 1;
        float sy = 1;
        if (w > h && w >= targetW) {
            sy = sx = ((float) targetW) / w;
        } else if (w < h) {
            if (w >= targetW) {
                if (h >= targetH) {
                    if (w / h >= targetW / targetH) {
                        sy = sx = ((float) targetW) / w;
                    } else {
                        sx = sy = ((float) targetH) / h;
                    }
                } else {
                    sy = sx = ((float) targetW) / w;
                }
            } else {
                if (h >= targetH) {
                    sx = sy = ((float) targetH) / h;
                }
            }
        } else {
            if (w >= targetW) {
                sy = sx = ((float) targetW) / w;
            }
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        // create the new Bitmap object
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        src = new BitmapDrawable(resizedBitmap).getBitmap();

        if (src.getWidth() > 250) {
            int wh = watermark.getHeight();
            // create the new blank bitmap
            newb = Bitmap.createBitmap((int) (sx * w), (int) (sy * h), Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
            Canvas cv = new Canvas(newb);
            // draw src into
            cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
            // draw watermark into
            cv.drawBitmap(watermark, 10, newb.getHeight() - wh - 10, null);// 在src的右下角画入水印
            // save all clip
            cv.save(Canvas.ALL_SAVE_FLAG);// 保存
            // store
            cv.restore();// 存储
        } else {
            newb = src;
        }
        return newb;
    }

    public static Bitmap createBitmapForWatermark(Bitmap src) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // float scale = 1;
        float sx = 1;
        float sy = 1;
        if (w > h && w >= 500) {
            sy = sx = ((float) 500) / w;
        } else if (w < h) {
            if (w >= 500) {
                if (h >= 800) {
                    if (w / h >= 5 / 8) {
                        sy = sx = ((float) 500) / w;
                    } else {
                        sx = sy = ((float) 800) / h;
                    }
                } else {
                    sy = sx = ((float) 500) / w;
                }
            } else {
                if (h >= 800) {
                    sx = sy = ((float) 800) / h;
                }
            }
        } else {
            if (w >= 500) {
                sy = sx = ((float) 500) / w;
            }
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        // create the new Bitmap object
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        src = new BitmapDrawable(resizedBitmap).getBitmap();

        // int wh = watermark.getHeight();
        // // create the new blank bitmap
        // Bitmap newb = Bitmap.createBitmap((int) (sx * w), (int) (sy * h),
        // Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
        // Canvas cv = new Canvas(newb);
        // // draw src into
        // cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // // draw watermark into
        // cv.drawBitmap(watermark, 10, newb.getHeight() - wh - 10, null);//
        // 在src的右下角画入水印
        // // save all clip
        // cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // // store
        // cv.restore();// 存储
        return src;
    }

    /**
     * 压缩图片
     */
    public static void compressBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 500);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
        bitmap = BitmapFactory.decodeFile(path, options);

        // 保存入sdCard
        File send = new File(DeviceUtils.getSDPath() + "/qsbk/send/send.png");
        try {
            FileOutputStream out = new FileOutputStream(send);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * if source bitmap {@link Bitmap#getWidth()} is greater than maxWidth or {@link
     * Bitmap#getHeight()} is greater than maxHeight. we will scale it to fit maxWidth or maxHeight
     * according to Math.min(scaleX, scaleY)} it is equal ratio. <ol>For example: <li>Source
     * bitmap's is 400x400, maxWidth is 200, maxHeight is 100, then returning bitmap's width is 100,
     * height is 100;</li> <li>Source bitmap's is 800x400. maxWidth is 400, maxHeight is 600, then
     * returning bitmap's width is 400, height is 200;</li> <li>Source bitmap's is 400x400, maxWidth
     * is 480, maxHeight is 800, then returning bitmao's width is 400, height is 400. Exactly
     * returning the source bitmap.</li> </ol>
     *
     * @param recyleSrc recycle source only when decode successfully and return bitmap is not the
     *                  reference of source
     */
    public static Bitmap scaleBitmapIfNecessary(Bitmap src, int maxWidth, int maxHeight, boolean recyleSrc) {
        Bitmap retBitmap = src;
        final int oriWidth = src.getWidth(), oriHeight = src.getHeight();
        if (oriWidth > maxWidth || oriHeight > maxHeight) {
            float sw = (float) maxWidth / oriWidth;
            float sh = (float) maxHeight / oriHeight;
            float scale = Math.min(sw, sh);
            Matrix matrix = new Matrix();
//			log(" scale is "+scale);

            //等比缩放
            matrix.postScale(scale, scale);
            try {
                retBitmap = Bitmap.createBitmap(src, 0, 0, oriWidth, oriHeight, matrix, true);

            } catch (OutOfMemoryError e) {
                // TODO: handle exception
            } finally {
                if (recyleSrc && retBitmap != null && src != retBitmap && !src.isRecycled()) {
                    src.recycle();
                    src = null;
                }
            }
        }
        return retBitmap;
    }

    public static Bitmap scaleBitmapIfNecessaryUseScale(Bitmap src, int maxWidth, int maxHeight, boolean recyleSrc) {
        Bitmap retBitmap = src;
        final int oriWidth = src.getWidth(), oriHeight = src.getHeight();
        if (oriWidth > maxWidth || oriHeight > maxHeight) {
            float sw = (float) maxWidth / oriWidth;
            float sh = (float) maxHeight / oriHeight;
            float scale = Math.min(sw, sh);

            int destWidth = (int) (oriWidth * scale);
            int destHeight = (int) (oriHeight * scale);

            try {
                retBitmap = Bitmap.createBitmap(destWidth, destHeight, Config.ARGB_8888);
                Canvas canvas = new Canvas(retBitmap);
                canvas.drawBitmap(src, new Rect(0, 0, oriWidth, oriHeight), new Rect(0, 0, destWidth, destHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
            } catch (OutOfMemoryError e) {

            } finally {
                if (recyleSrc && retBitmap != null && src != retBitmap && !src.isRecycled()) {
                    src.recycle();
                    src = null;
                }
            }

            /*
            try{
                retBitmap = Bitmap.createScaledBitmap(src, (int) (oriWidth * scale), (int) (oriHeight * scale), true);
            }catch (OutOfMemoryError e) {
                // TODO: handle exception
            }finally{
                if(recyleSrc && retBitmap!=null && src != retBitmap && !src.isRecycled()){
                    src.recycle();
                    src = null;
                }
            }
            */
        }
        return retBitmap;
    }

    /**
     *
     * @param options
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static int calculateInSampleSize(Options options, int maxWidth, int maxHeight) {
        final int outHeight = options.outHeight;
        final int outWidth = options.outWidth;
        int inSampleSize = 1;
//    	log(" calculateInSampleSize : "+
//        			String.format("(oriWidth:%1$d, oriHeight:%2$d, maxWidth:%3$d, maxHeight:%4$d)",
//        					outWidth, outHeight, maxWidth, maxHeight));
        if (outHeight > maxHeight || outWidth > maxWidth) {
            float sx = (float) outHeight / maxHeight;
            float sy = (float) outWidth / maxWidth;
            // 设置长图最大的横向压缩倍数
            inSampleSize = (int) Math.floor(Math.max(sx, sy));

            if (inSampleSize < 1) {
                inSampleSize = 1;
            }
        }
        return inSampleSize;
    }

    public static Options decodeBitmapOpt(Context ctx, String picPath) {
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = null;
        try {
            if (picPath.startsWith("content") || picPath.startsWith("file")) {
                ContentResolver resolver = ctx.getContentResolver();
                is = (InputStream) resolver.openInputStream(Uri.parse(picPath));
            } else {
                is = new FileInputStream(picPath);
            }
            BitmapFactory.decodeStream(is, null, opt);
            return opt;
        } catch (Exception e) {
            // TODO: handle exception
            opt = null;
            return opt;
        }
    }

    /**
     * Try to decode bitmap relative to maxWidth and maxHeight. If original {@link
     * Bitmap#getWidth()} is greater than maxWidth or {@link Bitmap#getHeight()} is greater than
     * maxHeight, then we scale them to fit maxHeight and maxWidth use {@link Options#inSampleSize}
     * in order to lower memory cost ( can avoid {@link OutOfMemoryError} in same case etc).
     *
     * @param cfg can be null, by {@link Config#RGB_565} defualt
     * @return null if {@link BitmapFactory#decodeStream(InputStream, android.graphics.Rect,
     * Options)} failed.
     */
    public static Bitmap decodeBitmap(Context ctx, String picPath,
                                      int maxWidth, int maxHeight, Config cfg) {
        Bitmap retBitmap = null;
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        if (cfg != null) {
            opt.inPreferredConfig = cfg;
        } else {
            opt.inPreferredConfig = Config.RGB_565;
        }
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = null;
        try {
            if (picPath.startsWith("content") || picPath.startsWith("file")) {
                ContentResolver resolver = ctx.getContentResolver();
                is = (InputStream) resolver.openInputStream(Uri.parse(picPath));
            } else {
                is = new FileInputStream(picPath);
            }

        } catch (Exception e) {
            // TODO: handle exception
            opt = null;
            return retBitmap;
        }

        BitmapFactory.decodeStream(is, null, opt);

        opt.inSampleSize = calculateInSampleSize(opt, maxWidth, maxHeight);
//		log(String.format("test inSampleSize : %1$d, picPath : %2$s, parse picPath: %3$s, fis : %4$b",
//				opt.inSampleSize, picPath, Uri.parse(picPath), is==null));

        //real start to decode
        opt.inJustDecodeBounds = false;
        opt.inDither = false;
        opt.inScaled = false;

        //需要重新初始化is...要不然会出现 SkImageDecoder::Factory returned null
        try {
            if (picPath.startsWith("content") || picPath.startsWith("file")) {
                ContentResolver resolver = ctx.getContentResolver();
                is = (InputStream) resolver
                        .openInputStream(Uri.parse(picPath));
            } else {
                is = new FileInputStream(picPath);
            }

        } catch (Exception e1) {
            // TODO: handle exception
            opt = null;
            return retBitmap;
        }
        try {
            retBitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
            return retBitmap;
        }
        return retBitmap;
    }

    /**
     * Create a fitted rotate bitmap.
     *
     * @param rotate in order to make maxWidth and maxHeight effective, it should be a multiple of
     *               90.
     */
    public static Bitmap fitRotate(Bitmap src, int rotate, int maxWidth, int maxHeight, boolean recycleSrc) {

        if (src == null || src.isRecycled()) {
            throw new NullPointerException("the src bitmap is null or is recycled");
        }

        if (rotate == 0 || rotate % 360 == 0) {
            return src;
        }

        final int width = src.getWidth();
        final int height = src.getHeight();
        int newWidth = width;
        int newHeight = height;

        float scaleWidth = ((float) maxHeight) / newWidth;
        float scaleHeight = ((float) maxWidth) / newHeight;
        float scale = Math.min(scaleWidth, scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postRotate(rotate);
        Bitmap resizeBitmap = null;
        try {
            resizeBitmap = Bitmap.createBitmap(src, 0, 0,
                    width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
        } finally {
            if (recycleSrc && resizeBitmap != null && resizeBitmap != src) {
                src.recycle();
                src = null;
            }
        }
//		log(String.format("scale width:%1$f, scale height:%2$f, resize bitmap width:%3$d, height:%4$d",
//						scaleWidth, scaleHeight, resizeBitmap.getWidth(),resizeBitmap.getHeight()));
        return resizeBitmap;
    }

    static void log(String msg) {
        Log.i("qsbk", "ImageUtils--" + msg);
    }
}
