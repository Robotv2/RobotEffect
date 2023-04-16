package fr.robotv2.roboteffect;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.robotv2.roboteffect.util.PlayerEffectLimit;
import fr.robotv2.roboteffect.worldguard.WorldGuardHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"roboteffect", "re", "effect"})
public class EffectCommand {

    private final RobotEffect instance;

    public EffectCommand(RobotEffect instance) {
        this.instance = instance;
    }

    @Subcommand("reload")
    @CommandPermission("roboteffect.command.reload")
    public void onReload(BukkitCommandActor actor) {
        this.instance.onReload();
        actor.reply(ChatColor.GREEN + "The plugin has been reloaded successfully.");
    }

    public void onTargetOther(CommandSender sender, Player target, Effect effect) {


        if(!sender.hasPermission("roboteffect.command.others")) {
            instance.sendMessagePath(sender, "permission-required");
            return;
        }

        if(effect == null) {
            instance.sendMessagePath(sender, "dont-exist");
            return;
        }

        if(Effect.hasEffect(target, effect)) {
            effect.deactivatePlayer(target);
        } else {
            effect.activatePlayer(target);
        }
    }

    @Default
    @AutoComplete("@effects")
    public void onEffect(BukkitCommandActor actor, Effect effect, @Optional Player target) {

        if(target != null) {
            this.onTargetOther(actor.getSender(), target, effect);
            return;
        }

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

            final int limit = PlayerEffectLimit.getLimit(player);

            if(instance.getEffectManager().getActive(player).size() + 1 > limit) {
                instance.sendMessagePath(
                        player,
                        "limit-reached",
                        message -> message.replace("%limit%", String.valueOf(limit))
                );
                return;
            }

            effect.activatePlayer(player);
            instance.sendMessagePath(player, "add-effect");
        }
    }
}
