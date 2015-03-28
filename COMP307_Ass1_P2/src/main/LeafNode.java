package main;

public class LeafNode implements Node {

	private String classification;
	private double probability;
	
	public LeafNode(String classification, double probability){
		this.classification = classification;
		this.probability = probability;
	}
}
