/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package security.zw.com.securitycheck.utils.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.WindowUtils;
import security.zw.com.securitycheck.utils.imagepicker.recyclerview.MediaGridInset;


public class ImagePickFragment extends Fragment {

    public static final String EXTRA_IMAGE_FOLDER = "extra_album";

    private final ImageInfoController mImageInfoController = new ImageInfoController();
    private RecyclerView mRecyclerView;
    private ImageGridAdapter mAdapter;
    private ImageGridAdapter.OnMediaChoooseListener mOnMediaChoooseListener;
    private int spanCount = 4;
    private ImageFolderInfo mImageFolder;
    private boolean mLastSelectState;

    public static ImagePickFragment newInstance(ImageFolderInfo imageFolder) {
        ImagePickFragment fragment = new ImagePickFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_FOLDER, imageFolder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ImageGridAdapter.OnMediaChoooseListener) {
            mOnMediaChoooseListener = (ImageGridAdapter.OnMediaChoooseListener) context;
        } else {
            throw new IllegalArgumentException("context must implement OnMediaCheckedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_pick, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageFolder = (ImageFolderInfo) getArguments().getSerializable(EXTRA_IMAGE_FOLDER);

        mAdapter = new ImageGridAdapter(getContext(), null, listenerWrapper);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        int spacing = WindowUtils.dp2Px(4);
        mRecyclerView.addItemDecoration(new MediaGridInset(spanCount, spacing, false));
        mRecyclerView.setAdapter(mAdapter);

        mImageInfoController.onCreate(getActivity(), new ImageInfoController.ImageInfoLoadCallbacks() {
            @Override
            public void onImageLoad(Cursor cursor) {
                mAdapter.swapCursor(cursor);
            }

            @Override
            public void onAlbumReset() {

            }

        }, mImageFolder.getId());
        mImageInfoController.loadImage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageInfoController.onDestroy();
    }

    ImageGridAdapter.OnMediaChoooseListener listenerWrapper= new ImageGridAdapter.OnMediaChoooseListener() {
        @Override
        public boolean isMediaSelected(ImageInfo imageInfo) {
            return mOnMediaChoooseListener.isMediaSelected(imageInfo);
        }

        @Override
        public int indexOfSelect(ImageInfo imageInfo) {
            return mOnMediaChoooseListener.indexOfSelect(imageInfo);
        }

        @Override
        public boolean isMaxCount() {
            return mOnMediaChoooseListener.isMaxCount();
        }

        @Override
        public int getMaxChooseCount() {
            return mOnMediaChoooseListener.getMaxChooseCount();
        }

        @Override
        public void startCamera() {
            mOnMediaChoooseListener.startCamera();
        }

        @Override
        public void goPreview(ImageFolderInfo folderInfo, ImageInfo imageInfo) {
            mOnMediaChoooseListener.goPreview(mImageFolder, imageInfo);
        }

        @Override
        public void select(ImageInfo imageInfo) {
            mOnMediaChoooseListener.select(imageInfo);
            boolean isMax = mOnMediaChoooseListener.isMaxCount();
            if (mLastSelectState != isMax) {
                mLastSelectState = isMax;
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void unSelect(ImageInfo imageInfo) {
            mOnMediaChoooseListener.unSelect(imageInfo);
            boolean isMax = mOnMediaChoooseListener.isMaxCount();
            if (mLastSelectState != isMax) {
                mLastSelectState = isMax;
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void refreshData() {
        mImageInfoController.restart();
    }

    public void notifyDataChange() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
