package main;

/**
 * The LeafNode holds a classification and a probability that that
 * classification occurs. It is a kind of Node.
 * 
 * @author Cameron Probert
 *
 */
public class LeafNode implements Node {

	private String classification;
	private double probability;

	public LeafNode(String classification, double probability) {
		this.classification = classification;
		this.probability = probability;
	}

	public String getClassification() {
		return classification;
	}

	public double getProbability() {
		return probability;
	}

	/**
	 * Returns a String that represents a line of the tree
	 */
	public String printTree(String indent) {
		StringBuilder ans = new StringBuilder();
		String s = toString().replaceFirst("LeafNode: ", "");
		ans.append("\n" + indent + s);
		return ans.toString();
	}

	/**
	 * Returns a string that contains all needed information about the LeafNode
	 */
	@Override
	public String toString() {
		return ("LeafNode: Classification = " + classification
				+ " || Probability = " + probability);
	}
}
