package me.tauntaunchewie.utils;

import me.tauntaunchewie.LifeStealPlugin;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class UserDataHandler {

    /**
     * Get a File object by plugins/LifeSteal/[unique player id].yml
     * @param player
     * @return
     */
    private static File getUserFileForPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        File userFile = new File(LifeStealUtils.getPlugin().getDataFolder(), uuid + ".yml");
        return userFile;
    }

    /**
     * Retrieve the configuration object for specific player
     * @param player
     * @return
     */
    public static FileConfiguration getConfigForPlayer(Player player) {
        FileConfiguration userConfig;
        File userFile = getUserFileForPlayer(player);

        // If config doesn't exist create it
        if (!userFile.exists()) {
            userConfig = createUserConfig(player, userFile);
        }
        else {
            // Load existing config
            userConfig = YamlConfiguration.loadConfiguration(getUserFileForPlayer(player));
        }
        return userConfig;
    }

    /**
     * Add a heart to the player who killed this player
     * @param killer
     * @param isCurrentPlayer  Is the player that was the killer the current player in this instance
     */
    public static void addHeartToPlayer(Player killer, boolean isCurrentPlayer) {
        FileConfiguration userConfig = getConfigForPlayer(killer);

        // Get player's current maximum heart count and increase by 1 heart
        AttributeInstance attribute = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(attribute.getBaseValue() + 2);

        // Increase user health by one heart as well
        killer.setHealth(killer.getHealth() + 2);

        userConfig.set("User.Info.currentHearts", killer.getHealth());
        userConfig.set("User.Info.maxHearts", attribute.getBaseValue());
        saveConfig(killer, userConfig);
    }

    /**
     * Remove a heart from the current player because they were killed.  If they have no hearts left ban permanently.
     * @param killedPlayer
     * @param isCurrentPlayer Is the player that was killed the current player in this instance
     */
    public static void removeHeartFromPlayer(Player killedPlayer, boolean isCurrentPlayer) {
        FileConfiguration userConfig = getConfigForPlayer(killedPlayer);

        // Remove heart from player attribute instance
        AttributeInstance attribute = killedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        // Add 1 heart and increament health by 1
        attribute.setBaseValue(attribute.getBaseValue() - 2);

        // Set currentHearts to maxHearts since they died
        userConfig.set("User.Info.currentHearts", attribute.getBaseValue());
        userConfig.set("User.Info.maxHearts", attribute.getBaseValue());
        saveConfig(killedPlayer, userConfig);

        // If user has no hearts left they are banned
        if (attribute.getBaseValue() <= 0) {
            // Ban player
            Bukkit.getBanList(BanList.Type.NAME).addBan(killedPlayer.getName(), "You have been banned because you have no hearts left :womp: :womp:", null, "");
            killedPlayer.kickPlayer("You have been banned because you have no hearts left :(");
        }
    }

    /**
     * Save the current state of a user.  Used during disconnect/shutdown events.
     * @param player
     */
    public static void saveCurrentPlayerState(Player player) {
        FileConfiguration userConfig = getConfigForPlayer(player);

        // Get player's current maximum heart count and increase by 1 heart
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        userConfig.set("User.Info.currentHearts", player.getHealth());
        userConfig.set("User.Info.maxHearts", attribute.getBaseValue());
        saveConfig(player, userConfig);
    }

    /**
     * Reset player to default state and save config.  Used when players are dead and Ops want to reset a user.
     * @param player
     */
    public static void resetPlayerToDefault(Player player) {
        FileConfiguration userConfig = getConfigForPlayer(player);

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(LifeStealUtils.getDefaultHearts());
        player.setHealth(LifeStealUtils.getDefaultHearts());

        userConfig.set("User.Info.currentHearts", LifeStealUtils.getDefaultHearts());
        userConfig.set("User.Info.maxHearts", LifeStealUtils.getDefaultHearts());
        saveConfig(player, userConfig);
    }

    /**
     * Save the config data to a file on the server
     */
    public static void saveConfig(Player player, FileConfiguration userConfig)  {
        Logger logger = Logger.getLogger("Minecraft");
        try {
            userConfig.save(getUserFileForPlayer(player));
        }
        catch (Exception e) {
            logger.severe("LifeSteal Plugin - saveConfig() Error trying to save user config file for player: " + player.getDisplayName() + " = " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * On first user load into server, create a config file with base information
     * @param player
     */
    private static FileConfiguration createUserConfig(Player player, File userFile) {
        Logger logger = Logger.getLogger("Minecraft");
        if (!userFile.exists()) {
            try {
                YamlConfiguration UserConfig = YamlConfiguration.loadConfiguration(userFile);

                // Get base user info
                UserConfig.set("User.Info.PreviousName", player.getName());
                UserConfig.set("User.Info.UniqueID", player.getUniqueId().toString());
                UserConfig.set("User.Info.ipAddress", player.getAddress().getAddress().getHostAddress());

                // Set last amount of hearts user had at log out (initial amount is max)
                UserConfig.set("User.Info.currentHearts", LifeStealUtils.getDefaultHearts());
                // Set max hearts user can have on login
                UserConfig.set("User.Info.maxHearts", LifeStealUtils.getDefaultHearts());

                UserConfig.save(userFile);

                // Update local reference
                return UserConfig;
            }
            catch (Exception e) {
                logger.severe("LifeSteal Plugin - createUserConfig() Error during creation of initial config for new player: " + player.getName() + "(" + player.getUniqueId().toString() + ") error: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * Constructor retriving/creating current user config and populating hearts
     * @param player
     */
    public UserDataHandler(Player player) {
        FileConfiguration userConfig = getConfigForPlayer(player);

        LifeStealUtils.setPlayerMaxHearts(player, userConfig.getInt("User.Info.maxHearts"));
        LifeStealUtils.setPlayerCurrentHearts(player, userConfig.getInt("User.Info.currentHearts"));
    }
}