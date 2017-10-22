package com.ganapathy.cricscorer;

import java.util.ArrayList;
import java.util.Iterator;

public class dtoTeamPlayerList extends ArrayList<dtoTeamPlayer> {
    private static final long serialVersionUID = 2812655837492240263L;

    public void addPlayer(String playerName) {
        dtoTeamPlayer newPlayer = new dtoTeamPlayer();
        newPlayer.setPlayerName(playerName);
        newPlayer.setPlayerOrder(size());
        add(newPlayer);
    }

    public boolean contains(String playerName) {
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((dtoTeamPlayer) it.next()).getPlayerName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    public void updatePlayer(dtoTeamPlayer player) {
        if (size() <= player.getPlayerOrder()) {
            player.setPlayerOrder(Math.max(size() - 1, 0));
        }
        dtoTeamPlayer current = (dtoTeamPlayer) get(Math.max(player.getPlayerOrder(), 0));
        current.setHandedBat(player.getHandedBat());
        current.setHandedBowl(player.getHandedBowl());
        current.setIsBatsman(player.getIsBatsman());
        current.setIsBowler(player.getIsBowler());
        current.setIsKeeper(player.getIsKeeper());
        current.setIsCaptain(player.getIsCaptain());
        current.setPlayerName(player.getPlayerName());
        current.setWhatBowler(player.getWhatBowler());
    }
}
