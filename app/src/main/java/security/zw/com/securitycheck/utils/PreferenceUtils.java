package security.zw.com.securitycheck.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.util.Map;

import security.zw.com.securitycheck.SecurityApplication;

/**
 * @author lk
 */

public class PreferenceUtils {
    private static final String PREFERENCES = "ye";
    private static PreferenceUtils mInstance;
    private SharedPreferences mPreferences;
    private Editor mEditor;
    
    public static PreferenceUtils instance() {
        if (null == mInstance) {
        	synchronized(PreferenceUtils.class) {
        		if(null == mInstance) {
        			mInstance = new PreferenceUtils(SecurityApplication.getInstance().getAppContext());
        		}
        	}
        }
        return mInstance;
    }

    private PreferenceUtils(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES, Activity.MODE_PRIVATE | Activity.MODE_MULTI_PROCESS);
        mEditor = mPreferences.edit();
    }
    
    public void registerOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(
            OnSharedPreferenceChangeListener listener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public Map<String, ?> getAll() {
        return mPreferences.getAll();
    }

    public boolean contains(String key){
        return mPreferences.contains(key);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }
    
    public boolean putBoolean(String key, boolean b) {
        mEditor.putBoolean(key, b);
        return mEditor.commit();
    }

    public boolean putInt(String key, int i) {
        mEditor.putInt(key, i);
        return mEditor.commit();
    }

    public boolean putFloat(String key, float f) {
        mEditor.putFloat(key, f);
        return mEditor.commit();
    }

    public boolean putLong(String key, long l) {
        mEditor.putLong(key, l);
        return mEditor.commit();
    }

    public boolean putString(String key, String s) {
        mEditor.putString(key, s);
        return mEditor.commit();
    }

    public boolean removeKey(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }
}


