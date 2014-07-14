package ch.epfl.modularity.datastructure;

import java.util.ArrayList;

public abstract class BaseObject {
	private String name;
	private BaseObject parent;
	private ArrayList<BaseObject> listChild;
	
	
	public BaseObject(String name){
		this.name = name;
		parent = null;
		listChild = new ArrayList<BaseObject>();
		
	}
	
	public BaseObject(String name, BaseObject parent){
		this.name = name;
		this.parent = parent;
		listChild = new ArrayList<BaseObject>();
	}
	
	public int addChild(BaseObject child){
		listChild.add(child);
		return listChild.size();
	}

	public String getName() {
		return name;
	}
	
	public String getFullName(){
		return name;
	}

	public BaseObject getParent() {
		return parent;
	}

	public ArrayList<BaseObject> getChildren() {
		return listChild;
	}
	
	public BaseObject getChild(String name){
		for(BaseObject bo: listChild){
			if(bo.getName().equals(name)){
				return bo;
			}
		}
		
		return null;
	}
	
	public boolean isLeaf(){
		return listChild.isEmpty();
	}
	
	public String toString(){
		return name;
	}

	public Object getSchema() {
		return null;
	}
}
