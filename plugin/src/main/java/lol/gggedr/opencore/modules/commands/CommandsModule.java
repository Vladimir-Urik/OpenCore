package lol.gggedr.opencore.modules.commands;

import lol.gggedr.opencore.api.modules.commands.ICommandsModule;
import lol.gggedr.opencore.api.modules.commands.cons.OpenCommand;
import lol.gggedr.opencore.lists.OpenList;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandsModule implements ICommandsModule {

    private final OpenList<OpenCommand> commands = new OpenList<>();

    @Override
    public void onEnable() {
        getModuleFolder().mkdir();
        loadAllCommands();
    }

    private void loadAllCommands() {
        OpenList.of(getModuleFolder().listFiles())
                .stream()
                .filter(file -> file.getName().endsWith(".yml"))
                .forEach(file -> {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                    String name = configuration.getString("name");
                    String requiredPermission = configuration.getString("required-permission", null);
                    List<String> actions = configuration.getStringList("actions");

                    registerCommand(new OpenCommand(name, requiredPermission, actions));
                });
    }

    @Override
    public void registerCommand(OpenCommand command) {
        if (getCommand(command.getName()) != null) {
            throw new IllegalArgumentException("Command with name " + command.getName() + " already exists!");
        }

        commands.add(command);
    }

    @Override
    public void unregisterCommand(String name) {
        commands.removeIf(command -> command.getName().equalsIgnoreCase(name));
    }

    @Override
    public OpenCommand getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
