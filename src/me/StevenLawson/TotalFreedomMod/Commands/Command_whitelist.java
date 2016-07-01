package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manage the whitelist.", usage = "/<command> <on | off | list | count | add <player> | remove <player> | addall | purge>")
public class Command_whitelist extends TFM_Command {

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        if (args.length < 1) {
            return false;
        }

        // list
        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GRAY + "Whitelisted players: " + TFM_Util.playerListToNames(Bukkit.getServer().getWhitelistedPlayers()));
            return true;
        }

        // count
        if (args[0].equalsIgnoreCase("count")) {
            int onlineWPs = 0;
            int offlineWPs = 0;
            int totalWPs = 0;

            for (OfflinePlayer player : Bukkit.getServer().getWhitelistedPlayers()) {
                if (player.isOnline()) {
                    onlineWPs++;
                } else {
                    offlineWPs++;
                }
                totalWPs++;
            }

            sender.sendMessage(ChatColor.GRAY + "Online whitelisted players: " + onlineWPs);
            sender.sendMessage(ChatColor.GRAY + "Offline whitelisted players: " + offlineWPs);
            sender.sendMessage(ChatColor.GRAY + "Total whitelisted players: " + totalWPs);

            return true;
        }

        // all commands past this line are superadmin-only
        if (!(senderIsConsole || TFM_AdminList.isSuperAdmin(sender))) {
            sender.sendMessage(TotalFreedomMod.MSG_NO_PERMS);
            return true;
        }

        // on
        if (args[0].equalsIgnoreCase("on")) {
            TFM_Util.adminAction(sender.getName(), "Turning the whitelist on.", true);
            server.setWhitelist(true);
            return true;
        }

        // off
        if (args[0].equalsIgnoreCase("off")) {
            TFM_Util.adminAction(sender.getName(), "Turning the whitelist off.", true);
            server.setWhitelist(false);
            return true;
        }

        // add
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 2) {
                return false;
            }

            String search_name = args[1].trim().toLowerCase();

            OfflinePlayer player = getPlayer(search_name);

            if (player == null) {
                player = TFM_DepreciationAggregator.getOfflinePlayer(Bukkit.getServer(), search_name);
            }

            TFM_Util.adminAction(sender.getName(), "Adding " + player.getName() + " to the whitelist.", false);
            player.setWhitelisted(true);
            return true;
        }

        // remove
        if ("remove".equals(args[0])) {
            if (args.length < 2) {
                return false;
            }

            String search_name = args[1].trim().toLowerCase();

            OfflinePlayer player = getPlayer(search_name);

            if (player == null) {
                player = TFM_DepreciationAggregator.getOfflinePlayer(Bukkit.getServer(), search_name);
            }

            if (player.isWhitelisted()) {
                TFM_Util.adminAction(sender.getName(), "Removing " + player.getName() + " from the whitelist.", false);
                player.setWhitelisted(false);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "That player is not whitelisted");
                return true;
            }

        }

        // addall
        if (args[0].equalsIgnoreCase("addall")) {
            TFM_Util.adminAction(sender.getName(), "Adding all online players to the whitelist.", false);
            int counter = 0;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!player.isWhitelisted()) {
                    player.setWhitelisted(true);
                    counter++;
                }
            }

            sender.sendMessage(ChatColor.GRAY + "Whitelisted " + counter + " players.");
            return true;
        }

        // all commands past this line are console/telnet only
        if (!senderIsConsole) {
            sender.sendMessage(TotalFreedomMod.MSG_NO_PERMS);
            return true;
        }

        //purge
        if (args[0].equalsIgnoreCase("purge")) {
            TFM_Util.adminAction(sender.getName(), "Removing all players from the whitelist.", false);
            sender.sendMessage(ChatColor.GRAY + "Removed " + TFM_ServerInterface.purgeWhitelist() + " players from the whitelist.");

            return true;
        }

        // none of the commands were executed
        return false;
    }
}
