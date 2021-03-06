package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Someone being a little bitch? Smite them down...", usage = "/<command> [playername] <reason>")
public class Command_smite extends TFM_Command
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

        String reason = "Unknown";
        if (args.length >= 2)
        {
            reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }

        TFM_Util.bcastMsg(ChatColor.RED + sender.getName() + " - Smiting " + player.getName() + " Reason: " + reason);

        smite(player);

        return true;
    }

    public static void smite(final Player player)
    {
        TFM_Util.bcastMsg(player.getName() + " has been a naughty, naughty asshole.", ChatColor.RED);
        // Deop
        player.setOp(false);

        // Set gamemode to survival:
        player.setGameMode(GameMode.SURVIVAL);

        // Clear inventory:
        player.getInventory().clear();

        // Strike with lightning effect:
        final Location targetPos = player.getLocation();
        final World world = player.getWorld();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(world, targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                world.strikeLightning(strike_pos);
            }
        }

        // Kill:
        player.setHealth(0.0);
    }
}
