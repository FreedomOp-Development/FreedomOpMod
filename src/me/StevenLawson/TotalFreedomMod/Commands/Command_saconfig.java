package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Admin;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Convert;
import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manage superadmins.", usage = "/<command> <list | clean | clearme [ip] | <add | delete | info> <username>>")
public class Command_saconfig extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        final SAConfigMode mode;
        try
        {
            mode = SAConfigMode.findMode(args, sender, senderIsConsole);
        }
        catch (final PermissionsException ex)
        {
            sender.sendMessage(ChatColor.GRAY + ex.getMessage());
            return true;
        }
        catch (final FormatException ex)
        {
            sender.sendMessage(ChatColor.GRAY + ex.getMessage());
            return false;
        }

        switch (mode)
        {
            case LIST:
            {
                sender.sendMessage(ChatColor.GOLD + "Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "));

                break;
            }
            case CLEAN:
            {
                TFM_Util.adminAction(sender.getName(), "Cleaning superadmin list", true);
                TFM_AdminList.cleanSuperadminList(true);
                sender.sendMessage(ChatColor.YELLOW + "Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "));

                break;
            }
            case CLEARME:
            {
                final TFM_Admin admin = TFM_AdminList.getEntry(sender_p);

                if (admin == null)
                {
                    sender.sendMessage(ChatColor.RED + "Could not find your admin entry! Please notify a developer.");
                    return true;
                }

                final String ip = TFM_Util.getIp(sender_p);

                if (args.length == 1)
                {
                    TFM_Util.adminAction(sender.getName(), "Cleaning my supered IPs", true);

                    int counter = admin.getIps().size() - 1;
                    admin.clearIPs();
                    admin.addIp(ip);

                    TFM_AdminList.saveAll();

                    sender.sendMessage(ChatColor.GRAY + TFM_Convert.toString(counter) + " IPs removed.");
                    sender.sendMessage(ChatColor.GRAY + admin.getIps().get(0) + " is now your only IP address");
                }
                else
                {
                    if (!admin.getIps().contains(args[1]))
                    {
                        sender.sendMessage(ChatColor.GRAY + "That IP is not registered to you.");
                    }
                    else if (ip.equals(args[1]))
                    {
                        sender.sendMessage(ChatColor.GRAY + "You cannot remove your current IP.");
                    }
                    else
                    {
                        TFM_Util.adminAction(sender.getName(), "Removing a supered IP", true);

                        admin.removeIp(args[1]);

                        TFM_AdminList.saveAll();

                        sender.sendMessage(ChatColor.GRAY + "Removed IP " + args[1]);
                        sender.sendMessage(ChatColor.GRAY + "Current IPs: " + StringUtils.join(admin.getIps(), ", "));
                    }
                }

                break;
            }
            case INFO:
            {
                TFM_Admin superadmin = TFM_AdminList.getEntry(args[1].toLowerCase());

                if (superadmin == null)
                {
                    final Player player = getPlayer(args[1]);
                    if (player != null)
                    {
                        superadmin = TFM_AdminList.getEntry(player.getName().toLowerCase());
                    }
                }

                if (superadmin == null)
                {
                    sender.sendMessage(ChatColor.GRAY + "Superadmin not found: " + args[1]);
                }
                else
                {
                    sender.sendMessage(ChatColor.GRAY + superadmin.toString());
                }

                break;
            }
            case ADD:
            {
                OfflinePlayer player = getPlayer(args[1], true); // Exact case-insensitive match.

                if (player == null)
                {
                    final TFM_Admin superadmin = TFM_AdminList.getEntry(args[1]);

                    if (superadmin == null)
                    {
                        sender.sendMessage(ChatColor.RED + TotalFreedomMod.PLAYER_NOT_FOUND);
                        return true;
                    }

                    player = TFM_DepreciationAggregator.getOfflinePlayer(Bukkit.getServer(), superadmin.getLastLoginName());
                }

                TFM_Util.adminAction(sender.getName(), "Adding " + player.getName() + " to the superadmin list", true);
                TFM_AdminList.addSuperadmin(player);

                if (player.isOnline())
                {
                    final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player.getPlayer());

                    if (playerdata.isFrozen())
                    {
                        playerdata.setFrozen(false);
                        Player unfrozeplayer = player.getPlayer();
                        unfrozeplayer.sendMessage(ChatColor.GRAY + "You have been unfrozen.");
                    }
                }

                break;
            }
            case DELETE:
            {
                String targetName = args[1];

                final Player player = getPlayer(targetName, true); // Exact case-insensitive match.

                if (player != null)
                {
                    targetName = player.getName();
                }

                if (!TFM_AdminList.getLowercaseSuperNames().contains(targetName.toLowerCase()))
                {
                    sender.sendMessage(ChatColor.RED + "Superadmin not found: " + targetName);
                    return true;
                }

                TFM_Util.adminAction(sender.getName(), "Removing " + targetName + " from the superadmin list", true);
                TFM_AdminList.removeSuperadmin(TFM_DepreciationAggregator.getOfflinePlayer(server, targetName));
                break;
            }
        }

        return true;
    }

    private static enum SAConfigMode
    {
        LIST("list", AdminLevel.OP, SourceType.BOTH, 1, 1),
        CLEAN("clean", AdminLevel.SENIOR, SourceType.BOTH, 1, 1),
        CLEARME("clearme", AdminLevel.SUPER, SourceType.ONLY_IN_GAME, 1, 2),
        INFO("info", AdminLevel.SUPER, SourceType.BOTH, 2, 2),
        ADD("add", AdminLevel.SUPER, SourceType.ONLY_CONSOLE, 2, 2),
        DELETE("delete", AdminLevel.SENIOR, SourceType.ONLY_CONSOLE, 2, 2);
        private final String modeName;
        private final AdminLevel adminLevel;
        private final SourceType sourceType;
        private final int minArgs;
        private final int maxArgs;

        private SAConfigMode(String modeName, AdminLevel adminLevel, SourceType sourceType, int minArgs, int maxArgs)
        {
            this.modeName = modeName;
            this.adminLevel = adminLevel;
            this.sourceType = sourceType;
            this.minArgs = minArgs;
            this.maxArgs = maxArgs;
        }

        private static SAConfigMode findMode(final String[] args, final CommandSender sender, final boolean senderIsConsole) throws PermissionsException, FormatException
        {
            if (args.length == 0)
            {
                throw new FormatException("Invalid number of arguments.");
            }

            boolean isSuperAdmin = TFM_AdminList.isSuperAdmin(sender);
            boolean isSeniorAdmin = isSuperAdmin ? TFM_AdminList.isSeniorAdmin(sender, false) : false;

            for (final SAConfigMode mode : values())
            {
                if (mode.modeName.equalsIgnoreCase(args[0]))
                {
                    if (mode.adminLevel == AdminLevel.SUPER)
                    {
                        if (!isSuperAdmin)
                        {
                            throw new PermissionsException(TotalFreedomMod.MSG_NO_PERMS);
                        }
                    }
                    else if (mode.adminLevel == AdminLevel.SENIOR)
                    {
                        if (!isSeniorAdmin)
                        {
                            throw new PermissionsException(TotalFreedomMod.MSG_NO_PERMS);
                        }
                    }

                    if (mode.sourceType == SourceType.ONLY_IN_GAME)
                    {
                        if (senderIsConsole)
                        {
                            throw new PermissionsException("This command may only be used in-game.");
                        }
                    }
                    else if (mode.sourceType == SourceType.ONLY_CONSOLE)
                    {
                        if (!senderIsConsole)
                        {
                            throw new PermissionsException("This command may only be used from the console.");
                        }
                    }

                    if (args.length >= mode.minArgs && args.length <= mode.maxArgs)
                    {
                        return mode;
                    }
                    else
                    {
                        throw new FormatException("Invalid number of arguments for mode: " + mode.modeName);
                    }
                }
            }

            throw new FormatException("Invalid mode.");
        }
    }

    private static class PermissionsException extends Exception
    {
        public PermissionsException(final String message)
        {
            super(message);
        }
    }

    private static class FormatException extends Exception
    {
        public FormatException(final String message)
        {
            super(message);
        }
    }
}