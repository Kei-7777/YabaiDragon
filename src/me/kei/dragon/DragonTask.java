package me.kei.dragon;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
            Dragon.entityDragon.teleportAsync(Dragon.locations.get(0));
            Dragon.locations.remove(0);
            Bukkit.broadcastMessage("Dragon: " + Dragon.nowTick + "/" + Dragon.completeTick + " tick");
            Dragon.nowTick++;
        }
    }
}
