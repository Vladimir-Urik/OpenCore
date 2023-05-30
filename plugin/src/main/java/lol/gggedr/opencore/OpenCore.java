package lol.gggedr.opencore;

import lol.gggedr.opencore.annotations.Depend;
import lol.gggedr.opencore.api.OpenCoreInstance;
import lol.gggedr.opencore.api.managers.Manager;
import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.lists.OpenList;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OpenCore extends OpenCoreInstance {

    @Override
    public void onEnable() {
        loadManagers();
        Managers.onEnable();
    }

    @Override
    public void onDisable() {
        Managers.onDisable();
    }

    private void loadManagers() {
        Reflections reflections = new Reflections("lol.gggedr.opencore.managers", Scanners.SubTypes);
        Set<Class<? extends Manager>> managersToLoad = reflections.getSubTypesOf(Manager.class);

        HashMap<Class<? extends Manager>, List<Class<? extends Manager>>> dependencyTree = new HashMap<>();
        for (Class<? extends Manager> manager : managersToLoad) {
            Depend depend = manager.getAnnotation(Depend.class);
            if (depend == null) {
                dependencyTree.put(manager, OpenList.of());
                continue;
            }

            OpenList<Class<? extends Manager>> dependencies = OpenList.of(depend.value());
            dependencyTree.put(manager, dependencies);
        }

        for (Class<? extends Manager> manager : managersToLoad) {
            List<Class<? extends Manager>> dependencies = dependencyTree.get(manager);
            for (Class<? extends Manager> dependency : dependencies) {
                if (dependencyTree.get(dependency).contains(manager)) {
                    throw new RuntimeException("Circular dependency detected between " + manager.getName() + " and " + dependency.getName());
                }
            }
        }

        // order managers by dependencies
        OpenList<Class<? extends Manager>> orderedManagers = OpenList.of();
        while (orderedManagers.size() < managersToLoad.size()) {
            for (Class<? extends Manager> manager : managersToLoad) {
                if (orderedManagers.contains(manager)) {
                    continue;
                }

                List<Class<? extends Manager>> dependencies = dependencyTree.get(manager);
                boolean allDependenciesLoaded = true;
                for (Class<? extends Manager> dependency : dependencies) {
                    if (!orderedManagers.contains(dependency)) {
                        allDependenciesLoaded = false;
                        break;
                    }
                }

                if (allDependenciesLoaded) {
                    orderedManagers.add(manager);
                }
            }
        }

        for (Class<? extends Manager> manager : orderedManagers) {
            Managers.register(manager);
        }
    }

}
