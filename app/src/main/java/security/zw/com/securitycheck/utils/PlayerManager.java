package security.zw.com.securitycheck.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import security.zw.com.securitycheck.SecurityApplication;

/**
 * 音乐播放管理类
 * Created by Administrator on 2015/8/27 0027.
 */
public class PlayerManager {

    private static final String TAG = PlayerManager.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private PlayCallback callback;
    private Context context;

    private boolean isPause = false;
    private String filePath;

    public static synchronized PlayerManager getInstance(){
        return new PlayerManager();
    }

    private PlayerManager(){
        this.context = SecurityApplication.getInstance().getAppContext();
        initMediaPlayer();
    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 播放回调接口
     */
    public static class PlayCallback{

        /**
         * 音乐准备完毕
         */
        public void onPrepared() {}

        /**
         * 音乐播放完成
         */
        public void onComplete() {}

        /**
         * 音乐停止播放
         */
        public void onStop() {}
    }

    public void play(String path) {
        play(path, false);
    }

    public void play(String path, boolean looping) {
        play(path, looping, null);
    }

    /**
     * 播放音乐
     * @param path 音乐文件路径
     * @param callback 播放回调函数
     */
    public void play(String path, boolean looping, final PlayCallback callback){
        this.filePath = path;
        this.callback = callback;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(path));
            mediaPlayer.setLooping(looping);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (callback != null) {
                        callback.onPrepared();
                    }
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    LogUtils.d(TAG, "play error " + what + ", " + extra);
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (callback != null) {
                        callback.onComplete();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPause(){
        return isPause;
    }

    public void pause(){
        if (isPlaying()){
            isPause = true;
            mediaPlayer.pause();
        }
    }

    public void resume(){
        if (isPause){
            isPause = false;
            mediaPlayer.start();
        }
    }

    /**
     * 停止播放
     */
    public void stop(){
//        if (isPlaying()){
            try {
                mediaPlayer.setLooping(false);
                mediaPlayer.stop();
                if (callback != null) {
                    callback.onStop();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
//        }
    }

    /**
     * 是否正在播放
     * @return 正在播放返回true,否则返回false
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

}
