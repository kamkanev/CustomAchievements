package me.kamen.achievements.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.netty.channel.epoll.Epoll;
import me.kamen.achievements.Achievements;
import me.kamen.achievements.AchievementsClass;
import me.kamen.achievements.Main;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.server.v1_16_R3.Tuple;

public class AchievementListener implements Listener{
	
	private Main plugin;
	private int fallingBlocks = 0;
	
	
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
	public void onPlayerMove(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		plugin.aClass.giveAchivement(e.getPlayer(), Achievements.FIRSTMOVE);
		
		if(p.getLocation().getY() >= p.getWorld().getMaxHeight()) {
			
			achievementCheck(p, Achievements.GOTOMAXHEIGHT);
		
		}else if(p.getLocation().getY() <= 6) {
			
			Material bmat = e.getFrom().getBlock().getType().name().contains("AIR") ? e.getFrom().clone().subtract(0, 1, 0).getBlock().getType() : e.getFrom().getBlock().getType();
			
			if(bmat.equals(Material.BEDROCK)) {
				achievementCheck(p, Achievements.GOTOBEDROCK);
			}
			
		}
	}
	
	@EventHandler
	public void onJumping(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		if(p.getVelocity().getY() > 0 && e.getFrom().getBlockY() < e.getTo().getBlockY() && !p.isSwimming() && !p.isFlying()) {

			Material bmat = e.getFrom().getBlock().getType().name().contains("AIR") ? e.getFrom().clone().subtract(0, 1, 0).getBlock().getType() : e.getFrom().getBlock().getType();
			
			if(bmat.name().contains("_BED")) {
				
				this.achievementCheck(p, Achievements.JUMPONBED);
				
			}else if(bmat.equals(Material.MAGMA_BLOCK)) {
				
				achievementCheck(p, Achievements.JUMPONMAGMA);
			
			}else if(bmat.equals(Material.CACTUS)) {
			
				achievementCheck(p, Achievements.JUMPONCACTI);
				
			}
			
			
		}else if(p.getVelocity().getY() < 0 && e.getFrom().getBlockY() > e.getTo().getBlockY() && !p.isSwimming() && !p.isFlying()) {
			
			if(!this.plugin.aClass.hasAchievement(p, Achievements.DIVEINWATER)) {
				
				Material bmat = e.getTo().getBlock().getType().name().contains("AIR") ? e.getTo().clone().subtract(0, 1, 0).getBlock().getType() : e.getTo().getBlock().getType();
				
				if(bmat.equals(Material.AIR)) {
					fallingBlocks += (e.getFrom().getBlockY() - e.getTo().getBlockY());
				}
				System.out.println(bmat + " " + fallingBlocks );
				
				if(bmat.equals(Material.WATER) && fallingBlocks >= 50) {
					achievementCheck(p, Achievements.DIVEINWATER);
					fallingBlocks = 0;
				}
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
	public void onEating(PlayerItemConsumeEvent e) {
		
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		switch (i.getType()) {
		case GOLDEN_APPLE:
			achievementCheck(p, Achievements.EATGAPPLES);
			break;
		case ENCHANTED_GOLDEN_APPLE:
			achievementCheck(p, Achievements.EATGODAPPLE);
			break;
		case BREAD:
			achievementCheck(p, Achievements.EATBREAD);
			break;
			
		default:
			break;
		}
		
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDamageEvent e) {
		
		if(e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getEntity();
			
			if(p.getHealth() <= e.getFinalDamage()) {
				
				if(p.getLastDamageCause().getCause().equals(DamageCause.LAVA))
					achievementCheck(p, Achievements.DIEINLAVA);
				else if(p.getLastDamageCause().getCause().equals(DamageCause.DROWNING))
					achievementCheck(p, Achievements.DROWN);
			}
			
		}
		
	}
	
	@EventHandler
	public void onCrafting(CraftItemEvent e) {
		
		ItemStack test = e.getRecipe().getResult().clone();
		ClickType click = e.getClick();

		int recipeAmount = test.getAmount();

		switch (click) {
			case NUMBER_KEY:
				// If hotbar slot selected is full, crafting fails (vanilla behavior, even when
				// items match)
				if (e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) != null)
					recipeAmount = 0;
				break;

			case DROP:
			case CONTROL_DROP:
				// If we are holding items, craft-via-drop fails (vanilla behavior)
				ItemStack cursor = e.getCursor();
				// Apparently, rather than null, an empty cursor is AIR. I don't think that's
				// intended.
				if (!(cursor == null || cursor.getType() == Material.matchMaterial("AIR")))
					recipeAmount = 0;
				break;

			case SHIFT_RIGHT:
			case SHIFT_LEFT:
				// Fixes ezeiger92/QuestWorld2#40
				if (recipeAmount == 0)
					break;

				int maxCraftable = this.getMaxCraftAmount(e.getInventory());
				int capacity = this.fits(test, e.getView().getBottomInventory());

				// If we can't fit everything, increase "space" to include the items dropped by
				// crafting
				// (Think: Uncrafting 8 iron blocks into 1 slot)
				if (capacity < maxCraftable)
					maxCraftable = ((capacity + recipeAmount - 1) / recipeAmount) * recipeAmount;

				recipeAmount = maxCraftable;
				break;
			default:
		}

		// No use continuing if we haven't actually crafted a thing
		if (recipeAmount == 0)
			return;

		test.setAmount(recipeAmount);

		Player player = (Player) e.getWhoClicked();
		
		for(int c = 0; c < test.getAmount(); c++) {
			
			if(test.getType().equals(Material.BREAD)) {
			
				achievementCheck(player, Achievements.CRAFTBREAD);
			
			}else if(test.getType().equals(Material.END_CRYSTAL)) {
				
				achievementCheck(player, Achievements.CRAFTENDCRYSTALS);
				
			}
			
		}
		
		
	}
	
	@EventHandler
	public void onKill(EntityDamageByEntityEvent e) {
		
		
		if(e.getDamager() instanceof Player) {
			
			Player killer = (Player) e.getDamager();
			
			LivingEntity entity = (LivingEntity) e.getEntity();
			
			if(entity.getHealth() <= e.getDamage()) {
				
				if(entity instanceof Monster) {
				
					switch (entity.getType()) {
					case WITHER:
						achievementCheck(killer, Achievements.KILLTHEWITHER);
						break;
					case ENDER_DRAGON:
						achievementCheck(killer, Achievements.KILLTHEDRAGON);
						break;
						
					case ENDERMAN:
						achievementCheck(killer, Achievements.KILLENDERMEN);
						break;
					case WITCH:
						achievementCheck(killer, Achievements.KILLWITCHES);
						break;
					case HUSK:
						achievementCheck(killer, Achievements.KILLHUSKS);
						break;
					case GHAST:
						achievementCheck(killer, Achievements.KILLGHASTS);
						break;
					case PIGLIN_BRUTE:
						achievementCheck(killer, Achievements.KILLPIGBRUTE);
						break;
						
					default:
						break;	
					}
					
					achievementCheck(killer, Achievements.KILLAMONSTER);
				
				}else if(entity instanceof Animals) {
					
					switch (entity.getType()) {
					case PIG:
						achievementCheck(killer, Achievements.KILLPIGS);
						break;
					case COW:
						achievementCheck(killer, Achievements.KILLCOWS);
						break;
					case FOX:
						achievementCheck(killer, Achievements.KILLFOXES);
						break;
					case RABBIT:
						achievementCheck(killer, Achievements.KILLBUNNIES);
						break;

					default:
						break;
					}
					
				}else if(entity instanceof IronGolem) {
					
					achievementCheck(killer, Achievements.KILLGOLEM);
					
				}
				
			}
			
		}
		
	}
	
	public static int getMaxCraftAmount(CraftingInventory inv) {
		if (inv.getResult() == null)
			return 0;

		int resultCount = inv.getResult().getAmount();
		int materialCount = Integer.MAX_VALUE;

		for (ItemStack is : inv.getMatrix())
			if (is != null && is.getAmount() < materialCount)
				materialCount = is.getAmount();

		return resultCount * materialCount;
	}
	
	public static int fits(ItemStack stack, Inventory inv) {
		ItemStack[] contents = inv.getContents();
		int result = 0;

		for (ItemStack is : contents)
			if (is == null)
				result += stack.getMaxStackSize();
			else if (is.isSimilar(stack))
				result += Math.max(stack.getMaxStackSize() - is.getAmount(), 0);

		return result;
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
