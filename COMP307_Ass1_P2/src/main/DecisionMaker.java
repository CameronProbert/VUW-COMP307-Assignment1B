package main;

import java.util.*;
import java.io.*;

public class DecisionMaker {

	// some bits of java code that you may use if you wish.
	// assumes that the enclosing class has fields:
	int numCategories;
	int numAtts;
	List<String> categoryNames;
	List<String> attNames;
	List<Instance> allInstances;
	
	String fileName1;
	String fileName2;
	
	public DecisionMaker(String file1, String file2) {
		fileName1 = file1;
		fileName2 = file2;
		readDataFile(file1);
		buildTree(allInstances, attNames);
	}
	
	private Instance buildTree(List<Instance> instances, List<String> attributes) {
	//instances: the set of training instances that have been classified to the node being constructed;
	//attributes:: the list of attributes that were not used on the path from the root to this node.
	//BuildTree (Set instances, List attributes)
			//if instances is empty
				//return a leaf node containing the name and probability of the overall most probable class (ie, the ‘‘baseline’’ predictor)
			//if instances are pure
				//return a leaf node containing the name of the class of the instances in the node and probability 1
			//if attributes is empty
				//return a leaf node containing the name and probability of the majority class of the instances in the node (choose randomly if classes are equal)
			//else find best attribute:
				//for each attribute
					//separate instances into two sets: 
								//instances for which the attribute is true, and
								//instances for which the attribute is false
					//compute purity of each set.
					//if weighted average purity of these sets is best so far
						//bestAtt = this attribute
						//bestInstsTrue = set of true instances
						//bestInstsFalse = set of false instances
				//build subtrees using the remaining attributes:
					//left = BuildTree(bestInstsTrue, attributes - bestAtt)
					//right = BuildTree(bestInstsFalse, attributes - bestAttr)
				//return Node containing (bestAtt, left, right)
		return null;
	}
	
	/**
	 * Checks to make sure it is given only 2 filenames and then creates a DecisionMaker
	 * @param args
	 */
	public static void main(String[] args){
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
	private void readDataFile(String fname) {
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

			allInstances = readInstances(din);
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
		String ln;
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
