package lol.gggedr.opencore.api.managers.impl;

import lol.gggedr.opencore.api.cons.OpenPlayer;
import lol.gggedr.opencore.api.managers.Manager;

public interface IPlayersManager extends Manager {

    public OpenPlayer getPlayer(String name);

    public void removePlayer(String name);

    public void addPlayer(OpenPlayer player);

}
