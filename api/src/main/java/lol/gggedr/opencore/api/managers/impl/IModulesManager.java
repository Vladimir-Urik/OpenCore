package lol.gggedr.opencore.api.managers.impl;

import lol.gggedr.opencore.api.managers.Manager;
import lol.gggedr.opencore.api.modules.Module;

public interface IModulesManager extends Manager {

    public <T extends Module> T getModule(Class<T> clazz);

    public void registerModule(Module module);

    public void unregisterModule(Module module);

    public void unregisterModule(String identifier);

}
