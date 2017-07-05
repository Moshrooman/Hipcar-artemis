package com.example.justinkwik.hipcar.ExpandAnimation;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class ExpandAnimation extends Animation {
    private View mAnimatedView;
    private LayoutParams mViewLayoutParams;
    private int mMarginStart, mMarginEnd;
    private boolean mIsVisibleAfter = false;
    private boolean mWasEndedAlready = false;
    private ListView listView;
    private boolean listViewBoolean;
    private boolean scrollToBottom;
    private RecyclerView recyclerView;

    /**
     * Initialize the animation
     * @param view The layout we want to animate
     * @param duration The duration of the animation, in ms
     */
    public ExpandAnimation(View view, int duration, ListView listView, boolean scrollToBottom) {

        setDuration(duration);
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();

        // decide to show or hide the view
        mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);

        mMarginStart = mViewLayoutParams.bottomMargin;
        mMarginEnd = (mMarginStart == 0 ? (0- view.getHeight()) : 0);

        view.setVisibility(View.VISIBLE);

        this.listView = listView;
        this.listViewBoolean = true;
        this.scrollToBottom = scrollToBottom;

    }

    public ExpandAnimation(View view, int duration, RecyclerView recyclerView, boolean scrollToBottom) {

        setDuration(duration);
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();

        // decide to show or hide the view
        mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);

        mMarginStart = mViewLayoutParams.bottomMargin;
        mMarginEnd = (mMarginStart == 0 ? (0- view.getHeight()) : 0);

        view.setVisibility(View.VISIBLE);

        this.recyclerView = recyclerView;
        this.listViewBoolean = true;
        this.scrollToBottom = scrollToBottom;

    }

    public ExpandAnimation(View view, int duration) {

        setDuration(duration);
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();

        // decide to show or hide the view
        mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);

        mMarginStart = mViewLayoutParams.bottomMargin;
        mMarginEnd = (mMarginStart == 0 ? (0- view.getHeight()) : 0);

        view.setVisibility(View.VISIBLE);
        this.listViewBoolean = false;

    }

    public void setUpCollapseSubMenus(boolean collapseOrExpand) {

        mIsVisibleAfter = collapseOrExpand;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {

            // Calculating the new bottom margin, and setting it
            mViewLayoutParams.bottomMargin = mMarginStart
                    + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

            // Invalidating the layout, making us seeing the changes we made
            mAnimatedView.requestLayout();

            // Making sure we didn't run the ending before (it happens!)
        } else if (!mWasEndedAlready) {
            mViewLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();

            if (mIsVisibleAfter) {
                mAnimatedView.setVisibility(View.GONE);
            }
            mWasEndedAlready = true;
        }

        if (recyclerView == null && listViewBoolean && scrollToBottom) {
            listView.setSelection(listView.getCount());
        } else if (recyclerView != null && listViewBoolean && scrollToBottom){
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
        }

    }
}
