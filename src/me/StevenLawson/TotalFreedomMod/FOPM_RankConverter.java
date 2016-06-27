package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FOPM_RankConverter
{
    public static boolean isOwner(Player player)
    {
        if (TFM_AdminList.isSeniorAdmin(player) && TFM_ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()))
        {
            return true;
        }
        else {
            return false;
        }
    }
    
    public static boolean isOwner(CommandSender sender)
    {
        if (TFM_AdminList.isSeniorAdmin(sender) && TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
        {
            return true;
        }
        else {
            return false;
        }
    }
}
