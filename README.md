# RobotEffect

## API Reference

#### Get all Registered Effects.

```java
  Collection<Effect> effects = RobotEffectAPI.getRegisteredEffects();
```
Returns an unmodifiable collection of registered effects.


#### Get all active effects of player.

```java
  Collection<Effect> effects = RobotEffectAPI.getActiveEffects(player);
```
Returns an unmodifiable collection of active effects for a specified player.


#### Get an effect from an id.

```java
  Effect effect = RobotEffectAPI.getEffectFromId("example-effect");
```
Returns the effect object with the given ID if it exists, otherwise returns null.


#### Check if a player has an effect active.

```java
  boolean hasEffect = RobotEffectAPI.hasEffect(player, effect);
```
Returns true if the player has the specified effect active, otherwise returns false.


#### Get all players with a certain effect activated.

```java
  Set<Player> players = RobotEffectAPI.getPlayersWith(effect);
```
Returns a set of players who have the specified effect active.
