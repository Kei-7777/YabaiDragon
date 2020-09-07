package me.kei.dragon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

public class MainCommandExecutor implements CommandExecutor {
    Dragon plugin;
    public MainCommandExecutor(Dragon dragon) {
        this.plugin = dragon;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Require OP.");
            return true;
        }

        if(sender instanceof BlockCommandSender || sender instanceof Player){
            // /dragon <x> <y> <z> <x2> <y2> <z2> <tick>

            if(args.length < 7){
                sender.sendMessage(ChatColor.RED + "/dragon <x> <y> <z> <x2> <y2> <z2> <tick>\n" +
                        "x,y,zからx2,y2,z2まで<tick>Tickでドラゴンが飛んでいきます。");
                return true;
            }

            if(Dragon.isLiving){
                sender.sendMessage(ChatColor.RED + "別のドラゴンがいるらしい...");
                return true;
            }


            Double x1,y1,z1,x2,y2,z2;
            int tick;
            World w = null;

            Location from;
            Location to;
            try{
                x1 = Double.parseDouble(args[0]);
                y1 = Double.parseDouble(args[1]);
                z1 = Double.parseDouble(args[2]);
                x2 = Double.parseDouble(args[3]);
                y2 = Double.parseDouble(args[4]);
                z2 = Double.parseDouble(args[5]);
                tick = Integer.parseInt(args[6]);
                if(sender instanceof Player) {
                    w = ((Player) sender).getWorld();
                } else if(sender instanceof BlockCommandSender){
                    w = ((BlockCommandSender) sender).getBlock().getWorld();
                }

                from = new Location(w, x1, y1, z1);
                to = new Location(w, x2, y2, z2);

                Dragon.locations = LocationUtils.getLocationsOnLine(from, to, from.distance(to)/tick);
                Dragon.completeTick = tick;
                Dragon.entityDragon = from.getWorld().spawn(from, EnderDragon.class);

                Dragon.entityDragon.setAI(true);

                new DragonTask(this.plugin).runTaskTimer(this.plugin, 1, 1);

                Dragon.isLiving = true;
            } catch (Exception ex){
                sender.sendMessage(ChatColor.RED + ex.getLocalizedMessage());
                return true;
            }

        } else {
            sender.sendMessage(ChatColor.RED + "Error: sender?");
            return true;
        }
        return true;
    }
}
