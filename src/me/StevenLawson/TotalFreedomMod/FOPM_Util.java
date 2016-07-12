package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FOPM_Util
{
    public static boolean isOwner(Player player)
    {
        if (TFM_AdminList.isSeniorAdmin(player) && TFM_ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isOwner(CommandSender sender)
    {
        if (TFM_AdminList.isSeniorAdmin(sender) && TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void strikeLightningInPlace(Player player)
    {
        Location loc = player.getLocation();
        World pworld = player.getWorld();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                Location strikePos = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z);
                pworld.strikeLightning(strikePos);
            }
        }
    }
}
