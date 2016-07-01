package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_ServiceChecker;
import me.StevenLawson.TotalFreedomMod.TFM_ServiceChecker.ServiceStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "Shows the status of all Mojang services", usage = "/<command>")
public class Command_services extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        sender.sendMessage(ChatColor.BLUE + "Mojang Services" + ChatColor.WHITE + ":");

        for (ServiceStatus service : TFM_ServiceChecker.getAllStatuses())
        {
            sender.sendMessage(service.getFormattedStatus());
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "Version" + ChatColor.WHITE + ": " + TFM_ServiceChecker.getVersion());
        sender.sendMessage(ChatColor.DARK_PURPLE + "Last Check" + ChatColor.WHITE + ": " + TFM_ServiceChecker.getLastCheck());

        return true;
    }
}
