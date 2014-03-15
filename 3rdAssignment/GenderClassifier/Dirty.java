import javax.swing.*;
import java.util.*;
import java.io.*;

/// General Class.
/**
* Due to assignment constraints, all classes will be nested to this class.
*
* @author Abilio Oliveira and James Dryden
*/

public class Dirty { 
	final static double MALE = 0.9; // high confidence that a test is MALE
	final static double FEMALE = -0.9; // high confidence that a test is FEMALE
	final static int NUMBEROFOUTPUTUNITS = 1;
	final static int NUMBEROFHIDDENUNITS = 4;
	final static int NUMBEROFINPUTS = 120*128;

	/// Classifier class
	/**
	* Computes over the ANN; or computes THE ANN
	*/
	public static class Classifier {

		//@todo
		public static int train(ANN ann, String net_file_name) {
			double n = 0.05; // learning rate
			ArrayList<Integer> input = new ArrayList<Integer>();
			ArrayList<String> trainingEpisodeSeq = new ArrayList<String>();
			ArrayList<String> testEpisodeSeq = new ArrayList<String>();

			// five-fold cross-validationw
			for (int i = 0; i < 5; i++){

				episodeSeq(i, trainingEpisodeSeq, testEpisodeSeq);
				Iterator itr = trainingEpisodeSeq.iterator();

				while(itr.hasNext()){
					String e = (String)itr.next();
					input.add(1); // x0 is always 1
					input.addAll(parsePixelsToInput(e));

					// train


					input.clear();
				}

				test(ann, testEpisodeSeq);

			}

			ann.saveNetwork(net_file_name);
			return 0;
		}

		// @todo: trainingEpisodeSeq and testEpisodeSeq will hold an array of file names, in the right sequence, so we can train our network and test it
		public static void episodeSeq(int fold, ArrayList<String> trainingEpisodeSeq, ArrayList<String> testEpisodeSeq) {
			return;
		}

		// @todo: this method will parse a certain file and return an integer array with the values of each pixel over 128 
		// (we need to normalize the values) so it fit the range -1 to 1
		public static ArrayList<Integer> parsePixelsToInput(String inputFile) {
			return null;
		}

		// @todo
		public static int test(ANN ann, ArrayList<String> testEpisodeSeq) {
			Iterator itr = testEpisodeSeq.iterator();
			ArrayList<Integer> input = new ArrayList<Integer>();

			while(itr.hasNext()){
				String e = (String)itr.next();
				input.add(1);
				input.addAll(parsePixelsToInput(e));

				// test

				input.clear();
			}
			
			// code block for debugging
			/*for (int i = 0; i < NUMBEROFOUTPUTUNITS; i++) {
				System.out.println("\n\n output " + i + " " + ann.output_units.get(i).output + "\n\t");
				for (int j = 0; j <= NUMBEROFHIDDENUNITS; j++){
					System.out.print(ann.output_units.get(i).weights.get(j) + " ");
				}
			}
			for (int i = 0; i< NUMBEROFHIDDENUNITS; i++) {
				System.out.println("\n\n hidden unit " + i + " " + ann.hidden_units.get(i).output +"\n\t");
				for (int j = 0; j <= NUMBEROFINPUTS; j++){
					System.out.print(ann.hidden_units.get(i).weights.get(j) + " ");
				}
			}*/
			return -1;
		}

	}

	/**
	* Holds the Artificial Neural Network (ANN) structure and its related methods
	*/
	public static class ANN implements Serializable {
		public ArrayList<SigmoidUnit> hidden_units = new ArrayList<SigmoidUnit>(NUMBEROFHIDDENUNITS); // maybe a graph instead of an ArrayList? This needs some discussion.
		public ArrayList<SigmoidUnit> output_units = new ArrayList<SigmoidUnit>(NUMBEROFOUTPUTUNITS);

		public ANN (){
			for (int i = 0; i < NUMBEROFOUTPUTUNITS; i++) {
				output_units.add(new SigmoidUnit(NUMBEROFHIDDENUNITS));
			}
			for (int i = 0; i< NUMBEROFHIDDENUNITS; i++) {
				hidden_units.add(new SigmoidUnit(NUMBEROFINPUTS));
			}
		}

		// retrieve network structure from file
		public ANN (String net_file_name){
			ANN a = null;
			try {	
				FileInputStream file_in = new FileInputStream(net_file_name + ".net");
				ObjectInputStream in = new ObjectInputStream(file_in);
				a = (ANN)in.readObject();
				in.close();
				file_in.close();
			} catch (IOException i){
				i.printStackTrace();
				System.exit(5);
			} catch (ClassNotFoundException c) {
				System.out.println("ANN class not found");
				c.printStackTrace();
				System.exit(6);
			} 
			this.hidden_units = a.hidden_units;
			this.output_units = a.output_units;
		}

		// save the network topology and weights in file
		public void saveNetwork(String net_file_name) {
			try {
				FileOutputStream fileOut = new FileOutputStream(net_file_name + ".net");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(this);
				out.close();
			} catch (IOException i){
				i.printStackTrace();
				System.exit(7);
			}
		}
	}

	/// Sigmoid unit class
	/**
	* Holds the structure and methods related to the sigmoid units
	*/
	public static class SigmoidUnit implements Serializable {
		public ArrayList<Double> weights;
		public double output = 0.0;

		// construct the sigmoid unit weight array
		public SigmoidUnit (int number_of_weights) {
			this.weights = new ArrayList<Double>(number_of_weights + 1);
			for (int i = 0; i <= number_of_weights; i++) {
				weights.add(0.0);
			}
		}

		// @todo
		public void sigmoidFunction(ArrayList<Integer> x) {
			for (int i = 0; i < x.size(); i++) {
				double output = 1.0/(1.0 + Math.exp(-(x.get(i)*this.output)));
			}
			this.output = output;
		}


	}

	/// Program startup method
	/**
	* Parses command line arguments and set up our Classifier
	*/
	public static void main(String[] args) {
		ANN ann;
		String net_file_name = "ANNProperties";
		boolean train = false, test = false;
		int i = 0, result = 0;
		try{
			while (i < args.length){
				if (args[i].equalsIgnoreCase("-train")) {
					// perform training
					train = true;
					i++;
				} else if (args[i].equalsIgnoreCase("-test")) {
					// perform testing
					test = true;
					i++;
				} else if (args[i].equalsIgnoreCase("-f")) {
					net_file_name = args[i+1];
					if (net_file_name.length() == 0) {
						net_file_name = "ANNProperties";
					}
					i+=2;
				} else {
					i++;
					throw new IllegalArgumentException("not known argument");
				}
			}
		} catch(IllegalArgumentException iae) {
			System.err.println("Illegal Arguments: " + iae.getMessage());
			System.exit(1);
		} catch(IndexOutOfBoundsException ioobe) {
			System.err.println("Illegal Arguments");
			System.exit(2);
		}
		if (train && test){
			System.out.println("please, -train XOR -test");
			System.exit(3);
		}else if(train){
			ann = new ANN();
			result = Classifier.train(ann, net_file_name);
		}else if (test){
			ann = new ANN(net_file_name);
			result = Classifier.test(ann, new ArrayList<String>());
		} else {
			System.out.println("no -train/-test argument passed");
			System.exit(4);
		}

		System.out.println("result = " + result);
	}
}