/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.rbac.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author datouxi
 */
public class MapUtil {
    
    public static <T> List<T> toList(Map map){
        List<T> result=new ArrayList<>();
        
        Iterator iter=map.entrySet().iterator();
        while(iter.hasNext()){
            Entry entry=(Entry) iter.next();
            result.add((T) entry.getKey());
            result.addAll((Collection<? extends T>) toList((Map) entry.getValue()));
        }
        
        return result;
    }
    
    public static boolean removeObject(Map map,Object obj){
        if(map.containsKey(obj)){
            map.remove(obj);
            return true;
        }else{
            Iterator iter=map.keySet().iterator();
            while(iter.hasNext()){
                if(removeObject((Map) map.get(iter.next()),obj))
                    return true;
            }
        }
        
        return false;
    }
    
    public static boolean containObject(Map map,Object obj){
        if(map.containsKey(obj)){
            return true;
        }else{
            Iterator iter=map.keySet().iterator();
            while(iter.hasNext()){
                if(containObject((Map) map.get(iter.next()), obj))
                    return true;
            }
        }
        return false;
    }
    
}
