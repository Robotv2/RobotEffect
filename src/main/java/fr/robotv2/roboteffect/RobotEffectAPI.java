package fr.robotv2.roboteffect;

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
    public static Collection<Effect> getRegistered() {
        return INSTANCE.getEffectManager().getRegisteredEffects();
    }

    @UnmodifiableView
    public static Collection<Effect> getActive(Player player) {
        return INSTANCE.getEffectManager().getActive(player);
    }

    @Nullable
    @Contract("null -> null")
    public static Effect fromId(@Nullable String id) {
        return INSTANCE.getEffectManager().fromId(id);
    }

    @NotNull
    public static Set<Player> getPlayersWith(@NotNull Effect effect) {
        return Effect.getPlayers(effect).stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}
