package Canjas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TimebombListener implements Listener {

    private final Timebomb plugin;

    public TimebombListener(Timebomb plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInteract(PlayerDeathEvent event) {

        final Player player = event.getEntity();
        final Location loc = player.getLocation().clone();
        Block block = loc.getBlock();

        block = block.getRelative(BlockFace.DOWN);
        block.setType(Material.CHEST);

        BlockData leftData = block.getBlockData();
        ((Directional) leftData).setFacing(BlockFace.NORTH);
        block.setBlockData(leftData);
        Chest chestDataLeft = (Chest) leftData;
        chestDataLeft.setType(Chest.Type.RIGHT);
        block.setBlockData(chestDataLeft);
        org.bukkit.block.Chest chest = (org.bukkit.block.Chest) block.getState();
        block = block.getRelative(BlockFace.WEST);
        block.setType(Material.CHEST);
        BlockData rightData = block.getBlockData();
        ((Directional) rightData).setFacing(BlockFace.NORTH);
        block.setBlockData(rightData);
        Chest chestDataRight = (Chest) rightData;
        chestDataRight.setType(Chest.Type.LEFT);
        block.setBlockData(chestDataRight);


        for (ItemStack item : event.getDrops()) {
            if (item == null || item.getType() == Material.AIR)
                continue;
            chest.getInventory().addItem(item);
        }
        event.getDrops().clear();
        final ArmorStand stand = player.getWorld().spawn(chest.getLocation().clone().add(0, 1.0, 0), ArmorStand.class);
        stand.setCustomNameVisible(true);
        stand.setSmall(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setMarker(true);

        new BukkitRunnable() {

            private int time = 45;

            public void run() {
                time--;
                if (time == 0) {
                    Bukkit.broadcastMessage("§aLe corps de §6" + player.getName() + "§5 a explosé !");
                    loc.getBlock().setType(Material.AIR);
                    loc.getWorld().createExplosion(loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5, 5, false, true);
                    loc.getWorld().strikeLightning(loc);
                    stand.remove();
                    cancel();
                }
                else if (time == 1) {
                    stand.setCustomName("§4" + time + "s");
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(chest.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                }
                else if (time == 2) {
                    stand.setCustomName("§c" + time + "s");
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(chest.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                }
                else if (time == 3) {
                    stand.setCustomName("§6" + time + "s");
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(chest.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                }
                else if (time <= 15) {
                    stand.setCustomName("§e" + time + "s");
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(chest.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 0);
                }
                else
                    stand.setCustomName("§a" + time + "s");
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
