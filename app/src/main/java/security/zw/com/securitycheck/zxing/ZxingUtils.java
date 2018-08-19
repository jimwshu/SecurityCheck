package security.zw.com.securitycheck.zxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import security.zw.com.securitycheck.base.BaseActivity;
import security.zw.com.securitycheck.utils.FileUtils;
import security.zw.com.securitycheck.utils.LogUtils;
import security.zw.com.securitycheck.utils.toast.ToastUtil;

/**
 * Created by D'Russel on 2017/7/31.
 */

public class ZxingUtils {


   public static Bitmap createBitmap(String str){
       LogUtils.e(str);
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){ // ?
            return null;
        }
        return bitmap;
    }


    public static void saveBitmap(Activity activity, Bitmap bitmap) {
        if (activity != null && !activity.isFinishing() && bitmap != null) {
            Date now = new Date();
            String formatDate = DateFormat.format("yyyyMMddhhmmss", now).toString();


            try {

                final String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Security";
                File dir = new File(mPath);
                if (!dir.exists() || !dir.isDirectory()) {
                    dir.mkdirs();
                }
                String newPath = formatDate + ".jpg";
                File imageFile = new File(dir, newPath);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                ToastUtil.Long("二维码图片保存成功");

                FileUtils.notifyMediaScannerScanFile(activity, imageFile);
            } catch (Throwable e) {
                ToastUtil.Long("二维码图片保存失败");

                e.printStackTrace();
            }
        }
    }

}