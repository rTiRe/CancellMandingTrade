package ru.rtire.cancellmendingtrade;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class onTrade implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void on(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            Villager villager = (Villager) e.getRightClicked();
            for(MerchantRecipe r : villager.getRecipes()) {
                if(r.getResult().getItemMeta() instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) r.getResult().getItemMeta();
                    for(Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                        if(entry.getKey().equals(Enchantment.MENDING)) {
                            r.setMaxUses(0);
                        }
                    }
                }
            }
        }
    }
}
