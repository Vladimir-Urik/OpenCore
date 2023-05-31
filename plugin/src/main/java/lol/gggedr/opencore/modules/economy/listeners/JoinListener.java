package lol.gggedr.opencore.modules.economy.listeners;

import lol.gggedr.opencore.OpenCore;
import lol.gggedr.opencore.api.cons.OpenPlayer;
import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.api.managers.impl.IModulesManager;
import lol.gggedr.opencore.managers.PlayersManager;
import lol.gggedr.opencore.modules.economy.EconomyModule;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    public void onJoin(PlayerJoinEvent e) {
        OpenPlayer openPlayer = Managers.getManager(PlayersManager.class)
                .getPlayerOrNewObject(e.getPlayer().getName());

        Bukkit.getScheduler().runTaskAsynchronously(OpenCore.getInstance(), () -> {
            Managers.getManager(IModulesManager.class)
                    .getModule(EconomyModule.class)
                    .loadAll(openPlayer);

            openPlayer.addLoadedData("economy");
        });
    }

}
