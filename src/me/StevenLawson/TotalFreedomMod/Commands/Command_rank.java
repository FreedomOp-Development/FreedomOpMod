package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_PlayerRank;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "Shows your rank.", usage = "/<command>")
public class Command_rank extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (senderIsConsole && args.length < 1)
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                sender.sendMessage(ChatColor.GRAY + player.getName() + " is " + TFM_PlayerRank.fromSender(player).getLoginMessage());
            }
            return true;
        }

        if (args.length > 1)
        {
            return false;
        }

        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.AQUA + sender.getName() + " is " + TFM_PlayerRank.fromSender(sender).getLoginMessage());
            return true;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(TotalFreedomMod.PLAYER_NOT_FOUND);
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + player.getName() + " is " + TFM_PlayerRank.fromSender(player).getLoginMessage());

        return true;
    }
}
