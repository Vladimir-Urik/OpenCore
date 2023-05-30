package lol.gggedr.opencore.api.modules.chat.cons;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class ChatFormat {

    private final String group;
    private final String format;

    public ChatFormat(String group, String format) {
        this.group = group;
        this.format = format;
    }

    public String getGroup() {
        return group;
    }

    public String getFormat() {
        return format;
    }

    public String useFormat(Player player, String message) {
        if(player.hasPermission("opencore.chat.color")) {
            message = message.replace("&", "§");
        } else {
            message = message
                    .replace("&", "")
                    .replace("§", "");
        }

        return PlaceholderAPI
                .setPlaceholders(player, format)
                .replace("%message%", message);
    }

}
