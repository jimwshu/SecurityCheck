package security.zw.com.securitycheck.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import security.zw.com.securitycheck.SecurityApplication;

public class NetworkUtils {

	public static final int NETWORK_TYPE_UNKNOWN = -1;

	public static final int NETWORK_TYPE_WIFI = 0;

	public static final int NETWORK_TYPE_3G = 1;

	public static final int NETWORK_TYPE_2G = 2;

	public static final int NETWORK_TYPE_4G = 3;

	private ConnectivityManager mConnManager;
	
	private TelephonyManager mPhonyManager;

	private static NetworkUtils sInstance;
	private Context mContext;

	private NetworkUtils() {
		mContext = SecurityApplication.getInstance().getAppContext();
		mConnManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		mPhonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static NetworkUtils getInstance() {
		if (sInstance == null) {
			synchronized (NetworkUtils.class) {
				sInstance = new NetworkUtils();
			}
		}
		return sInstance;
	}

	/**
	 * @return
	 */
	public int getNetworkType() {
		if (isWifiAvailable()) {
			return NETWORK_TYPE_WIFI;
		}

		if (isMobileNetAvailable()) {
			if (isConnection3G()) {
				return NETWORK_TYPE_3G;
			}

			if (isConnection2G()) {
				return NETWORK_TYPE_2G;
			}

			if (isConnection4G()) {
				return NETWORK_TYPE_4G;
			}
		}

		return NETWORK_TYPE_UNKNOWN;
	}

	public String getNetworkDetailType() {
		if (isWifiAvailable()) {
			return "WiFi";
		}else if (isMobileNetAvailable()) {
			return mPhonyManager.getNetworkType() + "";
		}

		return "unknow";
	}

	/**
	 * @return
	 */
	public boolean isConnection4G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_LTE: // 4G
				result = true;
				break;
			default:
				break;
		}
		return result;
	}

	/**
	 * @return
	 */
	public boolean isConnection3G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
			case TelephonyManager.NETWORK_TYPE_EVDO_0: // 3G
			case TelephonyManager.NETWORK_TYPE_EVDO_A: // 3G
			case TelephonyManager.NETWORK_TYPE_HSDPA: // 3G
			case TelephonyManager.NETWORK_TYPE_HSUPA: // 3G
			case TelephonyManager.NETWORK_TYPE_HSPA: // 3G
				//case TelephonyManager.NETWORK_TYPE_EVDO_B: // 3G
				result = true;
				break;
			default:
				break;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConnection2G() {
		boolean result = false;
		switch (mPhonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			case TelephonyManager.NETWORK_TYPE_GPRS: // 2.5G
			case TelephonyManager.NETWORK_TYPE_EDGE: // 2.75G
			case TelephonyManager.NETWORK_TYPE_CDMA: // 2G
			case TelephonyManager.NETWORK_TYPE_1xRTT: // 2G
			case TelephonyManager.NETWORK_TYPE_IDEN: // 2G
				result = true;
				break;
			default:
				break;
		}
		return result;
	}

	public boolean isNetworkAvailable() {
		NetworkInfo info = mConnManager.getActiveNetworkInfo();
		return isLinkable(info);
	}

	public boolean isMobileNetAvailable() {
		NetworkInfo info = mConnManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return isLinkable(info);
	}
	
    public String getLocalMacAddressFromWifiInfo(){
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if (TextUtils.isEmpty(wifiMac)) {
            return "00:00:00:00:00:00";
        } else {
            return wifiMac;
        }
    }

	public boolean isWifiAvailable() {
		NetworkInfo info = mConnManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return isLinkable(info);
	}

	private boolean isLinkable(NetworkInfo info) {
		return (info != null) && info.isConnected() && info.isAvailable();
	}
	
}
