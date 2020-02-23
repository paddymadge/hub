package nl.devv.hub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        super.onEnable();
        Objects.requireNonNull(Bukkit.getWorld("world")).setSpawnLocation(3, 61, 0);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
            Inventory inv = Bukkit.createInventory(null, 9*3, ChatColor.LIGHT_PURPLE + "Server Selector");
            for (int i = 0; i < inv.getContents().length; i++) {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.DARK_GRAY + "");
                item.setItemMeta(itemMeta);
                inv.setItem(i, item);
            }

            ItemStack Kingdom = new ItemStack(Material.BOOK);
            ItemMeta KingdomMeta = Kingdom.getItemMeta();
            assert KingdomMeta != null;
            KingdomMeta.setDisplayName(ChatColor.GOLD + "Kingdom");
            Kingdom.setItemMeta(KingdomMeta);

            ItemStack Skyblock = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta SkyblockMeta = Skyblock.getItemMeta();
            assert SkyblockMeta != null;
            SkyblockMeta.setDisplayName(ChatColor.BLUE + "Skyblock");
            Skyblock.setItemMeta(SkyblockMeta);

            inv.setItem(11, Kingdom);
            inv.setItem(15, Skyblock);

            player.openInventory(inv);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("hub.admin")) {
            event.setCancelled(false);
        }  else {
            event.setCancelled(true);
        }

        if (!ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Server Selector"))
            return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        switch (event.getCurrentItem().getType()) {
            case BOOK:
                player.sendMessage(ChatColor.GOLD + "Je wordt naar de Kingdom server gestuurd!");
                out.writeUTF("Connect");
                out.writeUTF("Kingdom");
                player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
                player.closeInventory();
                break;

            case GRASS_BLOCK:
                player.sendMessage(ChatColor.BLUE + "Je wordt naar de Skyblock server gestuurd!");
                out.writeUTF("Connect");
                out.writeUTF("Skyblock");
                player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
                player.closeInventory();
                break;

            default:
                player.closeInventory();
                break;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("hub.admin")) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        } else event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0.483, 65, 0.624));
        event.getPlayer().sendTitle(ChatColor.DARK_PURPLE + "Welkom op LekkerNetworks!", ChatColor.LIGHT_PURPLE + "Veel plezier!", 30, 20, 20);
        event.getPlayer().getInventory().clear();
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Server Selector");
        itemStack.setItemMeta(itemMeta);
        event.getPlayer().getInventory().setItem(4, itemStack);
        event.getPlayer().setAllowFlight(true);
        event.getPlayer().setFlying(false);
        event.getPlayer().getLocation().add(0,1,0);
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType()== EntityType.VILLAGER) {
            event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Je hebt een easter egg gevonden!");
            ItemStack itemStack = new ItemStack(Material.EGG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Easter Egg");
            itemStack.setItemMeta(itemMeta);
            event.getPlayer().getInventory().setItem(1, itemStack);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE && event.getPlayer().hasPermission("hub.admin")) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onStarvationEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onTNTIgniteEvent(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage("");
    }

    @EventHandler
    public void onDisconnectEvent(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler
    public void noUproot(PlayerInteractEvent event) {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getBlockY() < 0) {
            event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0.483, 65, 0.624));
        }
    }
}

