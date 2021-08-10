package me.kamen.achievements.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kamen.achievements.Achievements;
import me.kamen.achievements.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.Tuple;

public class ListAll implements CommandExecutor{
	
	private Main plugin;
	
	public ListAll(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("listall").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		Achievements[] achievements = Achievements.values();
		
		for(Achievements a : achievements) {
			
			if(plugin.aClass.hasAchievement(p, a)){
				
				p.sendMessage("\n§2" + a.getText() + " - " + ChatColor.GOLD+ "§lCompleted\n");
				
			}else {
				
				Tuple<String, Integer> progress = plugin.getAchievementProgress(p, a.getName());
				
				p.sendMessage("\n§a" + a.getText()+ " - " + (progress != null ? progress.b() : "0") + " / " + a.getAmount() + "\n");
				
			}
			
		}
		
		return false;
	}

}
