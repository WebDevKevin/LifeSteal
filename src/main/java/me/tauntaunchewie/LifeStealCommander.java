package me.tauntaunchewie;

import me.tauntaunchewie.utils.LifeStealUtils;
import me.tauntaunchewie.utils.UserDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LifeStealCommander implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Make sure a player is calling this
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Make sure we have exactly 2 args
            if (args.length == 1) {
                // Admin utility to enable/disable/get status of lifesteal functionality
                if (player.hasPermission("lifestealplugin.enable")) {
                    if (args[0].equalsIgnoreCase("on")) {
                        // Turn shif pooping on
                        LifeStealUtils.enableLifeSteal(true);
                        player.sendMessage("Life Stealing turned ON");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        // Turn shift pooping off
                        LifeStealUtils.enableLifeSteal(false);
                        player.sendMessage("Life Stealing turned OFF");
                    } else if (args[0].equalsIgnoreCase("status")) {
                        player.sendMessage("Life Stealing status is: " + (LifeStealUtils.isEnabled() ? "ON" : "OFF"));
                    } else {
                        player.sendMessage("Usage: /lifesteal [on|off|status]");
                        player.sendMessage("Example: /lifesteal on");
                    }
                }
                else {
                    player.sendMessage("You do not have permission to execute this command");
                }
            }
            else if (args.length == 2) {
                // Admin utility to set base amount of hearts.  Only affects new players.
                if (args[0].equalsIgnoreCase("defaulthearts")) {
                    if (player.hasPermission("lifestealplugin.sethearts")) {
                        int defaultHearts = -1;
                        try {
                            defaultHearts = Integer.parseInt(args[1]);
                            if (defaultHearts > 0 && defaultHearts < 100) {
                                LifeStealUtils.setDefaultHearts(defaultHearts);
                                player.sendMessage("Default Hearts for Life Steal set to: " + defaultHearts);
                            } else {
                                player.sendMessage("Invalid amount of hearts.  Please make sure to pick a number between 1 and 100");
                            }
                        } catch (Exception e) {
                            player.sendMessage("Invalid amount of hearts.  Please make sure to pick a number between 1 and 100");
                        }
                    }
                    else {
                        player.sendMessage("You do not have permission to execute this command");
                    }
                }
                // Admin utility to reset a player's heart count and unban.
                else if (args[0].equalsIgnoreCase("resetuser")) {
                    if (player.hasPermission("lifestealplugin.resetuser")) {
                        // Make sure Arg 2 is a real player
                        Player playerToReset = Bukkit.getPlayerExact(args[1]);

                        if (playerToReset != null) {
                            UserDataHandler.resetPlayerToDefault(playerToReset);
                        }
                        else {
                            player.sendMessage(args[1] + " is not a player on this server");
                        }
                    }
                    else {
                        player.sendMessage("You do not have permission to execute this command");
                    }
                }
                else {
                    player.sendMessage("Usage: /lifesteal defaulthearts ##");
                    player.sendMessage("Example: /lifesteal defaulthearts 10");
                    player.sendMessage("Note: Changing this number only affects NEW players to the server.\n\n");
                    player.sendMessage("Usage: /lifesteal resetuser USERNAME");
                    player.sendMessage("Example: /lifesteal resetuser " + player.getDisplayName());
                    player.sendMessage("Note: This will unban and reset the player heart count to server defaults.");
                }
            }
            else {
                player.sendMessage("Invalid amount of arguments.  Expecting 1 or 2");
            }

            return true;
        }
        else {
            // Only allow players to invoke
            return false;
        }
    }
}