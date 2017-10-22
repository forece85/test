package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class EditTeamListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private boolean canDelete;
    private List<String> data;

    public EditTeamListAdapter(Activity a, List<String> d, boolean canDelete) {
        this.canDelete = false;
        this.activity = a;
        this.data = d;
        this.canDelete = canDelete;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public EditTeamListAdapter(Activity a, List<String> d) {
        this(a, d, false);
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
            vi = inflater.inflate(C0252R.layout.edit_teams_row, parent, false);
        }
        Button deleteButton = (Button) vi.findViewById(C0252R.id.deleteMember);
        if (this.canDelete) {
            deleteButton.setVisibility(0);
        } else {
            deleteButton.setVisibility(8);
        }
        ((TextView) vi.findViewById(C0252R.id.textTeamPlayer)).setText((String) this.data.get(position));
        return vi;
    }
}
