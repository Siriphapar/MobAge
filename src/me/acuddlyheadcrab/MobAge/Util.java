package me.acuddlyheadcrab.MobAge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Util {
    
    public static List<?>[] getVandK(Map<?, ?> map) {
        /*** 
         * Returns an array containing the keys and values
         */
        
        List<?>[] obj_list = {getValues(map), getKeys(map)};
        return obj_list;
    }
    
    public static List<?> getValues(Map<?, ?> map){
        List<Object> v_list = new ArrayList<Object>();
        Iterator<?> v_itr = map.values().iterator();
        while (v_itr.hasNext()) {
            Object ob = v_itr.next();
            v_list.add(ob);
        }
        return v_list;
    }
    
    public static List<?> getKeys(Map<?, ?> map){
        List<Object> k_list = new ArrayList<Object>();
        Iterator<?> k_itr = map.keySet().iterator();
        while (k_itr.hasNext()) {
            Object ob = k_itr.next();
            k_list.add(ob);
        }
        return k_list;
    }
}
