package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import me.StevenLawson.TotalFreedomMod.Bridge.TFM_WorldEditBridge;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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
        sender.sendMessage(ChatColor.RED + "Sending that Mother fucker to hell.");
        TFM_Util.bcastMsg(player.getName() + " has been sent to Hell.", ChatColor.RED);

        // Undo WorldEdits:
        try
        {
            TFM_WorldEditBridge.undo(player, 15);
        }
        catch (NoClassDefFoundError ex)
        {
        }

        // rollback
        TFM_RollbackManager.rollback(player.getName());

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
        //kill them.
        player.setHealth(0.0);
        //welcome xD
        player.sendMessage(ChatColor.RED + "Welcome to hell mother fucker");
        //insult them
        player.sendMessage(ChatColor.RED + "You faggot ass bitch go die in a hole");
        //send the admin the message
        sender.sendMessage(TotalFreedomMod.FREEDOMOP_MOD + ChatColor.WHITE + "The deed has been done.");
        sender.sendMessage(ChatColor.RED + "Banning is no longer done on here but it undos there edits and rolls them back :P");
        return true;
    }

}
