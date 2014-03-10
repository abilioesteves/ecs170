import javax.swing.*
import java.util.*


/// General Class.
/**
* Due to assignment constraints, all classes will be nested to this class.
*
* @author Abilio Oliveira and James Dryden
*/

public cass AbilioOliveira_JamesDryden {
	/// Sigmoid unit class
	/**
	* Holds the structure and functions related to the sigmoid units
	*/
	public class Sigmoid{

	}

	/// Classifier class
	/**
	* Encapsulates the network structure and deals with its behavior
	*/
	public class Classifier{

	}

	/// Entry Point class
	/**
	* Parses command line arguments and set up our Classifier
	*/
	public class Main{
		
		/// Program startup method
		public static void main(String[] args){
			try{
				int i = 0;
				while (i < args.length){
					if (args[i].equalsIgnoreCase("-train")) {
						// perform training
					} else if (args[i].equalsIgnoreCase("-test")){
						// perfrom testing
					}
				}
			}
		}
	}
}