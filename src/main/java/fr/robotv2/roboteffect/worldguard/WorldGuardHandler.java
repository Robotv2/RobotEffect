package fr.robotv2.roboteffect.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import fr.robotv2.roboteffect.Effect;
import fr.robotv2.roboteffect.RobotEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;

public class WorldGuardHandler extends Handler {

    public static final Factory FACTORY = new Factory();
    public static StateFlag EFFECTS_ENABLED_FLAG;

    public static class Factory extends Handler.Factory<WorldGuardHandler> {
        @Override
        public WorldGuardHandler create(Session session) {
            return new WorldGuardHandler(session);
        }
    }

    /**
     * Create a new handler.
     *
     * @param session The session
     */
    protected WorldGuardHandler(Session session) {
        super(session);
    }

    public static void register() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            StateFlag flag = new StateFlag("effects-enabled", true);
            registry.register(flag);
            EFFECTS_ENABLED_FLAG = flag;
        } catch (FlagConflictException e) {
            e.printStackTrace();
        }
    }

    public static Set<ProtectedRegion> getRegions(Player player) {
        final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        final ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        return set.getRegions();
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer localPlayer, Location from, Location to, ApplicableRegionSet toSet,
                                   Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {

        if(!toSet.testState(localPlayer, EFFECTS_ENABLED_FLAG)) {

            final Player player = Objects.requireNonNull(Bukkit.getPlayer(localPlayer.getUniqueId()));
            final Set<Effect> effects = RobotEffect.getInstance().getEffectManager().getActive(player);

            if(!effects.isEmpty()) {
                effects.forEach(effect -> effect.deactivatePlayer(player));
                RobotEffect.getInstance().sendMessagePath(player, "entered-disabled-region");
            }
        }

        return true;
    }
}
