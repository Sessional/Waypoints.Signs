/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sessional.waypoints.signs;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Andrew
 */
public class WpsSignPlugin extends JavaPlugin
{
    
    private Logger log;
    private String version = "0.1";
    private File configFile;
    private boolean bukkitPermissions;
    
    @Override
    public void onEnable()
    {
        configFile = new File("./plugins/Waypoints.Signs/config.yml");
        if (!configFile.exists())
        {
            this.saveDefaultConfig();
        }
        
        if (this.getConfig().getBoolean("bukkitPermissions") == true)
        {
            bukkitPermissions = true;
        } else
        {
            bukkitPermissions = false;
        }
        
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
    }
    
    public boolean isBukkitPermissions()
    {
        return bukkitPermissions;
    }
    
    @Override
    public void onDisable()
    {
        
    }
}