# RobotEffect

API
The RobotEffect plugin includes a comprehensive API for advanced customization and integration with other plugins. Here are some of the available methods and events:

Methods
getRegisteredEffects(): Returns a collection of all currently registered effects.
getActiveEffects(Player player): Returns a collection of all currently active effects for the specified player.
getEffectFromId(String id): Returns the effect with the specified ID, or null if no effect is found.
hasEffect(Player player, Effect effect): Returns true if the specified player currently has the given effect active.
getPlayersWith(Effect effect): Returns a set of all players currently affected by the specified effect.
Events
The RobotEffect plugin provides two main events for advanced customization of gameplay:

EffectActivateEvent: Fired when a player activates an effect.
getEffect(): Returns the effect that was activated.
EffectDeactivateEvent: Fired when a player deactivates an effect.
getEffect(): Returns the effect that was deactivated.
To access the API, simply import the relevant classes and methods into your plugin's code. Full documentation is provided within the plugin's Javadoc.
