package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Ban someone faster than PieGuy's nans tits can fall", usage = "/<command> <player>")
public class Command_nanban extends TFM_Command
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

        // Broadcast the ban message
        TFM_Util.bcastMsg(ChatColor.RED + sender.getName() + " - Is banning " + player.getName() + " faster than PieGuy's nans tits can fall");

        // Rollback player via CoreProtect
        Bukkit.dispatchCommand(sender, "co rb u:" + player.getName() + " t:24h r:global");

        player.kickPlayer(ChatColor.RED + "You have been banned by '" + sender.getName()
                + "'.\nMiscommunication, misunderstanding, wrongly banned? Appeal at http://freedomop.boards.net\nP.S. PieGuy's nan's tits have fallen. We were faster :)");

        // IP Ban
        for (String playerIp : TFM_PlayerList.getEntry(player).getIps())
        {
            TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
        }

        // UUID Ban
        TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));
        // We can't kick the player again as it causes errors
        return true;
    }

}
