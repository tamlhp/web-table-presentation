package ch.epfl.modularity.datastructure;

public class Attribute extends BaseObject {
	private Schema schema;

	public Attribute(Schema schema, String name) {
		super(name, schema);
		this.schema = schema;
	}

	public Attribute(Attribute parent, String name) {
		super(name, parent);
		this.schema = parent.schema;
	}

	public String getFullName() {
		return schema.getName() + "." + getName();
	}

	public String getFullPath() {
		String path = getName();
		BaseObject parent = getParent();
		while (parent != null) {
			path = parent.getName() + "." + path;
			parent = parent.getParent();
		}

		return path;
	}
	
	public Schema getSchema(){
		return schema;
	}
}
