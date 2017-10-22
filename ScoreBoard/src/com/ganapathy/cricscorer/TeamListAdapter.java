package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<String> data;
    private List<Integer> selectedPositions;
    private boolean singleChoice;

    public TeamListAdapter(Activity a, List<String> d, boolean s) {
        this.selectedPositions = new ArrayList();
        this.singleChoice = true;
        this.activity = a;
        this.data = d;
        this.singleChoice = s;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public TeamListAdapter(Activity a, List<String> d) {
        this(a, d, true);
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

    public int getSelectedPosition() {
        if (this.selectedPositions.isEmpty()) {
            return -1;
        }
        return ((Integer) this.selectedPositions.get(this.selectedPositions.size() - 1)).intValue();
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

    public String getTeamName(int position) {
        return (String) this.data.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(C0252R.layout.simplerow, parent, false);
        }
        RelativeLayout row = (RelativeLayout) vi.findViewById(C0252R.id.row);
        TextView teamTextView = (TextView) vi.findViewById(C0252R.id.rowTextView);
        if (this.selectedPositions.isEmpty() || !this.selectedPositions.contains(Integer.valueOf(position))) {
            row.setBackgroundColor(0);
        } else {
            row.setBackgroundColor(Utility.getColorInverse(teamTextView.getCurrentTextColor()));
        }
        teamTextView.setText("" + ((String) this.data.get(position)));
        return vi;
    }
}
