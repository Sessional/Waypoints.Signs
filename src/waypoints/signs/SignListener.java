/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waypoints.signs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
                if (wpsPlugin.getPlugin().doesWaypointExist(event.getLine(i + 1))) {
                    if (getPlugin().isBukkitPermissions()) {
                        if (event.getPlayer().hasPermission("waypoints.signs.create")) {
                            event.getPlayer().sendMessage("This sign will now teleport you to waypoint §a" + event.getLine(i + 1));
                            event.setLine(i, "§aWaypoint:");
                            ((Sign) event.getBlock().getState()).update();
                        } else {
                            event.getBlock().breakNaturally();
                            event.getPlayer().sendMessage("You do not have permissions to create a sign. " + event.getLine(i + 1));
                        }
                    } else {
                        event.getPlayer().sendMessage("This sign will now teleport you to waypoint §a" + event.getLine(i + 1));
                        event.setLine(i, "§aWaypoint:");
                        ((Sign) event.getBlock().getState()).update();
                    }
                } else {
                    event.getPlayer().sendMessage("A waypoint with name §a" + event.getLine(i + 1) + "§r does not seem to exist.");
                }
            }

            if (event.getLine(i).contains("Destination:")) {
                if (!wpsPlugin.getPlugin().doesWaypointExist(event.getLine(i + 1))) {
                    if (getPlugin().isBukkitPermissions()) {
                        if (event.getPlayer().hasPermission("waypoints.signs.create")) {
                            event.getPlayer().sendMessage("This sign will now be linked to waypoint §a" + event.getLine(i + 1));
                            event.setLine(i, "§aDestination:");
                            ((Sign) event.getBlock().getState()).update();
                            wpsPlugin.getPlugin().getCommandHandler().doCreateRemote(event.getPlayer(), event.getLine(i + 1), event.getBlock().getWorld().getName(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
                        } else {
                            event.getBlock().breakNaturally();
                            event.getPlayer().sendMessage("You do not have permissions to create a sign. " + event.getLine(i + 1));
                        }
                    } else {
                        event.getPlayer().sendMessage("This sign will now be linked to waypoint §a" + event.getLine(i + 1));
                        event.setLine(i, "§aDestination:");
                        ((Sign) event.getBlock().getState()).update();
                        wpsPlugin.getPlugin().getCommandHandler().doCreateRemote(event.getPlayer(), event.getLine(i + 1), event.getBlock().getWorld().getName(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
                    }
                } else {
                    event.getPlayer().sendMessage("The waypoint §a" + event.getLine(i + 1) + "§r already exists!");
                    ((Sign) event.getBlock().getState()).getBlock().breakNaturally();
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
                        if (getPlugin().isBukkitPermissions()) {
                            if (event.getPlayer().hasPermission("waypoints.go") || event.getPlayer().hasPermission("waypoints.signs.go")) {
                                if (!s.getLine(i).equals("§aWaypoint:")) {
                                    s.setLine(i, "§aWaypoint:");
                                    s.update();
                                }
                                wpsPlugin.getPlugin().getCommandHandler().doGo(event.getPlayer(), wp);
                            } else {
                                event.getPlayer().sendMessage("You do not have permission to sign waypoint.");
                            }
                        } else {
                            if (!s.getLine(i).equals("§aWaypoint:")) {
                                s.setLine(i, "§aWaypoint:");
                                s.update();
                            }
                            wpsPlugin.getPlugin().getCommandHandler().doGo(event.getPlayer(), wp);
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN) {
            Sign s = (Sign) e.getBlock().getState();
            for (int i = 0; i < s.getLines().length - 1; i++) {
                if (s.getLine(i).equals("Destination:") || s.getLine(i).equals("§aDestination:")) {
                    if (getPlugin().isBukkitPermissions()) {
                        if (e.getPlayer().hasPermission("waypoints.signs.destroy")) {
                            getPlugin().getPlugin().getCommandHandler().doDelete(s.getLine(i + 1));
                            e.getPlayer().sendMessage("The waypoint §a" + s.getLine(i + 1) + "§r has been deleted.");
                        } else {
                            e.getPlayer().sendMessage("You don't have permission to destroy that sign.");
                        }
                    } else {
                        getPlugin().getPlugin().getCommandHandler().doDelete(s.getLine(i + 1));
                        e.getPlayer().sendMessage("The waypoint §a" + s.getLine(i + 1) + "§r has been deleted.");
                    }
                }
            }
        }
    }
}
