/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessing.sampling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tuan Chau
 */
public class SRow {
    public int rid;
    public List<SInstance> instances;
    
    public SRow(int rid){
        this.rid = rid;
        instances = new ArrayList<>();
    }
    
    public SRow(int rid, List<SInstance> instances){
        this.rid = rid;
        this.instances = instances;
    }
    
    public SInstance getInstance(int id){
        return instances.get(id);
    }
    
    public void add(SInstance instance){
        this.instances.add(instance);
    }
    
    public List<String> getStringInstances(){
        List<String> result = new ArrayList<>();
        for(SInstance i:instances){
            result.add(i.value);
        }
        
        return result;
    }
    
    public List<String> getStringInstances(int size){
        List<String> result = new ArrayList<>();
        for(int i = 0; i < size || i < instances.size(); i++){
            result.add(instances.get(i).value);
        }
        return result;
    }
    
    public String toString(){
        List<String> list = new ArrayList<>();
        for(SInstance i: instances){
            list.add(i.value);
        }
        
        return Arrays.toString(list.toArray());
    }
}
