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

}
