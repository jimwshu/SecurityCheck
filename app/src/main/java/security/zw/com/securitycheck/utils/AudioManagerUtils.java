package security.zw.com.securitycheck.utils;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;

import security.zw.com.securitycheck.SecurityApplication;

/**
 * Created by chandler on 16/8/17.
 */

public class AudioManagerUtils {
    private String TAG = "AudioManagerUtils";
    private static AudioManagerUtils audioManagerUtils;
    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    private AudioManagerUtils() {
    }

    public synchronized static AudioManagerUtils getInstance() {
        if (audioManagerUtils == null) {
            audioManagerUtils = new AudioManagerUtils();
        }
        return audioManagerUtils;
    }

    public AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = (AudioManager)
                    SecurityApplication.getInstance().getSystemService(Service.AUDIO_SERVICE);
        }
        return audioManager;
    }

    public int getMaxVolume(int streamType) {
        if (maxVolume <= 0) {
            maxVolume = getAudioManager().getStreamMaxVolume(streamType);
        }
        return maxVolume;
    }

    public int getCurrentVolume(int streamType) {
        currentVolume = getAudioManager().getStreamVolume(streamType);
        return currentVolume;
    }
}
