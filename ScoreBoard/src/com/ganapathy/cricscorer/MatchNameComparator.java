package com.ganapathy.cricscorer;

import java.util.Comparator;

public class MatchNameComparator implements Comparator<dtoMatchList> {
    public int compare(dtoMatchList lhs, dtoMatchList rhs) {
        int result = rhs.getMatchName().compareToIgnoreCase(lhs.getMatchName());
        if (result == 0) {
            return rhs.getMatchDateTimeAsDate().compareTo(lhs.getMatchDateTimeAsDate());
        }
        return -result;
    }
}
