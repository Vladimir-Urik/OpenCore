package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.api.managers.Manager;

public class ConfigurationManager implements Manager {

    @Override
    public void onEnable() {
        getPlugin().getDataFolder().mkdirs();
    }

}
