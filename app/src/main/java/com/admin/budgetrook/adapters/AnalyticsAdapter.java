package com.admin.budgetrook.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.admin.budgetrook.fragments.BarChartFragment;
import com.admin.budgetrook.fragments.LineChartFragment;
import com.admin.budgetrook.fragments.RadarChartFragment;
import com.admin.budgetrook.fragments.ScreenSlidePageFragment;

public class AnalyticsAdapter extends FragmentPagerAdapter {
    private Context ctx;

    private static final String[] labels = {
            "Line Chart",
            "Bar Chart",
            "Radar Chart"
    };

    public AnalyticsAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0: {
                fragment = Fragment.instantiate(ctx, LineChartFragment.class.getName());
                break;
            }
            case 1: {
                fragment = Fragment.instantiate(ctx, BarChartFragment.class.getName());
                break;
            }
            case 2: {
                fragment = Fragment.instantiate(ctx, RadarChartFragment.class.getName());
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return labels[position];
    }
}
