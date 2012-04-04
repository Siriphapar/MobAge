package me.acuddlyheadcrab.MobAge;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

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
		sendPluginInfo("Debug: "+config.getBoolean("Debug.onStartup"));
		sendPluginInfo("Whitelist enabled: "+whitelist.getBoolean("Whitelist.Enabled"));
		if(config.getBoolean("Debug.showWhitelist")){
			sendPluginInfo("WHITELIST:");
			sendPluginInfo("  Can Spawn:");
			sendPluginInfo("	MONSTERS:");
			sendPluginInfo("		Blaze: "+getWLKey("Monsters", "Blaze", "spawn"));
			sendPluginInfo("		Cavespider: "+getWLKey("Monsters", "CaveSpider", "spawn"));
			sendPluginInfo("		Creeper: "+getWLKey("Monsters", "Creeper", "spawn"));
			sendPluginInfo("		Enderman: "+getWLKey("Monsters", "Enderman", "spawn"));
			sendPluginInfo("		Ghast: "+getWLKey("Monsters", "Ghast", "spawn"));
			sendPluginInfo("		Giant: : "+getWLKey("Monsters", "Giant", "spawn"));
			sendPluginInfo("		Magmacube: "+getWLKey("Monsters", "MagmaCube", "spawn"));
			sendPluginInfo("		Pigzombie: "+getWLKey("Monsters", "PigZombie", "spawn"));
			sendPluginInfo("		Silverfish: "+getWLKey("Monsters", "SilverFish", "spawn"));
			sendPluginInfo("		Skeleton: "+getWLKey("Monsters", "Skeleton", "spawn"));
			sendPluginInfo("		Spider: "+getWLKey("Monsters", "Spider", "spawn"));
			sendPluginInfo("		Zombie: "+getWLKey("Monsters", "Zombie", "spawn"));
			sendPluginInfo("	ANIMALS:");
			sendPluginInfo("		Chicken: "+getWLKey("Animals", "Chicken", "spawn"));
			sendPluginInfo("		Cow: "+getWLKey("Animals", "Cow", "spawn"));
			sendPluginInfo("		Mooshroom: "+getWLKey("Animals", "Mooshroom", "spawn"));
			sendPluginInfo("		Sheep: "+getWLKey("Animals", "Sheep", "spawn"));
			sendPluginInfo("		Slime: "+getWLKey("Animals", "Slime", "spawn"));
			sendPluginInfo("		Sqid: "+getWLKey("Animals", "Squid", "spawn"));
			sendPluginInfo("		Villager: "+getWLKey("Animals", "Villager", "spawn"));
			sendPluginInfo("		Wolf: "+getWLKey("Animals", "Wolf", "spawn"));
			sendPluginInfo("  Does Age:");
			sendPluginInfo("	MONSTERS:");
			sendPluginInfo("		Blaze: "+getWLKey("Monsters", "Blaze", "age"));
			sendPluginInfo("		Cavespider: "+getWLKey("Monsters", "CaveSpider", "age"));
			sendPluginInfo("		Creeper: "+getWLKey("Monsters", "Creeper", "age"));
			sendPluginInfo("		Enderman: "+getWLKey("Monsters", "Enderman", "age"));
			sendPluginInfo("		Ghast: "+getWLKey("Monsters", "Ghast", "age"));
			sendPluginInfo("		Giant: : "+getWLKey("Monsters", "Giant", "age"));
			sendPluginInfo("		Magmacube: "+getWLKey("Monsters", "MagmaCube", "age"));
			sendPluginInfo("		Pigzombie: "+getWLKey("Monsters", "PigZombie", "age"));
			sendPluginInfo("		Silverfish: "+getWLKey("Monsters", "SilverFish", "age"));
			sendPluginInfo("		Skeleton: "+getWLKey("Monsters", "Skeleton", "age"));
			sendPluginInfo("		Spider: "+getWLKey("Monsters", "Spider", "age"));
			sendPluginInfo("		Zombie: "+getWLKey("Monsters", "Zombie", "age"));
			sendPluginInfo("	ANIMALS:");
			sendPluginInfo("		Chicken: "+getWLKey("Animals", "Chicken", "age"));
			sendPluginInfo("		Cow: "+getWLKey("Animals", "Cow", "age"));
			sendPluginInfo("		Mooooshrooooom: "+getWLKey("Animals", "Mooshroom", "age"));
			sendPluginInfo("		Sheep: "+getWLKey("Animals", "Sheep", "age"));
			sendPluginInfo("		Slime: "+getWLKey("Animals", "Slime", "age"));
			sendPluginInfo("		Sqid: "+getWLKey("Animals", "Squid", "age"));
			sendPluginInfo("		Villager: "+getWLKey("Animals", "Villager", "age"));
			sendPluginInfo("		Wolf: "+getWLKey("Animals", "Wolf", "age"));
		}
	} 
	
	public static void displayHelp(CommandSender sender, String option) {
		config = MobAge.config;
		
		ChatColor 
	        gray = ChatColor.GRAY,
	        gold = ChatColor.GOLD,
	        green = ChatColor.GREEN,
	        dgray = ChatColor.DARK_GRAY,
	        red = ChatColor.RED
        ;
		
		if(option.equals("help")){
			sender.sendMessage(gold+"MobAge v"+MobAge.pdf.getVersion());
			sender.sendMessage(green+"   "+red+"/mobage"+green+" - Displays help");
			sender.sendMessage(green+"   "+red+"/mobage reload"+green+" - Reloads config");
			sender.sendMessage(green+"   "+red+"/mobage config"+green+" - Shows more help and current config stats");
			sender.sendMessage(green+"   "+red+"/mobage setconfig"+green+" - Edit a specified key in the config");
		}
		if(option.equals("config")){
			sender.sendMessage(gold+"MobAge config stats:");
			sender.sendMessage(gray+"  Age Check Delay:  "+config.getInt("Age_Check_delay"));
    		sender.sendMessage(gray+"  Age Limit: "+config.getInt("AgeLimit"));
    		sender.sendMessage(gray+"  Mob limit: "+config.getInt("MobLimit"));
    		sender.sendMessage(gray+"  Active radius: "+config.getInt("Active_Radius"));
    		sender.sendMessage(gray+"  Whitelist enabled: "+whitelist.getBoolean("Whitelist.Enabled"));
    		sender.sendMessage(gray+"  Debug(spawn): "+config.getBoolean("Debug_onSpawn"));
		}
		if(option.equals("setconfig")){
			sender.sendMessage(gold+"How to edit MobAge config :");
			sender.sendMessage(gray+"  /mobage setconfig age_check_delay "+dgray+"<default: 0.5>");
    		sender.sendMessage(gray+"  /mobage setconfig age_limit "+dgray+"<default: 45>");
    		sender.sendMessage(gray+"  /mobage setconfig mob_limit "+dgray+"<default: 50>");
    		sender.sendMessage(gray+"  /mobage setconfig active radius "+dgray+"<default: 50>");
    		sender.sendMessage(gray+"  /mobage setconfig debug "+dgray+"true/false");
    		sender.sendMessage(gray+"  /mobage setconfig whitelist "+dgray+"true/false");
		}
		
		if(option.equals("whitelist")){
		    sender.sendMessage("WHITELIST:");
            sender.sendMessage("  Can Spawn:");
            sender.sendMessage("    MONSTERS:");
            sender.sendMessage("        Blaze: "+getWLKey("Monsters", "Blaze", "spawn"));
            sender.sendMessage("        Cavespider: "+getWLKey("Monsters", "CaveSpider", "spawn"));
            sender.sendMessage("        Creeper: "+getWLKey("Monsters", "Creeper", "spawn"));
            sender.sendMessage("        Enderman: "+getWLKey("Monsters", "Enderman", "spawn"));
            sender.sendMessage("        Ghast: "+getWLKey("Monsters", "Ghast", "spawn"));
            sender.sendMessage("        Giant: : "+getWLKey("Monsters", "Giant", "spawn"));
            sender.sendMessage("        Magmacube: "+getWLKey("Monsters", "MagmaCube", "spawn"));
            sender.sendMessage("        Pigzombie: "+getWLKey("Monsters", "PigZombie", "spawn"));
            sender.sendMessage("        Silverfish: "+getWLKey("Monsters", "SilverFish", "spawn"));
            sender.sendMessage("        Skeleton: "+getWLKey("Monsters", "Skeleton", "spawn"));
            sender.sendMessage("        Spider: "+getWLKey("Monsters", "Spider", "spawn"));
            sender.sendMessage("        Zombie: "+getWLKey("Monsters", "Zombie", "spawn"));
            sender.sendMessage("    ANIMALS:");
            sender.sendMessage("        Chicken: "+getWLKey("Animals", "Chicken", "spawn"));
            sender.sendMessage("        Cow: "+getWLKey("Animals", "Cow", "spawn"));
            sender.sendMessage("        Mooshroom: "+getWLKey("Animals", "Mooshroom", "spawn"));
            sender.sendMessage("        Sheep: "+getWLKey("Animals", "Sheep", "spawn"));
            sender.sendMessage("        Slime: "+getWLKey("Animals", "Slime", "spawn"));
            sender.sendMessage("        Sqid: "+getWLKey("Animals", "Squid", "spawn"));
            sender.sendMessage("        Villager: "+getWLKey("Animals", "Villager", "spawn"));
            sender.sendMessage("        Wolf: "+getWLKey("Animals", "Wolf", "spawn"));
            sender.sendMessage("  Does Age:");
            sender.sendMessage("    MONSTERS:");
            sender.sendMessage("        Blaze: "+getWLKey("Monsters", "Blaze", "age"));
            sender.sendMessage("        Cavespider: "+getWLKey("Monsters", "CaveSpider", "age"));
            sender.sendMessage("        Creeper: "+getWLKey("Monsters", "Creeper", "age"));
            sender.sendMessage("        Enderman: "+getWLKey("Monsters", "Enderman", "age"));
            sender.sendMessage("        Ghast: "+getWLKey("Monsters", "Ghast", "age"));
            sender.sendMessage("        Giant: : "+getWLKey("Monsters", "Giant", "age"));
            sender.sendMessage("        Magmacube: "+getWLKey("Monsters", "MagmaCube", "age"));
            sender.sendMessage("        Pigzombie: "+getWLKey("Monsters", "PigZombie", "age"));
            sender.sendMessage("        Silverfish: "+getWLKey("Monsters", "SilverFish", "age"));
            sender.sendMessage("        Skeleton: "+getWLKey("Monsters", "Skeleton", "age"));
            sender.sendMessage("        Spider: "+getWLKey("Monsters", "Spider", "age"));
            sender.sendMessage("        Zombie: "+getWLKey("Monsters", "Zombie", "age"));
            sender.sendMessage("    ANIMALS:");
            sender.sendMessage("        Chicken: "+getWLKey("Animals", "Chicken", "age"));
            sender.sendMessage("        Cow: "+getWLKey("Animals", "Cow", "age"));
            sender.sendMessage("        Mooooshrooooom: "+getWLKey("Animals", "Mooshroom", "age"));
            sender.sendMessage("        Sheep: "+getWLKey("Animals", "Sheep", "age"));
            sender.sendMessage("        Slime: "+getWLKey("Animals", "Slime", "age"));
            sender.sendMessage("        Sqid: "+getWLKey("Animals", "Squid", "age"));
            sender.sendMessage("        Villager: "+getWLKey("Animals", "Villager", "age"));
            sender.sendMessage("        Wolf: "+getWLKey("Animals", "Wolf", "age"));
		}
	}
	
	public static long toTicks(double seconds){
	    return (long) (seconds*20);
	}
	
   public static boolean getWhiteListVal(Entity ent, String type){
        String monster = "Monsters", animal = "Animals";
        switch (ent.getType().getTypeId()) {
            case 50: return getWLKey(monster, "Creeper", type);
            case 51: return getWLKey(monster, "Skeleton", type);
            case 52: return getWLKey(monster, "Spider", type);
            case 53: return getWLKey(monster, "Giant", type);
            case 54: return getWLKey(monster, "Zombie", type);
            case 55: return getWLKey(animal, "Slime", type);
            case 56: return getWLKey(monster, "Ghast", type);
            case 57: return getWLKey(monster, "PigZombie", type);
            case 58: return getWLKey(monster, "Enderman", type);
            case 59: return getWLKey(monster, "CaveSpider", type);
            case 60: return getWLKey(monster, "SilverFish", type);
            case 61: return getWLKey(monster, "Blaze", type);
            case 62: return getWLKey(monster, "MagmaCube", type);
            case 63: return false; //EnderDragon
            case 90: return getWLKey(animal, "Pig", type);
            case 91: return getWLKey(animal, "Sheep", type);
            case 92: return getWLKey(animal, "Cow", type);
            case 93: return getWLKey(animal, "Chicken", type);
            case 94: return getWLKey(animal, "Squid", type);
            case 95: return getWLKey(animal, "Wolf", type);
            case 96: return getWLKey(animal, "Mooshroom", type);
            case 97: return false; //Snow Golem
            case 98: return getWLKey(animal, "Ocelot", type);
            case 99: return getWLKey(animal, "IronGolem", type);
            case 120: return getWLKey(animal, "Villager", type);
            default:return false;
        }
    }
   
   public static boolean getWLKey(String key, String subkey, String type){
       boolean returnable = true;
       String finalkey = "Whitelist."+key+"."+subkey;
       try{
           if(type.equalsIgnoreCase("spawn")){
               returnable =  MobAge.whitelist.getBooleanList(finalkey).get(0);  
           } else if(type.equalsIgnoreCase("age")){
               returnable =  MobAge.whitelist.getBooleanList(finalkey).get(1);
           }
       }catch(NullPointerException e){PluginIO.sendPluginInfo("YML error; Invalid key, \""+finalkey+"\""); return false;}
       
       return returnable;
       
   }
	
}
