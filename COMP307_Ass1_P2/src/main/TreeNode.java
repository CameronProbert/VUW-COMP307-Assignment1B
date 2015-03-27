package main;

public class TreeNode {
	
	private String attribute;
	private TreeNode left;
	private TreeNode right;
	
	public TreeNode(String bestAtt, TreeNode left, TreeNode right){
		this.attribute = bestAtt;
		this.left = left;
		this.right = right;
	}

}
