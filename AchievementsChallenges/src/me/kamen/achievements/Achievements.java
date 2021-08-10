package me.kamen.achievements;

public enum Achievements {
	
	//testing
	FIRSTJOIN("FirstJoin", "Join for first time",  1, 25),
	FIRSTMOVE("FIrstMove", "Move", 1, 5),
	FIRSTCHAT("FIrstChat", "Chat with someone!", 1, 50),
	//exploring
	GOTOMAXHEIGHT("GotoMaxHei", "Go to max height", 1, 44),
	GOTOBEDROCK("GoToBedRock", "Go to bedrock", 1, 14),
	//eating
	EATACAKE("EatACake", "Eat some slices of cake", 3, 20),
	EATGAPPLES("EatGapples", "Eat golden apples", 3, 90),
	EATGODAPPLE("EATGODAPPLE", "Eat an enchanted golden apple", 1, 180),
	//crafting
	CRAFTBREAD("CraftBread", "Craft bread: ", 6, 49),
	CRAFTDHOE("CraftDHoe", "Craft a diamond hoe.", 1, 200),
	CRAFTFURNES("CraftFurnes", "Craft furneses:", 6, 66),
	CRAFTGHELMET("CraftGHelmet", "Craft a golden helmet", 1, 50),
	CRAFTENDCRYSTALS("CraftEndCrystals", "Craft end crystals:", 4, 104),
	//jumping
	JUMPONBED("JumpOnBed", "Jumping on bed: ", 5, 10),
	JUMPONMAGMA("JumpOnMagma", "Jumping on magma block:", 10, 40),
	JUMPONCACTI("JumpOnCacti", "Jumping on cactus:", 9, 14),
	DIVEINWATER("DiveInWater", "Dive in water from 50 blocks.", 1, 30),
	//die
	DIEINLAVA("DieInLava", "Die in lava", 1, 3),
	DROWN("DROWN", "Drown to death", 1, 5),
	//killing monsters
	KILLENDERMEN("KillEndermen", "Kill endermen", 10, 100),
	KILLWITCHES("KILLWITCHES", "Kill witches", 5, 120),
	KILLHUSKS("KillHusks", "Kill husks", 12, 90),
	KILLGHASTS("KillGhast", "Kill ghasts", 3, 111),
	KILLAMONSTER("KillMonster", "Kill monsters", 13, 50),
	//killing friendly
	KILLPIGS("KillPigs", "Kill pigs", 10, 50),
	KILLCOWS("KillCows", "Kill cows", 10, 50),
	KILLGOLEM("KillGolem", "Kill a golem", 1, 69),
	//finishers
	KILLTHEWITHER("KillTheWither", "Kill The WITHER", 1, 300),
	KILLTHEDRAGON("KillTheDragon", "Kill The ENDER DRAGON", 1, 300);
	
	String name;
	String text;
	int amount;
	int exp;
	
	private Achievements(String name, String text, int amount, int exp) {
		
		this.name = name;
		this.text = text;
		this.amount = amount;
		this.exp = exp;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}
	
	public int getAmount() {
		return amount;
	}

	public int getExp() {
		return exp;
	}
	
	

}
