package com.example.ducvu212.gallary;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import static com.example.ducvu212.gallary.MainActivity.sPaths;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment implements IList {

    private ImageAdapter mAdapterImage;
    private ArrayList<Image> mItemArrayList;
    private RecyclerView mRecyclerView;
    private int mFirstItem = 0;

    public GridFragment() {
        // Required empty public constructor
    }

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getActivity().getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewByIds();
        addData();
        initComponents();
    }

    private void addData() {
        int itemHold = 10;
        for (int i = mFirstItem; i < mFirstItem + itemHold; i++) {
            mItemArrayList.add(new Image(sPaths.get(i)));
        }
        mFirstItem = mItemArrayList.size() - 1;
    }

    private void initComponents() {
        final RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapterImage = new ImageAdapter(mItemArrayList, this, getActivity(), mRecyclerView, manager);
        mRecyclerView.setAdapter(mAdapterImage);
        mRecyclerView.smoothScrollToPosition(0);
        mAdapterImage.setLoadMore(new ImageAdapter.ILoadMore() {
            @Override
            public void onLoadMore() {
                mItemArrayList.add(null);
                mAdapterImage.notifyItemInserted(mItemArrayList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mItemArrayList.remove(mItemArrayList.size() - 1);
                        mAdapterImage.notifyItemRemoved(mItemArrayList.size());
                        mAdapterImage.notifyDataSetChanged();
                        addData();
                        mAdapterImage.setLoaded();
                    }
                }, 2000);
            }
        });
    }

    private void findViewByIds() {
        mRecyclerView = getActivity().findViewById(R.id.recycleview_grid);
        mItemArrayList = new ArrayList<>();
    }

    @Override
    public Image getItem(int position) {
        return mItemArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        if (mItemArrayList == null) {
            return 0;
        }else {
            return mItemArrayList.size();
        }
    }
}
