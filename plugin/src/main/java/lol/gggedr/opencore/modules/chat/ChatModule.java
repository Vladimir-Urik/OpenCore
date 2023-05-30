package lol.gggedr.opencore.modules.chat;

import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.api.managers.impl.IRankProvidersManager;
import lol.gggedr.opencore.api.modules.chat.IChatModule;
import lol.gggedr.opencore.api.modules.chat.cons.ChatFormat;
import lol.gggedr.opencore.lists.OpenList;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatModule implements IChatModule {

    private final OpenList<ChatFormat> formats = new OpenList<>();

    @Override
    public List<String> getConfigurationFiles() {
        return OpenList.of(
                "formats.yml"
        );
    }

    @Override
    public void onEnable() {
        YamlConfiguration configuration = this.getConfiguration("formats.yml");
        configuration.getKeys(false).forEach(key -> {
            String format = configuration.getString(key);
            this.registerFormat(new ChatFormat(key, format));
        });
    }

    @Override
    public void registerFormat(ChatFormat format) {
        formats.add(format);
    }

    @Override
    public void unregisterFormat(ChatFormat format) {
        this.unregisterFormat(format.getGroup());
    }

    @Override
    public void unregisterFormat(String group) {
        ChatFormat f = this.getFormat(group);
        if (f != null) {
            formats.remove(f);
        }
    }

    @Override
    public ChatFormat getFormat(String group) {
        return formats.stream()
                .filter(format -> format.getGroup().equalsIgnoreCase(group))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ChatFormat getFormatForPlayer(Player player) {
        String rank = Managers.getManager(IRankProvidersManager.class)
                .getProvider()
                .getPlayerRank(player.getName());

        ChatFormat format = this.getFormat(rank);
        if (format == null) {
            format = this.getFormat("default");
        }

        return format;
    }

}
