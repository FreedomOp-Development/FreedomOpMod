package me.StevenLawson.TotalFreedomMod.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Super Admin Command - No fucks given", usage = "/<command> <player>", aliases = "nofucksgiven")
public class Command_nfg extends TFM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + TotalFreedomMod.PLAYER_NOT_FOUND);
            return true;
        }
        player.sendMessage(ChatColor.RED + "'Gaze into my fucks given and see that thou is barren.'  -buildcarter8 (2012)");
        player.setHealth(0);
        TFM_Util.bcastMsg(sender.getName() + " - Gives no fucks about " + player.getName(), ChatColor.RED);
        return true;
    }

}
