package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Log;
import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "Switch server online-mode on and off.", usage = "/<command> <on | off>")
public class Command_onlinemode extends TFM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            sender.sendMessage("Server is currently running with 'online-mode=" + (Bukkit.getServer().getOnlineMode() ? "true" : "false") + "'.");
            sender.sendMessage("\"/onlinemode on\" and \"/onlinemode off\" can be used to change online mode from the console.");
        }
        else
        {
            boolean online_mode;

            if (sender instanceof Player && !TFM_AdminList.isSeniorAdmin(sender, true))
            {
                sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }

            if (args[0].equalsIgnoreCase("on"))
            {
                online_mode = true;
            }
            else if (args[0].equalsIgnoreCase("off"))
            {
                online_mode = false;
            }
            else
            {
                return false;
            }

            try
            {
                TFM_ServerInterface.setOnlineMode(online_mode);

                if (online_mode)
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        player.kickPlayer("Server is activating \"online-mode=true\". Please reconnect.");
                    }
                }

                TFM_Util.adminAction(sender.getName(), "Turning player validation " + (online_mode ? "on" : "off") + ".", true);

                Bukkit.getServer().reload();
            }
            catch (Exception ex)
            {
                TFM_Log.severe(ex);
            }
        }

        return true;
    }
}
