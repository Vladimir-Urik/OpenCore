package lol.gggedr.opencore.modules.commands.listeners;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.cons.ActionCtx;
import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.api.managers.impl.IModulesManager;
import lol.gggedr.opencore.api.modules.commands.cons.OpenCommand;
import lol.gggedr.opencore.managers.ActionsManager;
import lol.gggedr.opencore.managers.ConfigurationManager;
import lol.gggedr.opencore.modules.commands.CommandsModule;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandsListener implements Listener {

    @EventHandler
    public void onCommandExecuted(PlayerCommandPreprocessEvent event) {
        String commandName = event.getMessage()
                .split(" ")[0]
                .replace("/", "");

        OpenCommand command = Managers
                .getManager(IModulesManager.class)
                .getModule(CommandsModule.class)
                .getCommand(commandName);

        if (command == null) {
            return;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();

        if(command.getRequiredPermission() != null && command.getRequiredPermission().equalsIgnoreCase("")) {
            if(!player.hasPermission(command.getRequiredPermission())) {
                YamlConfiguration generalMessages = Managers.getManager(ConfigurationManager.class).getMessages("general.yml");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', generalMessages.getString("no-permissions")));
                return;
            }
        }

        List<String> actions = command.getActions();
        ActionsManager manager = Managers.getManager(ActionsManager.class);
        for (String action: actions) {
            Action actionObject = manager.getAction(action);
            if (actionObject == null) {
                continue;
            }

            String arguments = event.getMessage().replace("/" + commandName + " ", "");
            ActionCtx<Player> ctx = new ActionCtx<>(player, action, arguments);

            actionObject.execute(ctx);
        }
    }

}
