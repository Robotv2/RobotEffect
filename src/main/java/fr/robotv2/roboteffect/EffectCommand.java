package fr.robotv2.roboteffect;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.robotv2.roboteffect.util.PlayerEffectLimit;
import fr.robotv2.roboteffect.worldguard.WorldGuardHandler;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.bukkit.BukkitCommandActor;

@Command({"roboteffect", "re", "effect"})
public class EffectCommand {

    private final RobotEffect instance;

    public EffectCommand(RobotEffect instance) {
        this.instance = instance;
    }

    @Default
    @AutoComplete("@effects")
    public void onEffect(BukkitCommandActor actor, Effect effect) {

        final Player player = actor.requirePlayer();

        if(effect == null) {

            instance.sendMessagePath(player, "dont-exist");
            return;

        }

        if(Effect.hasEffect(player, effect)) {

            effect.deactivatePlayer(player);
            instance.sendMessagePath(player, "remove-effect");

        } else {

            for(ProtectedRegion region : WorldGuardHandler.getRegions(player)) {
                final StateFlag.State state = region.getFlag(WorldGuardHandler.EFFECTS_ENABLED_FLAG);
                if(state == StateFlag.State.DENY) {
                    instance.sendMessagePath(player, "disabled-region");
                    return;
                }
            }

            if(Effect.hasEffect(player, effect)) {
                instance.sendMessagePath(player, "has-effect");
                return;
            }

            if(effect.getPermission() != null && !player.hasPermission(effect.getPermission())) {
                instance.sendMessagePath(player, "permission-required");
                return;
            }

            if(instance.getEffectManager().getActive(player).size() + 1 > PlayerEffectLimit.getLimit(player)) {
                instance.sendMessagePath(player, "limit-reached");
                return;
            }

            effect.activatePlayer(player);
            instance.sendMessagePath(player, "add-effect");
        }
    }
}
