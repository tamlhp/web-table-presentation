/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.datastruct;

import dataprocessing.sampling.SSchema;
import java.util.List;

/**
 *
 * @author Tuan Chau
 */
public class GSSchema extends SSchema{
    public String domain;
    
    public GSSchema(String name, List<String> attributes) {
        super(name, attributes);
    }

    public GSSchema(String name, String domain, List<String> attributes) {
        super(name, attributes);
        this.domain = domain;
    }
    
    public int getNumberOfAttribute(){
        return mapAttrs.size();
    }
    
    public int getNumberOfRow(){
        return table.size();
    }
}
