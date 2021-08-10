package me.kamen.achievements;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.kamen.achievements.Commands.ListAll;
import me.kamen.achievements.Commands.RemoveAll;
import me.kamen.achievements.Listeners.AchievementListener;
import net.minecraft.server.v1_16_R3.Tuple;

public class Main extends JavaPlugin{
	
	public AchievementsClass aClass = null;
	
	public HashMap<UUID, List<Tuple<String, Integer>>> mulityAchi = new HashMap<UUID, List<Tuple<String,Integer>>>();
	
	@Override
	public void onEnable() {
		new AchievementListener(this);
		new ListAll(this);
		new RemoveAll(this);
	}
	
	public Tuple<String, Integer> getAchievementProgress(Player player, String achName){
		
		if(mulityAchi.containsKey(player.getUniqueId())) {
			
			for(Tuple<String, Integer> t : mulityAchi.get(player.getUniqueId())) {
				
				if(t.a() == achName) {
					return t;
				}
				
			}
			
		}
		
		return null;
		
	}
}
