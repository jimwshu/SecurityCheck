package security.zw.com.securitycheck.utils.imagepicker;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;
import java.util.ArrayList;

import qsbk.app.model.QbBean;

/**
 * 图片文件夹的数据模型
 */
public class ImageFolderInfo implements Serializable {
    public static final int ID_ALL = -1;
    public static final String ALBUM_NAME_ALL = "相册";
    public int count;
    private int id;
    private String name;
    public String photoPath;
    private ArrayList<ImageInfo> images;

    public ImageFolderInfo() {
        this(0, null, new ArrayList<ImageInfo>());
    }

    public ImageFolderInfo(int id, String name) {
        this(id, name, new ArrayList<ImageInfo>());
    }

    private ImageFolderInfo(int id, String name, ArrayList<ImageInfo> images) {
        this.id = id;
        this.name = name;
        this.images = images;
    }

    public ImageFolderInfo(String bucket_id, String path, String bucket_display_name, int count) {
        try {
            this.id = Integer.valueOf(bucket_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.name = bucket_display_name;
        this.count = count;
        this.photoPath = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取文件夹下图片地址列表
     */
    public void addImage(ImageInfo info) {
        images.add(info);
    }

    public void addImage(int index, ImageInfo info) {
        images.add(index, info);
    }

    public String getPath(int location) {
        return images.get(location).url;
    }

    public ImageInfo getById(int id) {
        for (ImageInfo info : images) {
            if (info.id == id) {
                return info;
            }
        }
        return null;
    }

    public ImageInfo getByPath(String path) {
        for (ImageInfo info : images) {
            if (info.url.equalsIgnoreCase(path)) {
                return info;
            }
        }
        return null;
    }

    public ImageInfo get(int location) {
        return images.get(location);
    }

    public int size() {
        return images.size();
    }

    public ArrayList<ImageInfo> list() {
        return images;
    }

    public boolean isAll() {
        return id == ID_ALL;
    }

    public static ImageFolderInfo valueOf(Cursor cursor) {
        return new ImageFolderInfo(
                cursor.getString(cursor.getColumnIndex("bucket_id")),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                cursor.getString(cursor.getColumnIndex("bucket_display_name")),
                cursor.getInt(cursor.getColumnIndex(ImageFolderLoader.COLUMN_COUNT)));
    }
}
