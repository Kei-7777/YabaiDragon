package me.kei.dragon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Dragon extends JavaPlugin {

    static List<Location> locations;
    static EnderDragon entityDragon;
    static boolean isLiving = false;
    static int nowTick;
    static int completeTick;

    @Override
    public void onEnable() {

        reset();
        Bukkit.getPluginCommand("dragon").setExecutor(new MainCommandExecutor(this));

    }

    static void reset(){
        locations = new ArrayList<>();
        if(entityDragon != null && (!entityDragon.isDead() || !entityDragon.isEmpty())) entityDragon.remove();
        entityDragon = null;
        isLiving = false;
        nowTick = 0;
        completeTick = 0;
    }
}
