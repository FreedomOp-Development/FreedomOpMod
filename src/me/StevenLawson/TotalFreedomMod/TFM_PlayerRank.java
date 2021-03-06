package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TFM_PlayerRank
{
    WEB("the " + ChatColor.YELLOW + "Chief Web Developer!", ChatColor.YELLOW + "[Chief Web Developer]"), CDEV("the " + ChatColor.DARK_PURPLE + "Chief Developer!",
            ChatColor.DARK_PURPLE + "[Chief Developer]"), SECURITY("the " + ChatColor.RED + "Chief of Security", ChatColor.RED + "[Chief of Security]"), SYS("a " + ChatColor.DARK_RED + "System Admin",
                    ChatColor.DARK_RED + "[Sys-Admin]"), SPEC_EXEC("a " + ChatColor.YELLOW + "Special Executive", ChatColor.YELLOW + "[Spec-Exec]"), TF_DEVELOPER(
                            "a " + ChatColor.DARK_PURPLE + "TotalFreedom Developer",
                            ChatColor.DARK_PURPLE + "[TF-Dev]"), DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "[Dev]"), IMPOSTOR(
                                    "an " + ChatColor.YELLOW + ChatColor.UNDERLINE + "Impostor", ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "[IMP]"), NON_OP("a " + ChatColor.GREEN + "Non-OP",
                                            ChatColor.GREEN.toString()), OP("an " + ChatColor.RED + "OP", ChatColor.RED + "[OP]"), SUPER("a " + ChatColor.GOLD + "Super Admin",
                                                    ChatColor.GOLD + "[SA]"), TELNET("a " + ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"), SENIOR(
                                                            "a " + ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "[SrA]"), OWNER("the " + ChatColor.BLUE + "Owner",
                                                                    ChatColor.BLUE + "[Owner]"), CONSOLE("The " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "[Console]");
    private String loginMessage;
    private String prefix;

    private TFM_PlayerRank(String loginMessage, String prefix)
    {
        this.loginMessage = loginMessage;
        this.prefix = prefix;
    }

    public static String getLoginMessage(CommandSender sender)
    {
        // Handle console
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        // Handle admins
        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            // Player is not an admin
            return fromSender(sender).getLoginMessage();
        }

        // Custom login message
        final String loginMessage = entry.getCustomLoginMessage();

        if (loginMessage == null || loginMessage.isEmpty())
        {
            return fromSender(sender).getLoginMessage();
        }

        return ChatColor.translateAlternateColorCodes('&', loginMessage);
    }

    public static TFM_PlayerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return CONSOLE;
        }

        if (TFM_AdminList.isAdminImpostor((Player) sender))
        {
            return IMPOSTOR;
        }
        if (sender.getName().equals("buildcarter8"))
        {
            return CDEV;
        }
        if (sender.getName().equals("cowgomooo12"))
        {
            return SYS;
        }
        if (sender.getName().equals("EnderLolzeh"))
        {
            return SYS;
        }
        if (sender.getName().equals("keemismydad"))
        {
            return SECURITY;
        }
        if (TFM_Util.EXECS.contains(sender.getName()))
        {
            return SPEC_EXEC;
        }
        if (TFM_Util.TF_DEVELOPERS.contains(sender.getName()))
        {
            return TF_DEVELOPER;
        }
        if (TFM_Util.DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
        }

        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);

        final TFM_PlayerRank rank;

        if (entry != null && entry.isActivated())
        {
            if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
            {
                return OWNER;
            }

            if (entry.isSeniorAdmin())
            {
                rank = SENIOR;
            }
            else if (entry.isTelnetAdmin())
            {
                rank = TELNET;
            }
            else
            {
                rank = SUPER;
            }
        }

        else if (sender.isOp())
        {
            rank = OP;
        }
        else
        {
            rank = NON_OP;
        }
        return rank;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getLoginMessage()
    {
        return loginMessage;
    }
}