package lol.gggedr.opencore.api.modules.chat;

import lol.gggedr.opencore.api.modules.Module;
import lol.gggedr.opencore.api.modules.chat.cons.ChatFormat;
import org.bukkit.entity.Player;

public interface IChatModule extends Module {

    public void registerFormat(ChatFormat format);

    public void unregisterFormat(ChatFormat format);

    public void unregisterFormat(String group);

    public ChatFormat getFormat(String group);

    public ChatFormat getFormatForPlayer(Player player);

}
