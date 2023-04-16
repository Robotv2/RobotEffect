package fr.robotv2.roboteffect.api;

import fr.robotv2.roboteffect.Effect;
import fr.robotv2.roboteffect.RobotEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class RobotEffectAPI {

    private final static RobotEffect INSTANCE = RobotEffect.getInstance();

    @UnmodifiableView
    public static Collection<Effect> getRegisteredEffects() {
        return INSTANCE.getEffectManager().getRegisteredEffects();
    }

    @UnmodifiableView
    public static Collection<Effect> getActiveEffects(Player player) {
        return INSTANCE.getEffectManager().getActive(player);
    }

    @Nullable
    @Contract("null -> null")
    public static Effect getEffectFromId(@Nullable String id) {
        return INSTANCE.getEffectManager().fromId(id);
    }

    public static boolean hasEffect(Player player, Effect effect) {
        return Effect.hasEffect(player, effect);
    }

    @NotNull
    public static Set<Player> getPlayersWith(@NotNull Effect effect) {
        return Effect.getPlayers(effect).stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}
