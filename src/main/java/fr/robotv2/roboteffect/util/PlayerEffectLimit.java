package fr.robotv2.roboteffect.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerEffectLimit {

    public static int DEFAULT_LIMIT = 0;

    public static int getLimit(Player player) {

        int limit = DEFAULT_LIMIT;
        final String prefix = "roboteffect.limit.";

        for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

            if(!permission.getPermission().startsWith(prefix)) {
                continue;
            }

            final String temp = permission.getPermission().substring(prefix.length());

            try {
                final int current = Integer.parseInt(temp);
                if(current > limit) limit = current;
            } catch (NumberFormatException ignored) {}
        }

        return limit;
    }

}
