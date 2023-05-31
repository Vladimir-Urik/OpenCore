package lol.gggedr.opencore.actions;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.cons.ActionCtx;
import lol.gggedr.opencore.lists.OpenList;
import org.bukkit.Bukkit;

import java.util.ArrayList;


public class ConsoleAction implements Action {

    @Override
    public String getIdentifier() {
        return "console";
    }

    @Override
    public void execute(ActionCtx<?> ctx) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ctx.getData());
    }


}
