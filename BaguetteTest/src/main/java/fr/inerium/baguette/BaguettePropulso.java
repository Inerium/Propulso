package fr.inerium.baguette;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class BaguettePropulso implements Listener {
	
	private ItemStack baguettePropulso;
    private final Map<UUID, Instant> cooldownMap = new HashMap<>();

    public BaguettePropulso() {
        baguettePropulso = new ItemStack(Material.STICK);
        ItemMeta itemMeta = baguettePropulso.getItemMeta();

        itemMeta.setDisplayName(ChatColor.BLUE + "Baguette Magique (Propulso)");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        baguettePropulso.setItemMeta(itemMeta);
    }

    private void shoot(Player player) {
    	 

        if (!player.isSneaking()) {
            Vector direction = player.getLocation().getDirection();
            player.setVelocity(direction.add(new Vector(0, -.2, 0)).multiply(-2D));
        }
        
       
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, .5f, 1);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY, 2);
        player.spawnParticle(Particle.REDSTONE,
                player.getLocation().add(0, 1, 0), 10, .5, .5, .5, dustOptions);

        Arrow arrow = player.launchProjectile(Arrow.class, player.getLocation().getDirection().multiply(4D));
        cooldownMap.put(player.getUniqueId(), Instant.now());
    }


    @EventHandler
    public void baguettePropulsoShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.isSneaking() && !player.getInventory().contains(baguettePropulso)) {
            player.getInventory().addItem(baguettePropulso);
            return;
        }
        if(!(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(baguettePropulso.getItemMeta().getDisplayName()))){
         	return;
         }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
        	
            if (!cooldownMap.containsKey(player.getUniqueId())) {
            	
                shoot(player);
                event.setCancelled(true);
            } else {
                Instant lastCast = cooldownMap.get(player.getUniqueId());
                Instant currentTime = Instant.now();

                int reloadTime = 3;

                if (currentTime.isAfter(lastCast.plusSeconds(reloadTime))) {
                    shoot(player);
                    event.setCancelled(true);
                    return;
                }

                player.playSound(player.getLocation(),Sound.BLOCK_DISPENSER_FAIL,1,1);
                player.sendMessage(ChatColor.GRAY + "Rechargement en cours...");
            }
           
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.getEntity();
        if(!(event.getCause() == DamageCause.ENTITY_ATTACK))
            return;
        if(!(((EntityDamageByEntityEvent)event).getDamager() instanceof Player))
            return;
        Player player = (Player) ((EntityDamageByEntityEvent)event).getDamager();
        if(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) {
            event.setCancelled(true);
            System.out.println("test");

        }

    }

}