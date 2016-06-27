package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import me.StevenLawson.TotalFreedomMod.World.TFM_PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Goto the pvpworld.", usage = "/<command>")
public class Command_pvpworld extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (TFM_ConfigEntry.FLATLANDS_GENERATE.getBoolean())
        {
            TFM_PvpWorld.getInstance().sendToWorld(sender_p);
            sender.setOp(false);
            sender_p.setGameMode(GameMode.SURVIVAL);
        }
        else
        {
            sender.sendMessage(ChatColor.GRAY + "The world is currently disabled.");
        }
        return true;
    }
}
