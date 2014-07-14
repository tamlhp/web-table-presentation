/**
 * *****************************************************************************
 * Copyright 2013 Lars Behnke
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cluster implements Comparable<Cluster>{
    
    private String name;
    private int ssn;
    private int newSSN;
    private Cluster parent;
    private List<Cluster> children;
    private Double distance;
    private int numLeaf = 0;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<Cluster> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }

        return children;
    }

    public void setChildren(List<Cluster> children) {
        this.children = children;
    }

    public Cluster getParent() {
        return parent;
    }

    public void setParent(Cluster parent) {
        this.parent = parent;
    }

    public Cluster getBrother() {
        if (parent != null) {
            List<Cluster> list = parent.getChildren();
            if (list.get(0) == this) {
                return list.get(1);
            } else {
                return list.get(0);
            }
        }
        return null;
    }
    
    public boolean isLeftChild(){
        if(parent != null){
            return (parent.getChildren().get(0) == this);
        }
        else
            return false;
    }

    public Cluster(int ssn) {
        this.ssn = ssn;
        name = Integer.toString(ssn);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getSSN(){
        return ssn;
    }
    
    public void setNewSSN(int ssn){
        this.newSSN = ssn;
    }
    
    public int getNewSSN(){
        return newSSN;
    }

    public void addChild(Cluster cluster) {
        getChildren().add(cluster);

    }

    public boolean contains(Cluster cluster) {
        return getChildren().contains(cluster);
    }

    @Override
    public String toString() {
        return "Cluster " + name;
    }

    @Override
    public boolean equals(Object obj) {
        String otherName = obj != null ? obj.toString() : "";
        return toString().equals(otherName);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    public int countLeafs() {
        return countLeafs(this, 0);
    }

    public int countLeafs(Cluster node, int count) {
        if (node.isLeaf()) {
            count++;
        }
        for (Cluster child : node.getChildren()) {
            count += child.countLeafs();
        }
        return count;
    }

    public void toConsole(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");

        }
        String name = getName() + (isLeaf() ? " (leaf)" : "") + (distance != null ? "  distance: " + distance : "");
        System.out.println(name);
        for (Cluster child : getChildren()) {
            child.toConsole(indent + 1);
        }
    }

    public double getTotalDistance() {
        double dist = getDistance() == null ? 0 : getDistance();
        if (getChildren().size() > 0) {
            dist += children.get(0).getTotalDistance();
        }
        return dist;

    }

    public List<Cluster> getChildrenLeaf() {
        List<Cluster> result = new ArrayList<>();
        subGetChildrenLeaf(this, result);

        return result;
    }

    private void subGetChildrenLeaf(Cluster root, List<Cluster> result) {
        if (root.isLeaf()) {
            result.add(root);
        } else {
            for (Cluster c : root.getChildren()) {
                subGetChildrenLeaf(c, result);
            }
        }
    }
    
    public void flip(){
        Cluster c1 = getChildren().get(0);
        children.remove(0);
        children.add(c1);
    }
    
    public int getHeight(){
        int height = 1;
        Cluster root = this;
        while(root.parent != null){
            height++;
            root = root.parent;
        }
        
        return height;
    }
    
    public int getNumLeafs(){
        return numLeaf;
    }
    
    public void initNumLeafs(){
        countNumLeafs();
    }
    
    private int countNumLeafs(){
        if(this.isLeaf()){
            return 1;
        }
        else{
            for(Cluster c:children){
                numLeaf += c.countNumLeafs();
            }
        }
        
        return numLeaf;
    }
    
    public List<Cluster> getAllChildrenHasLeaf(int numberLeafs){
        List<Cluster> result = new ArrayList<>();
        for(Cluster c: children){
            if(c.numLeaf <=numberLeafs){
                result.add(c);
            }
            else{
                result.addAll(c.getAllChildrenHasLeaf(numberLeafs));
            }
        }
        
        return result;
    }
    
    public List<Cluster> getKLowestCluster(int numberClusters){
        List<Cluster> result = new ArrayList<>();
        if(this.isLeaf()){
            result.add(this);
        }
        else if(numberClusters == 1){
            result.add(this);
        }
        else{
           
        }
        return result;
    }

    @Override
    public int compareTo(Cluster o) {
        return (int)((o.distance - distance) * 1E9);
    }
    
    public Cluster getLeftestInSet(Collection<Cluster> clusters){
        Set<Cluster> hashSet = new HashSet<>(clusters);
        return getLeftestInSet(this, hashSet);
    }
    
    private Cluster getLeftestInSet(Cluster root, Set<Cluster> clusters){
        if(clusters.contains(root)){
            return root;
        }
        else if(root.isLeaf()) {
            return null;
        }
        else{
            for(Cluster c:root.children){
                Cluster leftest = getLeftestInSet(c, clusters);
                if(leftest != null){
                    return leftest;
                }
            }
        }
        
        return null;
    }
    
    
}
