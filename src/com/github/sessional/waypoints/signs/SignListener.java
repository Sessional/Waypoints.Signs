/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sessional.waypoints.signs;

import com.github.sessional.waypoints.WpsPlugin;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Andrew
 */
public class SignListener implements Listener
{

    WpsSignPlugin wpsPlugin;
    
    public SignListener(WpsSignPlugin wpsPlugin)
    {
        this.wpsPlugin = wpsPlugin;
    }
    
    public WpsSignPlugin getPlugin()
    {
        return wpsPlugin;
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        for (int i = 0; i < event.getLines().length - 1; i++)
        {
            if (event.getLine(i).equals("Waypoint:"))
            {
                WpsPlugin plug = (WpsPlugin) getPlugin().getServer().getPluginManager().getPlugin("Waypoints");
                if (plug.getCommandHandler().doesWaypointExist(event.getLine(i+1)))
                {
                    if (getPlugin().isBukkitPermissions())
                    {
                        if (event.getPlayer().hasPermission("waypoints.sign.create"))
                        {
                            event.getPlayer().sendMessage("This sign will now teleport you to waypoint " + event.getLine(i+1));
                        } else
                        {
                            event.getBlock().breakNaturally();
                            event.getPlayer().sendMessage("You do not have permissions to create a sign. " + event.getLine(i+1));
                        }
                    }
                    else
                    {
                        event.getPlayer().sendMessage("This sign will now teleport you to waypoint " + event.getLine(i+1));
                    }
                }
                else
                {
                    event.getPlayer().sendMessage("A waypoint with name " + event.getLine(i+1) + " does not seem to exist.");
                }
            }
        }
    }
    
    @EventHandler
    public void onSignUse(PlayerInteractEvent event)
    {
        if (event.hasBlock() && event.getAction() == event.getAction().RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.SIGN_POST)
            {
                Sign s = (Sign) event.getClickedBlock().getState();
                
                for (int i = 0; i < s.getLines().length - 1; i++)
                {
                    if (s.getLine(i).equals("Waypoint:"))
                    {
                        String wp = s.getLine(i + 1);
                        WpsPlugin plug = (WpsPlugin) getPlugin().getServer().getPluginManager().getPlugin("Waypoints");
                        if (getPlugin().isBukkitPermissions())
                        {
                            if (event.getPlayer().hasPermission("waypoints.basic.go") || event.getPlayer().hasPermission("waypoints.sign.go"))
                            {
                                plug.getCommandHandler().doGo(event.getPlayer(), new String[]
                                        {
                                            wp
                                        });
                            } else
                            {
                                event.getPlayer().sendMessage("You do not have permission to sign waypoint.");
                            }
                        } else
                        {
                            plug.getCommandHandler().doGo(event.getPlayer(), new String[]
                                    {
                                        wp
                                    });
                        }
                    }
                }
            }
        }
    }
}