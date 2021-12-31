package me.tauntaunchewie;

import me.tauntaunchewie.utils.LifeStealUtils;
import me.tauntaunchewie.utils.UserDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Only run when enabled
        if (LifeStealUtils.isEnabled()) {
            // On death decrease heart count by one.
            Player player = event.getEntity();

            // Get killer entity
            Player killer = player.getKiller();

            // Make sure killer is a player
            if (killer instanceof Player) {
                // Increase killer's hearts
                UserDataHandler.addHeartToPlayer(killer, false);

                // Remove heart from current user
                UserDataHandler.removeHeartFromPlayer(player, true);
            }
        }
    }
}