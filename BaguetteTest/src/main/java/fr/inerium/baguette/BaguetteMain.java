package fr.inerium.baguette;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BaguetteMain extends JavaPlugin {
	
	 @Override
	    public void onEnable() {
	   
	        this.getServer().getPluginManager().registerEvents(new BaguettePropulso(), this);
	        arrowTrailCreator();

	    }

	    private void arrowTrailCreator() {

	        new BukkitRunnable() {

	            @Override
	            public void run() {
	                if (Bukkit.getOnlinePlayers().size() == 0) return;

	                for (Player player : Bukkit.getOnlinePlayers()) {
	                    World world = player.getWorld();

	                    for (Entity arrows : world.getEntitiesByClass(Arrow.class)) {
	                        if (!arrows.isDead() && !arrows.isOnGround()) {
	                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 2);
	                            player.spawnParticle(Particle.REDSTONE, arrows.getLocation(), 10, dustOptions);
	                            arrows.remove();
	                            return;
	                        }
	                    }
	                }
	            }
	        }.runTaskTimer(this, 0, 0);
	    }

	    @Override
	    public void onDisable() {
	        
	    }
	}
