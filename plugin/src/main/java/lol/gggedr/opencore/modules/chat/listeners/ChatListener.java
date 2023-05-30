package lol.gggedr.opencore.modules.chat.listeners;

import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.api.managers.impl.IModulesManager;
import lol.gggedr.opencore.api.modules.chat.IChatModule;
import lol.gggedr.opencore.api.modules.chat.cons.ChatFormat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        ChatFormat format = Managers.getManager(IModulesManager.class)
                .getModule(IChatModule.class)
                .getFormatForPlayer(event.getPlayer());

        Bukkit.broadcastMessage(format.useFormat(event.getPlayer(), event.getMessage()));
    }

}
