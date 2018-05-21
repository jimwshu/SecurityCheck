package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;

public class ImageFolderController implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int ID = ImageFolderController.class.getSimpleName().hashCode();

    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private ImageFolderCallback mCallbacks;

    public void onCreate(FragmentActivity activity, ImageFolderCallback callbacks) {
        mContext = new WeakReference<Context>(activity);
        mLoaderManager = activity.getSupportLoaderManager();
        mCallbacks = callbacks;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return ImageFolderLoader.newInstance(mContext.get());
    }

    public void onDestory() {
        mLoaderManager.destroyLoader(ID);
        mCallbacks = null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCallbacks.onFilesLoad(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCallbacks.onReset();
    }

    public void load() {
        mLoaderManager.initLoader(ID, null, this);
    }

    public void restart() {
        mLoaderManager.restartLoader(ID, null, this);
    }

    public interface ImageFolderCallback {
        public void onFilesLoad(Cursor cursor);
        public void onReset();
    }
}
