package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
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
    
    public static World getAdminWorld() {
        try {
            return TFM_AdminWorld.getInstance().getWorld();
        }
        catch (Exception ex) {
            TFM_Log.warning(ex);
            return null;
        }
    }
}
