package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ganapathy.cricscorer.dtoArchiveFile.ArchiveFileIconType;
import java.util.ArrayList;
import java.util.List;

public class ArchiveFileAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<dtoArchiveFile> data;
    private List<Integer> selectedPositions;
    private boolean singleChoice;

    public ArchiveFileAdapter(Activity a, List<dtoArchiveFile> d, boolean s) {
        this.selectedPositions = new ArrayList();
        this.singleChoice = true;
        this.activity = a;
        this.data = d;
        this.singleChoice = s;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public ArchiveFileAdapter(Activity a, List<dtoArchiveFile> d) {
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

    public String getFileName(int position) {
        return ((dtoArchiveFile) this.data.get(position)).getFileName();
    }

    public ArchiveFileIconType getItemType(int position) {
        return ((dtoArchiveFile) this.data.get(position)).getIconType();
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(C0252R.layout.simplegrid_icontext, parent, false);
        }
        LinearLayout row = (LinearLayout) vi.findViewById(C0252R.id.row);
        TextView fileName = (TextView) vi.findViewById(C0252R.id.archiveText);
        dtoArchiveFile file = new dtoArchiveFile();
        file = (dtoArchiveFile) this.data.get(position);
        fileName.setCompoundDrawablesWithIntrinsicBounds(0, file.getIconResource(), 0, 0);
        fileName.setText(file.getFileName());
        if (this.selectedPositions.isEmpty() || !this.selectedPositions.contains(Integer.valueOf(position))) {
            row.setBackgroundColor(0);
        } else {
            row.setBackgroundColor(Utility.getColorInverse(fileName.getCurrentTextColor()));
        }
        return vi;
    }
}
