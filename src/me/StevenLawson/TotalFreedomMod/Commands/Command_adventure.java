package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(description = "Quickly change your own gamemode to Adventure, or define someone's username to change theirs.", usage = "/<command> [partialname]")
public class Command_adventure extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            sender_p.setGameMode(GameMode.ADVENTURE);
            sender.sendMessage(ChatColor.GOLD + "Set gamemode to " + ChatColor.AQUA + "ADVENTURE");
            sender.sendMessage("Your gamemode has been updated.");
            return true;
        }
        if (args.length == 1)
        {
            Player player = getPlayer(args[0]);

            if (!TFM_AdminList.isSuperAdmin(sender))
            {
                sender.sendMessage(TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }
            if (player == null)
            {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return false;
            }

            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(ChatColor.GOLD + "Set gamemode to " + ChatColor.AQUA + "ADVENTURE");
            player.sendMessage(ChatColor.RED + "Your gamemode was changed by an admin");
            sender.sendMessage(ChatColor.GREEN + player.getName() + "'s gamemode successfully updated.");

        }
        return true;
    }
}