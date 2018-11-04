package com.admin.budgetrook.comparators;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

public class EntryYComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry o1, Entry o2) {
        if (o1.getY() > o2.getY()) {
            return 1;
        } else if (o1.getY() < o2.getY()) {
            return -1;
        } else {
            return 0;
        }
    }
}
