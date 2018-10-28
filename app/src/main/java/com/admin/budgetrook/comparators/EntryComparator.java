package com.admin.budgetrook.comparators;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry o1, Entry o2) {
        if(o1.getX() > o2.getX()){
            return 1;
        } else if(o1.getX() < o2.getX()) {
            return -1;
        } else {
            return 0;
        }
    }
}
