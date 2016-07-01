package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Buildcarter8 Command- Manage the server ", usage = "/<command> [version,stop,fakejoin,fakeleave]")
public class Command_manager extends TFM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!sender.getName().equalsIgnoreCase("buildcarter8"))
        {
            sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
            return true;
        }

        if (args.length < 0)
        {
            return true;
        }

        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("version"))
            {
                sender.sendMessage(ChatColor.RED + "Manager Loaded 1.1");
                return true;
            }
            if (args[0].equals("stop"))
            {
                Bukkit.savePlayers();
                Bukkit.shutdown();
                {
                }
            }
            else if (args[0].equals("fakejoin"))
            {

                Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + " joined the game.");
            }
        }
        if (args[0].equals("fakeleave"))
        {

            TFM_Util.bcastMsg(ChatColor.YELLOW + sender.getName() + " left the game.");
        }
        return true;
    }

    
    {

    }
}
