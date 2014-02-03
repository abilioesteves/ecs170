import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

// A-Star implementation
// @authors: Abilio Oliveira and James Dryden
public class AStarExp2 implements AIModule
{

    public class Node
    {
		public Point point;
		public Node parent;
		public double g;
		public double f;

		public Node(final TerrainMap map, Point point, AStarExp2.Node parent, Double g)
		{
	    	this.point = new Point(point);
	    	this.parent = parent;
	    	this.g = g;
	    	this.f = getHeuristics(map, this.point, map.getEndPoint());
		}

		public void setValues(final TerrainMap map,Point newPoint, AStarExp2.Node node, double step_cost)
		{
		    this.point.setLocation(newPoint);
		    this.parent = node;
		    this.g = node.g + step_cost;
		    this.f = this.g + getHeuristics(map, this.point, map.getEndPoint());
		}
	}


	/// AStar algorithm

	public List<Point> createPath(final TerrainMap map)
	{
		System.out.println("begin createPath");
	    AStarExp2.Node node = new AStarExp2.Node(map, map.getStartPoint(), null, 0.0);
	    ArrayList<AStarExp2.Node> frontier = new ArrayList<AStarExp2.Node>();
	    final ArrayList<AStarExp2.Node> explored = new ArrayList<AStarExp2.Node>();
	    Point neighbors[] = new Point[8];

	    frontier.add(node);//add map parameter
	    AStarExp2.Node probe = null;
	    while(!frontier.isEmpty())
		{
			System.out.println("begin while createPath");
		    probe = null;
		    probe = frontier.remove(0);
		//    System.out.println(map.getStartPoint());
		    System.out.print(probe.point.x + "\n");
		    System.out.println(probe.point.y);
		//    System.exit(1);
		    explored.add(0, (probe));
		    if (probe.point.equals(map.getEndPoint()))
			{
			    return this.reconstructPath(explored, probe);
			}
		    neighbors = map.getNeighbors(probe.point);

		    for(int i=0; i<8; i++)
			{ 
			    probe = new AStarExp2.Node(map, neighbors[i], explored.get(0), map.getCost(explored.get(0).point, neighbors[i]));
			    if (!containsNode(frontier, probe) && !containsNode(frontier, probe))
				{
				    frontier = this.insertionSort(frontier, probe);
				}
			}
		}
		System.out.println("end: createPath");
	    return null;// failure
	}

	private double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2)
	{
		System.out.println("getHeuristics");
	    double hight_difference = map.getTile(pt2) - map.getTile(pt1);
	    int y_difference = pt2.y - pt1.y;
	    int x_difference = pt2.x - pt1.x;
	    int tiles_between = (Math.abs(y_difference)==0)?((Math.abs(x_difference)==0)?0:Math.abs(x_difference)):Math.abs(y_difference);
	    double up_or_down = (hight_difference < 0.0)?-1.0:1.0;

	    double h = Math.abs(hight_difference)*(Math.exp(up_or_down)) + (double)tiles_between;
	    
	    return h;
	}


	public ArrayList<AStarExp2.Node> insertionSort(ArrayList<AStarExp2.Node> frontier, AStarExp2.Node child)
	{
		System.out.println("begin: insertionSort");
	    Iterator<AStarExp2.Node> itr = frontier.iterator();
	    int i=0;
	    while(itr.hasNext())
		{
		    AStarExp2.Node object = itr.next();
		    if (child.f < object.f)
			{
			    break;
			}
		i++;
		}
	    frontier.add(i, child);
	    return frontier;
	}

	public List<Point> reconstructPath(ArrayList<AStarExp2.Node> explored, AStarExp2.Node n)
	{
		System.out.println("reconstructPath");
	    List<Point> path = new ArrayList<Point>();

	    while(!(n.parent == null))
		{
		    path.add(0, n.point);
		    n = search_explored(explored, n);
		}
	    return path;
	}


	public Node search_explored(ArrayList<AStarExp2.Node> explored, AStarExp2.Node n)
	{
		System.out.println("search_explored");
	    for(int i = 0; i < explored.size(); i++)
		{
		    if(explored.get(i).point == n.point)
			return explored.get(i).parent;
		}
	    return null;
	}

	public boolean containsNode(ArrayList<AStarExp2.Node> set, AStarExp2.Node node)
	{
		System.out.println("CONTAINS");
//		System.exit(1);
		Iterator<AStarExp2.Node> itr = set.iterator();
		while(itr.hasNext())
		{
	    	AStarExp2.Node object = itr.next();
	    	if(node.point.equals(object.point)){
	    		System.out.println("TRUE");
//	    		System.exit(0);
				return true;
	    	}
			
    	}
    	return false;
    }
}
