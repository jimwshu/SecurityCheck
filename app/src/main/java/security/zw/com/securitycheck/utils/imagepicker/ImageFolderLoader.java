package security.zw.com.securitycheck.utils.imagepicker;

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

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;




/**
 * Load all albums (grouped by bucket_id) into a single cursor.
 */
public class ImageFolderLoader extends CursorLoader {
    public static final int ID_ALL = -1;
    public static final String ALBUM_NAME_ALL = "相册";

    public static final String COLUMN_COUNT = "count";
    private static final Uri QUERY_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String[] COLUMNS = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            COLUMN_COUNT};
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            "COUNT(*) AS " + COLUMN_COUNT};

    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    private ImageFolderLoader(Context context, String selection, String[] selectionArgs) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, BUCKET_ORDER_BY);
    }

    public static CursorLoader newInstance(Context context) {
        String selection;
        selection = SELECTION_FOR_SINGLE_MEDIA_TYPE;
        return new ImageFolderLoader(context, selection, null);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);
        int totalCount = 0;
        String allAlbumCoverPath = "";
        if (albums != null) {
            while (albums.moveToNext()) {
                totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
            }
            if (albums.moveToFirst()) {
                allAlbumCoverPath = albums.getString(albums.getColumnIndex(MediaStore.MediaColumns.DATA));
            }
        }
        allAlbum.addRow(new Object[]{ID_ALL, ID_ALL, ALBUM_NAME_ALL, allAlbumCoverPath,
                String.valueOf(totalCount)});

        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}
