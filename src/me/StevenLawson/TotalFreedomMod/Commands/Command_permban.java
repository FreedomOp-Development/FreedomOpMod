package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Convert;
import me.StevenLawson.TotalFreedomMod.TFM_PermbanList;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Manage permanently banned players and IPs.", usage = "/<command> <list | reload>")
public class Command_permban extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            dumplist(sender);
        }
        else if (args[0].equalsIgnoreCase("reload"))
        {
            if (!senderIsConsole)
            {
                sender.sendMessage(TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Reloading permban list...");
            TFM_PermbanList.load();
            dumplist(sender);
        }
        else
        {
            return false;
        }

        return true;
    }

    private void dumplist(CommandSender sender)
    {
        if (TFM_PermbanList.getPermbannedPlayers().isEmpty())
        {
            sender.sendMessage(ChatColor.GRAY + "No permanently banned player names.");
        }
        else
        {
            sender.sendMessage(ChatColor.GRAY + TFM_Convert.toString(TFM_PermbanList.getPermbannedPlayers().size()) + " permanently banned players:");
            sender.sendMessage(ChatColor.GRAY + StringUtils.join(TFM_PermbanList.getPermbannedPlayers(), ", "));
        }

        if (TFM_PermbanList.getPermbannedIps().isEmpty())
        {
            sender.sendMessage(ChatColor.GRAY + "No permanently banned IPs.");
        }
        else
        {
            sender.sendMessage(ChatColor.GRAY + TFM_Convert.toString(TFM_PermbanList.getPermbannedIps().size()) + " permanently banned IPs:");
            sender.sendMessage(ChatColor.GRAY + StringUtils.join(TFM_PermbanList.getPermbannedIps(), ", "));
        }
    }
}
