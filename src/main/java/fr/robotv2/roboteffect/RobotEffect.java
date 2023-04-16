package fr.robotv2.roboteffect;

import com.sk89q.worldguard.WorldGuard;
import fr.robotv2.roboteffect.data.PlayerData;
import fr.robotv2.roboteffect.listeners.EffectActivateListener;
import fr.robotv2.roboteffect.listeners.PlayerConsumeListener;
import fr.robotv2.roboteffect.listeners.PlayerJoinListener;
import fr.robotv2.roboteffect.listeners.PlayerQuitListener;
import fr.robotv2.roboteffect.util.StringPlaceholder;
import fr.robotv2.roboteffect.worldguard.WorldGuardHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class RobotEffect extends JavaPlugin {

    private BukkitCommandHandler commandHandler;
    private EffectManager effectManager;
    private DatabaseManager database;

    private YamlConfiguration messages;

    public static RobotEffect getInstance() {
        return JavaPlugin.getPlugin(RobotEffect.class);
    }

    @Override
    public void onLoad() {
        WorldGuardHandler.register();
    }

    @Override
    public void onEnable() {

        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        saveDefaultConfig();

        this.database = new DatabaseManager(this);
        final DatabaseManager.DatabaseType type = getConfig().get("storage.mode") != null
                ? DatabaseManager.DatabaseType.valueOf(getConfig().getString("storage.mode").toUpperCase())
                : DatabaseManager.DatabaseType.SQLLITE; // Default storage mode

        try {
            database.connect(type);
            database.initialize();
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }

        this.effectManager = new EffectManager(this);
        this.effectManager.registerEffects();

        //listener
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new PlayerConsumeListener(this), this);
        pm.registerEvents(new EffectActivateListener(this), this);

        //command
        commandHandler = BukkitCommandHandler.create(this);
        commandHandler.registerValueResolver(Effect.class, context -> getEffectManager().fromId(context.pop()));
        commandHandler.getAutoCompleter().registerSuggestion("effects", this.effectManager.getRegisteredEffects()
                .stream()
                .map(Effect::getName)
                .collect(Collectors.toSet())
        );
        commandHandler.register(new EffectCommand(this));

        //messages
        final String filePath = "messages.yml";
        final File file = new File(getDataFolder(), filePath);

        if(!file.exists()) {
            this.saveResource(filePath, false);
        }

        this.messages = YamlConfiguration.loadConfiguration(file);
        InputStream defaultStream = this.getResource(filePath);

        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.messages.setDefaults(defaultConfig);
        }

        //worldguard
        if (!WorldGuard.getInstance().getPlatform().getSessionManager().registerHandler(WorldGuardHandler.FACTORY, null)) {
            getLogger().severe("Could not register worldguard entry handler !");
            getLogger().severe("All Worldguard related features will not work.");
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(this::save);
        this.database.closeConnection();
    }

    public void onReload() {
        this.reloadConfig();
        this.getEffectManager().registerEffects();
        commandHandler.getAutoCompleter().registerSuggestion("effects", this.effectManager.getRegisteredEffects()
                .stream()
                .map(Effect::getName)
                .collect(Collectors.toSet())
        );
    }

    public EffectManager getEffectManager() {
        return this.effectManager;
    }

    public DatabaseManager getDatabase() {
        return this.database;
    }

    public YamlConfiguration getMessages() {
        return this.messages;
    }

    public void sendMessagePath(Player player, String path, StringPlaceholder... placeholders) {
        final String prefix = ChatColor.translateAlternateColorCodes('&', getMessages().getString("prefix", "&8"));

        String message = ChatColor.translateAlternateColorCodes('&', getMessages().getString(path, "&8"));
        for(StringPlaceholder placeholder : placeholders) {
           message = placeholder.apply(message);
        }

        player.sendMessage(prefix.concat(message));
    }

    public void save(Player player) {
        final PlayerData data = new PlayerData();
        final HashSet<String> effects = new HashSet<>();

        for(Effect effect : getEffectManager().getActive(player)) {
            effects.add(effect.getName());
        }

        data.setUuid(player.getUniqueId());
        data.setEffects(effects);

        try {
            getDatabase().getDao().createOrUpdate(data);
        } catch (SQLException e) {
            getLogger().severe("An error occurred while saving data for " + player.getName());
            getLogger().severe("Error's message: " + e.getMessage());
        }
    }
}
