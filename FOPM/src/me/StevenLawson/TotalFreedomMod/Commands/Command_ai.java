package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "How to become admin", usage = "/<command>", aliases = "admininfo")
public class Command_ai extends TFM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        if (TFM_ConfigEntry.APPLICATIONS_ENABLED.getBoolean())
        {
            sender.sendMessage(ChatColor.AQUA + "The following is accurate as of 5/22/16");
            sender.sendMessage(ChatColor.GREEN + "To apply for admin you need to go to the forums at http://freedomop.boards.net");
            sender.sendMessage(ChatColor.YELLOW + "Then read the requirements in the 'admin applications' board.");
            sender.sendMessage(ChatColor.WHITE + "Then if you feel you are ready, make a new thread in the 'admin applications'' board.");
            sender.sendMessage(ChatColor.BLUE + "And fill out the template in the new thread.");
            sender.sendMessage(ChatColor.RED + "We ask for you not to ask existing admins for recommendations, this will get your application denied.");
            sender.sendMessage(ChatColor.GOLD + "Good Luck!");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Unfortunately, applications are closed at the moment, sorry. :(");
        }
        return true;
    }
}
