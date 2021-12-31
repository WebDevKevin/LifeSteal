package me.tauntaunchewie;

import me.tauntaunchewie.utils.LifeStealUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class LifeStealPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Pass reference to util function for plugin
        LifeStealUtils.loadDefaults(this);

        // Set up config file on server (one time)
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectListener(), this);
        this.getCommand("lifesteal").setExecutor(new LifeStealCommander());
    }
    @Override
    public void onDisable() {
        //getLogger().info("onDisable is called!");
    }
}
