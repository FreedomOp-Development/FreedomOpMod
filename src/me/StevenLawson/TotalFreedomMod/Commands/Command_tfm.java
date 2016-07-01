package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
import me.StevenLawson.TotalFreedomMod.TFM_Log;
import me.StevenLawson.TotalFreedomMod.TFM_PermbanList;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about TotalFreedomMod or reloads it", usage = "/<command> [reload]")
public class Command_tfm extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            if (!args[0].equals("reload"))
            {
                return false;
            }

            if (!TFM_AdminList.isSuperAdmin(sender))
            {
                sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }

            TFM_MainConfig.load();
            TFM_AdminList.load();
            TFM_PermbanList.load();
            TFM_PlayerList.load();
            TFM_BanManager.load();
            TFM_CommandBlocker.getInstance().load();

            final String message = String.format("%s v%s reloaded.",
                    TotalFreedomMod.pluginName,
                    TotalFreedomMod.pluginVersion);

            sender.sendMessage(message);
            TFM_Log.info(message);
            return true;
        }

        TotalFreedomMod.BuildProperties build = TotalFreedomMod.build;
        sender.sendMessage(ChatColor.GOLD + "TotalFreedomMod for 'Total Freedom', the original all-op server.");
        sender.sendMessage(ChatColor.GOLD + "Running on " + TFM_ConfigEntry.SERVER_NAME.getString() + ".");
        sender.sendMessage(ChatColor.GOLD + "Created by Madgeek1450 and Prozza.");
        sender.sendMessage(ChatColor.GOLD + String.format("Version "
                + ChatColor.BLUE + "%s.%s " + ChatColor.GOLD + "("
                + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                TotalFreedomMod.pluginVersion,
                build.number,
                build.head));
        sender.sendMessage(ChatColor.GOLD + String.format("Compiled "
                + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                + ChatColor.BLUE + "%s",
                build.date,
                build.builder));
        sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "http://github.com/TotalFreedom/TotalFreedomMod"
                + ChatColor.GREEN + " for more information.");

        return true;
    }
}