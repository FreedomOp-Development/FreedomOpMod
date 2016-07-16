package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.FOPM_Messages;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Destroy a faggot", usage = "/<command> <playername>")
public class Command_destroy extends TFM_Command
{

    @Override
    @SuppressWarnings("unused")
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
        String reason = null;
        if (args.length >= 2)
        {
            reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }
        sender.sendMessage(ChatColor.RED + "Sending that mother fucker to hell.");
        TFM_Util.bcastMsg(player.getName() + " has been sent to hell.", ChatColor.RED);

        // Use CoreProtect rollback function
        Bukkit.dispatchCommand(sender, "co rb u:" + player.getName() + " t:24h r:global");

        // // Undo WorldEdits:
        // try
        // {
        // TFM_WorldEditBridge.undo(player, 15);
        // }
        // catch (NoClassDefFoundError ex)
        // {
        // }
        //
        // // rollback
        // TFM_RollbackManager.rollback(player.getName());

        // deop
        player.setOp(false);

        // set gamemode to survival:
        player.setGameMode(GameMode.SURVIVAL);

        // clear inventory:
        player.getInventory().clear();

        // strike with lightning effect:
        final Location targetPos = player.getLocation();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                targetPos.getWorld().strikeLightning(strike_pos);
            }
        }
        // kill them.
        player.setHealth(0.0);
        // welcome xD
        player.sendMessage(ChatColor.RED + "Welcome to hell mother fucker");
        // insult them
        player.sendMessage(ChatColor.RED + "You faggot ass bitch go die in a hole");
        // send the admin the message
        sender.sendMessage(FOPM_Messages.MODTAG + ChatColor.WHITE + "The deed has been done.");
        sender.sendMessage(ChatColor.RED + "Banning doesn't happen in this command.");
        return true;
    }

}
