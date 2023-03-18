package fr.robotv2.roboteffect.listeners;

import fr.robotv2.roboteffect.Effect;
import fr.robotv2.roboteffect.RobotEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Set;

public class PlayerConsumeListener implements Listener {

    private final RobotEffect instance;
    public PlayerConsumeListener(RobotEffect instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {

        if(event.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }

        final Player player = event.getPlayer();
        final Set<Effect> effects = this.instance.getEffectManager().getActive(player);

        effects.forEach(effect -> effect.deactivatePlayer(player));
        Bukkit.getScheduler().runTaskLater(instance, () -> effects.forEach(effect -> effect.activatePlayer(player)), 2);
    }
}
