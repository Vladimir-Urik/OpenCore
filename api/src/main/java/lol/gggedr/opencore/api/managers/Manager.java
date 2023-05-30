package lol.gggedr.opencore.api.managers;

import lol.gggedr.opencore.api.OpenCoreInstance;

import java.util.List;

public interface Manager {

    /**
     * This function is called when the manager is enabled
     */
    default void onEnable() {

    }

    /**
     * This function is called when the manager is disabled
     */
    default void onDisable() {

    }

    /**
     * It returns the instance of the plugin
     *
     * @return The instance of the plugin.
     */
    default OpenCoreInstance getPlugin() {
        return OpenCoreInstance.getInstance();
    }

    /**
     * Get the manager of the given class.
     *
     * @param clazz The class of the manager you want to get.
     * @return The manager of the given class.
     */
    default <T extends Manager> T getManager(Class<T> clazz) {
        return Managers.getManager(clazz);
    }

}
