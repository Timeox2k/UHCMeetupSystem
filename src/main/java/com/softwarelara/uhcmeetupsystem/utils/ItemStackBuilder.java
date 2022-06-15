package com.softwarelara.uhcmeetupsystem.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBuilder {

    public static ItemStack buildItemStack(Material material,int count, String displayname) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
