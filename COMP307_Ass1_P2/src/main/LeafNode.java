package main;

public class LeafNode implements Node {

	private String classification;
	private double probability;
	
	public LeafNode(String classification, double probability){
		this.classification = classification;
		this.probability = probability;
		System.out.println(this.toString());
	}

	public String getClassification() {
		return classification;
	}

	public double getProbability() {
		return probability;
	}
	
	public String printTree(String indent){
		StringBuilder ans = new StringBuilder();
		ans.append("\n" + indent + toString());
		return ans.toString();
	}
	
	public String toString(){
		return ("LeafNode: Classification = " + classification + " || Probability = " + probability);
	}
}
