package me.kamen.achievements.Commands;

import org.apache.commons.lang.StringUtils;
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
		
		Player toCheck = p;
		
		if(p.isOp() && args != null && args.length > 0) {
			String pName = args[0];
			
			toCheck = p.getServer().getPlayer(pName);
		}else if(!p.isOp() && args.length>0) {
			toCheck = null;
			p.sendMessage(ChatColor.DARK_RED + "You need to be op check others achievements.");
		}
		
		if(toCheck != null) {
			

			p.sendMessage("§e============ §6§l"+ (p.getName().equals(toCheck.getName()) ? "My" : toCheck.getName()) +" achievements §e============ \n");
			
			for(Achievements a : achievements) {
				
				if(plugin.aClass.hasAchievement(toCheck, a)){
					
					p.sendMessage("\n§2" + a.getText() + " - " + ChatColor.GOLD+ "§lCompleted\n");
					
				}else {
					
					Tuple<String, Integer> progress = plugin.getAchievementProgress(toCheck, a.getName());
					
					p.sendMessage("\n§a" + a.getText()+ " - " + (progress != null ? progress.b() : "0") + " / " + a.getAmount() + "\n");
					
				}
				
			}
			
			p.sendMessage("\n§6§lCompleted achievements: §2§l" + plugin.aClass.getCompletedAchievements(toCheck).size() + " / " + Achievements.values().length);
			p.sendMessage("§e======================================"+ (p.getName().equals(toCheck.getName()) ? "==" : StringUtils.repeat("=", toCheck.getName().length())) +" \n");
			
		}
		
		return false;
	}

}
