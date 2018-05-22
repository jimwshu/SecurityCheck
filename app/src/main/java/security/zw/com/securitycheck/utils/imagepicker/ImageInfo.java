package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;


public class ImageInfo implements Serializable {
    public static final int MAX_GIF_SIZE = 15 * 1024 * 1024;// 15MB最大

    public int id = -1;
    public String url;
    public String bigUrl;
    public MediaFormat mediaFormat = MediaFormat.IMAGE_STATIC;
    public int width;
    public int height;
    public int size; //文件大小

    public ImageInfo() {
    }

    public ImageInfo(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    /**
     * @param url url must start with schema like http:// file://
     */
    public ImageInfo(String url) {
        this(url, MediaFormat.IMAGE_STATIC);
    }

    /**
     * @param url url must start with schema like http:// file://
     */
    public ImageInfo(String url, MediaFormat mediaFormat) {
        this.url = url;
        this.bigUrl = url;
        this.mediaFormat = mediaFormat;
    }

    public ImageInfo(String url, String bigUrl) {
        this.url = url;
        this.bigUrl = bigUrl;
    }

    /**
     * @param url url must start with schema like http:// file://
     */
    public ImageInfo(int id, String url, String mimeType) {
        this.id = id;
        this.url = url;
        this.bigUrl = url;
        this.mediaFormat = MediaFormat.getMediaFormatFromMimeType(mimeType);
    }

    public ImageInfo(int id, String url, String mimeType, int width, int height) {
        this.id = id;
        this.url = url;
        this.bigUrl = url;
        this.mediaFormat = MediaFormat.getMediaFormatFromMimeType(mimeType);
        this.width = width;
        this.height = height;

    }

    /**
     * 是否是本地文件路径
     */
    public static boolean isUrlFile(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            File file = new File(uri.getPath());
            if (!file.exists()) {
                file = new File(uri.getEncodedPath());
            }
            return file.exists();
        }
        return false;
    }

    public String getImageUrl() {
        return url;
    }

    public String getBigImageUrl() {
        if (TextUtils.isEmpty(bigUrl)) {
            return url;
        }
        return bigUrl;
    }

    public float getAspectRatio() {
        if (height > 0 && width > 0) {
            return width * 1.0f / height;
        }
        return 1;
    }

    public String getFilePath(Context context) {
        if (!TextUtils.isEmpty(url)) {
            return UriUtil.getRealPathFromUri(context.getContentResolver(), Uri.parse(url));
        }
        return null;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("url", url);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
            jsonObject.put("mediaformat", mediaFormat.mimeType);
            jsonObject.put("size", size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        id = jsonObject.optInt("id");
        url = jsonObject.optString("url");
        width = jsonObject.optInt("width");
        height = jsonObject.optInt("height");
        mediaFormat = MediaFormat.getMediaFormatFromMimeType(jsonObject.optString("mediaformat"));
        size = jsonObject.optInt("size");
    }

    public String getVideoUrl() {
        return null;
    }


    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(url)) {
            return url.hashCode();
        }
        return super.hashCode();

    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof ImageInfo)) {
            return false;
        }

        if (TextUtils.equals(this.url, ((ImageInfo) o).url)) {
            return true;
        }

        if (id > 0 && ((ImageInfo) o).id == this.id) {
            return true;
        }

        return (false);
    }


    public static ImageInfo valueOf(Cursor cursor) {
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        int id = 0;
        int idIndex;
        if ((idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)) != -1) {
            id = cursor.getInt(idIndex);
        }

        int size = 0;
        int sizeIndex;
        if ((sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)) != -1) {
            size = cursor.getInt(sizeIndex);
        }

        int mimeTypeIndex;
        String mimeType = null;
        if ((mimeTypeIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)) != -1) {
            mimeType = cursor.getString(mimeTypeIndex);
        }

        int width = 0;
        int widthIndex;
        if ((widthIndex = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)) != -1) {
            width = cursor.getInt(widthIndex);
        }

        int height = 0;
        int heightIndex;
        if ((heightIndex = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)) != -1) {
            height = cursor.getInt(heightIndex);
        }

        ImageInfo info = new ImageInfo(id,
                UriUtil.getUriForFile(new File(path)).toString(),
                mimeType,
                width,
                height);
        info.size = size;
        return info;
    }

    /**
     * 是否超过了预期的大小
     */
    public boolean isOverSize() {
        return size > MAX_GIF_SIZE;
    }

    public String getMaxSize() {
        return "15MB";
    }

}
