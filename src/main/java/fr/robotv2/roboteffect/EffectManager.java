package fr.robotv2.roboteffect;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class EffectManager {

    private final RobotEffect instance;
    private final Map<String, Effect> effects = new HashMap<>();

    public EffectManager(RobotEffect instance) {
        this.instance = instance;
    }

    public Collection<Effect> getRegisteredEffects() {
        return Collections.unmodifiableCollection(effects.values());
    }

    public boolean exist(String id) {
        if(id == null) return false;
        return effects.containsKey(id.toLowerCase());
    }

    public Effect fromId(String id) {
        if(id == null) return null;
        return effects.get(id.toLowerCase());
    }

    public Set<Effect> getActive(Player player) {
        return getRegisteredEffects().stream()
                .filter(effect -> Effect.hasEffect(player, effect))
                .collect(Collectors.toSet());
    }

    public void registerEffects() {

        effects.clear();
        final ConfigurationSection section = this.instance.getConfig().getConfigurationSection("effects");

        if(section == null) {
            return;
        }

        for(String name : section.getKeys(false)) {
            final ConfigurationSection effectSection = section.getConfigurationSection(name);
            if(effectSection == null) continue; // Shouldn't happen.
            effects.put(name.toLowerCase(), new Effect(effectSection));
        }
    }
}
