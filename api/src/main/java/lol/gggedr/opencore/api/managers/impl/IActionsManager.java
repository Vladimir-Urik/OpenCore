package lol.gggedr.opencore.api.managers.impl;

import lol.gggedr.opencore.api.actions.Action;
import lol.gggedr.opencore.api.managers.Manager;

public interface IActionsManager extends Manager {

    public void registerAction(Action action);

    public void unregisterAction(Action action);

    public void unregisterAction(String identifier);

    public Action getAction(String identifier);
}
