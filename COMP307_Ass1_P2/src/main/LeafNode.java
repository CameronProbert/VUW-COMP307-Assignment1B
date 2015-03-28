package main;

public class LeafNode implements Node {

	private String classification;
	private double probability;
	
	public LeafNode(String classification, double probability){
		this.classification = classification;
		this.probability = probability;
	}

	public String getClassification() {
		return classification;
	}

	public double getProbability() {
		return probability;
	}
}
