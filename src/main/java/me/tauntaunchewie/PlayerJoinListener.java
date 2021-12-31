package me.tauntaunchewie;

import me.tauntaunchewie.utils.LifeStealUtils;
import me.tauntaunchewie.utils.UserDataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Only run when enabled
        if (LifeStealUtils.isEnabled()) {
            Player player = event.getPlayer();

            // Try to create the config file if it doesn't already exist and load into memory
            UserDataHandler udh = new UserDataHandler(event.getPlayer());
        }
    }
}