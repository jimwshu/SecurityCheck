package security.zw.com.securitycheck.utils.image;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.CacheKeyUtil;
import com.facebook.cache.disk.DefaultDiskStorage;
import com.facebook.cache.disk.DiskStorageCache;
import com.facebook.cache.disk.DynamicDefaultDiskStorage;
import com.facebook.cache.disk.FileCache;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.SecurityApplication;


/**
 * Created by yrsx on 16/8/20.
 */

public class FrescoImageloader {
    private static final String TAG = "Imageloader";
    private static final String FILE_SCHEMA = "file://";
    private static final int MAX_CACHE_SIZE = 100;
    private static final Map<String, Uri> sCacheUris = new LinkedHashMap<String, Uri>(MAX_CACHE_SIZE + 1) {
        // This method is called just after a new entry has been added
        public boolean removeEldestEntry(Entry eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };


    private static void check(ImageView imageView) {
        if (!(imageView instanceof SimpleDraweeView)) {
            throw new IllegalArgumentException("ImageView must be kind of SimpleDraweeView");
        }
    }

    /**
     * Circle by default,
     */
    public static void displayAvatar(ImageView imageView, String uri) {
        displayAvatar(imageView, uri, R.mipmap.my_default_icon);
    }

    /**
     * Circle by default,
     */
    public static void displayAvatar(ImageView imageView, String uri, int defaultRes) {
        displayAvatar(imageView, uri, defaultRes, true, 0);
    }

    public static void displayAvatar(ImageView imageView, String uri, boolean circle) {
        displayAvatar(imageView, uri,  R.mipmap.my_default_icon, circle, 0);
    }

    public static void displayAvatar(ImageView imageView, String uri, int defaultRes, boolean circle, int cornersRadius) {
        // 图片变暗处理 UIHelper.imageViewFilter(imageView);
        if (defaultRes == 0) {
            defaultRes = R.mipmap.my_default_icon;
        }
        Uri imageURI = null;
        try {
            imageURI = Uri.parse(uri);
        } catch (Throwable e) {
            Log.e(TAG, "Invalid uri");
        }

        GenericDraweeHierarchy hierarchy = ((SimpleDraweeView) imageView).getHierarchy();
        if (hierarchy == null) {
            hierarchy = new GenericDraweeHierarchyBuilder(imageView.getResources()).build();
        }

        hierarchy.setPlaceholderImage(defaultRes);
        hierarchy.setFailureImage(defaultRes);

        if (circle) {
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            if (roundingParams == null) {
                roundingParams = RoundingParams.asCircle();
            }
            roundingParams.setRoundAsCircle(true);
            hierarchy.setRoundingParams(roundingParams);
        } else if (cornersRadius > 0) {
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            if (roundingParams == null) {
                roundingParams = RoundingParams.fromCornersRadius(cornersRadius);
            }
            roundingParams.setCornersRadius(cornersRadius);
            roundingParams.setRoundAsCircle(false);
            hierarchy.setRoundingParams(roundingParams);
        }

        ((SimpleDraweeView) imageView).setHierarchy(hierarchy);
        ((SimpleDraweeView) imageView).setImageURI(imageURI);

    }

    public static void displayImage(ImageView imageView, String uri) {
        check(imageView);
        displayImage(imageView, uri, 0);
    }

    public static void displayImage(ImageView imageView, String uri, int placeHolderRes) {
        check(imageView);
        displayImage(imageView, uri, placeHolderRes, 0);
    }



    public static void displayImage(ImageView imageView, String uri, Drawable placeHolder) {
        check(imageView);
        displayImage(imageView, uri, placeHolder, null);
    }

    public static void displayImage(ImageView imageView, String uri, int placeHolderRes, int errorRes) {
        displayImage(imageView, uri, placeHolderRes, errorRes, false);
    }

    public static void displayImage(ImageView imageView, String uri, int placeHolderRes, int errorRes, boolean circle) {
        check(imageView);
        Drawable placeHolderDrawable = null;
        if (placeHolderRes != 0) {
            try {
                placeHolderDrawable = imageView.getResources().getDrawable(placeHolderRes);
            } catch (Resources.NotFoundException e) {
            }
        }
        Drawable errorDrawable = null;
        if (errorRes != 0) {
            try {
                errorDrawable = imageView.getResources().getDrawable(errorRes);
            } catch (Resources.NotFoundException e) {

            }
        }
        if (errorDrawable == null) {
            errorDrawable = placeHolderDrawable;
        }
        displayImage(imageView, uri, placeHolderDrawable, errorDrawable, circle);
    }

    public static void displayImage(ImageView imageView, String uri, int placeHolder, int error, boolean circle, int cornersRadius) {
        displayImage(imageView, uri, imageView.getResources().getDrawable(placeHolder), imageView.getResources().getDrawable(error), circle, cornersRadius);
    }

    public static void displayImage(ImageView imageView, String uri, Drawable placeHolder, Drawable error) {
        check(imageView);
        displayImage(imageView, uri, placeHolder, error, false);
    }

    public static void displayImage(ImageView imageView, String uri, Drawable placeHolder, Drawable error, boolean circle) {
        check(imageView);
        displayImage(imageView, uri, placeHolder, error, circle, 0);
    }

    public static void displayImage(ImageView imageView, String uri, Drawable placeHolder, Drawable error, int cornersRadius) {
        check(imageView);
        displayImage(imageView, uri, placeHolder, error, false, cornersRadius);
    }

    public static void displayImage(ImageView imageView, String uri, Drawable placeHolder, Drawable error, boolean circle, int cornersRadius) {
        check(imageView);
        // 图片变暗处理 UIHelper.imageViewFilter(imageView);
        GenericDraweeHierarchy hierarchy = ((SimpleDraweeView) imageView).getHierarchy();
        if (hierarchy == null) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(imageView.getResources());
            ((SimpleDraweeView) imageView).setHierarchy(builder.build());
            hierarchy = ((SimpleDraweeView) imageView).getHierarchy();
        }

        if (placeHolder != null) {
            hierarchy.setPlaceholderImage(placeHolder);
        }

        if (error != null) {
            hierarchy.setFailureImage(error);
        }

        if (circle) {
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            if (roundingParams == null) {
                roundingParams = RoundingParams.asCircle();
            }
            roundingParams.setRoundAsCircle(true);
            hierarchy.setRoundingParams(roundingParams);
        } else if (cornersRadius > 0) {
            RoundingParams roundingParams = hierarchy.getRoundingParams();
            if (roundingParams == null) {
                roundingParams = RoundingParams.fromCornersRadius(cornersRadius);
            } else {
                roundingParams.setRoundAsCircle(false);
                roundingParams.setCornersRadius(cornersRadius);
            }
            hierarchy.setRoundingParams(roundingParams);
        }

        Uri imageURI = null;
        try {
            imageURI = get(uri);
        } catch (Throwable e) {
            Log.e(TAG, "Invalid uri");
        }
        ((SimpleDraweeView) imageView).setImageURI(imageURI);
    }

