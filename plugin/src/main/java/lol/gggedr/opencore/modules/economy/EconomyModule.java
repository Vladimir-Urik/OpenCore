package lol.gggedr.opencore.modules.economy;

import lol.gggedr.opencore.api.cons.OpenPlayer;
import lol.gggedr.opencore.api.managers.Managers;
import lol.gggedr.opencore.api.managers.impl.IPlayersManager;
import lol.gggedr.opencore.api.modules.economy.IEconomyModule;
import lol.gggedr.opencore.api.modules.economy.cons.Economy;
import lol.gggedr.opencore.lists.OpenList;
import lol.gggedr.opencore.managers.StorageManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class EconomyModule implements IEconomyModule {

    private final List<Economy> economies = OpenList.of();

    @Override
    public void onEnable() {
        YamlConfiguration economiesConfig = getConfiguration("economies.yml");
        economiesConfig.getKeys(false).forEach((name) -> {
            String format = economiesConfig.getString(name + ".format");
            long defaultValue = economiesConfig.getLong(name + ".default");
            long min = economiesConfig.getLong(name + ".min");
            long max = economiesConfig.getLong(name + ".max");
            economies.add(new Economy(name, format, defaultValue, min, max));
        });

        StorageManager storageManager = Managers.getManager(StorageManager.class);
        for (Economy economy : economies) {
            storageManager.createTable("economy_"+economy.getName(), "player VARCHAR(16), amount BIGINT, PRIMARY KEY (player)");
        }
    }

    @Override
    public List<String> getConfigurationFiles() {
        return OpenList.of(
                "economies.yml"
        );
    }

    @Override
    public Economy getEconomy(String name) {
        return economies.stream().filter(economy -> economy.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void addEconomy(String name, String format, long defaultValue, long min, long max) {
        if (economies.stream().anyMatch(economy -> economy.getName().equals(name))) {
            throw new RuntimeException("Economy already exists");
        }

        YamlConfiguration economiesConfig = getConfiguration("economies.yml");
        economiesConfig.set(name + ".format", format);
        economiesConfig.set(name + ".default", defaultValue);
        economiesConfig.set(name + ".min", min);
        economiesConfig.set(name + ".max", max);

        Economy economy = new Economy(name, format, defaultValue, min, max);
        economies.add(economy);

        StorageManager storageManager = Managers.getManager(StorageManager.class);
        storageManager.createTable("economy_"+economy.getName(), "player VARCHAR(16), amount BIGINT, PRIMARY KEY (player)");

        try {
            economiesConfig.save(getConfigurationFile("economies.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEconomy(String name) {
        if (economies.stream().noneMatch(economy -> economy.getName().equals(name))) {
            throw new RuntimeException("Economy not found");
        }

        YamlConfiguration economiesConfig = getConfiguration("economies.yml");
        economiesConfig.set(name, null);

        Economy economy = getEconomy(name);
        if (economy == null) {
            throw new RuntimeException("Economy not found");
        }

        economies.remove(economy);
        StorageManager storageManager = Managers.getManager(StorageManager.class);
        storageManager.dropTable("economy_"+economy.getName());

        try {
            economiesConfig.save(getConfigurationFile("economies.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAmount(String name, String player, long amount) {
        OpenPlayer openPlayer = Managers.getManager(IPlayersManager.class).getPlayer(player);
        if (openPlayer == null) {
            throw new RuntimeException("Player not found or is offline");
        }

        Economy economy = getEconomy(name);
        if (economy == null) {
            throw new RuntimeException("Economy not found");
        }

        long currentAmount = openPlayer.getEconomy().get(name);
        long newAmount = currentAmount + amount;
        if (newAmount < economy.getMin()) {
            newAmount = economy.getMin();
        } else if (newAmount > economy.getMax()) {
            newAmount = economy.getMax();
        }

        boolean hardUpdate = (currentAmount + amount) != newAmount;
        StorageManager storageManager = Managers.getManager(StorageManager.class);

        long finalNewAmount = newAmount;
        if(hardUpdate) {
            storageManager.useConnection(connection -> {
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO economy_"+economy.getName()+" (player, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?")) {
                    ps.setString(1, player);
                    ps.setLong(2, finalNewAmount);
                    ps.setLong(3, finalNewAmount);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            storageManager.useConnection(connection -> {
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO economy_"+economy.getName()+" (player, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = amount + ?")) {
                    ps.setString(1, player);
                    ps.setLong(2, amount);
                    ps.setLong(3, finalNewAmount);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        openPlayer.getEconomy().replace(name, newAmount);
    }

    @Override
    public void setAmount(String name, String player, long amount) {
        OpenPlayer openPlayer = Managers.getManager(IPlayersManager.class).getPlayer(player);
        if (openPlayer == null) {
            throw new RuntimeException("Player not found or is offline");
        }

        Economy economy = getEconomy(name);
        if (economy == null) {
            throw new RuntimeException("Economy not found");
        }

        if (amount < economy.getMin()) {
            amount = economy.getMin();
        } else if (amount > economy.getMax()) {
            amount = economy.getMax();
        }

        StorageManager storageManager = Managers.getManager(StorageManager.class);
        long finalAmount = amount;
        storageManager.useConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO economy_"+economy.getName()+" (player, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?")) {
                ps.setString(1, player);
                ps.setLong(2, finalAmount);
                ps.setLong(3, finalAmount);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        openPlayer.getEconomy().replace(name, amount);
    }

    @Override
    public void removeAmount(String name, String player, long amount) {
        addAmount(name, player, -amount);
    }

    @Override
    public long getAmount(String name, String player) {
        OpenPlayer openPlayer = Managers.getManager(IPlayersManager.class).getPlayer(player);
        if (openPlayer == null) {
            throw new RuntimeException("Player not found or is offline");
        }

        Economy economy = getEconomy(name);
        if (economy == null) {
            throw new RuntimeException("Economy not found");
        }

        return openPlayer.getEconomy().get(name);
    }

    @Override
    public HashMap<String, Long> getAmounts(String player) {
        return null;
    }

    public void loadAll(OpenPlayer player) {
        StorageManager storageManager = Managers.getManager(StorageManager.class);

        HashMap<String, Long> amounts = new HashMap<>();
        for (Economy economy : economies) {
            storageManager.useConnection(connection -> {
                try (PreparedStatement ps = connection.prepareStatement("SELECT amount FROM economy_"+economy.getName()+" WHERE player = ?")) {
                    ps.setString(1, player.getName());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            amounts.put(economy.getName(), rs.getLong("amount"));
                        } else {
                            amounts.put(economy.getName(), economy.getDefaultValue());
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        player.setEconomy(amounts);
    }

}
