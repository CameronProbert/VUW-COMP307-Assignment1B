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
	
	public String printTree(String indent){
		StringBuilder ans = new StringBuilder();
		ans.append("\n" + indent + "Classification: " + classification);
		ans.append(" || Probability: " + Double.toString(probability));
		return ans.toString();
	}
}
