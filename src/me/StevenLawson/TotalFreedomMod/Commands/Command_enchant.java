package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
import me.StevenLawson.TotalFreedomMod.TFM_Log;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@SuppressWarnings("deprecation")
@CommandPermissions(level = AdminLevel.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Enchant items.", usage = "/<command> <list | addall | reset | add <name> | remove <name>>")
public class Command_enchant extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        ItemStack itemInHand = TFM_DepreciationAggregator.getItemInHand(sender_p);

        if (itemInHand == null)
        {
            sender.sendMessage(ChatColor.RED + "You are holding an invalid item.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            boolean has_enchantments = false;

            StringBuilder possible_ench = new StringBuilder("Possible enchantments for held item: ");
            for (Enchantment ench : Enchantment.values())
            {
                if (ench.canEnchantItem(itemInHand))
                {
                    has_enchantments = true;
                    possible_ench.append(ench.getName()).append(", ");
                }
            }

            if (has_enchantments)
            {
                sender.sendMessage(ChatColor.GREEN + possible_ench.toString());
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "The held item has no enchantments.");
            }
        }
        else if (args[0].equalsIgnoreCase("addall"))
        {
            for (Enchantment ench : Enchantment.values())
            {
                try
                {
                    if (ench.canEnchantItem(itemInHand))
                    {
                        itemInHand.addEnchantment(ench, ench.getMaxLevel());
                    }
                }
                catch (Exception ex)
                {
                    TFM_Log.info("Error using " + ench.getName() + " on " + itemInHand.getType().name() + " held by " + sender_p.getName() + ".");
                }
            }

            sender.sendMessage(ChatColor.GREEN + "Added all possible enchantments for this item.");
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
            for (Enchantment ench : itemInHand.getEnchantments().keySet())
            {
                itemInHand.removeEnchantment(ench);
            }

            sender.sendMessage(ChatColor.GREEN + "Removed all enchantments.");
        }
        else
        {
            if (args.length < 2)
            {
                return false;
            }

            Enchantment ench = null;

            try
            {
                ench = Enchantment.getByName(args[1]);
            }
            catch (Exception ex)
            {
            }

            if (ench == null)
            {
                sender.sendMessage(ChatColor.RED + args[1] + " is an invalid enchantment for the held item. Type \"/enchant list\" for valid enchantments for this item.");
                return true;
            }

            if (args[0].equalsIgnoreCase("add"))
            {
                if (ench.canEnchantItem(itemInHand))
                {
                    itemInHand.addEnchantment(ench, ench.getMaxLevel());

                    sender.sendMessage(ChatColor.GREEN + "Added enchantment: " + ench.getName());
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Can't use this enchantment on held item.");
                }
            }
            else if (args[0].equals("remove"))
            {
                itemInHand.removeEnchantment(ench);

                sender.sendMessage(ChatColor.GREEN + "Removed enchantment: " + ench.getName());
            }
        }

        return true;
    }
}
