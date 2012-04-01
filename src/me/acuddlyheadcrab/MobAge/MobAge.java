package me.acuddlyheadcrab.MobAge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.entity.CraftBlaze;
import org.bukkit.craftbukkit.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.craftbukkit.entity.CraftEnderman;
import org.bukkit.craftbukkit.entity.CraftGhast;
import org.bukkit.craftbukkit.entity.CraftGiant;
import org.bukkit.craftbukkit.entity.CraftMagmaCube;
import org.bukkit.craftbukkit.entity.CraftMonster;
import org.bukkit.craftbukkit.entity.CraftMushroomCow;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftPigZombie;
import org.bukkit.craftbukkit.entity.CraftSheep;
import org.bukkit.craftbukkit.entity.CraftSilverfish;
import org.bukkit.craftbukkit.entity.CraftSkeleton;
import org.bukkit.craftbukkit.entity.CraftSlime;
import org.bukkit.craftbukkit.entity.CraftSnowman;
import org.bukkit.craftbukkit.entity.CraftSpider;
import org.bukkit.craftbukkit.entity.CraftSquid;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobAge extends JavaPlugin {
	public static MobAge plugin;
	public final static Logger log = Logger.getLogger("Minecraft");
	public static FileConfiguration config;
	public static FileConfiguration whitelist = null;
	private File whitelistConfigurationFile = null;
	public static PluginDescriptionFile pdf;
	
	public final MobListener entityListener = new MobListener(this);
	public final PluginIO sendPluginInfo = new PluginIO(this);
	
// -------------------- MAIN METHODS START --------------------
	
	@Override
	public void onEnable() {
    	getServer().getPluginManager().registerEvents(entityListener, this);
    	loadConfig();
    	loadWhitelist();
    	saveWhitelist();
    	if(config.getBoolean("Debug_onStartup")){PluginIO.debugStartup();}
    	
    	final List<World> wlist = Bukkit.getServer().getWorlds();
    	int delay = config.getInt("Age_Check_delay");
		try{
			this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {@Override
			public void run() {
				killOldMobs(wlist);
			}}, 10L, delay) ;
		}catch(IllegalAccessError e){PluginIO.sendPluginInfo("Illegal Access Error");plugin.setEnabled(false); return;}
		
		pdf = this.getDescription();
    	PluginIO.sendPluginInfo("v"+this.getDescription().getVersion()+" - by acuddlyheadcrab - is enabled");
	}
	
	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		PluginIO.sendPluginInfo("is now disabled :(");
	}
	
	public void onReload(){
		PluginIO.sendPluginInfo("reloaded");
		this.getServer().getScheduler().cancelTasks(this);
	}
	
