/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waypoints.signs;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import waypoints.WaypointPlugin;

/**
 *
 * @author Andrew
 */
public class SignListener implements Listener {

    WpsSignPlugin wpsPlugin;

    public SignListener(WpsSignPlugin wpsPlugin) {
        this.wpsPlugin = wpsPlugin;
    }

    public WpsSignPlugin getPlugin() {
        return wpsPlugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length - 1; i++) {
            if (event.getLine(i).contains("Waypoint:")) {
                WaypointPlugin plug = (WaypointPlugin) getPlugin().getServer().getPluginManager().getPlugin("Waypoints");
                if (plug.doesWaypointExist(event.getLine(i + 1))) {
                    if (getPlugin().isBukkitPermissions()) {
                        if (event.getPlayer().hasPermission("waypoints.signs.create")) {
                            event.getPlayer().sendMessage("This sign will now teleport you to waypoint §a" + event.getLine(i + 1));
                            event.setLine(i, "§aWaypoint:");
                        } else {
                            event.getBlock().breakNaturally();
                            event.getPlayer().sendMessage("You do not have permissions to create a sign. " + event.getLine(i + 1));
                        }
                    } else {
                        event.getPlayer().sendMessage("This sign will now teleport you to waypoint §a" + event.getLine(i + 1));
                        event.setLine(i, "§aWaypoint:");
                    }
                } else {
                    event.getPlayer().sendMessage("A waypoint with name " + event.getLine(i + 1) + " does not seem to exist.");
                }
            }
        }
    }

    @EventHandler
    public void onSignUse(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign s = (Sign) event.getClickedBlock().getState();

                for (int i = 0; i < s.getLines().length - 1; i++) {
                    if (s.getLine(i).equals("Waypoint:") || s.getLine(i).equals("§aWaypoint:")) {
                        String wp = s.getLine(i + 1);
                        WaypointPlugin plug = (WaypointPlugin) getPlugin().getServer().getPluginManager().getPlugin("Waypoints");
                        if (getPlugin().isBukkitPermissions()) {
                            if (event.getPlayer().hasPermission("waypoints.go") || event.getPlayer().hasPermission("waypoints.signs.go")) {
                                if (!s.getLine(i).equals("§aWaypoint:")) {
                                    s.setLine(i, "§aWaypoint:");
                                }
                                plug.getCommandHandler().doGo(event.getPlayer(), wp);
                            } else {
                                event.getPlayer().sendMessage("You do not have permission to sign waypoint.");
                            }
                        } else {
                            if (!s.getLine(i).equals("§aWaypoint:")) {
                                System.out.println("Set color!");
                                s.setLine(i, "§aWaypoint:");
                                s.update();
                            }
                            plug.getCommandHandler().doGo(event.getPlayer(), wp);
                        }
                    }
                }
            }
        }
    }
}
