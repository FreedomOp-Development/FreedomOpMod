package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manager operators", usage = "/<command> <count | purge>")
public class Command_ops extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equals("count"))
        {
            int totalOps = Bukkit.getServer().getOperators().size();
            int onlineOps = 0;

            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (player.isOp())
                {
                    onlineOps++;
                }
            }

            sender.sendMessage(ChatColor.GRAY + "Online OPs: " + onlineOps);
            sender.sendMessage(ChatColor.GRAY + "Offline OPs: " + (totalOps - onlineOps));
            sender.sendMessage(ChatColor.GRAY + "Total OPs: " + totalOps);

            return true;
        }

        if (args[0].equals("purge"))
        {
            if (!TFM_AdminList.isSuperAdmin(sender))
            {
                sender.sendMessage(ChatColor.RED + TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }

            TFM_Util.adminAction(sender.getName(), "Purging all operators", true);

            for (OfflinePlayer player : Bukkit.getServer().getOperators())
            {
                player.setOp(false);
                if (player.isOnline())
                {
                    Player myplay = (Player) player;
                    myplay.sendMessage(TotalFreedomMod.YOU_ARE_NOT_OP);
                }
            }
            return true;
        }

        return false;
    }
}
