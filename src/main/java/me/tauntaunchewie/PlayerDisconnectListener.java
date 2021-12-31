package me.tauntaunchewie;

import me.tauntaunchewie.utils.LifeStealUtils;
import me.tauntaunchewie.utils.UserDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        // Only run when enabled
        if (LifeStealUtils.isEnabled()) {
            // Save user configs
            Player player = event.getPlayer();
            UserDataHandler.saveCurrentPlayerState(player);
        }
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        // Only run when enabled
        if (LifeStealUtils.isEnabled()) {
            // Save user configs
            Player player = event.getPlayer();
            UserDataHandler.saveCurrentPlayerState(player);
        }
    }
}