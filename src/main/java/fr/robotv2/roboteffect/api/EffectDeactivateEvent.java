package fr.robotv2.roboteffect.api;

import fr.robotv2.roboteffect.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EffectDeactivateEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Effect effect;

    public EffectDeactivateEvent(@NotNull Player who, Effect effect) {
        super(who);
        this.effect = effect;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Effect getEffect() {
        return effect;
    }
}
