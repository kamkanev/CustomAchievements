package me.kamen.achievements.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Cake;

import me.kamen.achievements.Achievements;
import me.kamen.achievements.AchievementsClass;
import me.kamen.achievements.Main;
import net.minecraft.server.v1_16_R3.BlockCake;
import net.minecraft.server.v1_16_R3.Tuple;

public class AchievementListener implements Listener{
	
	private Main plugin;
	
	
	public AchievementListener(Main plugin) {
		this.plugin = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin.saveConfig();
		

		plugin.aClass = new AchievementsClass(plugin);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		plugin.aClass.giveAchivement(event.getPlayer(), Achievements.FIRSTJOIN);
		
	}
	
	@EventHandler
	public void onChatType(AsyncPlayerChatEvent event) {

		plugin.aClass.giveAchivement(event.getPlayer(), Achievements.FIRSTCHAT);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		plugin.aClass.giveAchivement(event.getPlayer(), Achievements.FIRSTMOVE);
		
	}
	
	@EventHandler
	public void onJumping(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		if(p.getVelocity().getY() > 0 && e.getFrom().getBlockY() < e.getTo().getBlockY() && !p.isSwimming() && !p.isFlying()) {

			Material bmat = e.getFrom().getBlock().getType().name().contains("AIR") ? e.getFrom().clone().subtract(0, 1, 0).getBlock().getType() : e.getFrom().getBlock().getType();
			
			if(bmat.name().contains("_BED")) {
				
				this.achievementCheck(p, Achievements.JUMPONBED);
				
			}
			
		}
		
		
	}
	
	@EventHandler
	public void onCakeEating(PlayerInteractEvent e) {
		
		if(e.getClickedBlock() != null) {
			
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType().name().contains("CAKE")) && e.getPlayer().getFoodLevel() < 20) {

				this.achievementCheck(e.getPlayer(), Achievements.EATACAKE);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onKill(EntityDamageByEntityEvent e) {
		
		
		if(e.getDamager() instanceof Player) {
			
			Player killer = (Player) e.getDamager();
			
			LivingEntity entity = (LivingEntity) e.getEntity();
			
			if(entity instanceof Monster && entity.getHealth() <= e.getDamage()) {
				
				System.out.println("Yeeee");
				
				achievementCheck(killer, Achievements.KILLAMONSTER);
				
			}
			
		}
		
	}
	
	public void achievementCheck(Player p, Achievements achievement) {
		

		boolean isCompleted = false;
		
		
		Tuple<String, Integer> sAch = null;
		
		
		if(plugin.mulityAchi.containsKey(p.getUniqueId())) {
			
			boolean isIn = false;
			
			for(Tuple<String, Integer> ach : plugin.mulityAchi.get(p.getUniqueId())) {
				
				if(ach.a() == achievement.getName()) {
					isIn = true;

					Tuple<String, Integer> Nach = new Tuple<String, Integer>(ach.a(), ach.b() + 1);
					
					plugin.mulityAchi.get(p.getUniqueId()).set(plugin.mulityAchi.get(p.getUniqueId()).indexOf(ach), Nach);
					
					sAch = Nach;
					
					isCompleted = (sAch.b().intValue() >= achievement.getAmount());
					break;
					
				}
				
			}
			
			if(!isIn) {
				Tuple<String, Integer> nAchAdd = new Tuple<String, Integer>(achievement.getName(), 1);
				sAch = nAchAdd;
				plugin.mulityAchi.get(p.getUniqueId()).add(nAchAdd);
				
				isCompleted = (nAchAdd.b().intValue() >= achievement.getAmount());
				
			}
				
			
		}else {
			
			plugin.mulityAchi.put(p.getUniqueId(), new ArrayList<Tuple<String, Integer>>());
			
			Tuple<String, Integer> nAchAdd = new Tuple<String, Integer>(achievement.getName(), 1);
			sAch = nAchAdd;
			plugin.mulityAchi.get(p.getUniqueId()).add(nAchAdd);
			
			isCompleted = (nAchAdd.b().intValue() >= achievement.getAmount());
			
		}
		
		if(isCompleted) {
			
			plugin.mulityAchi.get(p.getUniqueId()).remove(sAch);
			
			plugin.aClass.giveAchivement(p, achievement);
			
		}
		
	}

}
