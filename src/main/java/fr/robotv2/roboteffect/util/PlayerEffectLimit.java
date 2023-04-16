package fr.robotv2.roboteffect.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerEffectLimit {

    private static String PERMISSION_PREFIX = "roboteffect.limit.";
    public static int DEFAULT_LIMIT = 0;

    public static int getLimit(Player player) {

        int limit = DEFAULT_LIMIT;

        for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

            if(!permission.getPermission().startsWith(PERMISSION_PREFIX)) {
                continue;
            }

            final String temp = permission.getPermission().substring(PERMISSION_PREFIX.length());

            try {
                final int current = Integer.parseInt(temp);
                if(current > limit) limit = current;
            } catch (NumberFormatException ignored) {}
        }

        return limit;
    }

}
