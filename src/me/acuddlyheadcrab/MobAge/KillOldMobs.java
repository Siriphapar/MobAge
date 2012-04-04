package me.acuddlyheadcrab.MobAge;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class KillOldMobs implements Runnable {
    private static boolean taskDebug = true;
    
    @SuppressWarnings("unused")
    private static MobAge plugin;
    public KillOldMobs(MobAge mobAge) {plugin = mobAge;}
    
    
    @Override
    public void run(){
        
        int 
            killcountall = 0,
            agelimit = MobAge.config.getInt("AgeLimit")
        ;
        
        for(World world : Bukkit.getServer().getWorlds()){
            
            int killcountworld = 0;
            
            if(world.getPlayers().size()>0){
                
                List<LivingEntity> entlist = world.getLivingEntities();
                
                for(LivingEntity ent : entlist){
                    boolean 
                        player = (ent instanceof Player),
                        tamed = ent instanceof Tameable ? ((Tameable) ent).isTamed() : false,
                        active = MobListener.inhabited(ent.getLocation(), null),
                        
                        canKill = !player&&!tamed&&active
                    ;
                    
                    if(canKill){
                        if(PluginIO.getWhiteListVal(ent, "age")){
                            if(ent.getTicksLived()>=agelimit){
                                ent.remove();
                            }    
                        }
                    }
                    
                }
                
                if(taskDebug){
                    PluginIO.sendPluginInfo("Removed "+killcountworld+" entities from the world "+world.getName());
                }   
            }
        }
        System.out.println("Removed entities");
        if(taskDebug){
            PluginIO.sendPluginInfo("Removed "+killcountall+" entities");
        }
        
    }
    
}
