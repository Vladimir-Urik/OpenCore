package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.managers.impl.IActionsManager;
import lol.gggedr.opencore.lists.OpenList;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ActionsManager implements IActionsManager {

    private final OpenList<Action> actions = new OpenList<>();

    @Override
    public void onEnable() {
        Reflections reflections = new Reflections("lol.gggedr.opencore.actions", Scanners.SubTypes);
        reflections.getSubTypesOf(Action.class).forEach(action -> {
            try {
                this.registerAction(action.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void registerAction(Action action) {
        Action existing = this.getAction(action.getIdentifier());

        if(existing != null) {
            throw new RuntimeException("Action with identifier " + action.getIdentifier() + " already exists.");
        }

        this.actions.add(action);
    }

    @Override
    public void unregisterAction(Action action) {
        this.unregisterAction(action.getIdentifier());
    }

    @Override
    public void unregisterAction(String identifier) {
        Action existing = this.getAction(identifier);
        this.actions.remove(existing);
    }

    @Override
    public Action getAction(String identifier) {
        return this.actions.stream()
                .filter(action -> action.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }

}
