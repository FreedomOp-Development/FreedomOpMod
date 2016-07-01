package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Commands.TFM_Command;
import org.bukkit.command.CommandSender;

public class FOPM_Construct extends TotalFreedomMod
{
    
    public FOPM_Construct() {
        
    }
    
    public void destroyPussy(CommandSender sender) {
        sender.sendMessage("You are a pussy");
    }
    
    public TotalFreedomMod getPlugin() {
        return TotalFreedomMod.plugin;
    }
    
    public void getThatLogDou(String message) {
        TFM_Log.info(message);
    }
    
    public TFM_Command getCommandsWut() {
        return TFM_Command.commandokay;
    }
}
