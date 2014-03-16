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
	final static double LEARNINGRATE = 0.05;

	/// Classifier class
	/**
	* Computes over the ANN; or computes THE ANN
	*/
	public static class Classifier {

		//@todo
		public static void train(ANN ann, String net_file_name, ArrayList<String> trainingSeq) {
			Iterator itr = trainingSeq.iterator();
			ArrayList<Double> input = new ArrayList<Double>();
			ArrayList<Double> output_input = new ArrayList<Double>();
			double[] output_error = new double[NUMBEROFOUTPUTUNITS];
			double[] hidden_error = new double[NUMBEROFHIDDENUNITS];
			double type = 0.0;

			while(itr.hasNext()){ // for each training episode perfom BackPropagationAlgorithm
				String e = (String)itr.next();

				input.add(1.0); // x0 is always 1
				input.addAll(parsePixelsToInput(e, type)); 
				output_input.add(1.0);

				// 1st part - Backpropagation Algorithm
				for (int j = 0; j < NUMBEROFHIDDENUNITS; j++) {
					ann.hidden_units.get(j).sigmoidFunction(input);
					output_input.add(ann.hidden_units.get(j).output);
				}
				for (int j = 0; j < NUMBEROFOUTPUTUNITS; j++) {
					ann.output_units.get(j).sigmoidFunction(output_input);
				}
				// 1st part - Backpropagation Algorithm

				// 2nd part - Backpropagation Algorithm
				for (int j = 0; j < NUMBEROFOUTPUTUNITS; j++) {
					double o = ann.output_units.get(j).output;
					output_error[j] = o*(1.0 - o)*(type - o);
				}
				// 2nd part - Backpropagation Algorithm

				// 3rd part - Backpropagation Algorithm
				for (int j = 0; j < NUMBEROFHIDDENUNITS; j++) {
					double sum = 0.0;
					double h = ann.hidden_units.get(j).output;
					for (int k = 0; k < NUMBEROFOUTPUTUNITS; k++) {
						sum += output_error[k]*ann.output_units.get(k).weights[j+1];
					}
					hidden_error[j] = h*(1 - h)*sum;
				}
				// 3rd part - Backpropagation Algorithm

				// 4th part - Backpropagation Algorithm
					// hidden layer goes first
				for (int j = 0; j < NUMBEROFHIDDENUNITS; j++) {
					ann.hidden_units.get(j).updateWeights(input, hidden_error[j]);
				}
					// output layer by last
				for (int j = 0; j < NUMBEROFOUTPUTUNITS; j++) {
					ann.output_units.get(j).updateWeights(output_input, output_error[j]);
				}
				// 4th part - Backpropagation Algorithm

				input.clear();
				output_input.clear();
			}

			return;
		}

		public static int fiveFoldCrossValidation(ANN ann, String net_file_name) {
			double n = 0.05; // learning rate
			ArrayList<Double> input = new ArrayList<Double>();
			ArrayList<String> trainingEpisodeSeq = new ArrayList<String>();
			ArrayList<String> testEpisodeSeq = new ArrayList<String>();

			for (int x = 0; x < 10; x++) { // ten experiments
				for (int i = 0; i < 5; i++) { // five-fold cross-validation

					episodeSeq(x+1, i+1, trainingEpisodeSeq, testEpisodeSeq);
					
					train(ann, net_file_name, trainingEpisodeSeq);

					test(ann, testEpisodeSeq);

				}
			}

			ann.saveNetwork(net_file_name);
			return 0;
		}

		// @todo: trainingEpisodeSeq and testEpisodeSeq will hold an array of file names, in the right sequence, so we can train our network and test it
		public static void episodeSeq(int expirement, int fold, ArrayList<String> trainingEpisodeSeq, ArrayList<String> testEpisodeSeq) {
			return;
		}

		// @todo: this method will parse a certain file and return an integer array with the values of each pixel over 128 
		// (we need to normalize the values) so it fit the range -1 to 1
		public static ArrayList<Double> parsePixelsToInput(String inputFile, Double type) {
			ArrayList<Double> levels = new ArrayList<Double>();
			String line = null;
			int i, j;
			double g;
			char t;

			try {
				BufferedReader in = new BufferedReader(new FileReader(inputFile));
				if(inputFile.charAt(2) == 'M')
					type = MALE;
				else if (inputFile.charAt(2) == 'F')
					type = FEMALE;

				while((line = in.readLine()) != null)
				{
					i = 0;
					while(line.charAt(i) != '\n')
					{
						if(line.charAt(i) != ' ')
						{
							g = line.charAt(i) - '0';
							levels.add(g/256);
						}
						i++;
					}
				}
			} catch (IOException e)
			{
				System.err.print("Exception: ");
				System.exit(1);
			}
			return levels;
		}

		// @todo
		public static int test(ANN ann, ArrayList<String> testEpisodeSeq) {
			Iterator itr = testEpisodeSeq.iterator();
			ArrayList<Double> input = new ArrayList<Double>();

			while(itr.hasNext()){
				String e = (String)itr.next();
				double type = 0.0;
				input.add(1.0);
				input.addAll(parsePixelsToInput(e, type));

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
		public double[] weights;
		public double output = 0.0;

		// construct the sigmoid unit weight array
		public SigmoidUnit (int number_of_weights) {
			this.weights = new double[number_of_weights + 1];
			for (int i = 0; i <= number_of_weights; i++) {
				weights[i] = 0.0;
			}
		}

		public void sigmoidFunction(ArrayList<Double> x) {
			for (int i = 0; i < x.size(); i++) {
				double output = 1.0/(1.0 + Math.exp(-(x.get(i)*this.output)));
			}
			this.output = output;
		}

		public void updateWeights(ArrayList<Double> input, double error) {
			for (int i = 0; i < this.weights.length; i++) {
				this.weights[i] = this.weights[i] + LEARNINGRATE*error*input.get(i);
			}
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
			result = Classifier.fiveFoldCrossValidation(ann, net_file_name);
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