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
import org.bukkit.inventory.meta.ItemMeta;

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
                ItemMeta rMeta = r.getResult().getItemMeta();
                Material rType = r.getResult().getType();
                if(rMeta instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) rMeta;
                    for(Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                        if(entry.getKey().equals(Enchantment.MENDING)) {
                            r.setMaxUses(0);
                        }
                    }
                }
                if(rType.equals(Material.DIAMOND) || rType.equals(Material.DIAMOND_BLOCK) || rType.equals(Material.DIAMOND_ORE) || rType.equals(Material.DEEPSLATE_DIAMOND_ORE)
                || rType.equals(Material.DIAMOND_HELMET) || rType.equals(Material.DIAMOND_CHESTPLATE) || rType.equals(Material.DIAMOND_LEGGINGS) || rType.equals(Material.DIAMOND_BOOTS)
                || rType.equals(Material.DIAMOND_AXE) || rType.equals(Material.DIAMOND_HOE) || rType.equals(Material.DIAMOND_PICKAXE) || rType.equals(Material.DIAMOND_SHOVEL) || rType.equals(Material.DIAMOND_SWORD) || rType.equals(Material.DIAMOND_HORSE_ARMOR)) {
                    if(plugin.getConfig().getBoolean("cancelDiamonds")) {
                        r.setMaxUses(0);
                    }
                    else {
                        r.setMaxUses(3);
                    }
                }
            }
        }
        if(e.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) {
            WanderingTrader trader = (WanderingTrader) e.getRightClicked();
            List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
            for(MerchantRecipe r : recipes) {
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
                recipes.add(recipe);
                trader.setRecipes(recipes);
            }
        }
    }

    private int random(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
