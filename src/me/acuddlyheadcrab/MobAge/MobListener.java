package me.acuddlyheadcrab.MobAge;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.craftbukkit.entity.CraftOcelot;
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
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.yaml.snakeyaml.error.YAMLException;


public class MobListener implements Listener{
	public static MobAge plugin;
	public MobListener(MobAge instance){plugin = instance;}
	public static boolean eventDebug = false;
	
//	ENTITY EVENT HANDLERS	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		resetAge(event.getEntity(), "damage event");
		if(eventDebug)System.out.println("dmg");
	}
	@EventHandler
	public void onEntityInteract(EntityInteractEvent event){
		resetAge(event.getEntity(), "interact event");
		if(eventDebug)System.out.println("interact");
	}
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event){
		resetAge(event.getEntity(), "target event");
		if(eventDebug)System.out.println("target");
	}	
	@EventHandler
	public void onEntityTame(EntityTameEvent event){
		resetAge(event.getEntity(), "tame event");
		if(eventDebug)System.out.println("tame");
	}
//	ENTITY EVENT HANDLERS (END)
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event){
		
		List<LivingEntity> l_entlist = event.getEntity().getWorld().getLivingEntities();
		
		for(int c=0;c<l_entlist.size();c++){
			boolean remove = (l_entlist.get(c) instanceof Player)||(l_entlist.get(c) instanceof CraftSnowman);			
			if(remove){l_entlist.remove(c);}
		}
		
		int moblimit = MobAge.config.getInt("MobLimit");
		if((moblimit!=0)&&(l_entlist.size()>=moblimit)){
			if(MobAge.config.getBoolean("Debug_for_spawning")){
				System.out.println("Spawn cancelled due to mob limit ("+l_entlist.size()+"), (Limit: "+moblimit);
			}
			event.setCancelled(true);
			return;
		} else
		
		
		if(!inhabited(event.getLocation())){
			event.setCancelled(true);
			return;
		}
		
		boolean whitelist = false;
		try{
			whitelist = MobAge.whitelist.getBoolean("Whitelist.Enabled");
		} catch (YAMLException e){PluginIO.sendPluginInfo("YML error in key: \"Whitelist.Enabled\""); return;}
		if(whitelist){
			if(!isAllowedSpawn(event.getEntity())){
				if(MobAge.config.getBoolean("Debug_for_spawning")){
					PluginIO.sendPluginInfo(event.getEntity()+" is not on the whitelist!");
				}
				event.setCancelled(true); return;
				}
		}
		
	}
	

	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		
		if(MobListener.eventDebug){System.out.println("dmg");}
		
		Entity[] ent_ar = event.getTo().getBlock().getChunk().getEntities();
		
		
		
		for(int c=0;c<ent_ar.length;c++){
			Entity ent = ent_ar[c];
			
			if (!(ent instanceof Player)){
				resetAge(ent, "PlayerMoveEvent");
			}
			
			
		}
		
	}
	
	private boolean inhabited(Location loc) {
		Player[] playarr = Bukkit.getOnlinePlayers();
		double dist = 0;
		int rad = 5;
		
		
		try{
			rad = MobAge.config.getInt("Active_Radius");
		}catch(YAMLException e){PluginIO.sendPluginInfo("Exception in YML key: \"Active Radius:\""); return false;}
		
		
		for(int c=0;c<playarr.length;c++){
			Location ploc = playarr[c].getLocation();
			try{
				dist = ploc.distance(loc);
			}catch(IllegalArgumentException e){
				return false;
			}
			if(dist<=rad){return true;}
		}
		
		if(MobAge.config.getBoolean("Debug_for_spawning")){
			PluginIO.sendPluginInfo("A location was deemed inactive, and spawn cancelled ("+(int) dist+"!<"+rad+")");
		}
		
		return false;
	}

	public static void resetAge(Entity ent, String reason){
		ent.setTicksLived(1);
	}

	public List<Chunk> getNearbyChunks(Chunk chunk){
		int x = chunk.getX(), z = chunk.getZ();
		List<Chunk> chunklist = new ArrayList<Chunk>();
		chunklist.add(chunk.getWorld().getChunkAt(x-1, z-1));
		chunklist.add(chunk.getWorld().getChunkAt(x-1, z));
		chunklist.add(chunk.getWorld().getChunkAt(x-1, z+1));
		chunklist.add(chunk.getWorld().getChunkAt(x, z-1));
		chunklist.add(chunk.getWorld().getChunkAt(x, z));
		chunklist.add(chunk.getWorld().getChunkAt(x, z+1));
		chunklist.add(chunk.getWorld().getChunkAt(x+1, z-1));
		chunklist.add(chunk.getWorld().getChunkAt(x+1, z));
		chunklist.add(chunk.getWorld().getChunkAt(x+1, z+1));
		return chunklist;
	}
	
	public boolean isAllowedSpawn(Entity ent){
		
		boolean blaze = ent instanceof CraftBlaze;
		boolean cavespider = ent instanceof CraftCaveSpider;
		boolean creeper = ent instanceof CraftCreeper;
		boolean chicken = ent instanceof CraftChicken;
		boolean cow = ent instanceof CraftCow;
		boolean enderman = ent instanceof CraftEnderman;
		boolean ghast = ent instanceof CraftGhast;
		boolean giant = ent instanceof CraftGiant;
		boolean magmacube = ent instanceof CraftMagmaCube;
		boolean mooshroom = ent instanceof CraftMushroomCow;
		boolean monster = ent instanceof CraftMonster;
		boolean ocelot = ent instanceof CraftOcelot;
		boolean pigzombie = ent instanceof CraftPigZombie;
		boolean pig = ent instanceof CraftPig;
		boolean silverfish = ent instanceof CraftSilverfish;
		boolean skeleton = ent instanceof CraftSkeleton;
		boolean spider = ent instanceof CraftSpider;
		boolean sheep = ent instanceof CraftSheep;
		boolean slime = ent instanceof CraftSlime;
		boolean snowman = ent instanceof CraftSnowman;
		boolean squid = ent instanceof CraftSquid;
		boolean villager = ent instanceof CraftVillager;
		boolean wolf = ent instanceof CraftWolf;
		boolean zombie = ent instanceof CraftZombie;
		
		boolean no_match = !(blaze||cavespider||creeper||chicken||cow||enderman||ghast||giant||magmacube||mooshroom||monster||pigzombie||pig||silverfish||skeleton||spider||sheep||slime||snowman||squid||villager||wolf||zombie);
		boolean living = ent instanceof LivingEntity;
		
		if(no_match&&living){
			PluginIO.sendPluginInfo("Could not match Entity \""+ent+"\" to the whitelist");
			PluginIO.sendPluginInfo(" Please nag acuddlyheadcrab (author) about this at http://dev.bukkit.org/server-mods/mobage/");
		}
		
		
		
		if(blaze){return getWLSpawnKey("Monsters", "Blaze");}
		if(cavespider){return getWLSpawnKey("Monsters", "CaveSpider");}
		if(creeper){return getWLSpawnKey("Monsters", "Creeper");}
		if(enderman){return getWLSpawnKey("Monsters", "Enderman");}
		if(ghast){return getWLSpawnKey("Monsters", "Ghast");}
		if(giant){return getWLSpawnKey("Monsters", "Giant");}
		if(magmacube){return getWLSpawnKey("Monsters", "MagmaCube");}
		
		if(monster){return getWLSpawnKey("Monsters", "Monster");}
		
		if(pigzombie){return getWLSpawnKey("Monsters", "PigZombie");}
		if(silverfish){return getWLSpawnKey("Monsters", "SilverFish");}
		if(skeleton){return getWLSpawnKey("Monsters", "Skeleton");}
		if(spider){return getWLSpawnKey("Monsters", "Spider");}
		if(zombie){return getWLSpawnKey("Monsters", "Zombie");}
		
		
		
		if(chicken){return getWLSpawnKey("Animals", "Chicken");}
		if(cow){return getWLSpawnKey("Animals", "Cow");}
		if(mooshroom){return getWLSpawnKey("Animals", "MushroomCow");}
		
		if(pig){return getWLSpawnKey("Animals", "Pig");}
		if(ocelot){return getWLSpawnKey("Animals", "Ocelot");}
		
		if(sheep){return getWLSpawnKey("Animals", "Sheep");}
		if(slime){return getWLSpawnKey("Animals", "Slime");}
		if(snowman){return true;}
		if(squid){return getWLSpawnKey("Animals", "Squid");}
		if(villager){return getWLSpawnKey("Animals", "Villager");}
		if(wolf){return getWLSpawnKey("Animals", "Wolf");}
		
		return false;
	}
	
	public boolean getWLSpawnKey(String key, String subkey){
		boolean returnable = true;
		String finalkey = "Whitelist."+key+"."+subkey;
		try{
			returnable =  MobAge.whitelist.getBooleanList(finalkey).get(0);
		}catch(NullPointerException e){PluginIO.sendPluginInfo("YML error; Invalid key, \""+finalkey+"\""); return false;}
		
		return returnable;
		
	}
	
	
}