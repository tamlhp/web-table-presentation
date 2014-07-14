package dataprocessing.sampling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SSchema {
    public String domain;
    public String name;
    public int numAttr;
    public double relevant;
    protected Map<Integer, SRow> table = new HashMap<>();
    protected List<Map<Integer, Integer>> categories = new ArrayList<>();
    protected Map<String, Integer> mapAttrs = new HashMap<>();

    private SSchema(int numAtt) {
        this.numAttr = numAtt;
        for (int i = 0; i < this.numAttr; i++) {
            categories.add(new HashMap<Integer, Integer>());
        }
    }

    public SSchema(String name, List<String> attributes) {
        this.name = name;
        this.numAttr = attributes.size();
        for (int i = 0; i < this.numAttr; i++) {
            this.mapAttrs.put(attributes.get(i), i);
            categories.add(new HashMap<Integer, Integer>());
        }
    }

    public SRow getRow(int row) {
        return table.get(row);
    }

    public SCol getAttributeInstances(int aid) {
        SCol col = new SCol(aid);
        SInstance[] arr = new SInstance[table.size()];
        for (int i = 0; i < arr.length; i++) {
            col.add(table.get(i).getInstance(i));
        }

        return col;
    }

    public int getCatFrequency(int aid, int catid) {
        return categories.get(aid).get(catid);
    }

    public void addInstances(int iid, SInstance[] instances) {
        if (instances.length != numAttr) {
            throw new IllegalArgumentException();
        }

        SRow row = new SRow(iid, Arrays.asList(instances));
        table.put(iid, row);
        for (int i = 0; i < instances.length; i++) {
            Map<Integer, Integer> map = categories.get(i);
            int count = 1;
            int cid = instances[i].catId;
            if (map.containsKey(cid)) {
                count = map.get(cid) + 1;
            }
            map.put(cid, count);
        }
    }

    public void addInstances(SRow row) {
        if (row.instances.size() != numAttr) {
            throw new IllegalArgumentException();
        }

        int aid = 0;
        for (SInstance i : row.instances) {
            Map<Integer, Integer> map = categories.get(aid++);
            int count = 1;
            int cid = i.catId;
//            System.out.println("cid = " + cid);
            if (map.containsKey(cid)) {
                count = map.get(cid) + 1;
            }
//            System.out.println("cid = " + cid + "  " + count);
            map.put(cid, count);
        }

        table.put(row.rid, row);
    }

    public SRow removeInstance(int iid) {
        if (table.containsKey(iid)) {
//            SInstance[] ins = table.remove(iid);
            SRow row = table.remove(iid);
            int aid = 0;
            for (SInstance i : row.instances) {
                Map<Integer, Integer> map = categories.get(aid++);
                int count = 0;
                int cid = i.catId;
                if (map.containsKey(cid)) {
                    count = map.get(cid) - 1;
                }

                if (count < 0) {
                    count = 0;
                }

                map.put(cid, count);
            }

            return row;
        }

        return null;
    }

    public SSchema getSubSchema(int... instanceIds) {
        SSchema sub = new SSchema(this.numAttr);
        sub.name = name;
        sub.mapAttrs = mapAttrs;

        for (int id : instanceIds) {
            if (table.containsKey(id)) {
                sub.addInstances(table.get(id));
            }
        }
        return sub;
    }

    public int getLocalAttributeId(String name) {
        if (mapAttrs.containsKey(name)) {
            return mapAttrs.get(name);
        } else {
            return -1;
        }
    }

    public Collection<String> getAttributes() {
        return mapAttrs.keySet();
    }
    
    public int getNumberRows(){
        return table.size();
    }
    
    public List<SRow> getAllRows(){
        return new ArrayList<>(table.values());
    }

    public static void main(String[] args) {
        List<String> atts = Arrays.asList(new String[]{"1", "2", "3"});
        SSchema schema = new SSchema("schema", atts);

        SInstance[] ins = new SInstance[atts.size()];
        ins[0] = new SInstance("i00", 0);
        ins[1] = new SInstance("i01", 0);
        ins[2] = new SInstance("i02", 0);
        schema.addInstances(0, ins);

        ins = new SInstance[atts.size()];
        ins[0] = new SInstance("i10", 1);
        ins[1] = new SInstance("i11", 1);
        ins[2] = new SInstance("i12", 1);
        schema.addInstances(1, ins);

        ins = new SInstance[atts.size()];
        ins[0] = new SInstance("i20", 1);
        ins[1] = new SInstance("i21", 1);
        ins[2] = new SInstance("i22", 1);
        schema.addInstances(2, ins);

        ins = new SInstance[atts.size()];
        ins[0] = new SInstance("i30", 1);
        ins[1] = new SInstance("i31", 1);
        ins[2] = new SInstance("i32", 2);
        schema.addInstances(3, ins);

        SSchema sub = schema.getSubSchema(0, 1, 2, 3);

        System.out.println(sub.getCatFrequency(2, 1));

        HashSet<String> s = new HashSet<>();


    }
}
