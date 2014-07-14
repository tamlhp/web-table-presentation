/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessing.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tuan Chau
 */
public class SData {

    protected List<Map<String, Integer>> mapCategories = new ArrayList<>();
    protected Map<String, SSchema> schemas = new HashMap<>();
    protected Map<String, Integer> mapAttrs = new HashMap<>();
    
    public void addAttribute(String name, int aid){
        mapAttrs.put(name, aid);
    }
    
    public int getGlobalAttributeId(String name){
        if(mapAttrs.containsKey(name)){
            return mapAttrs.get(name);
        }
        else{
            return -1;
        }
    }
    
    public void addSchema(SSchema schema){
        schemas.put(schema.name,schema);
    }
    
    public SSchema getSchema(String name){
        return schemas.get(name);
    }
    
    public int getCatId(String value, int aid) {
        Map<String, Integer> map;
        if (mapCategories.size() <= aid) {
            map = new HashMap<>();
            mapCategories.add(map);
        } else {
            map = mapCategories.get(aid);
        }
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            int id = map.size();
            map.put(value, id);
            return id;
        }
    }
    
    public List<SSchema> getSchemas(){
        return new ArrayList<>(schemas.values());
    }
}
