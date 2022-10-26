package com.spacedev.auth.util;

import com.spacedev.auth.Auth;
import com.spacedev.auth.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.spacedev.auth.util.ChatUtils.format;

public class InventoryCreator {

    private static Inventory getPinInventory(String title) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, format(title));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, i+1));
        }

        return inventory;

    }

    public static void openPinInventory(Auth plugin, Player player, String title) {
        UUID uuid = player.getUniqueId();
        PlayerListener.inPinMenu.put(uuid, false); // Will reopen later, gets around the close inv event
        player.closeInventory(); // Close the inventory, do not call inventory close again please...

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            player.openInventory(getPinInventory(title));
            PlayerListener.inPinMenu.put(uuid, true);

        }, 1); // Reopen after 1 tick, prevents bugs :)

    }


}
