package Canjas;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Timebomb extends JavaPlugin {
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new TimebombListener(this), this);
    }
}
