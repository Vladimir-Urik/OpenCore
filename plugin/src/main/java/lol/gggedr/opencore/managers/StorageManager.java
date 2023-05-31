package lol.gggedr.opencore.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import lol.gggedr.opencore.api.managers.Manager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.util.function.Consumer;

public class StorageManager implements Manager {

    private HikariPool pool;

    @Override
    public void onEnable() {
        YamlConfiguration configuration = getConfiguration("storage.yml");

        HikariConfig config = new HikariConfig();
        switch (configuration.getString("type").toLowerCase()) {
            case "mysql":
                config.setJdbcUrl("jdbc:mysql://" + configuration.getString("host") + ":" + configuration.getInt("port") + "/" + configuration.getString("database"));
                config.setUsername(configuration.getString("username"));
                config.setPassword(configuration.getString("password"));
                config.setDriverClassName("com.mysql.jdbc.Driver");
                config.setMaximumPoolSize(6);
                break;
            case "sqlite":
                config.setJdbcUrl("jdbc:sqlite:" + configuration.getString("database"));
                config.setDriverClassName("org.sqlite.JDBC");
                break;
            case "postgresql":
                config.setJdbcUrl("jdbc:postgresql://" + configuration.getString("host") + ":" + configuration.getInt("port") + "/" + configuration.getString("database"));
                config.setUsername(configuration.getString("username"));
                config.setPassword(configuration.getString("password"));
                config.setDriverClassName("org.postgresql.Driver");
                config.setMaximumPoolSize(6);
            default:
                throw new IllegalArgumentException("Invalid storage type");
        }

        pool = new HikariPool(config);
    }

    public HikariPool getPool() {
        return pool;
    }

    public void useConnection(Consumer<Connection> consumer) {
        try (Connection connection = pool.getConnection()) {
            consumer.accept(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String table, String... columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" (");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]);
            if (i != columns.length - 1) {
                builder.append(", ");
            }
        }
        builder.append(");");
        useConnection(connection -> {
            try {
                connection.prepareStatement(builder.toString()).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void dropTable(String table) {
        useConnection(connection -> {
            try {
                connection.prepareStatement("DROP TABLE IF EXISTS " + table + ";").execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void update(String query) {
        useConnection(connection -> {
            try {
                connection.prepareStatement(query).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
