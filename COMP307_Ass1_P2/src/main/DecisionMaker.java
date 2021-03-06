package main;

import java.util.*;
import java.io.*;

/**
 * The DecisionMaker takes 2 files (a training file and a test file) and builds
 * a tree with the training file, then tests the accuracy of the tree with a
 * test file.
 * 
 * @author Cameron Probert
 *
 */
public class DecisionMaker {

	// Attributes of the data
	private int numCategories;
	private int numAtts;
	private List<String> categoryNames;
	private List<String> attNames;

	// Lists of the instances
	private List<Instance> allTrainingInstances;
	private List<Instance> allTestInstances;

	// Base live and die probability
	private String baselineCategory;
	private double baselineProb;

	// The root node of the decision tree
	private Node rootNode;
	private PrintWriter writer;

	/**
	 * Constructs and tests the tree
	 * 
	 * @param file1
	 * @param file2
	 */
	public DecisionMaker(String file1, String file2) {

		try {
			writer = new PrintWriter("output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read in training files
		readFile(file1, true);

		// Calculate the probability that a patient lives or dies
		findBaseline();

		// Build the decision tree
		this.rootNode = buildTree(allTrainingInstances, clone(attNames));

		// Print the tree
		printTree();

		// Read in test
		readFile(file2, false);

		// Test the tree with the second file
		classifySecondFile();

		writer.close();
	}

	/**
	 * Prints the tree
	 */
	private void printTree() {
		if (rootNode != null) {
			writer.println(rootNode.printTree(""));
			System.out.println(rootNode.printTree(""));
		} else {
			System.out.println("RootNode has not be initialised!");
		}
	}

	/**
	 * Iterates through the test set of instances and classifies each instance
	 * according to the Decision tree that has been created. It then checks the
	 * given classification against the actual class of the test instance and to
	 * determine if the classification was correct. It then works out the
	 * percentage of correct classifications.
	 */
	private void classifySecondFile() {
		int numTotal = allTestInstances.size();
		int numCorrect = 0;

		// Classify each instance
		for (Instance instance : allTestInstances) {
			int classification = classify(instance, rootNode);
			if (classification == instance.getCategory()) {
				numCorrect++;
			}
		}

		// Print data
		System.out.println("\nTotal number of tests: " + numTotal);
		System.out.println("Total number of correct tests: " + numCorrect);
		System.out.println("Percentage of correct tests: "
				+ (numCorrect / (double) numTotal) * 100);
		writer.println("\nTotal number of tests: " + numTotal);
		writer.println("Total number of correct tests: " + numCorrect);
		writer.println("Percentage of correct tests: "
				+ (numCorrect / (double) numTotal) * 100);
	}

	/**
	 * Recursively classifies an instance
	 * 
	 * @param instance
	 *            The instance to be classified
	 * @param node
	 *            Should be given the root node when manually called
	 * @return The classification in int form
	 */
	private int classify(Instance instance, Node node) {

		// If it is a leafnode then get a random number and if it is less than
		// the probability number then return its classification, otherwise
		// return the other classification
		if (node instanceof LeafNode) {
			LeafNode leaf = (LeafNode) node;
			return categoryNames.indexOf(leaf.getClassification());
		}

		// Otherwise check the attribute of the tree node and compare it to that
		// attribute of the instance to see which branch it should take
		TreeNode tree = (TreeNode) node;
		String attribute = tree.getAttribute();
		int attrNum = attNames.indexOf(attribute);
		if (instance.getAtt(attrNum)) {
			return classify(instance, tree.getLeft());
		}
		return classify(instance, tree.getRight());
	}

	/**
	 * Finds the most likely classification and its probability of the overall
	 * data (without factoring in attributes)
	 */
	private void findBaseline() {
		int[] tally = tallyCategories(allTrainingInstances);
		int numOccuring = 0;
		String category = null;
		for (int i = 0; i < tally.length; i++) {
			// The most occurring category (live or die) gets calculated and the
			// number of times
			if (tally[i] > numOccuring) {
				numOccuring = tally[i];
				category = categoryNames.get(i);
			}
		}
		baselineCategory = category;
		baselineProb = numOccuring / (double) allTrainingInstances.size();
	}

	/**
	 * Modelled off psuedo-code in the assignment handout. Recursively creates a
	 * decision tree out of a set of instances
	 * 
	 * @param instances
	 * @param attributes
	 * @return
	 */
	private Node buildTree(List<Instance> instances, List<String> attributes) {
		// if instances is empty
		if (instances.isEmpty()) {
			// return a leaf node containing the name and probability of the
			// overall most probable class (ie, the ‘‘baseline’’ predictor)
			return new LeafNode(baselineCategory, baselineProb);
		}

		// if instances are pure
		if (isPure(instances)) {
			// return a leaf node containing the name of the class of the
			// instances in the node and probability 1
			return new LeafNode(categoryNames.get(instances.get(0)
					.getCategory()), 1);
		}

		// if attributes is empty
		if (attributes.isEmpty()) {
			int[] tally = tallyCategories(instances);
			int numOccuring = 0;
			String category = null;
			for (int i = 0; i < tally.length; i++) {
				if (tally[i] > numOccuring) {
					numOccuring = tally[i];
					category = categoryNames.get(i);
				}
			}
			return new LeafNode(category, numOccuring
					/ (double) instances.size());
		}

		// else find best attribute:
		else {
			// for each attribute
			String bestAtt = null;
			List<Instance> bestInstsTrue = new ArrayList<Instance>();
			List<Instance> bestInstsFalse = new ArrayList<Instance>();
			double bestPurity = 1;

			for (String attribute : attributes) {

				// Find the index of the current attribute
				int indexOfAtt = attNames.indexOf(attribute);

				// Create the lists to sort each instance into for this
				// attribute
				List<Instance> instsTrue = new ArrayList<Instance>();
				List<Instance> instsFalse = new ArrayList<Instance>();

				// separate instances into two sets:
				for (Instance instance : instances) {
					// instances for which the attribute is true, and
					if (instance.getAtt(indexOfAtt)) {
						instsTrue.add(instance);
					}
					// instances for which the attribute is false
					else {
						instsFalse.add(instance);
					}
				}

				// compute average purity of each set.
				double aveAttPurity = calculateWeightedImpurity(instsTrue,
						instsFalse);
				// if weighted average purity of these sets is best so far
				if (aveAttPurity < bestPurity) {
					// Update best data
					bestPurity = aveAttPurity;
					bestAtt = attribute;
					bestInstsTrue = instsTrue;
					bestInstsFalse = instsFalse;
				}
			}
			attributes.remove(bestAtt);
			// build subtrees using the remaining attributes:
			// left = BuildTree(bestInstsTrue, attributes - bestAtt)
			Node leftNode = buildTree(bestInstsTrue, clone(attributes));
			// right = BuildTree(bestInstsFalse, attributes - bestAttr)
			Node rightNode = buildTree(bestInstsFalse, clone(attributes));

			// return Node containing (bestAtt, left, right)
			return new TreeNode(bestAtt, leftNode, rightNode);
		}
	}

	/**
	 * Creates a new List that contains the same strings as the given list
	 * 
	 * @param attributes
	 * @return
	 */
	private List<String> clone(List<String> attributes) {
		List<String> newList = new ArrayList<String>();
		for (String attribute : attributes) {
			newList.add(attribute);
		}
		return newList;
	}

	/**
	 * Finds the total number of each category (live or die) in a set of
	 * instances
	 * 
	 * @param instances
	 * @return
	 */
	private int[] tallyCategories(List<Instance> instances) {
		int[] tally = new int[numCategories];
		for (Instance instance : instances) {
			tally[instance.getCategory()]++;
		}
		return tally;
	}

	/**
	 * Calculates the impurity of the Only works properly with 2 categories
	 * 
	 * @param instances
	 * @return
	 */
	private boolean isPure(List<Instance> instances) {
		// If there are no instances return 0 impurity
		// This should never happen
		if (instances.size() == 0) {
			System.out
					.println("SOMETHING IS WRONG - CHECKED IMPURITY OF LENGTH 0 INSTANCES");
			return true;
		}

		// Find how often each category occurs
		int[] count = new int[numCategories];
		for (Instance instance : instances) {
			count[instance.getCategory()]++;
		}

		// Calculate Impurity
		int totalInstances = instances.size();
		double impurity = 1; // count[0]/(double)totalInstances;
		for (int index = 0; index < numCategories; index++) {
			impurity *= (count[index] / (double) totalInstances);
		}

		return impurity == 0;
	}

	/**
	 * Calculates the impurity of each subset and weights it to determine the
	 * weighted impurity
	 * 
	 * @param instsTrue
	 * @param instsFalse
	 * @return
	 */
	private double calculateWeightedImpurity(List<Instance> instsTrue,
			List<Instance> instsFalse) {
		double totalInsts = instsTrue.size() + instsFalse.size();
		double impurityTrue = calculateImpurity(instsTrue)
				* (instsTrue.size() / totalInsts);
		double impurityFalse = calculateImpurity(instsFalse)
				* (instsFalse.size() / totalInsts);
		return impurityTrue + impurityFalse;
	}

	/**
	 * Calculates the impurity of a set of instances
	 * 
	 * @param instances
	 * @return
	 */
	private double calculateImpurity(List<Instance> instances) {
		// If there are no instances return 0 impurity
		// This should never happen
		if (instances.size() == 0) {
			return 0;
		}

		// Find how often each category occurs
		int[] count = new int[numCategories];
		for (Instance instance : instances) {
			count[instance.getCategory()]++;
		}

		// Calculate Impurity
		int totalInstances = instances.size();
		double impurity = 1; // count[0]/(double)totalInstances;
		for (int index = 0; index < numCategories; index++) {
			impurity *= (count[index] / (double) totalInstances);
		}

		return impurity;
	}

	/**
	 * Checks to make sure it is given only 2 filenames and then creates a
	 * DecisionMaker
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// First make sure that the arguments are valid
		try {
			if (args.length != 2) {
				throw new Exception(
						"This program must be given  2 filenames as parameters");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		new DecisionMaker(args[0], args[1]);
	}

	/*
	 * This method given as helper code
	 */
	private void readFile(String fname, boolean train) {
		/*
		 * format of names file: names of categories, separated by spaces names
		 * of attributes category followed by true's and false's for each
		 * instance
		 */
		System.out.println("Reading data from file " + fname);
		try {
			Scanner din = new Scanner(new File(fname));

			categoryNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
				categoryNames.add(s.next());
			numCategories = categoryNames.size();
			System.out.println(numCategories + " categories");

			attNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
				attNames.add(s.next());
			numAtts = attNames.size();
			System.out.println(numAtts + " attributes");

			if (train) {
				allTrainingInstances = readInstances(din);
			} else {
				allTestInstances = readInstances(din);
			}
			din.close();
		} catch (IOException e) {
			throw new RuntimeException("Data File caused IO exception");
		}
	}

	/*
	 * This method given as helper code
	 */
	private List<Instance> readInstances(Scanner din) {
		/* instance = classname and space separated attribute values */
		List<Instance> instances = new ArrayList<Instance>();
		while (din.hasNext()) {
			Scanner line = new Scanner(din.nextLine());
			instances
					.add(new Instance(categoryNames.indexOf(line.next()), line));
		}
		System.out.println("Read " + instances.size() + " instances");
		return instances;
	}

	/*
	 * This class given as helper code
	 */
	private class Instance {

		private int category;
		private List<Boolean> vals;

		public Instance(int cat, Scanner s) {
			category = cat;
			vals = new ArrayList<Boolean>();
			while (s.hasNextBoolean())
				vals.add(s.nextBoolean());
		}

		public boolean getAtt(int index) {
			return vals.get(index);
		}

		public int getCategory() {
			return category;
		}

		public String toString() {
			StringBuilder ans = new StringBuilder(categoryNames.get(category));
			ans.append(" ");
			for (Boolean val : vals)
				ans.append(val ? "true  " : "false ");
			return ans.toString();
		}

	}
}
