package me.PieGuy7896.FreedomOpMod.Listeners;

import me.PieGuy7896.FreedomOpMod.FOPM_Util;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class FOPM_PlayerListener implements Listener
{
    
    @EventHandler(priority = EventPriority.HIGH)
    public static void onPlayerJoinEvent(PlayerJoinEvent event)
    {
             
        Player player = event.getPlayer();
        final String username = event.getPlayer().getName();
        final String IP = event.getPlayer().getAddress().getAddress().getHostAddress().trim();

        if (TFM_AdminList.isSuperAdmin(player))
        {
            TFM_PlayerData.getPlayerData(player).setCommandSpy(true);
        }
        
        if (FOPM_Util.FAMOUS.contains(player.getName().toLowerCase()))
        {
            player.setPlayerListName("[Fake]" + player.getName());
            TFM_PlayerData.getPlayerData(player).setTag("&8[&7Fake&8]");
            TFM_Util.bcastMsg(TotalFreedomMod.FREEDOMOP_MOD + "WARNING: " + player.getName() + " is completely and utterly FAKE! - This server is in Offline Mode so anybody can join as anyone!", ChatColor.RED);
        }
        
        else if (FOPM_Util.DEVELOPERS.contains(player.getName()))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
        }
        else if (FOPM_Util.EXECS.contains(player.getName())) {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&eSpec-Exec&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&eSpec-Exec&8]");
        }
        else if (FOPM_Util.SYS.contains(player.getName()))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&4SyS&8] &4" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&4System Admin&8]");
        }
        
        else if (username.equalsIgnoreCase("buildcarter8"))
        {
            //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Chief Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&4Chief Developer&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "buildcarter8 is the " + ChatColor.RED + "destroyer of all human kind " + ChatColor.AQUA + "and ");
        }
        else if (username.equalsIgnoreCase("Dragonfire147"))
        {
            //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "Dragonfire147 is a " + ChatColor.DARK_GREEN + "Zombie Killer " + ChatColor.AQUA + "and..");
        }
        else if (username.equalsIgnoreCase("keemismydad"))
        {   //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&cCOS&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&cChief of Security&8]");
            player.setCustomName("Cyro");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "keemismydad is a " + ChatColor.GOLD + "meme lord " + ChatColor.AQUA + "and.. ");
        }
        else if (username.equalsIgnoreCase("CrafterSmith12"))
        {
            //Set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&cFounder&8] &9" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&cFounder&8]");
        }
        
        else if (username.equalsIgnoreCase("DragonHunterGW"))
        {
            //ban username
            TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));
            //ban ip
            String ip = TFM_Util.getFuzzyIp(player.getAddress().getAddress().getHostAddress());
            TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName()));
            player.kickPlayer(ChatColor.RED + "Fuck off. :)");
        }
        if (TFM_Util.getFuzzyIp(IP).equalsIgnoreCase("94.175.*.*"))
        {
            TFM_Util.bcastMsg("WARNING" + username + " Is foodknight! Ban him asap", ChatColor.RED);
            //ban username
            TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));
            //ban ip
            String ip = TFM_Util.getFuzzyIp(player.getAddress().getAddress().getHostAddress());
            TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName()));
            TFM_AdminList.removeSuperadmin(player);
            player.kickPlayer(ChatColor.RED + "Fuck off. :)");
        }
        player.sendMessage(ChatColor.BLUE + "This server is using FreedomOPMod, a highly modified version of the TotalFreedomMod, created by:");
        player.sendMessage(ChatColor.BLUE + "Madgeek1450, DarthSalamon, Buildcarter8, Robo_Lord, PieGuy7896, Dragonfire147, cowgomooo12, CrafterSmith12, SupItsDillon, ImJustLazy, and tylerhyperHD.");
    }
    
        @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = (Player) event.getPlayer();
        
        String command = event.getMessage().toLowerCase().trim();

        if (command.contains("175:") || command.contains("double_plant:"))
        {
            event.setCancelled(true);
            TFM_Util.bcastMsg(TotalFreedomMod.FREEDOMOP_MOD + player.getName() + " just attempted to use the crash item! Deal with them appropriately please!", ChatColor.DARK_RED);
        }

        if (command.contains("&k") && !TFM_AdminList.isSuperAdmin(player))
        {
            event.setCancelled(true);
            TFM_Util.playerMsg(player, TotalFreedomMod.FREEDOMOP_MOD + ChatColor.RED + "You are not permitted to use &k!");
        }
    }
    
}
