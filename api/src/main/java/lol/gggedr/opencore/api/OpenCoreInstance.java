package lol.gggedr.opencore.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class OpenCoreInstance extends JavaPlugin {

    public static OpenCoreInstance getInstance() {
        return (OpenCoreInstance) Bukkit.getPluginManager().getPlugin("OpenCore");
    }

}
