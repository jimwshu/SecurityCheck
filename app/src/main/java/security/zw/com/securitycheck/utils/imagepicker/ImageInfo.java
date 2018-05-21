package security.zw.com.securitycheck.utils.imagepicker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageInfo implements Parcelable {
    public final int id;
    public final String path;
    /**
     * Content uri
     */
    public final Uri uri;

    public ImageInfo(int id, String path, Uri uri) {
        this.id = id;
        this.path = path;
        this.uri = uri;
    }

    protected ImageInfo(Parcel in) {
        id = in.readInt();
        path = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    @Override
    public int hashCode() {

        return path.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof ImageInfo)) {
            return false;
        }

        if (((ImageInfo) o).id < 0 && this.path.equals(((ImageInfo) o).path)) {
            return true;
        }

        if (((ImageInfo) o).id == this.id && this.path.equals(((ImageInfo) o).path)) {
            return true;
        }

        return (false);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeParcelable(uri, flags);
    }
}
