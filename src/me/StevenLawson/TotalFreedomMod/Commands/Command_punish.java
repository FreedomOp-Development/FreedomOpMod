package me.StevenLawson.TotalFreedomMod.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Joiner;

import net.md_5.bungee.api.ChatColor;

@CommandParameters(description="A simple punish GUI", usage="/<command> <player>")
public class Command_punish extends TFM_Command implements Listener
{
	
	public String offender;

	@Override
	public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args,
			boolean senderIsConsole) {
		
		if(sender instanceof Player)
		{
			
			if(Bukkit.getOnlinePlayers().contains(args[0]))
			{
			
			offender = args[0];
				
			Inventory inv = Bukkit.getServer().createInventory(null, 54, "Punish GUI");
			
			
			ItemStack spawnItem = nameItem(new ItemStack(Material.WOOL), ChatColor.RED + "Mute " + args[0]);
			
			inv.setItem(18, spawnItem);
			
			
			
			((Player) sender).openInventory(inv);
			}
		}
		

		return false;
	}
	
	private ItemStack nameItem(ItemStack item, String name) {
	ItemMeta meta = item.getItemMeta();	
	
	meta.setDisplayName(name);
	
	item.setItemMeta(meta);
	return item;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		
		Player player = (Player) e.getWhoClicked();
		
		Inventory inv = e.getInventory();
		
		if(!(inv.getName() == "Punish GUI")) 
			return;
		
		if(!(e.getWhoClicked() instanceof Player))  
			return;
		
		ItemStack item = e.getCurrentItem();
		
		if (item.getType() == Material.WOOL)
		{
			
			Bukkit.dispatchCommand(e.getWhoClicked(), "stfu " + offender);
			
			e.setCancelled(true);
			player.closeInventory();
		}
		
	}
	
}
