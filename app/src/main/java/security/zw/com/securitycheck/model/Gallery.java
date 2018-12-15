package security.zw.com.securitycheck.model;


import java.io.Serializable;

public class Gallery implements Serializable {

    public static final int USER_AVATOR_ID = -1;
    public static final int STATUS_NETWORK = 0, STATUS_LOCAL = 1, STATUS_UNKOW = 2;


    public Gallery(int id, String gallery_url, int type) {
        this.id = id;
        this.gallery_url = gallery_url;
    }

    public Gallery() {
    }

    public long id;
    public String gallery_url;
    // 状态,默认来自网络
    public transient int status = STATUS_NETWORK;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gallery gallery = (Gallery) o;

        return id == gallery.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "id=" + id +
                ", gallery_url='" + gallery_url + '\'' +
                ", status=" + status +
                '}';
    }
}
