package main;

public class TreeNode implements Node {
	
	private String attribute;
	private Node left;
	private Node right;
	
	public TreeNode(String bestAtt, Node left, Node right){
		this.attribute = bestAtt;
		this.left = left;
		this.right = right;
	}

	public String getAttribute() {
		return attribute;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}
	
	public String printTree(String indent){
		StringBuilder ans = new StringBuilder();
		ans.append("\n" + indent + attribute + " = True");
		ans.append(left.printTree(indent + "|  "));
		ans.append("\n" + indent + attribute + " = False");
		ans.append(right.printTree(indent + "|  "));
		return ans.toString();
	}
}
