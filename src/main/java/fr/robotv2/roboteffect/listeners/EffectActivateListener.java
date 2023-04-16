package fr.robotv2.roboteffect.listeners;

import fr.robotv2.roboteffect.RobotEffect;
import fr.robotv2.roboteffect.api.EffectActivateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EffectActivateListener implements Listener {

    private final RobotEffect instance;

    public EffectActivateListener(RobotEffect instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onActivate(EffectActivateEvent event) {

        final Player player = event.getPlayer();

        this.instance.getEffectManager().getActive(player).stream()
                .filter(active -> active.getEffectType() == event.getEffect().getEffectType())
                .forEach(active -> active.deactivatePlayer(player));
    }
}
