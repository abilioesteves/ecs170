import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

// A-Star implementation
// @authors: Abilio Oliveira and James Ryden
public class StupidAI implements AIModule
{
    /// Nested class to facilitate the reference between the points and its path-cost
    static class Node{
	public Point point;
	public Point cameFrom;
	public double g;
	public double f;
 
	/// Computes the exponential heuristic value between two points.
	/**
	 *@param map The terrain map to get the height of each point.
	 *@param pt1 The source point.
	 *@param pt2 The destination point.
	 *@return h The value of the best case cost between pt1 and pt2.
	 */
	public double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2){
	    int y_difference = pt2.y - pt1.y;
	    int x_difference = pt2.x - pt1.x;
	    double VT = map.getTile(pt2) - map.getTile(pt1);
	    int HT = (Math.abs(y_difference)==0)?((Math.abs(x_difference)==0)?0:Math.abs(x_difference)):Math.abs(y_difference);
	    
	    double relation = Math.abs(VT)/HT;
	    double Q = Math.ceil(relation);
	    double B = (Q-relation)*(Double)HT;
	    double R = VT - Q*B;
	    double S = (Double)HT - B + 1; 

	    double up_or_down = (VT < 0.0)?-1.0:1.0;

	    double h = Math.abs(hight_difference)*(Math.exp(up_or_down)) + (double)tiles_between;
	    
	    return h;
       	}

	/// Constructs and initializes a node at the initialization point and with some pre-defined g value.
	public Node(final TerrainMap map){
	    this.point = new Point(map.getStartPoint());
	    this.cameFrom = new Point(map.getStartPoint());
	    this.g = 0.0;
	    this.f = this.g + this.getHeuristics(map,this.point, map.getEndPoint());
	}
	
	/// Constructs and initializes a node with values from another node.
	public Node(StupidAI.Node node){
	    this.point = new Point(node.point);
	    this.cameFrom = new Point(node.cameFrom);
	    this.g = node.g;
	    this.f = node.f;
	}
	
	/// Constructs and initializes a node with empty/null values.
	public Node(){
	    this.point = new Point();
	    this.cameFrom = new Point();
	    this.g = 0.0;
	    this.f = Double.POSITIVE_INFINITY;
	}

	/// Calculates and sets the values to perform the successor function
	public void setValues(final TerrainMap map,Point newPoint, StupidAI.Node node, double step_cost){
	    this.point.setLocation(newPoint);
	    this.cameFrom.setLocation(node.point);
	    this.g = node.g + step_cost;
	    this.f = this.g + this.getHeuristics(map, this.point, map.getEndPoint());

	    return;
	}

	/// Copies the values from another node
	public void copyNode(Node newNode){
	    this.point.setLocation(newNode.point);
	    this.cameFrom = newNode.cameFrom;
	    this.g = newNode.g;
	    this.f = newNode.f;

	    return;
	}
    }
    
    /// AStar algorithm
    /**
     * @param map The terrain map that A-Star will compute.
     * @return The path from StartPoint to EndPoint or null in case of failure.
     */
    public List<Point> createPath(final TerrainMap map){
	// Necessary Variables
	StupidAI.Node node = new StupidAI.Node(map);
	StupidAI.Node child = new StupidAI.Node();
	ArrayList<StupidAI.Node> frontier = new ArrayList<StupidAI.Node>();
	frontier.add(new StupidAI.Node(node));
	final ArrayList<StupidAI.Node> explored = new ArrayList<StupidAI.Node>();
	Point neighbors[] = new Point[8];

	while(!frontier.isEmpty()){
	    node.copyNode(frontier.remove(0));
	    explored.add(0, new StupidAI.Node(node));
	    if (node.point.equals(map.getEndPoint())){
		return this.reconstructPath(explored);// TO-DO
	    }
	    neighbors = map.getNeighbors(node.point);	    
	    for(int i=0; i<8; i++){
		child.setValues(map, neighbors[i], node, map.getCost(node.point, child.point));
		if (!this.containsNode(frontier, child) && !this.containsNode(explored,child)){// TO-DO
		    frontier = this.insertionSort(frontier, child);
		}
	    }
	}

	return null;// failure
    }

    /// Insertion sort for the openSet
    /**
     * @param frontier The openset, which contains the Nodes to be explored.
     * @param child The node to be added to the openset
     * @return A new frontier
     */
    public ArrayList<StupidAI.Node> insertionSort(ArrayList<StupidAI.Node> frontier, StupidAI.Node child){
	Iterator<StupidAI.Node> itr = frontier.iterator();
	int i=0;
	while(itr.hasNext()){
	    StupidAI.Node object = itr.next();
	    if (child.f < object.f){
		break;
	    }
	    i++;
	}
        frontier.add(i, child);
	return frontier;
    }

    public Boolean containsNode(ArrayList<StupidAI.Node> set, StupidAI.Node node){
	Iterator<StupidAI.Node> itr = set.iterator();
	while(itr.hasNext()){
	    StupidAI.Node object = itr.next();
	    if(node.point.equals(object.point)){
		return true;
	    }
	}
	return false;
    }
    
    // TO-DO: see http://en.wikipedia.org/wiki/A*_search_algorithm
    public List<Point> reconstructPath(ArrayList<StupidAI.Node> explored){
	List<Point> path = new ArrayList<Point>();

	// construct path using cameFrom property
	
	return path;
    }
}
