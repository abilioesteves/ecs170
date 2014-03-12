import javax.swing.*;
import java.util.*;

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

		public static int train(ANN ann, String fileName){
			return 0;
		}

		public static int test(ANN ann, String fileName){
			return 0;
		}

	}

	/**
	* Holds the Artificial Neural Network (ANN) structure and its methods
	*/
	public static class ANN{
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

		public ANN (String file){
			
		}
	}

	/// Sigmoid unit class
	/**
	* Holds the structure and methods related to the sigmoid units
	*/
	public static class SigmoidUnit{
		public ArrayList<Double> weights = new ArrayList<Double>(NUMBEROFINPUTS+1);
		public double output = 0.0;

		public SigmoidUnit () {
			for (int i = 0; i <= NUMBEROFINPUTS; i++) {
				weights.add(0.0);
			}
		}

		public SigmoidUnit (String file) {
			
		}

	}

	/// Program startup method
	/**
	* Parses command line arguments and set up our Classifier
	*/
	public static void main(String[] args){
		ANN ann;
		String file = "", test_file = "";
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
					test_file = args[i+1]; // initialize from file
					if (test_file.length() == 0){
						throw new IllegalArgumentException("test file name not specified");
					}
					test = true;
					i+=2;
				} else if (args[i].equalsIgnoreCase("-f")) {
					file = args[i+1];
					if (file.length() == 0) {
						file = "ANNProperties";
					}
					i+=2;
				} else {
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
			result = Classifier.train(ann, file);
		}else {
			ann = new ANN(test_file);
			result = Classifier.test(ann, file);
		}
	}
}