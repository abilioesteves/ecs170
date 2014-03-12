import javax.swing.*;
import java.util.*;
import java.io.*;

/// General Class.
/**
* Due to assignment constraints, all classes will be nested to this class.
*
* @author Abilio Oliveira and James Dryden
*/

public class AbilioOliveira_JamesDryden {
	final static double MALE = 0.9; // 100% confidence that a test is MALE
	final static double FEMALE = -0.9; // 100% confidence that a test is FEMALE
	final static int NUMBEROFOUTPUTUNITS = 1;
	final static int NUMBEROFHIDDENUNITS = 4;
	final static int NUMBEROFINPUTS = 120*128;

	/// Classifier class
	/**
	* Computes over the ANN; or computes THE ANN
	*/
	public static class Classifier{

		public static int train(ANN ann, String fileName) {
			Classifier.saveNetwork(ann, fileName);
			return 0;
		}

		public static int test(ANN ann, String fileName) {
			return 0;
		}

		public static void saveNetwork(ANN ann, String fileName) {
			try {
				FileOutputStream fileOut = new FileOutputStream(fileName + ".net");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(ann);
				out.close();
			} catch (IOException i){
				i.printStackTrace();
				System.exit(7);
			}
		}
	}

	/**
	* Holds the Artificial Neural Network (ANN) structure and its methods
	*/
	public static class ANN implements Serializable{
		public ArrayList<SigmoidUnit> hidden_units = new ArrayList<SigmoidUnit>(NUMBEROFHIDDENUNITS); // maybe a graph instead of an ArrayList? This needs some discussion.
		public ArrayList<SigmoidUnit> output_units = new ArrayList<SigmoidUnit>(NUMBEROFOUTPUTUNITS);

		public ANN (){
			for (int i = 0; i < NUMBEROFOUTPUTUNITS; i++) {
				output_units.add(new SigmoidUnit());
			}
			for (int i = 0; i< NUMBEROFHIDDENUNITS; i++) {
				hidden_units.add(new SigmoidUnit());
			}
		}

		// retrieve network structure from file
		public ANN (String fileName){
			ANN a = null;
			try {
				FileInputStream fileIn = new FileInputStream(fileName + ".net");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				a = (ANN)in.readObject();
				in.close();
				fileIn.close();
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
	}

	/// Sigmoid unit class
	/**
	* Holds the structure and methods related to the sigmoid units
	*/
	public static class SigmoidUnit implements Serializable{
		public ArrayList<Double> weights = new ArrayList<Double>(NUMBEROFINPUTS+1);
		public double output = 0.5;

		public SigmoidUnit () {
			for (int i = 0; i <= NUMBEROFINPUTS; i++) {
				weights.add(0.1);
			}
		}

	}

	/// Program startup method
	/**
	* Parses command line arguments and set up our Classifier
	*/
	public static void main(String[] args){
		ANN ann;
		String net_topol_file_name = "ANNProperties", test_file_name = "";
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
					test_file_name = args[i+1]; // initialize from file
					if (test_file_name.length() == 0){
						throw new IllegalArgumentException("test file name not specified");
					}
					test = true;
					i+=2;
				} else if (args[i].equalsIgnoreCase("-f")) {
					net_topol_file_name = args[i+1];
					if (net_topol_file_name.length() == 0) {
						net_topol_file_name = "ANNProperties";
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
			System.out.println("train XOR test = FALSE");
			System.exit(3);
		}else if(train){
			ann = new ANN();
			result = Classifier.train(ann, net_topol_file_name);
		}else if (test){
			ann = new ANN(net_topol_file_name);
			result = Classifier.test(ann, test_file_name);
		} else {
			System.out.println("no -train/-test argument passed");
			System.exit(4);
		}

		System.out.println("result = " + result);
	}
}