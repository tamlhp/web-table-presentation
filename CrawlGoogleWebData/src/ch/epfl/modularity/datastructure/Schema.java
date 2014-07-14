package ch.epfl.modularity.datastructure;

import java.util.ArrayList;
import java.util.List;

public class Schema extends BaseObject {

	public Schema(String name) {
		super(name);
	}

	public void printSchemaTree() {
		// TODO print tree of schema
	}

	public void traverseTree(TrarverseFunction func) {
		subTraverseTree(func, this, 0);
	}

	private void subTraverseTree(TrarverseFunction func, BaseObject root, int level) {
		func.read(root, level, root.getChildren().isEmpty());
		for (BaseObject child : root.getChildren()) {
			subTraverseTree(func, child, level + 1);
		}
	}

	public interface TrarverseFunction {
		public void read(BaseObject object, int level, boolean isLeaf);
	}

	public List<Attribute> getLeafChildren() {
		List<Attribute> list = new ArrayList<Attribute>();
		for (BaseObject b : getChildren()) {
			getLeafChildren(b, list);
		}

		return list;
	}

	private void getLeafChildren(BaseObject root, List<Attribute> result) {
		if (root.isLeaf()) {
			result.add((Attribute) root);
		} else {
			for (BaseObject b : root.getChildren()) {
				getLeafChildren(b, result);
			}
		}
	}
}
