package lol.gggedr.opencore.actions;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.cons.ActionCtx;
import lol.gggedr.opencore.lists.OpenList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class CommandAction implements Action {

    @Override
    public String getIdentifier() {
        return "command";
    }

    @Override
    public ArrayList<String> getAliases() {
        return OpenList.of("cmd");
    }

    @Override
    public void execute(ActionCtx<Object> ctx) {
        if(!ctx.isPlayer() && !ctx.isConsole()) {
            throw new RuntimeException("Command action can only be executed by a player or console.");
        }

        if(ctx.isPlayer()) {
            Player p = ctx.asPlayer().getObject();
            ctx.asPlayer().getObject().performCommand(PlaceholderAPI.setPlaceholders(p, ctx.getData()));
        } else {
            Bukkit.getServer().dispatchCommand(ctx.asConsole().getObject(), ctx.getData());
        }
    }


}
