package main;

public class Node {
	
	private String attribute;
	private Node left;
	private Node right;
	
	public Node(String bestAtt, Node left, Node right){
		this.attribute = bestAtt;
		this.left = left;
		this.right = right;
	}

}
