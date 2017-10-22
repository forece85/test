package com.ganapathy.cricscorer;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class OverallBatStatsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoOverallBatsStat> data;

    public OverallBatStatsAdapter(Activity a, List<dtoOverallBatsStat> d) {
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
            vi = inflater.inflate(C0252R.layout.overall_bat_stats, parent, false);
        }
        dtoOverallBatsStat player = new dtoOverallBatsStat();
        player = (dtoOverallBatsStat) this.data.get(position);
        Button expandButton = (Button) vi.findViewById(C0252R.id.expandStikerName);
        expandButton.setText(Utility.toDisplayCase(player.getStrikerName()));
        expandButton.setTag("" + position);
        if (player.hasDetails() && player.getShowDetails()) {
            expandButton.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.toggle_minus, 0, 0, 0);
            ((LinearLayout) vi.findViewById(C0252R.id.playerDetails)).setVisibility(0);
            ((TextView) vi.findViewById(C0252R.id.textMatches)).setText("" + player.getMatches());
            ((TextView) vi.findViewById(C0252R.id.textInnings)).setText("" + player.getInnings());
            ((TextView) vi.findViewById(C0252R.id.textRuns)).setText("" + player.getRuns());
            ((TextView) vi.findViewById(C0252R.id.textBalls)).setText("" + player.getBalls());
            ((TextView) vi.findViewById(C0252R.id.textAverage)).setText("" + player.getAverage());
            ((TextView) vi.findViewById(C0252R.id.textStrikerRate)).setText("" + player.getStrikeRate());
            ((TextView) vi.findViewById(C0252R.id.textSixes)).setText("" + player.getSixes());
            ((TextView) vi.findViewById(C0252R.id.textFours)).setText("" + player.getFours());
            ((TextView) vi.findViewById(C0252R.id.textTwos)).setText("" + player.getTwos());
            ((TextView) vi.findViewById(C0252R.id.textDots)).setText("" + player.getDots());
            ((TextView) vi.findViewById(C0252R.id.textNotOuts)).setText("" + player.getNotOuts());
            ((TextView) vi.findViewById(C0252R.id.textHighScore)).setText(Html.fromHtml("" + player.getHighScore()));
            ((TextView) vi.findViewById(C0252R.id.textFifties)).setText("" + player.getFifties());
            ((TextView) vi.findViewById(C0252R.id.textTons)).setText("" + player.getTons());
        } else {
            expandButton.setCompoundDrawablesWithIntrinsicBounds(C0252R.drawable.toggle_plus, 0, 0, 0);
            ((LinearLayout) vi.findViewById(C0252R.id.playerDetails)).setVisibility(8);
        }
        return vi;
    }
}
