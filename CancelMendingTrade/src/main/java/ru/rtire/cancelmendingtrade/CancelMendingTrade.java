package ru.rtire.cancelmendingtrade;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CancelMendingTrade extends JavaPlugin {

    private static CancelMendingTrade instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new onTrade(), this);
        getLogger().info("from TiRe");

        File config = new File(getDataFolder() + File.separator + "src/main/resources/config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CancelMendingTrade getInstance() {
        return instance;
    }
}
