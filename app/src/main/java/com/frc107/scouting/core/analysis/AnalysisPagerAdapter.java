package com.frc107.scouting.core.analysis;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AnalysisPagerAdapter extends FragmentPagerAdapter {
    private static final String[] TITLES = { "Chart", "Graph" };

    private ChartFragment chartFragment;
    private GraphFragment graphFragment;

    AnalysisPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            chartFragment = new ChartFragment();
            return chartFragment;
        }

        graphFragment = new GraphFragment();
        return graphFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
