package fr.robotv2.roboteffect.listeners;

import fr.robotv2.roboteffect.Effect;
import fr.robotv2.roboteffect.RobotEffect;
import fr.robotv2.roboteffect.data.PlayerData;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public class PlayerQuitListener implements Listener {

    private final RobotEffect instance;
    public PlayerQuitListener(RobotEffect instance) {
        this.instance = instance;
    }

    @EventHandler @SneakyThrows
    public void onQuit(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final PlayerData data = new PlayerData();
        final HashSet<String> effects = new HashSet<>();

        for(Effect effect : this.instance.getEffectManager().getActive(player)) {
            effect.deactivatePlayer(player);
            effects.add(effect.getName());
        }

        data.setUuid(player.getUniqueId());
        data.setEffects(effects);

        instance.getDatabase().getDao().createOrUpdate(data);
    }
}
