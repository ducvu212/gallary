package com.example.ducvu212.gallary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IList {

    private static final int ITEM_IMAGE = 0;
    private static final int ITEM_LOADING = 1;
    private ArrayList<ItemImage> mItemImages;
    private IList mIList;
    private Activity mActivity;
    private boolean mIsLoading;
    private int mTotalItem;
    private int mLastVisibleItem;
    private final int mVisibleThreeHold = 10;
    private ILoadMore mILoadMore;
    private final String BUNDLE_IMAGE_FULL = "image";
    private final String TRANSACTION_TAG = "tag";
    private final String TRANSACTION_BACKSTACK = "ahihi";

    public ImageAdapter(ArrayList<ItemImage> itemImages, IList IList, Activity activity, RecyclerView recyclerView, RecyclerView.LayoutManager manager) {
        mItemImages = itemImages;
        mIList = IList;
        mActivity = activity;
        RecyclerView recyclerView1 = recyclerView;
        final LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mLastVisibleItem = layoutManager.findLastVisibleItemPosition();
                mTotalItem = layoutManager.getItemCount();
                if (!mIsLoading && (mVisibleThreeHold + mLastVisibleItem) >= mTotalItem) {
                    if (mILoadMore != null) mILoadMore.onLoadMore();
                    mIsLoading = true;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.mILoadMore = loadMore;
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemImages.get(position) == null ? ITEM_LOADING : ITEM_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ITEM_IMAGE) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item, viewGroup, false);
            return new ItemImageViewHolder(view);
        } else if (i == ITEM_LOADING) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.progressbar, viewGroup, false);
            return new ItemLoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof ItemImageViewHolder) {
            //Nens anh
//            Bitmap bitmapImage = BitmapFactory.decodeFile(mItemImages.get(i).getPath());
//            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
//            final Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            File file = new File(mItemImages.get(i).getPath());
            final ItemImageViewHolder itemImageViewHolder = (ItemImageViewHolder) viewHolder;
            Picasso.get().load(file).resize(1333, 1000).centerCrop().into(itemImageViewHolder.mImageView);
            itemImageViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    FragmentManager manager = ((FragmentActivity) mActivity).getSupportFragmentManager();
                    PhotoViewerFragment photoViewerFragment = new PhotoViewerFragment();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.relative_main, photoViewerFragment, TRANSACTION_TAG);
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_IMAGE_FULL, mItemImages.get(i).getPath());
                    photoViewerFragment.setArguments(bundle);
                    transaction.addToBackStack(TRANSACTION_BACKSTACK);
                    transaction.commit();
                }
            });
        } else if (viewHolder instanceof ItemLoadingViewHolder) {
            ItemLoadingViewHolder itemImageViewHolder = (ItemLoadingViewHolder) viewHolder;
            itemImageViewHolder.mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public ItemImage getItem(int position) {
        return mIList.getItem(position);
    }

    @Override
    public int getItemCount() {
        return mIList.getItemCount();
    }

    static class ItemImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public ItemImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_im);
        }
    }

    static class ItemLoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        public ItemLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface ILoadMore {
        void onLoadMore();
    }
}
