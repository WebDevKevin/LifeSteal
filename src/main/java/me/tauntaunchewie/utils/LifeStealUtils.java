package me.tauntaunchewie.utils;

import me.tauntaunchewie.LifeStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class LifeStealUtils {

    private static LifeStealPlugin plugin;
    private static boolean stealOn = false;
    private static int defaultHearts = 1;
    private File customConfigFile;
    private FileConfiguration customConfig;

    /**
     * Returns instance of LifeSteal Plugin object
     * @return
     */
    public static LifeStealPlugin getPlugin() {
        return plugin;
    }

    /**
     * Set the default heart count in the config. Only affects new players to the server.
     * @param heartCount
     */
    public static void setDefaultHearts(int heartCount) {
        defaultHearts = heartCount;

        plugin.getConfig().set("defaultHeartAmount", heartCount);
        plugin.saveConfig();
    }

    /**
     * Retrieves the default heart count from the server config
     * @return
     */
    public static int getDefaultHearts() {
        // Note we have to multiply by 2 since Minecraft hearts are by 2's.
        // For symplicity the Op sets this default amount by actual hearts they see so we return double
        return defaultHearts * 2;
    }

    /**
     * Set a specific player's maximum heart count to specific value (used on login)
     * @param player
     * @param maxHearts
     */
    public static void setPlayerMaxHearts(Player player, int maxHearts) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(maxHearts);
    }

    /**
     * Set specific player's current heart count to specific value (used on login)
     * @param player
     * @param currentHearts
     */
    public static void setPlayerCurrentHearts(Player player, int currentHearts) {
        player.setHealth(currentHearts);
    }

    /**
     * Enable the LifeSteal plugin on the server.  Writes to server config as well.
     * @param onOff
     */
    public static void enableLifeSteal(boolean onOff) {
        stealOn = onOff;

        plugin.getConfig().set("enabled", onOff);
        plugin.saveConfig();

        // Send a message to all players
        if (stealOn) {
            Bukkit.broadcastMessage("Attention!  Life Stealing has been enabled.  BEWARE!");
        }
    }

    /**
     * Convenience method to return if the life steal mode is enabled
     * @return
     */
    public static boolean isEnabled() {
        return stealOn;
    }

    /**
     * Grab defaults from config and load locally
     * @param instance
     */
    public static void loadDefaults(LifeStealPlugin instance) {
        plugin = instance;
        enableLifeSteal(plugin.getConfig().getBoolean("enabled"));
        setDefaultHearts(plugin.getConfig().getInt("defaultHeartAmount"));
    }
}