package fr.robotv2.roboteffect.listeners;

import fr.robotv2.roboteffect.RobotEffect;
import fr.robotv2.roboteffect.data.PlayerData;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    private final RobotEffect instance;
    public PlayerJoinListener(RobotEffect instance) {
        this.instance = instance;
    }

    @EventHandler @SneakyThrows
    public void onJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final PlayerData data = instance.getDatabase().getDao().queryForId(player.getUniqueId());

        if(data == null || data.getEffects().isEmpty()) {
            return;
        }

        data.getEffects().stream()
                .filter(Objects::nonNull) // Just to be sure
                .map(this.instance.getEffectManager()::fromId) // See if the effect is registered on the server
                .forEach(effect -> effect.activatePlayer(player));
    }
}
