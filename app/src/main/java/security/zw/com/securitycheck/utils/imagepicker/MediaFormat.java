package security.zw.com.securitycheck.utils.imagepicker;

public enum MediaFormat {

    UNKNOW("unknow", "unknow", 0),
    IMAGE_STATIC("img", "image/", 1);

    public String name;
    public String mimeType;
    public int upload;//上传图片时用来标志格式的数字

    MediaFormat(String name, String mimetype, int upload) {
        this.name = name;
        this.mimeType = mimetype;
        this.upload = upload;
    }

    public static MediaFormat getMediaFormatFromMimeType(String mimtype) {
        return IMAGE_STATIC;
    }

    public static MediaFormat getMediaFormatFromNetwork(String key) {
        return IMAGE_STATIC;
    }



    public static int getFormatTagImage(MediaFormat format) {
        int typeDrawable = 0;
        switch (format) {
            default:
                typeDrawable = 0;
                break;
        }
        return typeDrawable;
    }

}
