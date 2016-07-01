package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Send a command as someone else.", usage = "/<command> <fromname> <outcommand>")
public class Command_gcmd extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 2)
        {
            return false;
        }
        Player player;
        player = getPlayer(args[0]);
        String outCommand = StringUtils.join(args, " ", 1, args.length);
        if (TFM_CommandBlocker.getInstance().isCommandBlocked(outCommand, sender))
        {
            return true;
        }
        if ((args[0].equalsIgnoreCase("cowgomooo12")) || (args[0].equalsIgnoreCase("CrafterSmith12")) || (args[0].equalsIgnoreCase("Robo_Lord")))
        {
            sender.sendMessage(ChatColor.RED + "Nice try, but you are not going to gcmd me.");
            return true;
        }
        try
        {
            sender.sendMessage(ChatColor.GRAY + "Sending command as " + player.getName() + ": " + outCommand);
            if (Bukkit.getServer().dispatchCommand(player, outCommand))
            {
                sender.sendMessage(ChatColor.GRAY + "Command sent.");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Unknown error sending command.");
            }
        }
        catch (Throwable ex)
        {
            sender.sendMessage(ChatColor.RED + "Error sending command: " + ex.getMessage());
        }
        return true;
    }
}
