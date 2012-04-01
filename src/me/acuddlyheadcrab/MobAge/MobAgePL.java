package me.acuddlyheadcrab.MobAge;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class MobAgePL implements Listener{
	
	public static MobAge plugin;
	public MobAgePL(MobAge instance){plugin = instance;}
	
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		
		if(MobAgeEL.eventDebug){System.out.println("dmg");}
		
		Entity[] ent_ar = event.getTo().getBlock().getChunk().getEntities();
		
		for(int c=0;c<ent_ar.length;c++){
			Entity ent = ent_ar[c];
			
			if (!(ent instanceof Player)){
				ent.setTicksLived(1);
			}
			
			
		}
		
	}
	
	

}
