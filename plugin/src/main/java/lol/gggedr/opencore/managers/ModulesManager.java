package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.annotations.Depend;
import lol.gggedr.opencore.api.managers.impl.IModulesManager;
import lol.gggedr.opencore.api.modules.Module;
import lol.gggedr.opencore.lists.OpenList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

@Depend({ConfigurationManager.class})
public class ModulesManager implements IModulesManager {

    private final OpenList<Module> modules = new OpenList<>();

    public void onEnable() {
        // TODO: Register modules from package

        this.enableAll();
    }

    public void onDisable() {
        this.disableAll();
    }

    private void enableAll() {
        this.modules.forEach((module -> {
            if(module.getConfigurationFiles().size() > 0) {
                module.getModuleFolder().mkdirs();
                module.copyAllConfiguration();
            }

            module.onEnable();

            Reflections reflections = new Reflections(module.getClass().getPackage().getName() +".listeners", Scanners.SubTypes);
            reflections.getSubTypesOf(Listener.class).forEach(listener -> {
                try {
                    Bukkit.getPluginManager().registerEvents(listener.newInstance(), module.getPlugin());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }));
    }

    private void disableAll() {
        this.modules.forEach(Module::onDisable);
    }

    public Module getModule(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <T extends Module> T getModule(Class<T> clazz) {
        return modules.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void registerModule(Module module) {
        Module existing = this.getModule(module.getName());

        if(existing != null) {
            throw new RuntimeException("Module with name " + module.getName() + " already exists.");
        }

        this.modules.add(module);
    }

    @Override
    public void unregisterModule(Module module) {
        this.unregisterModule(module.getName());
    }

    @Override
    public void unregisterModule(String identifier) {
        Module existing = this.getModule(identifier);
        this.modules.remove(existing);
    }
}
