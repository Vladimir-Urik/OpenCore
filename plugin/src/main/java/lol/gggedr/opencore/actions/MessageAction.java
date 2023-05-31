package lol.gggedr.opencore.actions;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.cons.ActionCtx;
import lol.gggedr.opencore.lists.OpenList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class MessageAction implements Action {

    @Override
    public String getIdentifier() {
        return "message";
    }

    @Override
    public ArrayList<String> getAliases() {
        return OpenList.of("msg");
    }

    @Override
    public void execute(ActionCtx<?> ctx) {
        if(ctx.isPlayer()) {
            Player p = ctx.asPlayer().getObject();
            ctx.sendMessage(PlaceholderAPI.setPlaceholders(p, ctx.getData()));
            return;
        }

        ctx.sendMessage(ctx.getData());
    }

}
