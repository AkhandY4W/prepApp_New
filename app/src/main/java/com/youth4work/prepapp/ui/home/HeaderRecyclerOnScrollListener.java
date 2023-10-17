package com.youth4work.prepapp.ui.home;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class HeaderRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = HeaderRecyclerOnScrollListener.class.getSimpleName();
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public HeaderRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItem < 5) {
            onShowHeader();
        } else {
            onHideHeader();
        }
    }

    public abstract void onShowHeader();

    public abstract void onHideHeader();
}
