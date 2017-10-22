package com.ganapathy.cricscorer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditTeamPlayersListAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private boolean canDelete;
    private boolean canEdit;
    private boolean canSelect;
    private dtoTeamPlayerList data;

    public EditTeamPlayersListAdapter(Activity a, dtoTeamPlayerList d, boolean canEdit, boolean canDelete, boolean canSelect) {
        this.canEdit = false;
        this.canDelete = false;
        this.canSelect = false;
        this.activity = a;
        this.data = d;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
        this.canSelect = canSelect;
        inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
    }

    public EditTeamPlayersListAdapter(Activity a, dtoTeamPlayerList d) {
        this(a, d, false, false, false);
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

    public dtoTeamPlayer getTeamPlayer(int position) {
        if (this.data == null) {
            return null;
        }
        return (dtoTeamPlayer) this.data.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(C0252R.layout.list_team_players_row, parent, false);
        }
        Button deleteButton = (Button) vi.findViewById(C0252R.id.deleteMember);
        if (this.canDelete) {
            deleteButton.setVisibility(0);
        } else {
            deleteButton.setVisibility(8);
        }
        Button editButton = (Button) vi.findViewById(C0252R.id.editMember);
        if (this.canEdit) {
            editButton.setVisibility(0);
        } else {
            editButton.setVisibility(8);
        }
        ImageView checkPlayer = (ImageView) vi.findViewById(C0252R.id.checkPlayer);
        if (this.canSelect) {
            checkPlayer.setVisibility(0);
        } else {
            checkPlayer.setVisibility(8);
        }
        dtoTeamPlayer player = (dtoTeamPlayer) this.data.get(position);
        if (this.canSelect) {
            ((ImageView) vi.findViewById(C0252R.id.checkPlayer)).setBackgroundResource(player.getIsChecked() ? C0252R.drawable.tick_ok : C0252R.drawable.blank_24);
        }
        ((TextView) vi.findViewById(C0252R.id.textTeamPlayer)).setText((player.getPlayerOrder() + 1) + "." + player.getPlayerName() + " (" + (player.getHandedBat() == 0 ? vi.getContext().getResources().getString(C0252R.string.RHBtext) : vi.getContext().getResources().getString(C0252R.string.LHBtext)) + ")");
        ((TextView) vi.findViewById(C0252R.id.textTeamPlayer)).setTag(player.getPlayerName());
        ((ImageView) vi.findViewById(C0252R.id.imageBatsman)).setVisibility(player.getIsBatsman() == 1 ? 0 : 8);
        ((ImageView) vi.findViewById(C0252R.id.imageBowler)).setVisibility(player.getIsBowler() == 1 ? 0 : 8);
        ((ImageView) vi.findViewById(C0252R.id.imageKeeper)).setVisibility(player.getIsKeeper() == 1 ? 0 : 8);
        ((ImageView) vi.findViewById(C0252R.id.imageCaptain)).setVisibility(player.getIsCaptain() == 1 ? 0 : 8);
        return vi;
    }
}
