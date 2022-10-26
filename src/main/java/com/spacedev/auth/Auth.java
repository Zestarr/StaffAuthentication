package com.spacedev.auth;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.spacedev.auth.listener.PlayerListener;
import com.spacedev.auth.util.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Auth extends JavaPlugin {

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);


    }

}
