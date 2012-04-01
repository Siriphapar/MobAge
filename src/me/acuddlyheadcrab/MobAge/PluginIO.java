package me.acuddlyheadcrab.MobAge;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginIO{
	public static MobAge plugin;
	public final static Logger log = Logger.getLogger("Minecraft");
	public static FileConfiguration config;
	public static FileConfiguration whitelist;
	
	public PluginIO(MobAge mobAge) {}

	public static void sendPluginInfo(String message){
		log.info("[MobAge] "+message);
	}
	
	public static void debugStartup(){
		
		whitelist = MobAge.whitelist;
		config = MobAge.config;
		
		sendPluginInfo("Age_Check Delay: "+config.getInt("Age_Check_delay"));
		sendPluginInfo("Age Limit: "+config.getInt("AgeLimit"));
		sendPluginInfo("Mob limit: "+config.getInt("MobLimit"));
		sendPluginInfo("Inhabited radius: "+config.getInt("Active_Radius"));
		sendPluginInfo("Whitelist enabled"+config.getBoolean("Whitelist.Enabled"));
		sendPluginInfo("Debug: "+config.getBoolean("Debug_onStartup"));
		sendPluginInfo("Whitelist_Enabled: "+whitelist.getBoolean("Whitelist.Enabled"));
		if(config.getBoolean("Debug_show_whitelist")){
			sendPluginInfo("WHITELIST:");
			sendPluginInfo("  Can Spawn:");
			sendPluginInfo("	MONSTERS:");
			sendPluginInfo("		Blaze: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Blaze").get(0));
			sendPluginInfo("		Cavespider: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.CaveSpider").get(0));
			sendPluginInfo("		Creeper: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Creeper").get(0));
			sendPluginInfo("		Enderman: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Enderman").get(0));
			sendPluginInfo("		Ghast: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Ghast").get(0));
			sendPluginInfo("		Giant: : "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Giant").get(0));
			sendPluginInfo("		Magmacube: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.MagmaCube").get(0));
			sendPluginInfo("		Pigzombie: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.PigZombie").get(0));
			sendPluginInfo("		Silverfish: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.SilverFish").get(0));
			sendPluginInfo("		Skeleton: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Skeleton").get(0));
			sendPluginInfo("		Spider: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Spider").get(0));
			sendPluginInfo("		Zombie: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Zombie").get(0));
			sendPluginInfo("	ANIMALS:");
			sendPluginInfo("		Chicken: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Chicken").get(0));
			sendPluginInfo("		Cow: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Cow").get(0));
			sendPluginInfo("		Mooshroom: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Mooshroom").get(0));
			sendPluginInfo("		Sheep: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Sheep").get(0));
			sendPluginInfo("		Slime: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Slime").get(0));
			sendPluginInfo("		Sqid: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Squid").get(0));
			sendPluginInfo("		Villager: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Villager").get(0));
			sendPluginInfo("		Wolf: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Wolf").get(0));
			sendPluginInfo("  Does Age:");
			sendPluginInfo("	MONSTERS:");
			sendPluginInfo("		Blaze: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Blaze").get(1));
			sendPluginInfo("		Cavespider: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.CaveSpider").get(1));
			sendPluginInfo("		Creeper: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Creeper").get(1));
			sendPluginInfo("		Enderman: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Enderman").get(1));
			sendPluginInfo("		Ghast: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Ghast").get(1));
			sendPluginInfo("		Giant: : "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Giant").get(1));
			sendPluginInfo("		Magmacube: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.MagmaCube").get(1));
			sendPluginInfo("		Pigzombie: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.PigZombie").get(1));
			sendPluginInfo("		Silverfish: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.SilverFish").get(1));
			sendPluginInfo("		Skeleton: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Skeleton").get(1));
			sendPluginInfo("		Spider: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Spider").get(1));
			sendPluginInfo("		Zombie: "+MobAge.whitelist.getBooleanList("Whitelist.Monsters.Zombie").get(1));
			sendPluginInfo("	ANIMALS:");
			sendPluginInfo("		Chicken: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Chicken").get(1));
			sendPluginInfo("		Cow: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Cow").get(1));
			sendPluginInfo("		Mooooshrooooom: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Mooshroom").get(1));
			sendPluginInfo("		Sheep: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Sheep").get(1));
			sendPluginInfo("		Slime: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Slime").get(1));
			sendPluginInfo("		Sqid: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Squid").get(1));
			sendPluginInfo("		Villager: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Villager").get(1));
			sendPluginInfo("		Wolf: "+MobAge.whitelist.getBooleanList("Whitelist.Animals.Wolf").get(1));
			
			
		}
		
		
	} 
	
	public static void displayHelp(CommandSender cmdsndr, String option) {
		config = MobAge.config;
		
		ChatColor gray = ChatColor.GRAY, gold = ChatColor.GOLD, green = ChatColor.GREEN, dgray = ChatColor.DARK_GRAY, red = ChatColor.RED;
		
		if(option=="help"){
			cmdsndr.sendMessage(gold+"MobAge v"+MobAge.pdf.getVersion());
			cmdsndr.sendMessage(green+"   "+red+"/mobage"+green+" - Displays help");
			cmdsndr.sendMessage(green+"   "+red+"/mobage reload"+green+" - Reloads config");
			cmdsndr.sendMessage(green+"   "+red+"/mobage config"+green+" - Shows more help and current config stats");
			cmdsndr.sendMessage(green+"   "+red+"/mobage setconfig"+green+" - Edit a specified key in the config");
		}
		if(option=="config"){
			cmdsndr.sendMessage(gold+"MobAge config stats:");
			cmdsndr.sendMessage(gray+"  Age Check Delay:  "+config.getInt("Age_Check_delay"));
    		cmdsndr.sendMessage(gray+"  Age Limit: "+config.getInt("AgeLimit"));
    		cmdsndr.sendMessage(gray+"  Mob limit: "+config.getInt("MobLimit"));
    		cmdsndr.sendMessage(gray+"  Active radius: "+config.getInt("Active_Radius"));
    		cmdsndr.sendMessage(gray+"  Whitelist enabled: "+config.getBoolean("Whitelist.Enabled"));
    		cmdsndr.sendMessage(gray+"  Debug: "+config.getBoolean("Debug_onStartup"));
		}
		if(option=="setconfig"){
			cmdsndr.sendMessage(gold+"How to edit MobAge config :");
			cmdsndr.sendMessage(gray+"  /mobage setconfig age_check_delay "+dgray+"<default: 10>");
    		cmdsndr.sendMessage(gray+"  /mobage setconfig age_limit "+dgray+"<default: 900>");
    		cmdsndr.sendMessage(gray+"  /mobage setconfig mob_limit "+dgray+"<default: 50>");
    		cmdsndr.sendMessage(gray+"  /mobage setconfig active radius "+dgray+"<default: 50>");
    		cmdsndr.sendMessage(gray+"  /mobage setconfig debug "+dgray+"true/false");
    		cmdsndr.sendMessage(gray+"  /mobage setconfig whitelist "+dgray+"true/false");
			
		}
		
	}
	
}
