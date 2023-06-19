package ru.rtire.cancellmendingtrade;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CancellMendingTrade extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new onTrade(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