    /**
     * Fast get, if not cached then create.
     */
    public static Uri get(String url) {
        Uri uri = sCacheUris.get(url);
        if (uri == null) {
            uri = parseUri(url);
            sCacheUris.put(url, uri);
        }
        return uri;
    }

    public static Uri parseUri(String url) {
        if (TextUtils.isEmpty(url)) {
            return Uri.EMPTY;
        } else if (url.startsWith(FILE_SCHEMA)) {
            File file = new File(url.substring(FILE_SCHEMA.length()));
            return Uri.fromFile(file);
        } else {
            return Uri.parse(url);
        }
    }

    public static File getDiskCacheFile(String url) {
        try {
            Uri uri = Uri.parse(url);
            return getDiskCacheFile(uri);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getDiskCacheFile(Uri imageUri) {
        if (imageUri == null) {
            return null;
        }
        File file = null;
        ImageRequest imageRequest = ImageRequest.fromUri(imageUri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, SecurityApplication.mContext);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        if (resource != null && resource instanceof FileBinaryResource) {
             file = ((FileBinaryResource) resource).getFile();
        }
        return file;
    }

    /**
     * 因为没加载过的图片不知道缓存地址。。。
     */
    public static File getDiskCacheFileWithReflection(String url) {
        Uri imageUri = get(url);
        ImageRequest imageRequest = ImageRequest.fromUri(imageUri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, SecurityApplication.mContext);
        List<String> resourceIds = CacheKeyUtil.getResourceIds(cacheKey);
        FileCache fileCache = ImagePipelineFactory.getInstance()
                .getMainFileCache();
        if (resourceIds == null || resourceIds.isEmpty()) {
            return null;
        }
        if (fileCache instanceof DiskStorageCache) {
            //DynamicDefaultDiskStorage or DefaultDiskStorage
            Object object = ReflectionUtils.get(fileCache, "mStorage");
            if (object instanceof DynamicDefaultDiskStorage) {
                object = ReflectionUtils.get(object, "mCurrentState");
                if (object != null) {
                    object = ReflectionUtils.get(object, "delegate");
                }
            }
            if (object instanceof DefaultDiskStorage) {
                Method method = ReflectionUtils.getMethod(object, "getContentFileFor", String.class);
                object = ReflectionUtils.invokeMethod(object, method, resourceIds.get(0));
                return (File) object;
            }
        }
        return null;
    }

    public static boolean isInMemoryCache(String url) {
        try {
            Uri uri = get(url);
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            return imagePipeline.isInBitmapMemoryCache(uri);
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isInMemoryCache(Uri uri) {
        try{
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            return imagePipeline.isInBitmapMemoryCache(uri);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 从内存缓存里面删除
     */
    public static void evictFromMemoryCache(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        Uri tmpUri;
        for (int i = 0, size = urls.size(); i < size; i++) {
            tmpUri = get(urls.get(i));
            Fresco.getImagePipeline().evictFromMemoryCache(tmpUri);
        }
    }

    /**
     * 从内存缓存里面删除
     */
    public static void evictFromMemoryCache2(List<Uri> uris) {
        if (uris == null || uris.isEmpty()) {
            return;
        }
        Uri tmpUri;
        for (int i = 0, size = uris.size(); i < size; i++) {
            tmpUri = uris.get(i);
            Fresco.getImagePipeline().evictFromMemoryCache(tmpUri);
        }
    }

    public static String getFrescoResUrl(int resId) {
        return "res://"
                + SecurityApplication.getInstance().getPackageName()
                + "/"
                + resId;
    }


    public static void clearAllMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

}
