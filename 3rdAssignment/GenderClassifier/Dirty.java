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
	final static int NUMBEROFEXPERIMENTS = 10;
	final static int NUMBEROFFOLDS = 5;
	static ArrayList<ArrayList<String>> folds = new ArrayList<ArrayList<String>>(5);

	/// Classifier class
	/**
	* Computes over the ANN; or computes THE ANN
	*/
	public static class Classifier {

		//@todo
		public static void train(ANN ann, ArrayList<String> trainingSeq) {
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

		public static double[] fiveFoldCrossValidation(ANN ann) {
			int n = NUMBEROFEXPERIMENTS*NUMBEROFFOLDS;
			ArrayList<Double> input = new ArrayList<Double>();
			double[] results = new double[n];
			double mean = 0.0;
			double[] result = new double[2];

			for (int x = 0; x < NUMBEROFEXPERIMENTS; x++) { // ten experiments
				System.out.println("Fold " + x);
				Classifier.createFolds();
				System.out.println(folds.get(x) + "\n");
				for (int i = 0; i < NUMBEROFFOLDS; i++) { // five-fold cross-validation
					train(ann, Classifier.trainingSeq(i+1));
					results[i + x*NUMBEROFFOLDS] = test(ann, Classifier.testingSeq(i+1));
					mean += results[i + x*NUMBEROFFOLDS];
				}
				ann.clear();
				folds.clear();
			}
			mean = mean/n;
			result[0] = mean;
			result[1] = Classifier.computeStandardDeviation(results, mean);
			
			return result ;
		}

		
		public static void createFolds()
		{
			File[] malePaths, femPaths; //these four need to be stored outside of function
			String mstr, e;

			File m = null, f = null;
			int[] fnums = new int[5];
			int i,j,temp, ind;
			Random rand = new Random();
			ArrayList<String> allPaths = new ArrayList<String>();

			m = new File("./Male/");		//create list of files in each directory
			malePaths = m.listFiles();
			f = new File("./Female/");
			femPaths = f.listFiles();

			for(File t1:malePaths) {
				if (t1.getName().charAt(0) != 'a'){
					mstr = ("./Male/");
					mstr = mstr + t1.getName();
					allPaths.add(mstr);
				}
			}
			for(File t2:femPaths) {
				if (t2.getName().charAt(0) != 'b'){
					mstr = ("./Female/");
					mstr = mstr + t2.getName();
					allPaths.add(mstr);
				}
			}

			for (int n = 0; n < 5; n++)
				fnums[n] = n;

			Collections.shuffle(Arrays.asList(fnums));

			Iterator itr = allPaths.iterator();
			while (itr.hasNext()) {
				e = (String)itr.next();
				for(i = 0; i < 5; i++) {
					ind = fnums[i];
				//	System.out.println(e);
					folds.get(ind).add(e);
					if(itr.hasNext())
					{
						e = (String)itr.next();
						continue;
					}
				}
			}
		}

		public static ArrayList<String> trainingSeq(int fold){
			ArrayList<String> l = new ArrayList<String>();
			for (int i = NUMBEROFFOLDS; i > 0; i--) {
				if (i != fold)
					l.addAll(folds.get(i-1));
			}
			return l;
		}

		public static ArrayList<String> testingSeq(int fold){
			if (fold == 0){
				File t = new File("./Female/");;
				File[] paths = t.listFiles();
				ArrayList<String> allPaths = new ArrayList<String>();
				for(File p:paths) {
					if (p.getName().charAt(0) != 'b'){
						String str = ("./Female/");
						str = str + p.getName();
						allPaths.add(str);
					}
				}
				return allPaths;
			}
			return folds.get(fold);
		}

		public static double computeStandardDeviation(double[] results, double mean){
			double sum = 0.0;

			for (int i = 0; i < NUMBEROFFOLDS*NUMBEROFEXPERIMENTS; i++) {
				sum += Math.pow(results[i] - mean,2);
			}	

			return Math.sqrt(sum/(results.length-1));
		}

		// this method will parse a certain file and return an integer array with the values of each pixel over 256
		// (we need to normalize the values) so it fit the range -1 to 1
		public static ArrayList<Double> parsePixelsToInput(String inputFile, Double type) {
			ArrayList<Double> levels = new ArrayList<Double>();
			String line = null;
			int i;
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
					Scanner scanner = new Scanner(line);
					scanner.useDelimiter(" ");
					while (scanner.hasNext()){
						String result = (String)scanner.next();
						levels.add(Double.parseDouble(result)/256.0);
					}
				}
			} catch (IOException e)
			{
				System.err.print("Exception: " + e.getMessage() + "\n");
				System.exit(1);
			}
			return levels;
		}

		// 
		public static int test(ANN ann, ArrayList<String> testingSeq) {
			Iterator itr = testingSeq.iterator();
			ArrayList<Double> input = new ArrayList<Double>();
			ArrayList<Double> output_input = new ArrayList<Double>();

			while(itr.hasNext()){
				String e = (String)itr.next();
				double type = 0.0;
				input.add(1.0); // biased term
				input.addAll(parsePixelsToInput(e, type));

				for (int j = 0; j < NUMBEROFHIDDENUNITS; j++) {
					ann.hidden_units.get(j).sigmoidFunction(input);
					output_input.add(ann.hidden_units.get(j).output);
				}
				for (int j = 0; j < NUMBEROFOUTPUTUNITS; j++) {
					ann.output_units.get(j).sigmoidFunction(output_input);
				}

				Classifier.report(ann);

				input.clear();
				output_input.clear();
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

		// The whole algorithm was made as general as possible. This is the only method that actually ignores the constant for the number of output units.
		// In case of change in the number of output units, this method MUST be changed.
		// The confidence here calculated is simply the output/(MALE OR FEMALE - depending on the case)
		// if output = 0 (which is very unlikely), we have an UNDECIDABLE result (this is often related to some error in the algorithm)
		public static void report(ANN ann) {
			if(ann.output_units.get(0).output > 0.0) {
				System.out.println("MALE " + ann.output_units.get(0).output/MALE);
			} else if (ann.output_units.get(0).output < 0.0) {
				System.out.println("FEMALE " + ann.output_units.get(0).output/(FEMALE));
			} else {
				System.out.println("UNIDECIDABLE " + 0.0);
			}
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

		// clear the values for the network, without calling garbage collector.
		public void clear(){
			for (int i = 0; i < NUMBEROFOUTPUTUNITS; i++) {
				output_units.clear();
			}
			for (int i = 0; i< NUMBEROFHIDDENUNITS; i++) {
				hidden_units.clear();
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
		public Random g = new Random();

		// construct the sigmoid unit weight array
		public SigmoidUnit(int number_of_weights) {
			this.weights = new double[number_of_weights + 1];
			for (int i = 0; i <= number_of_weights; i++) { // we need one more weight for the biased term
				this.weights[i] = this.g.nextGaussian();
			}
		}

		public void sigmoidFunction(ArrayList<Double> input) {
			double sum = 0.0;
			for (int i = 0; i < input.size(); i++) {
				sum = input.get(i)*this.weights[i]; // scalar product
			}
			this.output	= 1.0/(1.0 + Math.exp(-(sum)));
		}

		public void updateWeights(ArrayList<Double> input, double error) {
			for (int i = 0; i < this.weights.length; i++) {
				this.weights[i] = this.weights[i] + LEARNINGRATE*error*input.get(i);
			}
		}

		// clear the values for the sigmoidUnit, without calling garbage collector.
		public void clear(){
			for (int i = 0; i <= this.weights.length; i++) { // we need one more weight for the biased term
				this.weights[i] = this.g.nextGaussian();
			}
			this.output = 0.0;
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
		int i = 0;
		double[] result = new double[2];
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
			for (i = 0; i < NUMBEROFFOLDS; i++) {
				folds.add(new ArrayList<String>());
			}
			// result = Classifier.fiveFoldCrossValidation(ann);
			// System.out.println("Mean: " + result[0] + "Standard Deviation: " + result[1]);
			Classifier.createFolds();
			Classifier.train(ann, Classifier.trainingSeq(0));
			ann.saveNetwork(net_file_name);
		}else if (test){
			ann = new ANN(net_file_name);
			Classifier.test(ann, Classifier.testingSeq(0));
		} else {
			System.out.println("no -train/-test argument passed");
			System.exit(4);
		}
	}
}