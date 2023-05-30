package lol.gggedr.opencore.api.modules;

import lol.gggedr.opencore.api.OpenCoreInstance;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface Module {

    default void onEnable() {}

    default void onDisable() {}

    default List<String> getConfigurationFiles() {
        return new ArrayList<>();
    }

    default OpenCoreInstance getPlugin() {
        return OpenCoreInstance.getInstance();
    }

    default String getName() {
        return getClass().getSimpleName()
                .replace("Module", "")
                .toLowerCase();
    }

    default File getModuleFolder() {
        return new File(getPlugin().getDataFolder() + File.separator + "modules" + File.separator + getName());
    }

    default YamlConfiguration getConfiguration(String name) {
        File file = new File(getModuleFolder() + File.separator + name);
        if (!file.exists()) {
            getPlugin().saveResource("modules" + File.separator + getName() + File.separator + name, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    default void copyAllConfiguration() {
        getConfigurationFiles().forEach(name -> {
            File file = new File(getModuleFolder() + File.separator + name);
            if (!file.exists()) {
                getPlugin().saveResource("modules" + File.separator + getName() + File.separator + name, false);
            }
        });
    }

}
