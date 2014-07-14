/**
 * *****************************************************************************
 * Copyright 2013 Lars Behnke, Tuan Chau
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package ch.epfl.modularity.clustering.hierachy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultClusteringAlgorithm implements ClusteringAlgorithm {
    private final Map<Integer, ClusterPair> mapPair = new HashMap<Integer, ClusterPair>();
    @Override
    public Cluster performClustering(double[][] distances, LinkageStrategy linkageStrategy) {

        /* Argument checks */
        if (distances == null || distances.length == 0
                || distances[0].length != distances.length) {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
       
        if (linkageStrategy == null) {
            throw new IllegalArgumentException("Undefined linkage strategy");
        }
        mapPair.clear();
        /* Setup model */
        List<Cluster> clusters = createClusters(distances.length);
//        System.out.println("distaces length = " + distances.length);
        
        List<ClusterPair> linkages = createLinkages(distances, clusters, mapPair);
//        System.out.println("number pair = " + linkages.size() + "    number pair = " + mapPair.size());
        /* Process */
        long t = System.currentTimeMillis();
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages, mapPair);
        while (!builder.isTreeComplete()) {
            builder.agglomerate2(linkageStrategy);
        }
//        t = System.currentTimeMillis() - t;
//        System.out.println("base order time = " + t);
        
        Cluster c = builder.getRootCluster();
        
        List<Cluster> leafs = c.getChildrenLeaf();
        for(Cluster l:leafs){
            l.setDistance(distanceClusters(l, l.getBrother()));
        }
        
        List<Cluster> listParentCluster = builder.getListParentCluster();
//        t = System.currentTimeMillis();
        flip(c, mapPair, listParentCluster);
