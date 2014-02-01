import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

// A-Star implementation
// @authors: Abilio Oliveira and James Ryden
public class AStarExp2 implements AIModule
{
    static class Node{
	public Point point;
	public Node parent;
	public double g;
	public double f;

	public boolean equals(AStarExp2.Node n){
	    return this.point.equals(n.point)?true:false;
	}

	public Node(final TerrainMap map, Point point, AStarExp.Node parent = null){
	    this.point = new Point(map.getStartPoint());
	    this.parent = parent;
	    this.g = 0.0;
	    this.f = super.getHeuristics(map, this.point, map.getEndPoint());
	}
    }

    /// AStar algorithm
    /**
     * @param map The terrain map that A-Star will compute.
     * @return The path from StartPoint to EndPoint or null in case of failure.
     */
    public List<Point> createPath(final TerrainMap map){
	// Necessary Variables
	AStarExp.Node node = new AStarExp.Node(map, map.getStartPoint());
	AStarExp.Node child = new AStarExp.Node(map, map.getStartPoint());
	ArrayList<AStarExp.Node> frontier = new ArrayList<AStarExp.Node>();
	final ArrayList<AStarExp.Node> explored = new ArrayList<AStarExp.Node>();
	Point neighbors[] = new Point[8];

	frontier.add(new AStarExp.Node(node));

	while(!frontier.isEmpty()){
	    node.copyNode(frontier.remove(0));
	    explored.add(0, new AStarExp.Node(node));
	    if (node.point.equals(map.getEndPoint())){
		return this.reconstructPath(explored, node);
	    }
	    neighbors = map.getNeighbors(node.point);	    
	    for(int i=0; i<8; i++){
		child.setValues(map, neighbors[i], node, map.getCost(node.point, child.point));
		if (!frontier.contains(child) && !explored.contains(child)){
		    frontier = this.insertionSort(frontier, child);
		}
	    }
	}

	return null;// failure
    }

    /// Computes the exponential heuristic value between two points.
    /**
     *@param map The terrain map to get the height of each point.
     *@param pt1 The source point.
     *@param pt2 The destination point.
     *@return h The value of the best case cost between pt1 and pt2.
     */
    private double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2){
	double hight_difference = map.getTile(pt2) - map.getTile(pt1);
	int y_difference = pt2.y - pt1.y;
	int x_difference = pt2.x - pt1.x;
	int tiles_between = (Math.abs(y_difference)==0)?((Math.abs(x_difference)==0)?0:Math.abs(x_difference)):Math.abs(y_difference);
	double up_or_down = (hight_difference < 0.0)?-1.0:1.0;

	double h = Math.abs(hight_difference)*(Math.exp(up_or_down)) + (double)tiles_between;
	    
	return h;
    }

}
