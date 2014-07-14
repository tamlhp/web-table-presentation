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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HierarchyBuilder {

    private List<ClusterPair> distances;
    private List<Cluster> clusters;
    private int numCluster;
    private Map<Integer, ClusterPair> mapPair;
    private Set<ClusterPair> setDistances;
    private Set<Cluster> setClusters;
    private List<Cluster> listParentCluster;

    public List<ClusterPair> getDistances() {
        return distances;
    }

    public Set<Cluster> getClusters() {
        clusters.addAll(setClusters);
        return setClusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, List<ClusterPair> distances, Map<Integer, ClusterPair> mapPair) {
        this.clusters = clusters;
        this.distances = distances;
        numCluster = clusters.size();
        this.mapPair = mapPair;
        
        setDistances = new HashSet<>();
        setDistances.addAll(distances);
        
        setClusters = new HashSet<>();
        setClusters.addAll(clusters);
        listParentCluster = new ArrayList<>();
        
        clusters.clear();
    }
    
    private ClusterPair findMinDistance(){
        ClusterPair min = null;
        for(ClusterPair p: setDistances){
            if(min == null || min.getLinkageDistance() > p.getLinkageDistance()){
                min = p;
            }
        }
        return min;
    }
    
    public void agglomerate2(LinkageStrategy linkageStrategy) {
        if (!setDistances.isEmpty()) {
            ClusterPair minDistLink = findMinDistance();
            setDistances.remove(minDistLink);
            setClusters.remove(minDistLink.getrCluster()); //slow point
            setClusters.remove(minDistLink.getlCluster()); //slow point

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(numCluster++);
            listParentCluster.add(newCluster);
//            System.out.println("new Cluster ssn = " + newCluster.getSSN() + "  oldL = " + oldClusterL.getName() + "  oldR = " + oldClusterR.getName());

            for (Cluster iClust : setClusters) {
//                System.out.println("cluster: " + iClust.getName());
                ClusterPair link1 = findByClusters(iClust, oldClusterL); //slow point
                ClusterPair link2 = findByClusters(iClust, oldClusterR); // slow point
                if (link1 == null || link2 == null) {
                    throw new RuntimeException("link null: [" + iClust.getName() + "," + oldClusterL.getName() + "] : " + link1 + "      [" + iClust.getName() + "," + oldClusterR.getName() + "] : " + link2 + "  ");
                }

                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Double> distanceValues = new ArrayList<>();
                if (link1 != null) {
                    distanceValues.add(link1.getLinkageDistance());
                    setDistances.remove(link1); // slow point 2
                }
                if (link2 != null) {
                    distanceValues.add(link2.getLinkageDistance());
                    setDistances.remove(link2); //slow point 3
                }
                Double newDistance = linkageStrategy
                        .calculateDistance(distanceValues);
                newLinkage.setLinkageDistance(newDistance);
                setDistances.add(newLinkage);
                mapPair.put(DefaultClusteringAlgorithm.hashCode(iClust.getSSN(), newCluster.getSSN()), newLinkage);
            }
            setClusters.add(newCluster);
        }
    }
    
    

    public void agglomerate(LinkageStrategy linkageStrategy) {
        long t0 = System.currentTimeMillis();
        Collections.sort(distances);//slow point 1

        if (distances.size() > 0) {
            ClusterPair minDistLink = distances.remove(0);
            System.out.println(minDistLink.toString());
            clusters.remove(minDistLink.getrCluster()); //slow point
            clusters.remove(minDistLink.getlCluster()); //slow point

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(numCluster++);
//            System.out.println("new Cluster ssn = " + newCluster.getSSN() + "  oldL = " + oldClusterL.getName() + "  oldR = " + oldClusterR.getName());

            for (Cluster iClust : clusters) {
//                System.out.println("cluster: " + iClust.getName());
                ClusterPair link1 = findByClusters(iClust, oldClusterL); //slow point
                ClusterPair link2 = findByClusters(iClust, oldClusterR); // slow point
                if (link1 == null || link2 == null) {
                    throw new RuntimeException("link null: [" + iClust.getName() + "," + oldClusterL.getName() + "] : " + link1 + "      [" + iClust.getName() + "," + oldClusterR.getName() + "] : " + link2 + "  ");
                }

                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Double> distanceValues = new ArrayList<>();
                if (link1 != null) {
                    distanceValues.add(link1.getLinkageDistance());
                    distances.remove(link1); // slow point 2
                }
                if (link2 != null) {
                    distanceValues.add(link2.getLinkageDistance());
                    distances.remove(link2); //slow point 3
                }
                Double newDistance = linkageStrategy
                        .calculateDistance(distanceValues);
                newLinkage.setLinkageDistance(newDistance);
                distances.add(newLinkage);
                mapPair.put(DefaultClusteringAlgorithm.hashCode(iClust.getSSN(), newCluster.getSSN()), newLinkage);
            }
            clusters.add(newCluster);
        }
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
//        System.out.println("c1 = " + c1.getName() + "  c2 = " + c2.getName());
        ClusterPair result = null;
//		for (ClusterPair link : distances) {
//			boolean cond1 = link.getlCluster().equals(c1)
//			        && link.getrCluster().equals(c2);
//			boolean cond2 = link.getlCluster().equals(c2)
//			        && link.getrCluster().equals(c1);
//			if (cond1 || cond2) {
//				result = link;
//				break;
//			}
//		}
        result = mapPair.get(DefaultClusteringAlgorithm.hashCode(c1.getSSN(), c2.getSSN()));
//        if (result != null) {
//            System.out.println("findByClusters hashcode = " + MatrixLocation.getInstance().toString());
//        }
        if (result == null) {
            result = mapPair.get(DefaultClusteringAlgorithm.hashCode(c2.getSSN(), c1.getSSN()));
        }
//        System.out.println("findByClusters hashcode = " + MatrixLocation.getInstance().toString() + "   " + result);
        return result;
    }

    public boolean isTreeComplete() {
        return setClusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        clusters.clear();
        clusters.addAll(setClusters);
        return clusters.get(0);
    }
    
    public List<Cluster> getListParentCluster(){
    	return listParentCluster;
    }
}
