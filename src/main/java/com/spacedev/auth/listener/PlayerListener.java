package com.spacedev.auth.listener;

import com.spacedev.auth.Auth;
import com.spacedev.auth.util.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.spacedev.auth.util.ChatUtils.getPluginMessage;
import static com.spacedev.auth.util.InventoryCreator.openPinInventory;

public class PlayerListener implements Listener {

    public static HashMap<UUID, String> enteredPin;
    public static HashMap<UUID, Integer> attempts;
    public static HashMap<UUID, Boolean> inPinMenu;
    private final Auth plugin;

    public PlayerListener(Auth plugin) {
        this.plugin = plugin;

        enteredPin = new HashMap<>();
        attempts = new HashMap<>();
        inPinMenu = new HashMap<>();

    }

    private static void clearHashMaps(UUID uuid) {

        enteredPin.remove(uuid);
        attempts.remove(uuid);
        inPinMenu.remove(uuid);

    }

    private static String enterPinMessage(Auth plugin, UUID uuid) {

        String underscores = "";
        int maxPinLength = plugin.getConfig().getInt("Pin-Length");


        for (int i = 0; i < maxPinLength - enteredPin.get(uuid).length(); i++) {
            underscores = underscores.concat(" &7_");
        }

        return "&7" + enteredPin.get(uuid) + underscores;
    }

    @EventHandler // Join listener to display the Auth Menu
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (player.hasPermission(plugin.getConfig().getString("AuthDisplay-Permission"))) {
            openPinInventory(plugin, player, "&7&lEnter Pin:");

            // Player has Permission, add values to hashmap

            enteredPin.put(uuid, "");
            attempts.put(uuid, 0);
            inPinMenu.put(uuid, true);

        }

    }

    @EventHandler // Handle Inventory Interaction
    public void onInventoryInteraction(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        String slot = String.valueOf(event.getRawSlot() + 1);
        String playersPin = "1234";
        UUID uuid = player.getUniqueId();

        event.setCancelled(true);
        if (Integer.parseInt(slot) >= 1 && Integer.parseInt(slot) <= 9) {


            enteredPin.put(uuid, enteredPin.get(uuid) + slot);

            // Check if players pin should be longer or not
            if (enteredPin.get(uuid).length() == plugin.getConfig().getInt("Pin-Length")) {

                if (enteredPin.get(uuid).equals(playersPin)) { // Players pin is correct, close inventory
                    clearHashMaps(uuid);
                    player.closeInventory();

                    player.sendMessage(getPluginMessage(plugin, "&a&lCorrect Pin!"));

                } else { // Players pin is correct length, but incorrect pin

                    enteredPin.put(uuid, "");
                    attempts.put(uuid, attempts.get(uuid) + 1);
                    // Check if players attempts is over the max
                    if (attempts.get(uuid) >= plugin.getConfig().getInt("Attempts-Before-Kick")) {

                        player.kickPlayer(getPluginMessage(
                                plugin,
                                plugin.getConfig().getString("Incorrect-Pin-Past-Attempts")
                        ));

                    } else { // Incorrect pin, but player has more attempts

                        openPinInventory(plugin, player, "&c&lIncorrect Pin");

                        enteredPin.put(uuid, "");

                        return;

                    }


                }

            }

            openPinInventory(plugin, player, enterPinMessage(plugin, uuid));

        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (inPinMenu.get(event.getPlayer().getUniqueId())) {
            UUID uuid = event.getPlayer().getUniqueId();

            openPinInventory(plugin, (Player) event.getPlayer(), enterPinMessage(plugin, uuid));

        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        clearHashMaps(event.getPlayer().getUniqueId());
    }
}