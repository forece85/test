package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private int batOrBowl;
    private dtoTeamPlayerList data;
    private List<Integer> selectedPositions;
    private boolean singleChoice;

    public PlayerListAdapter(Activity a, dtoTeamPlayerList d, boolean s, int b) {
        this.selectedPositions = new ArrayList();
        this.singleChoice = true;
        this.batOrBowl = 1;
        this.activity = a;
        this.data = d;
        this.singleChoice = s;
        this.batOrBowl = b;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public PlayerListAdapter(Activity a, dtoTeamPlayerList d) {
        this(a, d, true, 1);
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

    public String getPlayerName(int position) {
        return ((dtoTeamPlayer) this.data.get(position)).getPlayerName();
    }

    public int getPosition(String playerName) {
        int pos = 0;
        Iterator it = this.data.iterator();
        while (it.hasNext()) {
            if (((dtoTeamPlayer) it.next()).getPlayerName().equalsIgnoreCase(playerName)) {
                return pos;
            }
            pos++;
        }
        return -1;
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
            vi = inflater.inflate(C0252R.layout.list_choose_players_row, parent, false);
        }
        ((ImageView) vi.findViewById(C0252R.id.checkPlayer)).setVisibility(8);
        dtoTeamPlayer player = (dtoTeamPlayer) this.data.get(position);
        String suffix = " (" + (player.getHandedBat() == 0 ? vi.getContext().getResources().getString(C0252R.string.RHBtext) : vi.getContext().getResources().getString(C0252R.string.LHBtext)) + ")";
        if (player.getPlayerName() == null || player.getPlayerName().trim().equalsIgnoreCase("") || this.batOrBowl == 2) {
            suffix = "";
        }
        ((TextView) vi.findViewById(C0252R.id.textTeamPlayer)).setText(player.getPlayerName() + suffix);
        ((TextView) vi.findViewById(C0252R.id.textTeamPlayer)).setTag(player.getPlayerName());
        ImageView imageView = (ImageView) vi.findViewById(C0252R.id.imageBatsman);
        int i = (this.batOrBowl == 1 && player.getIsBatsman() == 1) ? 0 : 8;
        imageView.setVisibility(i);
        imageView = (ImageView) vi.findViewById(C0252R.id.imageBowler);
        i = (this.batOrBowl == 1 && player.getIsBowler() == 1) ? 0 : 8;
        imageView.setVisibility(i);
        ImageView bowlArm = (ImageView) vi.findViewById(C0252R.id.imageBowlArm);
        ImageView whatBowler = (ImageView) vi.findViewById(C0252R.id.imageWhatBowler);
        int i2 = (this.batOrBowl == 2 && player.getIsBowler() == 1) ? 0 : 8;
        bowlArm.setVisibility(i2);
        i2 = (this.batOrBowl == 2 && player.getIsBowler() == 1) ? 0 : 8;
        whatBowler.setVisibility(i2);
        if (player.getIsBowler() == 1) {
            bowlArm.setImageResource(player.getHandedBowl() == 0 ? C0252R.drawable.rh_16 : C0252R.drawable.lh_16);
            whatBowler.setImageResource(player.getWhatBowler() == 0 ? C0252R.drawable.pace_16 : C0252R.drawable.spin_16);
        }
        return vi;
    }
}
