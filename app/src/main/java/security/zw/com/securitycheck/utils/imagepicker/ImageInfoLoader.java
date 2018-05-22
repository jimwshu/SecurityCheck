/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Load images and videos into a single cursor.
 */
public class ImageInfoLoader extends CursorLoader {

    public static final int MIN_FILE_SIZE = 0;

    public static final int ITEM_ID_CAPTURE = Integer.MIN_VALUE;
    public static final String ITEM_DISPLAY_NAME_CAPTURE = "camera";

    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public int id;

    public static final int ID_ALL = -1;
    public static final String ALBUM_NAME_ALL = "相册";


    static final String[] PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
    };

    // 选择相册中的所有图片
    private static final String SELECTION_ALL =
            MediaStore.MediaColumns.SIZE + ">" + MIN_FILE_SIZE;

    // 选择特定的文件夹的图片
    private static final String SELECTION_ALBUM =
                    " bucket_id=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">" + MIN_FILE_SIZE;


    private static String[] getSelectionArgsForBucket(int bucket) {
        return new String[]{String.valueOf(bucket)};
    }

    private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
    private final boolean mEnableCapture;

    private ImageInfoLoader(Context context, String selection, String[] selectionArgs, boolean capture) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY);
        mEnableCapture = capture;
    }

    public static CursorLoader newInstance(Context context, int id, boolean capture) {
        String selection;
        String[] selectionArgs = null;
        boolean enableCapture;

        if (id == ID_ALL) {
            selection = SELECTION_ALL;
            enableCapture = true;
        } else {
            selection = SELECTION_ALBUM;
            selectionArgs = getSelectionArgsForBucket(id);
            enableCapture = true;
        }
        return new ImageInfoLoader(context, selection, selectionArgs, enableCapture);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = super.loadInBackground();
        if (!mEnableCapture) {
            return result;
        }
        MatrixCursor dummy = new MatrixCursor(PROJECTION);
        dummy.addRow(new Object[]{
                ITEM_ID_CAPTURE + "",
                ITEM_DISPLAY_NAME_CAPTURE,
                ITEM_ID_CAPTURE + "",
                0,
                0,
                0,
                "",
                0,
                0});
        return new MergeCursor(new Cursor[]{dummy, result});
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}
