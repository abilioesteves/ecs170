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
	    this.point = new Point(point);
	    this.parent = parent;
	    this.g = 0.0;
	    this.f = super.getHeuristics(map, this.point, map.getEndPoint());
	}
	public void setValues(final TerrainMap map,Point newPoint, AStarExp2.Node node, double step_cost){
	    this.point.setLocation(newPoint);
	    this.cameFrom.setLocation(node.point);
	    this.g = node.g + step_cost;
	    this.f = this.g + super.getHeuristics(map, this.point, map.getEndPoint());

	    return;
	}
	public Node(AStarExp2.Node n) {
		this.point = n.point;
		this.parent = n.parent;
		this.g = n.g;
		this.f = n.f;
	}
	public void copyNode(Node newNode){
	    this.point.setLocation(newNode.point);
	    this.cameFrom = newNode.cameFrom;
	    this.g = newNode.g;
	    this.f = newNode.f;

	    return;
	}

    }

    /// AStar algorithm

    public List<Point> createPath(final TerrainMap map) {

	AStarExp.Node node = new AStarExp.Node(map, map.getStartPoint());
	AStarExp.Node child = new AStarExp.Node(map, map.getStartPoint());
	ArrayList<AStarExp.Node> frontier = new ArrayList<AStarExp.Node>();
	final ArrayList<AStarExp.Node> explored = new ArrayList<AStarExp.Node>();
	Point neighbors[] = new Point[8];

	frontier.add(new AStarExp.Node(node));//add map parameter

	while(!frontier.isEmpty()){
	    node.copyNode(frontier.remove(0));
	    explored.add(0, new AStarExp.Node(node));
	    if (node.point.equals(map.getEndPoint()))
	    {
			return this.reconstructPath(explored, node);
	    }
	    neighbors = map.getNeighbors(node.point);	    
	    for(int i=0; i<neighbors.size(); i++)
	    {
			child.setValues(map, neighbors[i], node, map.getCost(node.point, child.point));
			if (!frontier.contains(child) && !explored.contains(child))
			{
		    	frontier = this.insertionSort(frontier, child);
			}
	    }
	}

	return null;// failure
    }

    private double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2) {
		double hight_difference = map.getTile(pt2) - map.getTile(pt1);
		int y_difference = pt2.y - pt1.y;
		int x_difference = pt2.x - pt1.x;
		int tiles_between = (Math.abs(y_difference)==0)?((Math.abs(x_difference)==0)?0:Math.abs(x_difference)):Math.abs(y_difference);
		double up_or_down = (hight_difference < 0.0)?-1.0:1.0;

		double h = Math.abs(hight_difference)*(Math.exp(up_or_down)) + (double)tiles_between;
	    
		return h;
    }


	public ArrayList<AStarExp2.Node> insertionSort(ArrayList<AStarExp2.Node> frontier, AStarExp2.Node child){
		Iterator<AStarExp2.Node> itr = frontier.iterator();
		int i=0;
		while(itr.hasNext()) {
		    AStarExp2.Node object = itr.next();
		    if (child.f < object.f){
				break;
	    }
	    i++;
	}
	    frontier.add(i, child);
		return frontier;
    }


    public Boolean containsNode(ArrayList<AStarExp2.Node> set, AStarExp2.Node node) {
		Iterator<AStarExp2.Node> itr = set.iterator();
		while(itr.hasNext()) {
		    AStarExp2.Node object = itr.next();
		    if(node.point.equals(object.point)){
			return true;
	    }
	}
	return false;
    }

    public List<Point> reconstructPath(ArrayList<AStarExp2.Node> explored, ArrayList<AStarExp2.Node> n) {
		List<Point> path = new ArrayList<Point>();

		while(!n.point == map.getStartPoint()) {
			path.add(0, n.point)
			n = search_explored(explored, n)
		}
	return path;
	}


	public Node search_explored(ArrayList<AStarExp2.Node> explored, AStarExp2.Node n) {
		for(int i = 0; i < explored.size(); i++)
			if(explored.get(i).point == n.point)
				return explored.get(i).parent;
	}
}
