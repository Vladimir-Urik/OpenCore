package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.api.managers.Manager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigurationManager implements Manager {

    @Override
    public void onEnable() {
        getPlugin().getDataFolder().mkdirs();
        getMessagesDirectory().mkdirs();
    }

    public File getMessagesDirectory() {
        return new File(getPlugin().getDataFolder(), "messages");
    }

    public YamlConfiguration getMessages(String name) {
        File file = new File(getMessagesDirectory(), name + ".yml");
        if (!file.exists()) {
            getPlugin().saveResource("messages/" + name + ".yml", false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
