package com.boxnotfound.popularmovies.movies;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
        Create custom GridLayoutManager that dynamically provides target width and height
        values to poster ImageViews in order to properly fit in the layout space,
        given a set spanCount.
*/
public class MovieGridLayoutManager extends GridLayoutManager {

    private static final String LOG_TAG = MovieGridLayoutManager.class.getSimpleName();

    private Context context;
    private int targetItemWidth;
    private int targetItemHeight;
    private int targetItemHorizontalPadding;
    private static final double ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION = 1.5;
    private static final int SPAN_COUNT_PORTRAIT = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 4;

    public MovieGridLayoutManager(final Context context, final int screenWidthInPx) {
        super(context, 2); // spanCount must be set here, but will be adjusted shortly...
        this.context = context;
        setTargetSpanCount();
        setTargetItemWidth(screenWidthInPx);
        setTargetItemHeight();
        setTargetItemHorizontalPadding(screenWidthInPx);
    }

    /*
        Set the grid item's width and appropriate span count according
        to the current screen width, the maximum span size requested
        by the user.
    */
    private void setTargetItemWidth(final int screenWidthInPx) {
        int totalWidth = getHorizontalLayoutSpace(screenWidthInPx);
        targetItemWidth = totalWidth / getSpanCount();
    }

    /*
        With the item width now set, scale the height according to the constant
        aspect ratio in order to maintain proper image size proportions.
    */
    private void setTargetItemHeight() {
        targetItemHeight = (int) (targetItemWidth * ITEM_HEIGHT_TO_WIDTH_ASPECT_RATION);
    }

    private void setTargetItemHorizontalPadding(final int screenWidthInPx) {
        int screenSizeRemaining = screenWidthInPx - (getTargetItemWidth() * getSpanCount());
        if (screenSizeRemaining == 0) {
            targetItemHorizontalPadding = 0;
        } else {
            targetItemHorizontalPadding = 2 / (screenWidthInPx - (getTargetItemWidth() * getSpanCount()));
        }
    }

    private void setTargetSpanCount() {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setSpanCount(SPAN_COUNT_PORTRAIT);
        } else {
            setSpanCount(SPAN_COUNT_LANDSCAPE);
        }
    }

    public int getTargetItemWidth() {
        return targetItemWidth;
    }

    public int getTargetItemHeight() {
        return targetItemHeight;
    }

    public int getTargetItemHorizontalPadding() {
        return targetItemHorizontalPadding;
    }

    private int getHorizontalLayoutSpace(final int screenWidthInPx) {
        return screenWidthInPx - getPaddingRight() - getPaddingLeft();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(LOG_TAG, "onLayoutChildren error: " + e.getMessage());
        }
    }
}
