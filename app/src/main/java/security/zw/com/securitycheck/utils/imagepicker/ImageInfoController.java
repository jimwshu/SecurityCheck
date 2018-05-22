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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;

public class ImageInfoController implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = -10;
    private static final String STATE_CURRENT_SELECTION = "state_current_selection";
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private ImageInfoLoadCallbacks mCallbacks;
    private int mCurrentSelection;
    private boolean isLoaderStart;
    private int id;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        return ImageInfoLoader.newInstance(context, this.id, this.id == ImageInfoLoader.ID_ALL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mCallbacks.onImageLoad(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mCallbacks.onAlbumReset();
    }

    public void onCreate(FragmentActivity activity, ImageInfoLoadCallbacks callbacks, int id) {
        this.id = id;
        mContext = new WeakReference<Context>(activity);
        mLoaderManager = activity.getSupportLoaderManager();
        mCallbacks = callbacks;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        mCurrentSelection = savedInstanceState.getInt(STATE_CURRENT_SELECTION);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_SELECTION, mCurrentSelection);
    }

    public void onDestroy() {
        if (isLoaderStart) {
            mLoaderManager.destroyLoader(LOADER_ID);
            mCallbacks = null;
        }
    }

    public void loadImage() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
        isLoaderStart = true;
    }

    public int getCurrentSelection() {
        return mCurrentSelection;
    }

    public void setStateCurrentSelection(int currentSelection) {
        mCurrentSelection = currentSelection;
    }

    public void restart() {
        mLoaderManager.restartLoader(LOADER_ID, null, this);
        isLoaderStart = true;
    }

    public interface ImageInfoLoadCallbacks {
        void onImageLoad(Cursor cursor);

        void onAlbumReset();
    }
}
