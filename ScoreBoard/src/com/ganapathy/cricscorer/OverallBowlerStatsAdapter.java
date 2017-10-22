package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class OverallBowlerStatsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoOverallBowlerStats> data;

    public OverallBowlerStatsAdapter(Activity a, List<dtoOverallBowlerStats> d) {
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
            vi = inflater.inflate(C0252R.layout.overall_bowl_stats, parent, false);
        }
        dtoOverallBowlerStats player = new dtoOverallBowlerStats();
        player = (dtoOverallBowlerStats) this.data.get(position);
        Button expandButton = (Button) vi.findViewById(C0252R.id.expandBowlerName);
        expandButton.setText(Utility.toDisplayCase(player.getBowlerName()));
        expandButton.setTag("" + position);
        if (player.hasDetails() && player.getShowDetails()) {
            expandButton.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.toggle_minus, 0, 0, 0);
            ((LinearLayout) vi.findViewById(C0252R.id.playerDetails)).setVisibility(0);
            ((TextView) vi.findViewById(C0252R.id.textBowlMatches)).setText("" + player.getMatches());
            ((TextView) vi.findViewById(C0252R.id.textBowlInnings)).setText("" + player.getInnings());
            ((TextView) vi.findViewById(C0252R.id.textBowlOvers)).setText("" + player.getTotalOvers());
            ((TextView) vi.findViewById(C0252R.id.textBowlRuns)).setText("" + player.getRuns());
            ((TextView) vi.findViewById(C0252R.id.textBowlRunRate)).setText("" + player.getRunRate());
            ((TextView) vi.findViewById(C0252R.id.textBowlAverage)).setText("" + player.getAverage());
            ((TextView) vi.findViewById(C0252R.id.textBowlMaidens)).setText("" + player.getMaidens());
            ((TextView) vi.findViewById(C0252R.id.textBowlWides)).setText("" + player.getWides());
            ((TextView) vi.findViewById(C0252R.id.textBowlNoballs)).setText("" + player.getNoballs());
            ((TextView) vi.findViewById(C0252R.id.textBowlWickets)).setText("" + player.getWickets());
            ((TextView) vi.findViewById(C0252R.id.textBowlBest)).setText("" + player.getBowlerBest());
            ((TextView) vi.findViewById(C0252R.id.textBowlSixes)).setText("" + player.getSixes());
            ((TextView) vi.findViewById(C0252R.id.textBowlFours)).setText("" + player.getFours());
            ((TextView) vi.findViewById(C0252R.id.textFiveWktsHaul)).setText("" + player.getFiveWktsHaul());
        } else {
            expandButton.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.toggle_plus, 0, 0, 0);
            ((LinearLayout) vi.findViewById(C0252R.id.playerDetails)).setVisibility(8);
        }
        return vi;
    }
}
