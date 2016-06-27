package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
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
        TFM_Util.bcastMsg(ChatColor.RED + sender.getName() + " - Is banning " + player.getName() + " Faster than PieGuy's nans tits can fall");

        server.dispatchCommand(sender, "co rb u:" + player.getName() + " t:24h r:global");
        player.kickPlayer(ChatColor.RED + "You have been banned by '" + sender.getName() + "'.  Miscommunication, misunderstanding, wrongly banned?  Appeal at FreedomOP.boards.net P.S PieGuy's nan's tits have fallen we was faster :)");
        for (String playerIp : TFM_PlayerList.getEntry(player).getIps())
        {
            TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
        }

        // ban name
        TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));
        //IPBAN
        player.kickPlayer("PieGuy's nans tits have fallen we was faster!");
        return true;
    }

}
