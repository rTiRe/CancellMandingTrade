package ru.rtire.cancelmendingtrade;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class onTrade implements Listener {

    private static CancelMendingTrade plugin;

    public onTrade() { plugin = CancelMendingTrade.getInstance(); }

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
        if(e.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) {
            WanderingTrader trader = (WanderingTrader) e.getRightClicked();
            List<MerchantRecipe> recipies = new ArrayList<>(trader.getRecipes());
            for(MerchantRecipe r : recipies) {
                if(r.getResult().getItemMeta() instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta m = (EnchantmentStorageMeta) r.getResult().getItemMeta();
                    for(Map.Entry<Enchantment, Integer> entry : m.getStoredEnchants().entrySet()) {
                        if(entry.getKey().equals(Enchantment.MENDING)) {
                            return;
                        }
                    }
                }
            }
            if(Math.random() <= plugin.getConfig().getDouble("chance")) {
                ItemStack mending = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) mending.getItemMeta();
                assert meta != null;
                meta.addStoredEnchant(Enchantment.MENDING, 1, false);
                mending.setItemMeta(meta);
                MerchantRecipe recipe = new MerchantRecipe(mending, plugin.getConfig().getInt("maximumPurchasesFromTheWanderingMerchant"));
                recipe.addIngredient(new ItemStack(Material.EMERALD, random(plugin.getConfig().getInt("minPrice"), plugin.getConfig().getInt("maxPrice"))));
                recipies.add(recipe);
                trader.setRecipes(recipies);
            }
        }
    }

    private int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
