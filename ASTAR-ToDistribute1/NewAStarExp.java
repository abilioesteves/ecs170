import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

// A-Star implementation
// @authors Abilio Oliveira and James Dryden
public class NewAStarExp implements AIModule{
    
    // To enable a efficient behavior for the PriorityQueue
    public class NodeComparator implements Comparator<NewAStarExp.Node>, Serializable{
    	public int compare(NewAStarExp.Node n1, NewAStarExp.Node n2){
	    return (n1.f < n2.f)?-1:((n1.f>n2.f)?1:0);
    	}
    }

    public class Node{
	public String id; // used as a key for the HashMaps
	public Point point; //  "state"
	public NewAStarExp.Node parent; // from where it came from
	public double g;
	public double f;
	
	// To enable comparision between states
	@Override
	public boolean equals(Object obj){
	    if ((obj == null)||(getClass() != obj.getClass())){
		return false;
	    }
	    final NewAStarExp.Node other = (NewAStarExp.Node) obj;
	    if (this.point.equals(other.point)){
		return true;
	    }
	    return false; 
	}

	// Constructor
	public Node(final TerrainMap map, Point point, double g){
	    this.point = point;
	    this.id = point.x + "," + point.y;
	    this.parent = parent;
	    this.g = g;
	    this.f = g + getHeuristics(map, this.point, map.getEndPoint());
	}
	
    }
    /// AStar algorithm
    /**
     * @param map The terrain map that A-Star will compute.
     * @return The path from StartPoint to EndPoint or null in case of failure.
     */
    public List<Point> createPath(final TerrainMap map){
	// necessary to indicate with efficiency to closedSet which node needs to be removed
	PriorityQueue<NewAStarExp.Node> pQueue = new PriorityQueue<NewAStarExp.Node>(10,new NodeComparator()); 
	Map<String, NewAStarExp.Node> openSet = new HashMap<String, NewAStarExp.Node>();
	Map<String, NewAStarExp.Node> closedSet = new HashMap<String, NewAStarExp.Node>();
	NewAStarExp.Node start = new NewAStarExp.Node(map, map.getStartPoint(), 0.0);
	NewAStarExp.Node end = new NewAStarExp.Node(map, map.getEndPoint(),0.0);
	
	// initializations
	openSet.put(start.id, start);
	pQueue.add(start);
	while(openSet.size()>0){
	    // x, current node and also the best node in the path so far
	    NewAStarExp.Node x = pQueue.poll();
	    openSet.remove(x.id);
	    
	    if(x.id.equals(end.id)){ // got to the goal
		return reconstructPath(x);
	    } else {
		closedSet.put(x.id, x); // x will not be investigated anymore
		Point[] neighbors = map.getNeighbors(x.point);
		for (int i=0; i < neighbors.length; i++){
		    // if the neighbor being evaluated is already on the closed list, the algorithm simply ignores it
		    NewAStarExp.Node visited = closedSet.get(neighbors[i].x+","+neighbors[i].y); 
		    if (visited == null){
			double g = x.g + map.getCost(x.point, neighbors[i]);
			NewAStarExp.Node n = openSet.get(neighbors[i].x+","+neighbors[i].y);
			
			// if the neighbor is not in the openSet, initialize a new one adding it to the sets
			if (n == null){ 
			    n = new NewAStarExp.Node(map, neighbors[i], g);
			    n.parent = x;
			    openSet.put(n.id, n);
			    pQueue.add(n);
			}
			// if the neighbor is in the open set, we need to update its state, in case its g is greater than the one found
			else if (g < n.g){ 
			    n.parent = x;
			    n.g = g;
			    n.f = g + getHeuristics(map, n.point, end.point);
			}
		    }
		}
	    }
	}

	return null;
    }

    /// Retrieves the path found
    /**
     * @param node The goal node from which the path will be recovered
     * @return The path of points found
     */
    public List<Point> reconstructPath(NewAStarExp.Node node){
	ArrayList<Point> path = new ArrayList<Point>();
	
	path.add(node.point);

	while(node.parent != null){
	    path.add(0, node.parent.point);
	    node = node.parent;
	}

	return path;
    }

    /**
     *@param map The terrain map to get the height of each point.
     *@param pt1 The source point.
     *@param pt2 The destination point.
     *@return The value of the best case cost between pt1 and pt2.
     */
    private double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2){
	double VT = map.getTile(pt2) - map.getTile(pt1);
	int yd = pt2.y - pt1.y;
	int xd = pt2.x - pt1.x;
	int HT = (Math.abs(yd)==0)?((Math.abs(xd)==0)?0:(Math.abs(xd)-1)):(Math.abs(yd)-1);

	return HT*Math.exp((VT/(double)HT));
    }

    // public List<Point> createPath(final TerrainMap map){
    // 	NewAStarExp.Node probe = new NewAStarExp.Node(map, map.getStartPoint(), null, 0.0);
    // 	NewAStarExp.Node child = null;
    // 	Hashtable<String, NewAStarExp.Node> explored = new Hashtable<String, NewAStarExp.Node>(map.getWidth()*map.getHeight());
    // 	ArrayList<NewAStarExp.Node> frontier = new ArrayList<NewAStarExp.Node>();
    // 	Point[] neighbors = new Point[8];
    // 	int j = -1;
	
    // 	frontier.add(new NewAStarExp.Node(probe));

    // 	while(!frontier.isEmpty()){
    // 	    probe = frontier.remove(0);
    // 	    if(probe.point.equals(map.getEndPoint())){
    // 		return reconstructPath(probe);
    // 	    }
    // 	    explored.put(probe.point.x + "," + probe.point.y, probe);
    // 	    neighbors = map.getNeighbors(probe.point);
    // 	    for (int i = 0; i< neighbors.length; i++){
    // 		if(explored.get(neighbors[i].x + "," + neighbors[i].y)!=null) continue;
    // 		child = new Node(map, neighbors[i], probe, probe.g + map.getCost(probe.point, neighbors[i]));
    // 		j = frontier.indexOf(child);
    // 		if(j != -1){
    // 		    probe = frontier.remove(j);
    // 		    if (probe.f >= child.f){
    // 			probe.f = child.f;
    // 			probe.parent = child.parent;
    // 		    }
    // 		    insertionSort(frontier, probe);
    // 		    continue;
    // 		}
    // 		insertionSort(frontier, child);
    // 	    }
    // 	}

    // 	return null;
    // }

    // public void insertionSort(ArrayList<NewAStarExp.Node> frontier, NewAStarExp.Node child){
    // 	Iterator<NewAStarExp.Node> itr = frontier.iterator();
    // 	int i=0;
    // 	while(itr.hasNext()){
    // 	    NewAStarExp.Node object = itr.next();
    // 	    if (child.f < object.f){
    // 		break;
    // 	    }
    // 	    i++;
    // 	}
    // 	frontier.add(i, child);
    // 	return;
    // }

    /// Computes the exponential heuristic value between two points.

}
