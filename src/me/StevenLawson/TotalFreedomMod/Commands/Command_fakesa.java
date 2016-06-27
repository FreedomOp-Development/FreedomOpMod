package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Manage superadmins in a fake way", usage = "/<command> [add:del] <player>")
public class Command_fakesa extends TFM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 0)
        {
            return false;
        }

        if (args.length == 1)
        {
            final Player player = getPlayer(args[0]);

            if (player == null)
            {
                sender.sendMessage(ChatColor.RED + TotalFreedomMod.PLAYER_NOT_FOUND);

                if (args[0].equalsIgnoreCase("add"))
                {
                    TFM_Util.bcastMsg(sender.getName() + " - Adding " + player + " to the superadmin list.", ChatColor.RED);
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    TFM_Util.bcastMsg(sender.getName() + " - Removing " + player + " from the superadmin list.");
                    return true;
                }
                return true;

            }
            {
                return true;
            }
        }
        return true;
    }
}
