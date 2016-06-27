package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Announce to the server that you are testing something", usage = "/<command> <on | off>")
public class Command_devtest extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!TFM_Util.DEVELOPERS.contains(sender.getName()))
        {
            sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
            return true;
        }

        if (args.length == 1)
        {
            if (args[0].equals("on"))
            {
                TFM_Util.adminAction(ChatColor.RED + "WARNING: " + sender.getName(), "has started Testing on the server, The server may lag more that usual", false);
                return true;
            }

            else if (args[0].equals("off"))
            {
                TFM_Util.adminAction(ChatColor.RED + "WARNING: " + sender.getName(), "has succesfully ended Testing on the server, If the testing was lagging the server this will stop now", false);
                return true;
            }
        }

        return true;
    }
}
