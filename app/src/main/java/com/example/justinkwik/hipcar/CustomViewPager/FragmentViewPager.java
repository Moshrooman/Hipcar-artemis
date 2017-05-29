package com.example.justinkwik.hipcar.CustomViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Justin Kwik on 29/05/2017.
 */
public class FragmentViewPager extends ViewPager {

    PagerAdapter mPagerAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.setAdapter(null);
        super.onDetachedFromWindow();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {

        Log.e("Stored Adapter: ", "True");
        mPagerAdapter = pagerAdapter;
    }

    public FragmentViewPager(Context context) {
        super(context);
    }

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
