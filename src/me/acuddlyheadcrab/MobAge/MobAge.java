package me.acuddlyheadcrab.MobAge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
    public final KillOldMobs killOldMobstask = new KillOldMobs(this);
    
// -------------------- MAIN METHODS START --------------------
    
    @Override
    public void onEnable() {
        pdf = getDescription();
        getServer().getPluginManager().registerEvents(entityListener, this);
        loadConfig();
        loadWhitelist();
        saveWhitelist();
        if(config.getBoolean("Debug.onStartup")) PluginIO.debugStartup();
        
        long delay = PluginIO.toTicks(config.getDouble("Age_Check_delay"));
        try{
            getServer().getScheduler().scheduleSyncRepeatingTask(this, killOldMobstask, 10L, delay);
        }catch(IllegalAccessError e){
            PluginIO.sendPluginInfo("Illegal Access Error");
            plugin.setEnabled(false);
            return;
        }
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
    
//    TODO: Fix/tidy up commands
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        
        boolean 
            mobage_ = cmd.getName().equalsIgnoreCase("mobage"),
            mwhitelist_ = cmd.getName().equalsIgnoreCase("mwhitelist"),
            test = cmd.getName().equalsIgnoreCase("mbtest")
        ;
        
        String
            perm_mobage_reload = "mobage.reload",
            perm_mobage_config = "mobage.config",
            perm_mobage_setconfig = "mobage.setconfig"
        ;
        
        if(test){
            if(sender instanceof Player){
                Location loc = ((Player) sender).getLocation();
                config.set("TestLocation", loc);
                sender.sendMessage("Put \""+loc+"\" in the config");
                saveConfig();
                reloadConfig();
            }
        }
        
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
        
        if(mwhitelist_){
            try{
                String arg1 = args[0], path = "";
                
                boolean
                    age = arg1.equalsIgnoreCase("age"),
                    spawn = arg1.equalsIgnoreCase("spawn")
                ;
                
                try{
                    
                    String 
                        arg2 = args[1];
                    path = getWhitelistEntPath(arg2);
                    
                    if(path!=null){
                        try{
                            String arg3 = args[2];
                            
                            if(age) setEntDoesAge(path, Boolean.parseBoolean(arg3));
                            if(spawn) setEntDoesAge(path, Boolean.parseBoolean(arg3));
                            
                            sender.sendMessage("Sucess!");
                            return true;
                            
                        }catch(IndexOutOfBoundsException e){
                            sender.sendMessage(ChatColor.RED+"/mbwhitelist <age|spawn> <entityname> [true/false]");
                        }
                    } else sender.sendMessage(ChatColor.RED+"Could not find the path \""+arg2+"\"");
                }catch(IndexOutOfBoundsException e){
                    
                }
            }catch(IndexOutOfBoundsException e){
                sender.sendMessage("/mwhitelist help");
            }
        }
        
        return true;
    }
    
    public void setEntCanSpawn(String path, boolean spawn){
        List<Boolean> bool_list = whitelist.getBooleanList(path);
        bool_list.set(0, spawn);
        whitelist.set(path, bool_list);
    }
    
    public void setEntDoesAge(String path, boolean spawn){
        List<Boolean> bool_list = whitelist.getBooleanList(path);
        bool_list.set(1, spawn);
        whitelist.set(path, bool_list);
    }
    
    public void setWhitelist(String path, Object newvalue){
        whitelist.set(path, newvalue);
        saveWhitelist();
        loadWhitelist();
    }
    
    public void setConfigVal(String path, boolean newvalue){
        config.set(path, newvalue);
        saveConfig();
        loadConfig();
    }
    
    public String getWhitelistEntPath(String entityname){
        if(entityname.equalsIgnoreCase("blaze")) return "Whitelist.Animal.Blaze";
        if(entityname.equalsIgnoreCase("cavespider")) return "Whitelist.Animal.CaveSpider";
        if(entityname.equalsIgnoreCase("creeper")) return "Whitelist.Animal.Creeper";
        if(entityname.equalsIgnoreCase("enderman")) return "Whitelist.Animal.Enderman";
        if(entityname.equalsIgnoreCase("ghast")) return "Whitelist.Animal.Ghast";
        if(entityname.equalsIgnoreCase("giant")) return "Whitelist.Animal.Giant";
        if(entityname.equalsIgnoreCase("magmacube")) return "Whitelist.Animal.MagmaCube";
        if(entityname.equalsIgnoreCase("monster")) return "Whitelist.Animal.Monster";
        if(entityname.equalsIgnoreCase("pigzombie")) return "Whitelist.Animal.PigZombie";
        if(entityname.equalsIgnoreCase("silverfish")) return "Whitelist.Animal.SilverFish";
        if(entityname.equalsIgnoreCase("skeleton")) return "Whitelist.Animal.Skeleton";
        if(entityname.equalsIgnoreCase("spider")) return "Whitelist.Animal.Spider";
        if(entityname.equalsIgnoreCase("zombie")) return "Whitelist.Animal.Zombie";
        if(entityname.equalsIgnoreCase("chicken")) return "Whitelist.Animal.Chicken";
        if(entityname.equalsIgnoreCase("cow")) return "Whitelist.Animal.Cow";
        if(entityname.equalsIgnoreCase("irongolem")) return "Whitelist.Animal.IronGolem";
        if(entityname.equalsIgnoreCase("mooshroom")) return "Whitelist.Animal.MooShroom";
        if(entityname.equalsIgnoreCase("ocelot")) return "Whitelist.Animal.Ocelot";
        if(entityname.equalsIgnoreCase("pig")) return "Whitelist.Animal.Pig";
        if(entityname.equalsIgnoreCase("sheep")) return "Whitelist.Animal.Sheep";
        if(entityname.equalsIgnoreCase("slime")) return "Whitelist.Animal.Slime";
        if(entityname.equalsIgnoreCase("squid")) return "Whitelist.Animal.Squid";
        if(entityname.equalsIgnoreCase("villager")) return "Whitelist.Animal.Villager";
        if(entityname.equalsIgnoreCase("wolf")) return "Whitelist.Animal.Wolf";
        return null;
    }
    
}
