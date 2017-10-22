package com.ganapathy.cricscorer;

import java.util.ArrayList;
import java.util.Iterator;

public class PartnershipStatsList extends ArrayList<dtoPartnershipStats> {
    private static final long serialVersionUID = 1;

    public PartnershipStatsList(dtoPartnershipStats data) {
        add(data);
    }

    public boolean isRowExists(String striker, String nonstriker) {
        Iterator it = iterator();
        while (it.hasNext()) {
            dtoPartnershipStats d = (dtoPartnershipStats) it.next();
            if ((d.striker1.equalsIgnoreCase(striker) || d.striker2.equalsIgnoreCase(striker)) && (d.striker1.equalsIgnoreCase(nonstriker) || d.striker2.equalsIgnoreCase(nonstriker))) {
                return true;
            }
        }
        return false;
    }

    public void addRow(dtoPartnershipRawData data) {
        boolean isStrikerInNonStriker = false;
        boolean isNonStrikerInStriker = false;
        Iterator it = iterator();
        while (it.hasNext()) {
            dtoPartnershipStats d = (dtoPartnershipStats) it.next();
            if ((d.striker1.equalsIgnoreCase(data.striker) && d.striker2.equalsIgnoreCase(data.nonstriker)) || (d.striker1.equalsIgnoreCase(data.nonstriker) && d.striker2.equalsIgnoreCase(data.striker))) {
                if (d.striker1.equalsIgnoreCase(data.striker)) {
                    d.striker1Runs = data.runs;
                    d.striker1Extras = data.extras;
                    d.striker1Balls = data.balls;
                }
                if (d.striker2.equalsIgnoreCase(data.striker)) {
                    d.striker2Runs = data.runs;
                    d.striker2Extras = data.extras;
                    d.striker2Balls = data.balls;
                    return;
                }
                return;
            }
            isStrikerInNonStriker = d.striker2.equalsIgnoreCase(data.striker);
            isNonStrikerInStriker = d.striker1.equalsIgnoreCase(data.nonstriker);
        }
        dtoPartnershipStats newRow = new dtoPartnershipStats();
        if (isStrikerInNonStriker || isNonStrikerInStriker) {
            newRow.striker1 = data.nonstriker;
            newRow.striker2 = data.striker;
            newRow.striker2Runs = data.runs;
            newRow.striker2Extras = data.extras;
            newRow.striker2Balls = data.balls;
        } else {
            newRow.striker1 = data.striker;
            newRow.striker2 = data.nonstriker;
            newRow.striker1Runs = data.runs;
            newRow.striker1Extras = data.extras;
            newRow.striker1Balls = data.balls;
        }
        add(newRow);
    }
}
