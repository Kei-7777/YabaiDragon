package me.kei.dragon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class DragonTask extends BukkitRunnable {

    Dragon plugin;
    public DragonTask(Dragon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(Dragon.entityDragon.isDead()) {
            Dragon.reset();
            this.cancel();
        } else if(Dragon.locations.size() < 1){
            Dragon.reset();
            this.cancel();
        } else {
            Location from = Dragon.entityDragon.getLocation();
            Location to = Dragon.locations.get(0);
            if(from.getZ() > to.getZ()) to.setYaw(0);
            else if(from.getX() < to.getX() && from.getZ() > to.getZ()) to.setYaw(45);
            else if(from.getX() < to.getX()) to.setYaw(90);
            else if(from.getX() < to.getX() && from.getZ() < to.getZ()) to.setYaw(135);
            else if(from.getZ() < to.getZ()) to.setYaw(180);
            else if(from.getZ() < to.getZ() && from.getX() > to.getX()) to.setYaw(225);
            else if(from.getX() > to.getX()) to.setYaw(270);

            Dragon.entityDragon.teleportAsync(to);
            Dragon.locations.remove(0);
            //Bukkit.broadcastMessage("Dragon: " + Dragon.nowTick + "/" + Dragon.completeTick + " tick");
            Dragon.nowTick++;

        }
    }
}
