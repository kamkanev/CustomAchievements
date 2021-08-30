package me.kamen.achievements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.Tuple;

public class AchievementsClass {

	Plugin plugin = null;
	
	public AchievementsClass(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean hasAchievement(Player player, Achievements achievement) {
		
		
		return plugin.getConfig().get(player.getName() + ".Achievements." + achievement.getName()) != null ? true : false;
	}
	
	public void giveAchivement(Player player, Achievements achievement) {
		if(!hasAchievement(player, achievement)) {
			plugin.getConfig().set(player.getName() + ".Achievements." + achievement.getName(), true);
			plugin.saveConfig();
			
			player.sendMessage("§6You got \'§b" + achievement.getName() + "§6\'");
			player.sendMessage("§a -> " + achievement.getText());
			player.giveExp(achievement.getExp());
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		}
	}
	
	protected void removeAchievement(Player player, Achievements achievement) {
		if(hasAchievement(player, achievement)) {
			plugin.getConfig().set(player.getName() + ".Achievements." + achievement.getName(), null);
			plugin.saveConfig();
			
			player.sendMessage("§cYou removed \'§b" + achievement.getName() + "§6\'");
			
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, 1);
		}
	}
	
	public void removeAllAchievements(Player player) {
		
		Achievements[] achievements = Achievements.values();
		
		for(Achievements ach : achievements) {
			
			removeAchievement(player, ach);
			
		}
		
	}

	public List<String> getCompletedAchievements(Player p) {
		
		Achievements[] achievements = Achievements.values();
		List<String> compAchievements = new ArrayList<String>();
		
		for(Achievements a : achievements) {
			
			if(hasAchievement(p, a)){
				
				compAchievements.add(a.getName());
				
			}
			
		}
		
		return compAchievements;
	}
	
}
