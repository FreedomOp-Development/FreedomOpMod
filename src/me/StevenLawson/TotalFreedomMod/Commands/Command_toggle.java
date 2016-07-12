package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler;
import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler.TFM_GameRule;
import net.md_5.bungee.api.ChatColor;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Toggles TotalFreedomMod settings", usage = "/<command> [option] [value] [value]")
public class Command_toggle extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.GRAY + "Available toggles: ");
            sender.sendMessage(ChatColor.GRAY + "- waterplace");
            sender.sendMessage(ChatColor.GRAY + "- fireplace");
            sender.sendMessage(ChatColor.GRAY + "- lavaplace");
            sender.sendMessage(ChatColor.GRAY + "- fluidspread");
            sender.sendMessage(ChatColor.GRAY + "- lavadmg");
            sender.sendMessage(ChatColor.GRAY + "- firespread");
            sender.sendMessage(ChatColor.GRAY + "- prelog");
            sender.sendMessage(ChatColor.GRAY + "- lockdown");
            sender.sendMessage(ChatColor.GRAY + "- petprotect");
            sender.sendMessage(ChatColor.GRAY + "- droptoggle");
            sender.sendMessage(ChatColor.GRAY + "- nonuke");
            sender.sendMessage(ChatColor.GRAY + "- explosives");
            return false;
        }

        if (args[0].equals("waterplace"))
        {
            toggle(sender, "Water placement is", TFM_ConfigEntry.ALLOW_WATER_PLACE);
            return true;
        }

        if (args[0].equals("fireplace"))
        {
            toggle(sender, "Fire placement is", TFM_ConfigEntry.ALLOW_FIRE_PLACE);
            return true;
        }

        if (args[0].equals("lavaplace"))
        {
            toggle(sender, "Lava placement is", TFM_ConfigEntry.ALLOW_LAVA_PLACE);
            return true;
        }

        if (args[0].equals("fluidspread"))
        {
            toggle(sender, "Fluid spread is", TFM_ConfigEntry.ALLOW_FLUID_SPREAD);
            return true;
        }

        if (args[0].equals("lavadmg"))
        {
            toggle(sender, "Lava damage is", TFM_ConfigEntry.ALLOW_LAVA_DAMAGE);
            return true;
        }

        if (args[0].equals("firespread"))
        {
            toggle(sender, "Fire spread is", TFM_ConfigEntry.ALLOW_FIRE_SPREAD);
            TFM_GameRuleHandler.setGameRule(TFM_GameRule.DO_FIRE_TICK, TFM_ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean());
            return true;
        }

        if (args[0].equals("prelog"))
        {
            toggle(sender, "Command prelogging is", TFM_ConfigEntry.ENABLE_PREPROCESS_LOG);
            return true;
        }

        if (args[0].equals("lockdown"))
        {
            TFM_Util.adminAction(sender.getName(), (TotalFreedomMod.lockdownEnabled ? "De-a" : "A") + "ctivating server lockdown", true);
            TotalFreedomMod.lockdownEnabled = !TotalFreedomMod.lockdownEnabled;
            return true;
        }

        if (args[0].equals("petprotect"))
        {
            toggle(sender, "Tamed pet protection is", TFM_ConfigEntry.ENABLE_PET_PROTECT);
            return true;
        }

        if (args[0].equals("droptoggle"))
        {
            toggle(sender, "Automatic entity wiping is", TFM_ConfigEntry.AUTO_ENTITY_WIPE);
            return true;
        }

        if (args[0].equals("nonuke"))
        {
            if (args.length >= 2)
            {
                try
                {
                    TFM_ConfigEntry.NUKE_MONITOR_RANGE.setDouble(Math.max(1.0, Math.min(500.0, Double.parseDouble(args[1]))));
                }
                catch (NumberFormatException nfex)
                {
                }
            }

            if (args.length >= 3)
            {
                try
                {
                    TFM_ConfigEntry.NUKE_MONITOR_COUNT_BREAK.setInteger(Math.max(1, Math.min(500, Integer.parseInt(args[2]))));
                }
                catch (NumberFormatException nfex)
                {
                }
            }

            toggle(sender, "Nuke monitor is", TFM_ConfigEntry.NUKE_MONITOR_ENABLED);

            if (TFM_ConfigEntry.NUKE_MONITOR_ENABLED.getBoolean())
            {
                sender.sendMessage(ChatColor.GRAY + "Anti-freecam range is set to " + TFM_ConfigEntry.NUKE_MONITOR_RANGE.getDouble() + " blocks.");
                sender.sendMessage(ChatColor.GRAY + "Block throttle rate is set to " + TFM_ConfigEntry.NUKE_MONITOR_COUNT_BREAK.getInteger() + " blocks destroyed per 5 seconds.");
            }

            return true;
        }
        if (args[0].equals("explosives"))
        {
            if (args.length == 2)
            {
                try
                {
                    TFM_ConfigEntry.EXPLOSIVE_RADIUS.setDouble(Math.max(1.0, Math.min(30.0, Double.parseDouble(args[1]))));
                }
                catch (NumberFormatException ex)
                {
                    sender.sendMessage(ex.getMessage());
                    return true;
                }
            }

            toggle(sender, "Explosions are", TFM_ConfigEntry.ALLOW_EXPLOSIONS);

            if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean())
            {
                sender.sendMessage(ChatColor.GRAY + "Radius set to " + TFM_ConfigEntry.EXPLOSIVE_RADIUS.getDouble());
            }
            return true;
        }

        return false;
    }

    private void toggle(CommandSender sender, String name, TFM_ConfigEntry entry)
    {
        sender.sendMessage(ChatColor.GRAY + name + " now " + (entry.setBoolean(!entry.getBoolean()) ? "enabled." : "disabled."));
    }
}