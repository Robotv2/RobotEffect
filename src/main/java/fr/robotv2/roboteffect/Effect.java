package fr.robotv2.roboteffect;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.robotv2.roboteffect.api.EffectActivateEvent;
import fr.robotv2.roboteffect.api.EffectDeactivateEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class Effect {

    private final static Multimap<Effect, UUID> players = ArrayListMultimap.create();

    public static Collection<UUID> getPlayers(Effect effect) {
        return players.get(effect);
    }

    public static boolean  hasEffect(Player player, Effect effect) {
        return getPlayers(effect).contains(player.getUniqueId());
    }

    private final String name;
    private final PotionEffectType type;
    private final int level;
    private final String permission;

    public Effect(ConfigurationSection section) {
        this.name = section.getName();
        this.type = PotionEffectType.getByName(Objects.requireNonNull(section.getString("potion-effect")).toUpperCase());
        this.level = section.getInt("level", 1) - 1;
        this.permission = section.getString("permission");
    }

    public PotionEffectType getEffectType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getName() {
        return name;
    }

    public PotionEffect getPotionEffect() {
        return new PotionEffect(this.type, Integer.MAX_VALUE, this.level, true, false);
    }

    public void activatePlayer(Player player) {

        if(Effect.hasEffect(player, this)) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new EffectActivateEvent(player, this));

        player.addPotionEffect(this.getPotionEffect());
        players.put(this, player.getUniqueId());
    }

    public void deactivatePlayer(Player player) {

        if(!Effect.hasEffect(player, this)) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new EffectDeactivateEvent(player, this));

        player.removePotionEffect(this.type);
        players.remove(this, player.getUniqueId());
    }

    @Override
    public boolean equals(Object o) {

        if(o == this) {
            return true;
        }

        if(!(o instanceof Effect)) {
            return false;
        }

        return Objects.equals(this.name, ((Effect) o).name);
    }
}
