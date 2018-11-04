package com.admin.budgetrook.customDataSets;

import android.util.Log;

import com.admin.budgetrook.comparators.EntryYComparator;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.Collections;
import java.util.List;

public class ColoredMaxDataSet extends BarDataSet {
    private static final String TAG = "BUDGETROOK";
    float maxValue;

    public ColoredMaxDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
        Log.d(TAG, "ColoredMaxDataSet: yVals " + yVals.toString());
        maxValue = getHighestEntry(yVals).getY();
        Log.d(TAG, "ColoredMaxDataSet: maxValue " + maxValue);

    }

    private BarEntry getHighestEntry(List<BarEntry> values) {
        Collections.sort(values, new EntryYComparator());
        Log.d(TAG, "ColoredMaxDataSet: sorted " + values.toString());
        int lastIndex = values.size() == 1 ? 0 : values.size() - 1;
        return values.get(lastIndex);
    }

    @Override
    public int getColor(int index) {
        Log.d(TAG, "getColor: getEntryForIndex(index).getY() " + getEntryForIndex(index).getY());
        if (maxValue <= getEntryForIndex(index).getY()) {
            Log.d(TAG, "getColor: if " + mColors.get(1));
            return mColors.get(1);
        } else {
            Log.d(TAG, "getColor: else " + mColors.get(0));
            return mColors.get(0);
        }
    }
}
