import javax.swing.*;
import java.util.*;

/// General Class.
/**
* Due to assignment constraints, all classes will be nested to this class.
*
* @author Abilio Oliveira and James Dryden
*/

public class AbilioOliveira_JamesDryden {
	final int MALE = 1; // 100% confidence that a test is MALE
	final int FEMALE = -1; // 100% confidence that a test is FEMALE

	/// Program startup method
	/**
	* Parses command line arguments and set up our Classifier
	*/
	public static void main(String[] args){
		ANN ann;
		int result;
		int i = 0;
		while (i < args.length){
			if (args[i].equalsIgnoreCase("-train")) {
				// perform training
				ann = new ANN();
				result = Classifier.train(ann);

			} else if (args[i].equalsIgnoreCase("-test")){
				// perfrom testing
				ann = new ANN(args[i+1]); // initialize from file
				result = Classifier.test(ann);
			}
		}
	}

	/// Classifier class
	/**
	* Computes over the ANN; or computes THE ANN
	*/
	public static class Classifier{

		public static int train(ANN ann){
			return 0;
		}

		public static int test(ANN ann){
			return 0;
		}

	}

	/**
	* Holds the Artificial Neural Network (ANN) structure and its methods
	*/
	public static class ANN{
		public ArrayList<SigmoidUnit> sigmoids = new ArrayList<SigmoidUnit>(); // maybe a graph instead of an ArrayList? This needs some discussion.

		public ANN (){

		}

		public ANN (String file){

		}
	}

	/// Sigmoid unit class
	/**
	* Holds the structure and methods related to the sigmoid units
	*/
	public class SigmoidUnit{
		public ArrayList<Integer> weights = new ArrayList<Integer>();
		public double output = 0.0;

	}
}