import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/// A sample AI that takes a very suboptimal path.
/**
 * This is a sample AI that moves as far horizontally as necessary to reach the target,
 * then as far vertically as necessary to reach the target.  It is intended primarily as
 * a demonstration of the various pieces of the program.
 * 
 * @author Leonid Shamis
 */
public class StupidAI implements AIModule
{
    // Nested class to facilitate the reference between the points and its path-cost
    static class Node{
	public Point point;
	public Point cameFrom;
	public double g;
	public double f;

	// TO-DO: straight 3-D line 
	public double calculatesF(double g, Point point){
	    return g;// uniform-cost search for now
       	}

	// Constructs and initializes a node at the initialization point and with some pre-defined g value.
	public Node(Point initPoint, double gInit){
	    this.point = new Point(initPoint);
	    this.cameFrom = new Point(initPoint);
	    this.g = 0.0;
	    this.f = this.calculatesF(this.g, this.point);
	}
	
	// Constructs and initializes a node with values from another node.
	public Node(StupidAI.Node node){
	    this.point = new Point(node.point);
	    this.cameFrom = new Point(node.cameFrom);
	    this.g = node.g;
	    this.f = node.f;
	}
	
	// Constructs and initializes a node with empty/null values.
	public Node(){
	    this.point = new Point();
	    this.cameFrom = new Point();
	    this.g = 0.0;
	    this.f = 0.0;
	}

	// Calculates and sets the values to perform the successor function
	public void setValues(Point newPoint, StupidAI.Node node, double step_cost){
	    this.point.setLocation(newPoint);
	    this.cameFrom.setLocation(node.point);
	    this.g = node.g + step_cost;
	    this.f = this.calculatesF(this.g, this.point);

	    return;
	}

	// Copies the values from another node
	public void copyNode(Node newNode){
	    this.point.setLocation(newNode.point);
	    this.cameFrom = newNode.cameFrom;
	    this.g = newNode.g;
	    this.f = newNode.f;

	    return;
	}
    }
    
    public List<Point> createPath(final TerrainMap map){
	StupidAI.Node node = new StupidAI.Node(map.getStartPoint(), 0.0);
	StupidAI.Node child = new StupidAI.Node();
	final ArrayList<StupidAI.Node> frontier = new ArrayList<StupidAI.Node>();
	frontier.add(new StupidAI.Node(node));
	final ArrayList<StupidAI.Node> explored = new ArrayList<StupidAI.Node>();
	Point neighbors[] = new Point[8];

	while(!frontier.isEmpty()){
	    node.copyNode(frontier.remove(0));
	    if (node.point.equals(map.getEndPoint())){
		explored.add(new StupidAI.Node(node));
		return this.reconstructPath(explored);// TO-DO
	    }
	    explored.add(new StupidAI.Node(node));
	    neighbors = map.getNeighbors(node.point);
	    for(int i=0; i<8; i++){
		child.setValues(neighbors[i], node, map.getCost(node.point, child.point));
		if (!frontier.contains(child) && !explored.contains(child)){// TO-DO
		    frontier.add(new StupidAI.Node(child));
		}
	    }
	}

	return null;// failure
    }

    // TO-DO: see http://en.wikipedia.org/wiki/A*_search_algorithm
    public List<Point> reconstructPath(ArrayList<StupidAI.Node> explored){
	List<Point> path = new ArrayList<Point>();

	// construct path using cameFrom property
	
	return path;
    }
}
