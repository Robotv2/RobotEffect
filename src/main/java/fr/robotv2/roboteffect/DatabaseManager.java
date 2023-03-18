package fr.robotv2.roboteffect;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.robotv2.roboteffect.data.PlayerData;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    public enum DatabaseType {
        MYSQL,
        SQLLITE,
    }

    private final RobotEffect instance;
    private ConnectionSource source;
    private Dao<PlayerData, UUID> dao;

    public DatabaseManager(RobotEffect instance) {
        this.instance = instance;
    }

    public Dao<PlayerData, UUID> getDao() {
        return this.dao;
    }

    public void connect(DatabaseType type) throws IOException, SQLException {

        if(source != null) {
            closeConnection();
        }

        ConnectionSource connectionSource;

        switch (type) {

            case SQLLITE:
                final File file = new File(instance.getDataFolder(), "database.db");
                if(!file.exists()) file.createNewFile();
                connectionSource = new JdbcConnectionSource("jdbc:sqlite:".concat(file.getPath()));
                break;

            case MYSQL:
                final String host = instance.getConfig().getString("storage.mysql-credentials.host");
                final String port = instance.getConfig().getString("storage.mysql-credentials.port");
                final String database = instance.getConfig().getString("storage.mysql-credentials.database");
                final String username = instance.getConfig().getString("storage.mysql-credentials.username");
                final String password = instance.getConfig().getString("storage.mysql-credentials.password");
                final boolean ssl = instance.getConfig().getBoolean("storage.mysql-credentials.useSSL", false);
                connectionSource = new JdbcConnectionSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl, username, password);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        this.source = connectionSource;
    }

    public void closeConnection() {
        if(source != null) {
            source.closeQuietly();
            source = null;
        }
    }

    public void initialize() throws SQLException {

        if(source == null) {
            throw new NullPointerException("source");
        }

        this.dao = DaoManager.createDao(source, PlayerData.class);
        TableUtils.createTableIfNotExists(source, PlayerData.class);
    }
}