//        t = System.currentTimeMillis() - t;
//        System.out.println("flip time = " + t);
        
        
        
        return c;
    }

    private List<ClusterPair> createLinkages(double[][] distances,
            List<Cluster> clusters, Map<Integer, ClusterPair> mapPair) {
//        System.out.println("createLinkages");
        List<ClusterPair> linkages = new ArrayList<>();
        for (int col = 0; col < clusters.size(); col++) {
            for (int row = col + 1; row < clusters.size(); row++) {
                ClusterPair link = new ClusterPair();
                link.setLinkageDistance(distances[col][row]);
                link.setlCluster(clusters.get(col));
                link.setrCluster(clusters.get(row));
                linkages.add(link);
//                MatrixLocation ml = new MatrixLocation(row, col);
//                System.out.println("hash: " + ml.toString());
                int hash = hashCode(row, col);
                if(mapPair.containsKey(hash)){
                    System.out.println("similar key: [" + row + "," + col + "]   link = " + link + "  [" + row + "," + col + "]");
                }
                mapPair.put(hash, link);
//                System.out.println(MatrixLocation.getInstance().toString() + " : " + link);
            }
        }
        return linkages;
    }
    
    

    private List<Cluster> createClusters(int numCluster) {
        List<Cluster> clusters = new ArrayList<>();
        for (int col = 0; col < numCluster; col++) {
            Cluster cluster = new Cluster(col);
            clusters.add(cluster);
        }
        return clusters;
    }

    //improve performance
    private void flip(Cluster cluster, double[][] disMatrix, LinkageStrategy ls) {
        List<Cluster> list = clusterNeedFlip(cluster);
//        System.out.println("flip: size = " + list.size());
        for (Cluster c : list) {
            List<Cluster> l = c.getChildren();
            Cluster bc = c.getBrother();
            if (bc != null) {
                double d1 = distanceClusters(l.get(0), bc, disMatrix, ls);
                double d2 = distanceClusters(l.get(1), bc, disMatrix, ls);
                boolean isLeft = c.isLeftChild();
                if (isLeft && d1 <= d2 || !isLeft && d1 >= d2) {
//                    System.out.println("flip: " + c.getName());
                    c.flip();
                } else {
//                    System.out.println("no flip: " + c.getName() + "  : " + d1 + "  " + d2);
                }
            }

        }
    }
    
    private void flip(Cluster cluster, Map<Integer, ClusterPair> mapPair) {
        List<Cluster> list = clusterNeedFlip(cluster);
//        System.out.println("flip: size = " + list.size());
        for (Cluster c : list) {
            List<Cluster> l = c.getChildren();
            Cluster bc = c.getBrother();
            if (bc != null) {
                double d1 = distanceClusters(l.get(0), bc, mapPair);
                double d2 = distanceClusters(l.get(1), bc, mapPair);
                boolean isLeft = c.isLeftChild();
                if (isLeft && d1 <= d2 || !isLeft && d1 >= d2) {
                    System.out.println("flip: " + c.getName());
                    c.flip();
                } else {
                    System.out.println("no flip: " + c.getName() + "  : " + d1 + "  " + d2);
                }
            }

        }
    }
    
    private void flip(Cluster cluster, Map<Integer, ClusterPair> mapPair, List<Cluster> listParent) {
        List<Cluster> list = listParent;
//        System.out.println("flip: size = " + list.size());
        for (Cluster c : list) {
            List<Cluster> l = c.getChildren();
            Cluster bc = c.getBrother();
            if (bc != null) {
                double d1 = distanceClusters(l.get(0), bc, mapPair);
                double d2 = distanceClusters(l.get(1), bc, mapPair);
                boolean isLeft = c.isLeftChild();
                if (isLeft && d1 <= d2 || !isLeft && d1 >= d2) {
                    c.flip();
                } else {
                }
            }

        }
    }

    //flag direct node inside cluster
    private List<Cluster> clusterNeedFlip(Cluster root) {
        List<Cluster> result = new ArrayList<>();
        subClusterNeedFlip2(root, result);
        return result;
    }

    private void subClusterNeedFlip(Cluster root, List<Cluster> result) {
        List<Cluster> children = root.getChildren();

        boolean flag = true;
        for (Cluster c : children) {
            if (!c.isLeaf()) {
                subClusterNeedFlip(c, result);
                flag = false;
            }
        }

        if (flag) {
            result.add(root);
        }
    }

    private void subClusterNeedFlip2(Cluster root, List<Cluster> result) {
        List<Cluster> children = root.getChildren();
        //all intermediate node need to flip
        if (children.size() >= 2) {
            result.add(root);
            for (Cluster c : children) {
              subClusterNeedFlip2(c, result);
          }
        }
        else{
        }
    }

    //improve performance
    private double distanceClusters(Cluster c1, Cluster c2, double[][] disMatrix, LinkageStrategy ls) {
        List<Cluster> l1 = c1.getChildrenLeaf();
        List<Cluster> l2 = c2.getChildrenLeaf();
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < l1.size(); i++) {
            int ii = Integer.parseInt(l1.get(i).getName());
            for (int j = 0; j < l2.size(); j++) {
                int jj = Integer.parseInt(l2.get(j).getName());
                list.add(disMatrix[ii][jj]);
            }
        }

        return ls.calculateDistance(list);
    }
    
    public double distanceClusters(Cluster c1, Cluster c2, Map<Integer, ClusterPair> mapPair){
        ClusterPair cp = mapPair.get(hashCode(c1.getSSN(), c2.getSSN()));
        if(cp == null){
            cp = mapPair.get(hashCode(c2.getSSN(), c1.getSSN()));
        }
        
        if(cp != null){
            return cp.getLinkageDistance().doubleValue();
        }
        else{
            return -1;
        }
    }
    
    public double distanceClusters(Cluster c1, Cluster c2){
        return distanceClusters(c1, c2, mapPair);
    }
    
    public Map<Integer, ClusterPair> getMapPair(){
        return mapPair;
    }
    
    public static int hashCode(int row, int col){
        return row * 1000000 + col;
    }
}
