package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Use the camera", usage = "/<command> <selfie | throw>")
public class Command_camera extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (args.length == 1)
        {
            if (args[0].equals("selfie"))
            {
                sender_p.chat("op me");
                sender_p.chat("But first, let me take a selfie");
                sender_p.chat("SELFIEEEEEE");
                TFM_Util.adminAction(ChatColor.GOLD + "WARNING: " + sender.getName(), "has started taking selfies on the server, Tell them that they are pretty!!", false);
                return true;
            }

            else if (args[0].equals("throw"))
            {
                sender_p.chat("F*ck u camera u bitch");
                TFM_Util.adminAction(ChatColor.GREEN + "WARNING: " + sender.getName(), "has thrown the camera they are a bad person they didnt take a selfie!", false);
                return true;
            }
        }

        return true;
    }
}
