package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.FOPM_RankConverter;
import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.DEVELOPERS;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Run any command on all users, username placeholder = ?.", usage = "/<command> [fluff] ? [fluff] ?")
public class Command_wildcard extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }
        
        if (args[0].equals("wildcard"))
        {
            sender.sendMessage(ChatColor.RED + "What the hell are you trying to do, you stupid idiot...");
            return true;
        }
        if (args[0].equals("gtfo"))
        {
            sender.sendMessage(ChatColor.RED + "Nice try");
            return true;
        }
        if (args[0].equals("doom"))
        {
            sender.sendMessage(ChatColor.RED + "Look, we all hate people, but this is not the way to deal with it, doom is evil enough!");
            return true;
        }
        if (args[0].equals("saconfig"))
        {
            sender.sendMessage(ChatColor.RED + "WOA, WTF are you trying to do???");
            return true;
        }
        if (args[0].equals("survival") || args[0].equals("creative"))
        {
            if(FOPM_RankConverter.isOwner(sender) || DEVELOPERS.contains(sender.getName()))
            {
                // let it run through if the person is of a high rank
            }
            else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to set everyone's gamemode.");
                return true;
            }
        }

        String baseCommand = StringUtils.join(args, " ");

        if (TFM_CommandBlocker.getInstance().isCommandBlocked(baseCommand, sender))
        {
            // CommandBlocker handles messages and broadcasts
            return true;
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            String out_command = baseCommand.replaceAll("\\x3f", player.getName());
            sender.sendMessage(ChatColor.GRAY + "Running Command: " + out_command);
            Bukkit.dispatchCommand(sender, out_command);
        }

        return true;
    }
}
