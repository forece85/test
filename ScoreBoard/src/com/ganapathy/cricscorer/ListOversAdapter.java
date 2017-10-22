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

public class ListOversAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoBall> data;
    private List<Integer> selectedPositions;
    private boolean singleChoice;

    public ListOversAdapter(Activity a, List<dtoBall> d, boolean s) {
        this.selectedPositions = new ArrayList();
        this.singleChoice = true;
        this.activity = a;
        this.data = d;
        this.singleChoice = s;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public ListOversAdapter(Activity a, List<dtoBall> d) {
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
            vi = inflater.inflate(C0252R.layout.over_row, parent, false);
        }
        LinearLayout row = (LinearLayout) vi.findViewById(C0252R.id.row);
        dtoBall ball = (dtoBall) this.data.get(position);
        TextView firstLine = (TextView) vi.findViewById(C0252R.id.firstLineText);
        TextView secondLine = (TextView) vi.findViewById(C0252R.id.secondLineText);
        TextView group = (TextView) vi.findViewById(C0252R.id.textGroup);
        if (group != null) {
            if (ball.GroupTitle.equalsIgnoreCase("")) {
                group.setVisibility(8);
            } else {
                group.setText(Utility.toDisplayCase(ball.GroupTitle));
                group.setVisibility(0);
            }
        }
        firstLine.setText(ball.getFirstLine());
        secondLine.setText(ball.getSecondLine());
        if (this.selectedPositions.isEmpty() || !this.selectedPositions.contains(Integer.valueOf(position))) {
            row.setBackgroundColor(0);
        } else {
            row.setBackgroundColor(Utility.getColorInverse(firstLine.getCurrentTextColor()));
        }
        return vi;
    }
}
