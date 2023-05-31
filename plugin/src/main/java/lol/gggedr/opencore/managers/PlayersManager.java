package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.api.cons.OpenPlayer;
import lol.gggedr.opencore.api.managers.impl.IPlayersManager;
import lol.gggedr.opencore.lists.OpenList;

import java.util.HashMap;
import java.util.List;

public class PlayersManager implements IPlayersManager {

    private final List<OpenPlayer> players = OpenList.of();

    @Override
    public OpenPlayer getPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    public OpenPlayer getPlayerOrNewObject(String name) {
        OpenPlayer player = getPlayer(name);

        if(player == null) {
            player = new OpenPlayer(name);
            addPlayer(player);
        }

        return player;
    }

    @Override
    public void removePlayer(String name) {
        players.removeIf(player -> player.getName().equals(name));
    }

    @Override
    public void addPlayer(OpenPlayer player) {
        if(players.stream().anyMatch(player1 -> player1.getName().equals(player.getName()))) {
            throw new RuntimeException("Player already exists");
        }

        players.add(player);
    }

}
