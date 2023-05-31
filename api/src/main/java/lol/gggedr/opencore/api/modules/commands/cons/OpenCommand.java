package lol.gggedr.opencore.api.modules.commands.cons;

import lol.gggedr.opencore.api.cons.ActionCtx;
import lol.gggedr.opencore.api.modules.commands.enums.OnlyFor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class OpenCommand {

    private final String name;
    private String requiredPermission;
    private List<String> actions;

    public OpenCommand(String name, String requiredPermission, List<String> actions) {
        this.name = name;
        this.requiredPermission = requiredPermission;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

}
