package lol.gggedr.opencore.api.modules.commands;

import lol.gggedr.opencore.api.modules.Module;
import lol.gggedr.opencore.api.modules.commands.cons.OpenCommand;

public interface ICommandsModule extends Module {

    public void registerCommand(OpenCommand command);

    public void unregisterCommand(String name);

    public OpenCommand getCommand(String name);

}
