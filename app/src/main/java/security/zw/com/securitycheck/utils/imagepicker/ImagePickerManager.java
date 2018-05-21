package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择控制器，使用单例模式
 */
public class ImagePickerManager {
    private static final int STATE_INVALID = 0, STATE_INITING = 1, STATE_INITED = 2;
    private ArrayList<ImageFolderInfo> folders;
    private int state = STATE_INVALID;
    private List<OnInitCompletedListener> listeners = new ArrayList<OnInitCompletedListener>();

    private ImagePickerManager() {
    }

    public void reset() {
        if (state == STATE_INITED) {
            state = STATE_INVALID;
        }
    }

    /**
     * 开启异步初始化，必须在使用前调用，可多次调用，但初始化动作只会执行一次。
     */
    public void init(Context context, final OnInitCompletedListener listener) {
        if (state == STATE_INITED) {
            if (listener != null) {
                listener.onCompleted();
            }
            return;
        }
        if (listener != null) {
            listeners.add(listener);
        }
        if (state == STATE_INITING) {
            return;
        }
        state = STATE_INITING;
        String[] projection = new String[]{MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE};
        CursorLoader loader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        loader.registerListener(0, new OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
                ArrayList<ImageFolderInfo> folders = new ArrayList<ImageFolderInfo>();
                if (cursor != null) {
                    state = STATE_INITED;
                    ImageFolderInfo lastFolder = new ImageFolderInfo(-1, "相册");
                    folders.add(lastFolder);
                    while (cursor.moveToNext()) {
                        int fid = cursor.getInt(0);
                        String name = cursor.getString(1);
                        int id = cursor.getInt(2);
                        String path = cursor.getString(3);
                        int size = cursor.getInt(5);

                        if (TextUtils.isEmpty(path)) {
                            continue;
                        }
                        File file = new File(path);
                        if (!file.exists()) {
                            continue;
                        }

                        ImageFolderInfo folder = null;
                        for (ImageFolderInfo temp : folders) {
                            if (temp.getId() == fid) {
                                folder = temp;
                                break;
                            }
                        }
                        if (folder == null) {
                            folder = new ImageFolderInfo(fid, name);
                            folders.add(folder);
                        }
                        ImageInfo info = new ImageInfo(id, path, Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id + ""));
                        folder.addImage(info);
                        if (size > 16 * 1024) {
                            lastFolder.addImage(info);
                        }
                    }
                    ImagePickerManager.this.folders = folders;
                    for (OnInitCompletedListener listener : listeners) {
                        listener.onCompleted();
                    }
                    listeners.clear();
                }
            }
        });
        loader.startLoading();
    }

    /**
     * 获取单例对象
     */
    public static ImagePickerManager newInstance() {
        return new ImagePickerManager();
    }

    public ArrayList<ImageFolderInfo> getImageFolders() {
        return folders;
    }

    public ImageFolderInfo getImageFolder(int pos) {
        return folders.get(pos);
    }

    public ImageInfo getImageById(int id) {
        ImageInfo info;
        for (ImageFolderInfo folder : folders) {
            info = folder.getById(id);
            if (info != null) {
                return info;
            }
        }
        return null;
    }

    public ImageInfo getImageByPath(String path) {
        ImageInfo info;
        for (ImageFolderInfo folder : folders) {
            info = folder.getByPath(path);
            if (info != null) {
                return info;
            }
        }
        return null;
    }

    public int getCount() {
        return folders.size();
    }

    public static interface OnInitCompletedListener {
        void onCompleted();
    }
}
