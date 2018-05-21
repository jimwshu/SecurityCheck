package security.zw.com.securitycheck.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import security.zw.com.securitycheck.utils.toast.ToastUtil;


/**
 * 设置用户头像的公用类，从UserSetting.java里面抽取出来
 * @author powerfj@gmail.com
 */
public class PictureGetHelper {
    //activity之类的
    private Activity mContext;

    public static final int IMAGE_SIZE = 640;

    public PictureGetHelper(Activity mContext, Bundle savedInstanceState) {
        this.mContext = mContext;
        if(savedInstanceState!=null){
            String savedFile = savedInstanceState.getString(KEY_CAPTURE_FILE_PATH);
            if(!TextUtils.isEmpty(savedFile)) {
                mCapturedFile = new File(savedFile);
            }
        }
    }

    public static final String KEY_CAPTURE_FILE_PATH="CAPTURE_FILE_PATH";

    public void onSaveInstanceState(Bundle savedInstanceState){
        String path=getCapturedFilePath();
        if(!TextUtils.isEmpty(path)) {
            savedInstanceState.putString(KEY_CAPTURE_FILE_PATH, path);
        }
    }

    // 用当前时间给取得的图片命名
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public File mCapturedFile;

    public static final int CAMERA_WITH_DATA = 0; // 拍照
    public static final int PHOTO_PICKED_WITH_DATA = 1; // gallery
    public static final int PHOTO_CROPPED_WITH_DATA = 2;// 图片裁剪
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
            + "/DCIM/Camera");// 图片的存储目录


    // 对图片进行剪裁
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", IMAGE_SIZE);
        intent.putExtra("outputY", IMAGE_SIZE);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    /**
     * 剪裁图片
     * @param uri
     */
    public void doCropPhoto(Uri uri) {
        try {
            // 启动gallery去剪辑这个照片
            Intent intent = getCropImageIntent(uri);
            mContext.startActivityForResult(intent, PictureGetHelper.PHOTO_CROPPED_WITH_DATA);
        } catch (Exception e) {
            ToastUtil.Long( "拍照出错了");
        }
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序 剪裁后的图片跳转到新的界面
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("com/soundcloud/android/crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", IMAGE_SIZE);
        intent.putExtra("outputY", IMAGE_SIZE);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        /*
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        */

        return intent;
    }

    // 相册
    public void getPicFromContent() {
        try {
            Intent intent = getPhotoPickIntent();
            mContext.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            ToastUtil.Long("错误");
        }
    }

    // 拍照
    public void getPicFromCapture() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            PHOTO_DIR.mkdir();
            //传入路径
            String photoName = getPhotoFileName();
            mCapturedFile = new File(PHOTO_DIR,photoName); // 用当前时间给取得的图片命名
            PreferenceUtils.instance().putString("photoname", photoName);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCapturedFile));
            try {
                mContext.startActivityForResult(intent, CAMERA_WITH_DATA);
            } catch (ActivityNotFoundException e) {
                ToastUtil.Long("没有摄像头");
            }

        } else {
            ToastUtil.Long("没有sd卡");
        }
    }

    public final String KEY_TEMP_FILE="temp_camera_file_name";

    /**
     *  设置到本地
     */
    private void saveGenerateFileToCache(String fileName){
        PreferenceUtils.instance().putString(KEY_TEMP_FILE,fileName);
    }

    private Bitmap mPickedBitmap = null;


    public Bitmap getPickedBitmap(){
        return mPickedBitmap;
    }

    public void doCropPhotoWithCaptured() {

        doCropPhoto(Uri.fromFile(mCapturedFile));
    }

    /*public String savePickedBitmap(Intent data) {
        Bundle extras = data.getExtras();

        Bitmap pickedBitmap=null;
        if(extras==null || extras.get("data")==null){
            LogUtils.d("pickedBitmapData:"+data.getData());
            //这里有一些相册选择程序有bug，只返回一个URL
            pickedBitmap=decodeUriAsBitmap(data.getData());
            if(pickedBitmap==null){
                ToastUtil.Short("选择的图片为空");
                return null;
            }
        }
        else {
            pickedBitmap = (Bitmap) extras.get("data");
        }

        mPickedBitmap=pickedBitmap;

        String imagePath = mContext.getCacheDir() + Constants.IMG_CACHE_PATH_MEDIUM;
        String path;
        
        User user = AppUtils.getInstance().getUserInfoProvider().getUser();
        if(null != user) {
        	path = FileUtils.saveDrawable(pickedBitmap, user.id + "_" + System.currentTimeMillis(), imagePath,
                    null, true);
//            FileUtils.saveDrawable(pickedBitmap, user.id + "", mContext.getCacheDir()
//                    + Constants.IMG_CACHE_PATH_AVATAR, null, true);
        } else {
        	path = FileUtils.saveDrawable(pickedBitmap, "image" + System.currentTimeMillis(), imagePath,
                    null, true);
//            FileUtils.saveDrawable(pickedBitmap, "avatar", mContext.getCacheDir()
//                    + Constants.IMG_CACHE_PATH_AVATAR, null, true);
        }
        

        return path;

    }*/

    public String getCapturedFilePath() {
        return mCapturedFile!=null?mCapturedFile.getAbsolutePath():null;
    }
    
    public Uri getCapturedUri() {
        if (mCapturedFile == null)
            mCapturedFile = new File(PHOTO_DIR,PreferenceUtils.instance().getString("photoname",""));
    	return Uri.fromFile(mCapturedFile);
    }

}
