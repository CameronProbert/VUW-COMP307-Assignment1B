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
}
