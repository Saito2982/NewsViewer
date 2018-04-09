package com.example.hiroyki.newsviewer;

import android.util.Log;

import java.util.Comparator;

public class DateComparator implements Comparator<Item>{

    @Override
    public int compare(Item lhs, Item rhs)
    {
        String name1 = lhs.getDate();
        String name2 = rhs.getDate();
        Log.d("compare", name1+"--------------"+name2);

        return name2.compareTo(name1);
    }
}
