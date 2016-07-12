package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.FOPM_Util;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Superadmin Command - A terrible command with horrific ideas.", usage = "/<command> <exterminate | csg | jelly | wtf | fgt | drown> <partialname>", aliases = "rm")
public class Command_impl extends TFM_Command
{
    @Override
    public boolean run(final CommandSender sender, Player sender_p, Command cmd, String lbl, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.GOLD + "Please enter one of the usages below.");
            return false;
        }
        if (args.length == 2)
        {
            // 62nd Commit: Fix amount of instances for player and location
            final Player player = getPlayer(args[1]);

            if (player == null)
            {
                sender.sendMessage(TotalFreedomMod.PLAYER_NOT_FOUND);
                return true;
            }

            final Location loc = player.getLocation();
            final World pworld = loc.getWorld();

            if (args[0].equalsIgnoreCase("exterminate"))
            {
                TFM_Util.adminAction(sender.getName(), "Exterminating " + player.getName() + "...", true);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        FOPM_Util.strikeLightningInPlace(player);
                    }
                }.runTaskLater(this.plugin, 20L);

                pworld.createExplosion(loc, 3.0F);

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.teleport(new Location(pworld, loc.getBlockX(), 0.0D, loc.getBlockZ()));
                        player.setVelocity(new Vector(0, -10, 0));
                    }
                }.runTaskLater(this.plugin, 40L);

                TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);

                playerdata.setCaged(true, loc, Material.GLASS, Material.AIR);
            }
            else if (args[0].equalsIgnoreCase("jelly"))
            {
                TFM_Util.bcastMsg("Hey " + player.getName() + ", what's the difference between jelly and jam?", ChatColor.RED);
                FOPM_Util.strikeLightningInPlace(player);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        TFM_Util.bcastMsg("I can't jelly my banhammer up your ass.", ChatColor.RED);
                        pworld.createExplosion(loc, 3.0F);
                        player.setHealth(0.0D);
                        player.closeInventory();
                        player.getInventory().clear();
                        server.dispatchCommand(sender, "co rb u:" + player.getName() + " t:24h r:global");
                        // TFM_WorldEditBridge.undo(p, 15);
                        // TFM_RollbackManager.rollback(p.getName());
                    }
                }.runTaskLater(this.plugin, 60L);

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        String userIP = player.getAddress().getAddress().getHostAddress();
                        String[] IPParts = userIP.split("\\.");
                        if (IPParts.length == 4)
                        {
                            userIP = String.format("%s.%s.*.*", new Object[]
                                { IPParts[0], IPParts[1] });
                        }
                        TFM_Util.bcastMsg(String.format("%s - banning: %s, IP: %s.", new Object[]
                            { sender.getName(), player.getName(), userIP }), ChatColor.RED);
                        server.dispatchCommand(sender, "glist ban " + player.getName());
                        player.kickPlayer(ChatColor.RED + "You couldn't handle the banhammer.");
                    }
                }.runTaskLater(this.plugin, 80L);
            }
            else if (args[0].equalsIgnoreCase("csg"))
            {
                TFM_Util.bcastMsg(player.getName() + " has been a naughty, naughty boy.");
                FOPM_Util.strikeLightningInPlace(player);
                TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
                playerdata.setCaged(true, loc, Material.GLASS, Material.AIR);
                player.teleport(new Location(pworld, loc.getBlockX(), 120.0D, loc.getBlockZ()));
                player.setVelocity(new Vector(0, 10, 0));
                player.teleport(new Location(loc.getWorld(), loc.getBlockX(), 0.0D, loc.getBlockZ()));
                player.setVelocity(new Vector(0, -10, 0));
                player.setHealth(0.0D);
            }
            else if (args[0].equalsIgnoreCase("wtf"))
            {
                TFM_Util.bcastMsg(player.getName() + " is being a damn idiot.", ChatColor.RED);
                player.sendMessage(ChatColor.RED + "What the hell are you doing you damn idiot?");
                FOPM_Util.strikeLightningInPlace(player);
                player.setHealth(0.0D);
            }
            else if (args[0].equalsIgnoreCase("fgt"))
            {
                TFM_Util.bcastMsg(player.getName() + " doesn't know when to stop.", ChatColor.RED);
                player.getInventory().clear();
                player.closeInventory();
                player.setHealth(0.0D);
                FOPM_Util.strikeLightningInPlace(player);
            }
            else if (args[0].equalsIgnoreCase("drown"))
            {
                TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
                TFM_Util.adminAction(sender_p.getName(), "Drowning " + player.getName(), true);

                // Let's be nice to our admins
                if (!TFM_AdminList.isSuperAdmin(player))
                {
                    playerdata.setCommandsBlocked(true);
                }

                playerdata.setHalted(true);
                playerdata.setFrozen(true);
                player.setGameMode(GameMode.SURVIVAL);
                playerdata.setCaged(true, player.getLocation(), Material.GLASS, Material.WATER);
                player.setFoodLevel(0);
                playerdata.setMuted(true);
            }
        }
        else
        {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    private void cancelLockup(TFM_PlayerData playerdata)
    {
        BukkitTask lockupScheduleID = playerdata.getLockupScheduleID();
        if (lockupScheduleID != null)
        {
            lockupScheduleID.cancel();
            playerdata.setLockupScheduleID(null);
        }
    }
}
