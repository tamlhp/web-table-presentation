/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessing.sampling;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuan Chau
 */
public class SCol {
    public int cid;
    public List<SInstance> instances;
    
    public SCol(int cid){
        this.cid = cid;
        this.instances = new ArrayList<>();
    }
    
    public SCol(int cid, List<SInstance> list){
        this.cid = cid;
        this.instances = list;
    }
    
    public void add(SInstance instance){
        this.instances.add(instance);
    }
    
    public SInstance getInstace(int index){
        return this.instances.get(index);
    }
}
