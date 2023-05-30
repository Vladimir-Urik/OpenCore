package lol.gggedr.opencore.api.cons;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ActionCtx<T> {

    private final T executor;
    private final String identifier;
    private final String data;

    public ActionCtx(T executor, String identifier, String data) {
        this.executor = executor;
        this.identifier = identifier;
        this.data = data;
    }

    public ActionCtx<Player> asPlayer() {
        return new ActionCtx<>((Player) executor, identifier, data);
    }

    public ActionCtx<CommandSender> asSender() {
        return new ActionCtx<>((CommandSender) executor, identifier, data);
    }

    public ActionCtx<ConsoleCommandSender> asConsole() {
        return new ActionCtx<>((ConsoleCommandSender) executor, identifier, data);
    }

    public boolean isPlayer() {
        return executor instanceof Player;
    }

    public boolean isSender() {
        return executor instanceof CommandSender;
    }

    public boolean isConsole() {
        return executor instanceof ConsoleCommandSender;
    }

    public T getObject() {
        return executor;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getData() {
        return data;
    }

    public void sendMessage(String message) {
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);

        if (isPlayer()) {
            asPlayer().getObject().sendMessage(coloredMessage);
            return;
        }

        if (isSender()) {
            asSender().getObject().sendMessage(coloredMessage);
            return;
        }

        if (isConsole()) {
            asConsole().getObject().sendMessage(coloredMessage);
            return;
        }

        throw new RuntimeException("Unknown executor type");
    }

}
