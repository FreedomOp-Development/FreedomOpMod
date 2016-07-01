package me.StevenLawson.TotalFreedomMod.Commands;

import java.io.File;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SENIOR, source = SourceType.ONLY_CONSOLE)
@CommandParameters(description = "Removes essentials playerdata", usage = "/<command>")
public class Command_wipeuserdata extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials"))
        {
            sender.sendMessage(ChatColor.RED + "Essentials is not enabled on this server");
            return true;
        }

        TFM_Util.adminAction(sender.getName(), "Wiping Essentials playerdata", true);

        TFM_Util.deleteFolder(new File(Bukkit.getServer().getPluginManager().getPlugin("Essentials").getDataFolder(), "userdata"));

        sender.sendMessage(ChatColor.GRAY + "All playerdata deleted.");
        return true;
    }
}
