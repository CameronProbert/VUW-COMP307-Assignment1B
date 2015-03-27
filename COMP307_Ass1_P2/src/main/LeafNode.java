package main;

public class LeafNode implements Node {

	private int classification;
	private double probability;
	
	public LeafNode(int classification, double probability){
		this.classification = classification;
		this.probability = probability;
	}
}
