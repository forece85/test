package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MatchListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoMatchList> data;
    private List<Integer> selectedPositions;
    private boolean singleChoice;

    public MatchListAdapter(Activity a, List<dtoMatchList> d, boolean s) {
        this.selectedPositions = new ArrayList();
        this.singleChoice = true;
        this.activity = a;
        this.data = d;
        this.singleChoice = s;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public MatchListAdapter(Activity a, List<dtoMatchList> d) {
        this(a, d, true);
    }

    public int getCount() {
        return this.data.size();
    }

    public void setSelectedPosition(int pos) {
        if (this.singleChoice) {
            this.selectedPositions.clear();
            this.selectedPositions.add(Integer.valueOf(pos));
        } else if (this.selectedPositions.isEmpty() || !this.selectedPositions.contains(Integer.valueOf(pos))) {
            this.selectedPositions.add(Integer.valueOf(pos));
        } else {
            this.selectedPositions.remove(Integer.valueOf(pos));
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedPositions() {
        return this.selectedPositions;
    }

    public String getMatchID(int position) {
        return ((dtoMatchList) this.data.get(position)).getMatchID();
    }

    public int getSelectedPosition() {
        if (this.selectedPositions.isEmpty()) {
            return -1;
        }
        return ((Integer) this.selectedPositions.get(this.selectedPositions.size() - 1)).intValue();
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
            vi = inflater.inflate(C0252R.layout.match_list_row, parent, false);
        }
        LinearLayout row = (LinearLayout) vi.findViewById(C0252R.id.row);
        TextView title = (TextView) vi.findViewById(C0252R.id.textTitle);
        TextView teams = (TextView) vi.findViewById(C0252R.id.textTeamDetails);
        TextView toss = (TextView) vi.findViewById(C0252R.id.textTeamToss);
        TextView overs = (TextView) vi.findViewById(C0252R.id.textMatchOvers);
        TextView group = (TextView) vi.findViewById(C0252R.id.textGroup);
        dtoMatchList match = new dtoMatchList();
        match = (dtoMatchList) this.data.get(position);
        if (group != null) {
            if (match.getMatchName().equalsIgnoreCase("")) {
                group.setVisibility(8);
            } else {
                group.setText(match.getMatchName());
                group.setVisibility(0);
            }
        }
        title.setText(match.getMatchDateTime());
        teams.setText(match.getTeams());
        toss.setText(match.getToss());
        String result = match.getResult();
        if (result == null || result.trim().equalsIgnoreCase("")) {
            result = "";
        } else {
            result = ", " + match.getResult();
        }
        overs.setText(match.getOvers() + result);
        if (this.selectedPositions.isEmpty() || !this.selectedPositions.contains(Integer.valueOf(position))) {
            row.setBackgroundColor(0);
        } else {
            row.setBackgroundColor(Utility.getColorInverse(title.getCurrentTextColor()));
        }
        return vi;
    }
}
