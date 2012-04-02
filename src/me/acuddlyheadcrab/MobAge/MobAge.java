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
        pdf = getDescription();
        getServer().getPluginManager().registerEvents(entityListener, this);
        loadConfig();
        loadWhitelist();
        saveWhitelist();
        if(config.getBoolean("Debug.onStartup")) PluginIO.debugStartup();
        
        final List<World> wlist = Bukkit.getServer().getWorlds();
        long delay = PluginIO.toTicks(config.getDouble("Age_Check_delay"));
        try{
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    killOldMobs(wlist);
                }
            }, 10L, delay);
        }catch(IllegalAccessError e){PluginIO.sendPluginInfo("Illegal Access Error");plugin.setEnabled(false); return;}
    }
    
    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        PluginIO.sendPluginInfo("is now disabled :(");
    }
    
    public void loadConfig(){
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void reloadWhitelist() {
        if (whitelistConfigurationFile == null) 
            whitelistConfigurationFile = new File(getDataFolder(), "whitelist.yml");
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
        if (whitelist == null) reloadWhitelist();
        return whitelist;
    }
    
    public void saveWhitelist() {
        if (whitelist == null || whitelistConfigurationFile == null) return;
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
                    
                    if(PluginIO.getWhiteListVal(ent, "age")){
                        if(ent.getTicksLived()>=config.getInt("AgeLimit")){
                            ent.remove();
                        }    
                    }
                }
            }
        }
    }
    
//    TODO: Fix/tidy up commands
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        
        boolean 
            mobage_ = cmd.getName().equalsIgnoreCase("mobage")
        ;
        
        String
            perm_mobage_reload = "mobage.reload",
            perm_mobage_config = "mobage.config",
            perm_mobage_setconfig = "mobage.setconfig"
        ;
        
        if(mobage_){ 
            
            try{
                String arg1 = args[0];
                
                boolean
                    _help = arg1.equalsIgnoreCase("help"),
                    _reload = arg1.equalsIgnoreCase("reload"),
                    _config = arg1.equalsIgnoreCase("config")||arg1.equalsIgnoreCase("cfg"),
                    _setconfig = arg1.equalsIgnoreCase("setconfig")||arg1.equalsIgnoreCase("setcfg")
                ;
                
                if(_help){
                    PluginIO.displayHelp(sender, "help"); return true;
                }
                
                if(_reload){
                    if(sender.hasPermission(perm_mobage_reload)){
                        saveConfig();
                        saveWhitelist();
                        loadConfig();
                        loadWhitelist();
                        sender.sendMessage(ChatColor.GOLD+"Reloaded");
                        PluginIO.sendPluginInfo("Reloaded by "+sender.getName());
                    } else sender.sendMessage("You must have permission to do this"); return true;
                }
                
                if(_config){
                    if(sender.hasPermission(perm_mobage_config)){
                        PluginIO.displayHelp(sender, "config"); 
                        return true;
                    } else sender.sendMessage(ChatColor.RED+"You must have permission to do this"); return true;
                }
                
                if(_setconfig){
                    if(sender.hasPermission(perm_mobage_setconfig)){
                        try{
                            String arg2 = args[1];
                            String newv = "";
                            
                            boolean
                                _age_check_delay = arg2.equalsIgnoreCase("age_check_delay"),
                                _age_limit = arg2.equalsIgnoreCase("age_limit"),
                                _mob_limit = arg2.equalsIgnoreCase("mob_limit"),
                                _active_radius = arg2.equalsIgnoreCase("active_radius"),
                                _debug = arg2.equalsIgnoreCase("debug"),
                                _whitelist = arg2.equalsIgnoreCase("whitelist")
                            ;
                            
                            try{
                                String arg3 = args[2];
                                
                                if(_age_check_delay){
                                    try{
                                        config.set("Age_Check_delay", Double.parseDouble(arg3));
                                        newv = config.getString("Age_Check_Delay");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"Age_check_delay\" with the value: "+ChatColor.GRAY+newv); 
                                        saveConfig(); loadConfig();
                                        return true;
                                    }
                                    catch (NumberFormatException e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be recognized as a valid number!");
                                        return true;
                                    }
                                }
                                
                                if(_age_limit){
                                    try{
                                        config.set("AgeLimit", Double.parseDouble(arg3));
                                        newv = config.getString("AgeLimit");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"AgeLimit\" with the value: "+ChatColor.GRAY+newv); 
                                        saveConfig(); loadConfig();
                                        return true;
                                    }
                                    catch (NumberFormatException e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be recognized as a valid number!");
                                        return true;
                                    }
                                }
                                
                                if(_mob_limit){
                                    try{
                                        config.set("MobLimit", Double.parseDouble(arg3));
                                        newv = config.getString("MobLimit");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"MobLimit\" with the value: "+ChatColor.GRAY+newv); 
                                        saveConfig(); loadConfig();
                                        return true;
                                    }
                                    catch (NumberFormatException e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be recognized as a valid number!");
                                        return true;
                                    }
                                }
                                
                                if(_active_radius){
                                    try{
                                        config.set("Active_Radius", Double.parseDouble(arg3));
                                        newv = config.getString("Active_Radius");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"Active_Radius\" with the value: "+ChatColor.GRAY+newv); 
                                        saveConfig(); loadConfig();
                                        return true;
                                    }
                                    catch (NumberFormatException e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be recognized as a valid number!");
                                        return true;
                                    }
                                }
                                
                                if(_debug){
                                    try{
                                        config.set("Debug.onSpawn", Boolean.parseBoolean(arg3));
                                        newv = config.getString("Debug.onSpawn");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"Debug.onSpawn\" with the value: "+ChatColor.GRAY+newv); 
                                        saveConfig(); loadConfig();
                                        return true;
                                    }
                                    catch (Exception e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be parsed as a valid argument!");
                                        return true;
                                    }
                                }
                                
                                if(_whitelist){
                                    try{
                                        whitelist.set("Whitelist.Enabled", Boolean.parseBoolean(arg3));
                                        newv = whitelist.getString("Whitelist.Enabled");
                                        sender.sendMessage(ChatColor.GOLD+"Set key \"Whitelist.Enabled\" with the value: "+ChatColor.GRAY+newv);
                                        saveWhitelist(); loadWhitelist();
                                        return true;
                                    }
                                    catch (Exception e){
                                        sender.sendMessage(ChatColor.RED+"\""+arg2+"\" could not be parsed as a valid argument!");
                                        return true;
                                    }
                                }
                                
                            }catch(IndexOutOfBoundsException e){}
                        }catch (IndexOutOfBoundsException e) {}
                        PluginIO.displayHelp(sender, "setconfig");
                        return true;
                    } else sender.sendMessage(ChatColor.RED+"You must have permission to do this"); return true;
                }
            }catch(IndexOutOfBoundsException e){}
            PluginIO.displayHelp(sender, "help");
        }
        
        return true;
    }
    
}
