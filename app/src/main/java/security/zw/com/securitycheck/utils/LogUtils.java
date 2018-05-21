package security.zw.com.securitycheck.utils;

import android.util.Log;
/*
 * lk
 */

public class LogUtils {

	public final static String DEFAULT_TAG = "security";
	
	public static boolean LOGGABLE = true;
	
	public static void d(String tag, String str) {
		if(LOGGABLE) {
			Log.d(tag, str + "");
		}
	}
	
	public static void d(String str) {
		if(LOGGABLE) {
			Log.d(DEFAULT_TAG, str + "");
		}
	}
	
	public static void w(String tag, String str) {
		if(LOGGABLE) {
			Log.w(tag, str + "");
		}
	}
	
	public static void w(String str) {
		if(LOGGABLE) {
			Log.w(DEFAULT_TAG, str + "");
		}
	}
	
	public static void e(String tag, String str) {
		if(LOGGABLE) {
			Log.e(tag, str + "");
		}
	}
	
	public static void e(String tag, String msg, Throwable e) {
        if(LOGGABLE) {
            Log.e(tag, msg + "", e);
        }
    }
	
	public static void e(String str) {
		if(LOGGABLE) {
			Log.e(DEFAULT_TAG, str + "");
		}
	}
	
	public static void i(String tag, String str) {
		if(LOGGABLE) {
			Log.i(tag, str + "");
		}
	}
	
	public static void i(String str) {
		if(LOGGABLE) {
			Log.i(DEFAULT_TAG, str + "");
		}
	}
	
	public static void v(String tag, String str) {
		if(LOGGABLE) {
			Log.v(tag, str + "");
		}
	}
	
	public static void v(String str){
		if(LOGGABLE) {
			Log.v(DEFAULT_TAG, str + "");
		}
	}
}


