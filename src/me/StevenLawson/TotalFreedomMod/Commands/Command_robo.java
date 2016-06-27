package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Only those with the ULTIMATE purple abilities are able to even THINK about using this!", usage = "/<command> <player>", aliases = "destroy")
public class Command_robo extends TFM_Command
{
    @Override
    public boolean run(final CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }
        Player selected = this.server.getPlayer(args[0]);
        if (selected.getName().equalsIgnoreCase("Robo_Lord"))
        {
            sender.sendMessage("Someone GCMD'd you Robo!");
            server.dispatchCommand(sender, "wildcard lastcmd ?");
            return true;
        }
        if (!sender.getName().equalsIgnoreCase("Robo_Lord"))
        {
            sender.sendMessage("YOU ARE NOT ROBO LORD!!!!!!!!!");
            TFM_Util.bcastMsg(ChatColor.YELLOW + sender.getName() + TotalFreedomMod.MSG_NO_PERMS);
            final Player player;
            player = getPlayer(sender.getName());
            TFM_Util.adminAction("Robo_Lord", "I am REALLY annoyed with you " + player.getName() + "!!!", true);
            TFM_Util.bcastMsg(player.getName() + ", YOU SHALL FACE MY WRATH!!! ", ChatColor.DARK_GREEN);

            final String IP = player.getAddress().getAddress().getHostAddress().trim();
            if (TFM_AdminList.isSuperAdmin(player))
            {
                TFM_Util.adminAction("Robo_Lord", "Obliterating " + player.getName() + "'s Super Admin perms...", true);
                TFM_AdminList.removeSuperadmin(player);
            }
            player.setWhitelisted(false);

            player.setOp(false);

            // ban IPs
            for (String playerIp : TFM_PlayerList.getEntry(player).getIps())
            {
                TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
            }

            // ban name
            TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));

            player.setGameMode(GameMode.SURVIVAL);

            player.closeInventory();
            player.getInventory().clear();

            player.setFireTicks(10000);

            player.getWorld().createExplosion(player.getLocation(), 4.0F);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().strikeLightning(player.getLocation());

                    player.setHealth(0.0D);
                }
            }.runTaskLater(this.plugin, 40L);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    TFM_Util.adminAction(sender.getName(), "Banning " + player.getName() + ", IP: " + IP, true);

                    player.getWorld().createExplosion(player.getLocation(), 4.0F);

                    player.kickPlayer(ChatColor.DARK_GREEN + "You got on the wrong side of Robo bitch");
                }
            }.runTaskLater(this.plugin, 60L);

            return true;
        }
        final Player player;
        player = getPlayer(args[0]);
        TFM_Util.adminAction(ChatColor.DARK_PURPLE + sender.getName(), ": I am REALLY annoyed with you " + player.getName() + "!!!", true);
        TFM_Util.bcastMsg(player.getName() + ", YOU SHALL FACE MY WRATH!!! ", ChatColor.DARK_GREEN);

        final String IP = player.getAddress().getAddress().getHostAddress().trim();
        if (TFM_AdminList.isSuperAdmin(player))
        {
            TFM_Util.adminAction(ChatColor.DARK_PURPLE + sender.getName(), "Obliterating " + player.getName() + "'s Super Admin perms...", true);
            TFM_AdminList.removeSuperadmin(player);
        }
        player.setWhitelisted(false);

        player.setOp(false);

        // ban IPs
        for (String playerIp : TFM_PlayerList.getEntry(player).getIps())
        {
            TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
        }

        // ban name
        TFM_BanManager.addUuidBan(new TFM_Ban(player.getUniqueId(), player.getName()));

        player.setGameMode(GameMode.SURVIVAL);

        player.closeInventory();
        player.getInventory().clear();

        player.setFireTicks(10000);

        player.getWorld().createExplosion(player.getLocation(), 4.0F);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.getWorld().strikeLightning(player.getLocation());

                player.setHealth(0.0D);
            }
        }.runTaskLater(this.plugin, 40L);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                TFM_Util.adminAction(sender.getName(), "Banning " + player.getName() + ", IP: " + IP, true);

                player.getWorld().createExplosion(player.getLocation(), 4.0F);

                player.kickPlayer(ChatColor.DARK_GREEN + "You got on the wrong side of Robo bitch");
            }
        }.runTaskLater(this.plugin, 60L);

        return true;
    }
}
