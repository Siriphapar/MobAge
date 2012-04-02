package me.acuddlyheadcrab.MobAge;

import java.util.List;

import org.bukkit.craftbukkit.entity.CraftSnowman;
import org.bukkit.Bukkit;
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
			if(MobAge.config.getBoolean("Debug.onSpawn")){
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
			if(!PluginIO.getWhiteListVal(event.getEntity(), "spawn")){
				if(MobAge.config.getBoolean("Debug.onSpawn")){
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
		
		if(MobAge.config.getBoolean("Debug.onSpawn")){
			PluginIO.sendPluginInfo("A location was deemed inactive, and spawn cancelled ("+(int) dist+"!<"+rad+")");
		}
		return false;
	}

	public static void resetAge(Entity ent, String reason){
		ent.setTicksLived(1);
	}
}