//	---------------------- MAIN METHODS END ----------------------
	
	public void loadConfig(){
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}

	public void reloadWhitelist() {
	    if (whitelistConfigurationFile == null) {
	    whitelistConfigurationFile = new File(getDataFolder(), "whitelist.yml");
	    }
	    whitelist = YamlConfiguration.loadConfiguration(whitelistConfigurationFile);
	    
	    InputStream defConfigStream = getResource("whitelist.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        whitelist.setDefaults(defConfig);
	    }
	}
	
	public void loadWhitelist(){
		whitelist = getWhitelist();
		whitelist.options().copyDefaults(true);
		saveWhitelist();
	}
	
	public FileConfiguration getWhitelist() {
	    if (whitelist == null) {
	        reloadWhitelist();
	    }
	    return whitelist;
	}
	
	public void saveWhitelist() {
	    if (whitelist == null || whitelistConfigurationFile == null) {
	    return;
	    }
	    try {
	        whitelist.save(whitelistConfigurationFile);
	    } catch (IOException ex) {
	        PluginIO.sendPluginInfo("Could not save config to " + whitelistConfigurationFile.getName() + ex);
	    }
	}

	public static void killOldMobs(List<World> wlist){
		
		for(int c=0;c<wlist.size();c++){
			World cur_world = wlist.get(c);
			List<Entity> entlist = cur_world.getEntities();
			
			for(int c2=0;c2<entlist.size();c2++){
				
				Entity ent = entlist.get(c2);
				
				boolean player = (ent instanceof Player);
				boolean living = (ent instanceof LivingEntity);
				boolean tamed = false; if(ent instanceof Tameable){tamed = ((Tameable) ent).isTamed();}
				
				if(!player&&living&&!tamed){
					
					if(doesAge(ent)){
						if(ent.getTicksLived()>=config.getInt("AgeLimit")){
							ent.remove();
						}
						
					}
					
					
				}
				
			}
		}
		
	}
	
	public static boolean doesAge(Entity ent){
		boolean blaze = ent instanceof CraftBlaze;
		boolean cavespider = ent instanceof CraftCaveSpider;
		boolean creeper = ent instanceof CraftCreeper;
		boolean enderman = ent instanceof CraftEnderman;
		boolean ghast = ent instanceof CraftGhast;
		boolean giant = ent instanceof CraftGiant;
		boolean magmacube = ent instanceof CraftMagmaCube;
		boolean pigzombie = ent instanceof CraftPigZombie;
		boolean silverfish = ent instanceof CraftSilverfish;
		boolean skeleton = ent instanceof CraftSkeleton;
		boolean spider = ent instanceof CraftSpider;
		boolean zombie = ent instanceof CraftZombie;
		boolean chicken = ent instanceof CraftChicken;
		boolean cow = ent instanceof CraftCow;
		boolean mooshroom = ent instanceof CraftMushroomCow;
		boolean sheep = ent instanceof CraftSheep;
		boolean slime = ent instanceof CraftSlime;
		boolean snowman = ent instanceof CraftSnowman;
		boolean squid = ent instanceof CraftSquid;
		boolean villager = ent instanceof CraftVillager;
		boolean wolf = ent instanceof CraftWolf;
		boolean monster = ent instanceof CraftMonster;
		boolean pig = ent instanceof CraftPig;
		

		if(snowman){return false;}
		if(monster){return getWLAgeKey("Monsters", "Monster");}
		if(pig){return getWLAgeKey("Animals", "Pig");}
		if(blaze){return getWLAgeKey("Monsters", "Blaze");}
		if(cavespider){return getWLAgeKey("Monsters", "CaveSpider");}
		if(creeper){return getWLAgeKey("Monsters", "Creeper");}
		if(enderman){return getWLAgeKey("Monsters", "Enderman");}
		if(ghast){return getWLAgeKey("Monsters", "Ghast");}
		if(giant){return getWLAgeKey("Monsters", "Giant");}
		if(magmacube){return getWLAgeKey("Monsters", "MagmaCube");}
		if(pigzombie){return getWLAgeKey("Monsters", "PigZombie");}
		if(silverfish){return getWLAgeKey("Monsters", "SilverFish");}
		if(skeleton){return getWLAgeKey("Monsters", "Skeleton");}
		if(spider){return getWLAgeKey("Monsters", "Spider");}
		if(zombie){return getWLAgeKey("Monsters", "Zombie");}
		if(chicken){return getWLAgeKey("Animals", "Chicken");}
		if(cow){return getWLAgeKey("Animals", "Cow");}
		if(mooshroom){return getWLAgeKey("Animals", "MushroomCow");}
		if(sheep){return getWLAgeKey("Animals", "Sheep");}
		if(slime){return getWLAgeKey("Animals", "Slime");}
		if(squid){return getWLAgeKey("Animals", "Squid");}
		if(villager){return getWLAgeKey("Animals", "Villager");}
		if(wolf){return getWLAgeKey("Animals", "Wolf");}
		
		return false;
	}
	
	public static boolean getWLAgeKey(String key, String subkey){
		
		String finalkey = "Whitelist."+key+"."+subkey;
		
		boolean returnable = true;
		try{returnable =  MobAge.whitelist.getBooleanList(finalkey).get(1);
		}catch(NullPointerException e){PluginIO.sendPluginInfo("YML error; Invalid key, \""+finalkey+"\""); return false;}
		
		return returnable;
	}
	
	@Override
	public boolean onCommand(CommandSender cmdsndr, Command cmd, String commandLabel, String[] args){
		boolean cmd1 = cmd.getName().equalsIgnoreCase("mobage");
		if(cmd1){	
			if(!(args.length<1)){
				boolean isplayer = cmdsndr instanceof Player;
				if(args[0].equalsIgnoreCase("help")){
					PluginIO.displayHelp(cmdsndr, "help"); return true;
				}
				
				if(args[0].equalsIgnoreCase("reload")){
					if(isplayer&&(!(cmdsndr.hasPermission("mobage.reload")))){
						cmdsndr.sendMessage("You must have permission of be OP to do this");
					} else
					loadConfig();
					cmdsndr.sendMessage(ChatColor.GOLD+"Reloaded");
					PluginIO.sendPluginInfo("Reloaded by "+cmdsndr.getName());
				}
				
				if(args[0].equalsIgnoreCase("config")){
					if(isplayer&&(!(cmdsndr.hasPermission("mobage.config")))){
						cmdsndr.sendMessage("You must have permission of be OP to do this");
					} else
					if(args.length==1){
						PluginIO.displayHelp(cmdsndr, "config"); return true;
					}
					
				}
				
				if(args[0].equalsIgnoreCase("setconfig")){
					if(isplayer&&(!(cmdsndr.hasPermission("mobage.config")))){
						cmdsndr.sendMessage("You must have permission of be OP to do this");
					} else
					if(args.length==1){
						PluginIO.displayHelp(cmdsndr, "setconfig"); return true;
					}
					
					String arg1 = args[1]; String newv = "null";
					int arg2 = 0;
					
					if(arg1.equalsIgnoreCase("age_check_delay")){
						try{arg2 = Integer.parseInt(args[2]);}
						catch (NumberFormatException e){
							cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as a valid number!"); return false;
						}
						config.set("Age_Check_delay", arg2);
						newv = config.getString("Age_Check_Delay");
						cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"Age_check_delay\" with the value: "+ChatColor.GRAY+newv); loadConfig();
						return true;
					} else if(arg1.equalsIgnoreCase("age_limit")){
						try{arg2 = Integer.parseInt(args[2]);}
						catch (NumberFormatException e){
							cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as a valid number!"); return false;
						}
						config.set("AgeLimit", arg2);
						newv = config.getString("AgeLimit");
						cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"AgeLimit\" with the value: "+ChatColor.GRAY+newv); loadConfig();
						return true;
					} else if(arg1.equalsIgnoreCase("mob_limit")){
						try{arg2 = Integer.parseInt(args[2]);}
						catch (NumberFormatException e){
							cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as a valid number!"); return false;
						}
						config.set("MobLimit", arg2);
						newv = config.getString("MobLimit");
						cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"MobLimit\" with the value: "+ChatColor.GRAY+newv); loadConfig();
						return true;
					} else if(arg1.equalsIgnoreCase("active_radius")){
						try{arg2 = Integer.parseInt(args[2]);}
						catch (NumberFormatException e){
							cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as a valid number!"); return false;
						}
						config.set("Active_Radius", arg2);
						newv = config.getString("Active_Radius");
						cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"Active_Radius\" with the value: "+ChatColor.GRAY+newv); loadConfig();
						return true;
					} else if(arg1.equalsIgnoreCase("debug")){
						boolean t = args[2].equalsIgnoreCase("true");
						boolean f = args[2].equalsIgnoreCase("false");
						if(!(t||f)){cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as true/false!"); return false;} else {
							boolean barg2 = Boolean.parseBoolean(args[2]);
							config.set("Debug_onStartup", barg2); config.set("Debug_for_spawning", barg2);
							newv = config.getString("Debug_for_spawning");
							cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"Debug\" with the value: "+ChatColor.GRAY+newv); loadConfig();
							return true;
						}
					} else if(arg1.equalsIgnoreCase("whitelist")){
						boolean t = args[2].equalsIgnoreCase("true");
						boolean f = args[2].equalsIgnoreCase("false");
						if(!(t||f)){cmdsndr.sendMessage(ChatColor.RED+"\""+args[2]+"\" could not be recognized as true/false!"); return false;} else {
							boolean barg2 = Boolean.parseBoolean(args[2]);
							config.set("Whitelist", barg2);
							newv = config.getString("Whitelist");
							cmdsndr.sendMessage(ChatColor.GOLD+"Set key \"Whitelist\" with the value: "+ChatColor.GRAY+newv); loadConfig();
							return true;
						}
					} else PluginIO.displayHelp(cmdsndr, "setconfig"); return false;
				}
			} else 
				PluginIO.displayHelp(cmdsndr, "help");
			return true;
		}
		
		
		return false;
	}

	
}
