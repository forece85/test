package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MatchSummaryListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoMatchSummary> data;

    public MatchSummaryListAdapter(Activity a, List<dtoMatchSummary> d) {
        this.activity = a;
        this.data = d;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(C0252R.layout.matchsummary_list_row, parent, false);
        }
        LinearLayout row = (LinearLayout) vi.findViewById(C0252R.id.row);
        TextView textOvers = (TextView) vi.findViewById(C0252R.id.textOvers);
        TextView textScore = (TextView) vi.findViewById(C0252R.id.textScore);
        TextView textScoreDesc = (TextView) vi.findViewById(C0252R.id.textScoreDesc);
        TextView textExtras = (TextView) vi.findViewById(C0252R.id.textExtras);
        TextView textBats1 = (TextView) vi.findViewById(C0252R.id.textBats1);
        TextView textBats1Score = (TextView) vi.findViewById(C0252R.id.textBats1Score);
        TextView textBats2 = (TextView) vi.findViewById(C0252R.id.textBats2);
        TextView textBats2Score = (TextView) vi.findViewById(C0252R.id.textBats2Score);
        TextView textBats3 = (TextView) vi.findViewById(C0252R.id.textBats3);
        TextView textBats3Score = (TextView) vi.findViewById(C0252R.id.textBats3Score);
        TextView textBowl1 = (TextView) vi.findViewById(C0252R.id.textBowl1);
        TextView textBowl1Overs = (TextView) vi.findViewById(C0252R.id.textBowl1Overs);
        TextView textBowl2 = (TextView) vi.findViewById(C0252R.id.textBowl2);
        TextView textBowl2Overs = (TextView) vi.findViewById(C0252R.id.textBowl2Overs);
        TextView textBowl3 = (TextView) vi.findViewById(C0252R.id.textBowl3);
        TextView textBowl3Overs = (TextView) vi.findViewById(C0252R.id.textBowl3Overs);
        dtoMatchSummary summary = (dtoMatchSummary) this.data.get(position);
        ((TextView) vi.findViewById(C0252R.id.titleInnings)).setText(summary.title);
        textOvers.setText(summary.overs);
        textScore.setText(summary.totalScore);
        textScoreDesc.setText(summary.totalScoreDesc);
        textExtras.setText(summary.extras);
        textBats1.setText(summary.bats1);
        textBats2.setText(summary.bats2);
        textBats3.setText(summary.bats3);
        textBats1Score.setText(summary.bats1Score);
        textBats2Score.setText(summary.bats2Score);
        textBats3Score.setText(summary.bats3Score);
        textBowl1.setText(summary.bowl1);
        textBowl2.setText(summary.bowl2);
        textBowl3.setText(summary.bowl3);
        textBowl1Overs.setText(summary.bowl1Overs);
        textBowl2Overs.setText(summary.bowl2Overs);
        textBowl3Overs.setText(summary.bowl3Overs);
        return vi;
    }
}
