package security.zw.com.securitycheck.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import security.zw.com.securitycheck.SecurityApplication;

public class DeviceUtils {
	
	private static String CACHE_ANDROID_ID = null;

	private static String sChannel;
	private static String sBuildTime;

	public static String getAndroidId() {
    	if(CACHE_ANDROID_ID != null){
    		return CACHE_ANDROID_ID;
    	}
        String deviceID = null;
        // 获取设备码
        TelephonyManager tManager = (TelephonyManager) SecurityApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);

        //deviceId 有些山寨机器可能会是一样的，正规厂商的id应该不一样
        //Android_ID每次系统重新刷机一次，会随机产生一个
        deviceID = "\"DEVICEID\":\"" + tManager.getDeviceId() + "\"-" + "\"ANDROID_ID\":\""
                + Secure.getString(SecurityApplication.getInstance().mContext.getContentResolver(), Secure.ANDROID_ID) + "\"";
        
        CACHE_ANDROID_ID = "IMEI_" + Md5Utils.encryptMD5(deviceID);

        return CACHE_ANDROID_ID;
    }
	
	public static String getAppVersion() {
		String version = "1.0";
		try {
			PackageManager packageManager = SecurityApplication.getInstance().mContext.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(SecurityApplication.getInstance().getPackageName(), 0); // getPackageName()是你当前类的包名，0代表是获取版本信息
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	public static String getSystemVersion() {
		return Build.VERSION.RELEASE;
	}

	public static int getSystemSDKInt() {
		return Build.VERSION.SDK_INT;
	}

	public static String getDeviceModel() {
		return Build.MODEL;
	}
	

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	 public static int getAPPVersionCode() {
		 try {
	          PackageManager manager = SecurityApplication.getInstance().getPackageManager();
	          PackageInfo info = manager.getPackageInfo(SecurityApplication.getInstance().getPackageName(), 0);
	          return info.versionCode;
	     } catch (Exception e) {
	         e.printStackTrace();
	         return -1;
	     }
	 }
    
    /**
     * 外部储存卡是否可用
     * @return
     */
    public static boolean isExternalStorageAvailable() {
    	try{
    		return Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState());
    	} catch (Exception e){
    	}
    	return false;
    }

    /**
     * 获取可用的外部存储空间的大小 单位byte
     * @return
     */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getAvailableExternalMemorySize(){
		if(isExternalStorageAvailable()){
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize, availableBlocks;
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
					blockSize = stat.getBlockSize();
					availableBlocks = stat.getAvailableBlocks();
				} else {
					blockSize = stat.getBlockSizeLong();
					availableBlocks = stat.getAvailableBlocksLong();
				}
				return availableBlocks * blockSize;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		else{
			return -1;
		}
	}

    /**
     * 获取SD卡的路径
     * 
     * @return “”表示SD卡不可读写
     */
    public static String getSDPath() {

        String sdPath = "";
        File sdDir = null;
        boolean sdCardExist = isExternalStorageAvailable();
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            sdPath = sdDir.toString();
        }
        return sdPath;
    }
    
    public static boolean isMeizuMobile() {
    	return "meizu".equalsIgnoreCase(Build.BRAND);
    }

	public static String getChannel() {
		Context context = SecurityApplication.getInstance().mContext;
		return getChannel(context);
	}

	public static String getChannel(Context context) {
		if(!TextUtils.isEmpty(sChannel)){
			return sChannel;
		}

		sChannel = "security";


		return sChannel;
	}


}
