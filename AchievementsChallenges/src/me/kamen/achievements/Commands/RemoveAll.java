package me.kamen.achievements.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kamen.achievements.Achievements;
import me.kamen.achievements.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.Tuple;

public class RemoveAll implements CommandExecutor{
	
private Main plugin;
	
	public RemoveAll(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("removeall").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		plugin.aClass.removeAllAchievements(p);
		
		p.sendMessage(ChatColor.DARK_RED + "\nAll completed achievements are now reset!");
		
		return false;
	}

}
