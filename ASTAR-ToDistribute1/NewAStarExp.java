import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.TreeSet;

// A-Star implementation
// @authors Abilio Oliveira and James Dryden
public class NewAStarExp implements AIModule{
    public class Node{
	public Point point;
	public Node parent;
	public double g;
	public double f;

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

	public Node(final TerrainMap map, Point point, NewAStarExp.Node parent, double g){
	    this.point = point;
	    this.parent = parent;
	    this.g = g;
	    this.f = g + getHeuristics(map, this.point, map.getEndPoint());
	}

	public Node(NewAStarExp.Node node){
	    this.point = node.point;
	    this.parent = node.parent;
	    this.g = node.g;
	    this.f = node.f;
	}
    }
    
    public List<Point> createPath(final TerrainMap map){
	NewAStarExp.Node probe = new NewAStarExp.Node(map, map.getStartPoint(), null, 0.0);
	NewAStarExp.Node child = null;
	ArrayList<NewAStarExp.Node> frontier = new ArrayList<NewAStarExp.Node>();
	ArrayList<NewAStarExp.Node> explored = new ArrayList<NewAStarExp.Node>();
	Point[] neighbors = new Point[8];
	int j = -1;
	
	frontier.add(new NewAStarExp.Node(probe));

	while(!frontier.isEmpty()){
	    probe = frontier.remove(0);
	    if(probe.point.equals(map.getEndPoint())){
		return reconstructPath(probe);
	    }
	    explored.add(0, probe);
	    neighbors = map.getNeighbors(probe.point);
	    for (int i = 0; i< neighbors.length; i++){
		child = new Node(map, neighbors[i], probe, probe.g + map.getCost(probe.point, neighbors[i]));
		if(explored.contains(child)) continue;
		j = frontier.indexOf(child);
		if(j != -1){
		    probe = frontier.remove(j);
		    if (probe.f < child.f) continue;
		}
		insertionSort(frontier, child);
	    }
	}

	return null;
    }

    public List<Point> reconstructPath(NewAStarExp.Node node){
	ArrayList<Point> path = new ArrayList<Point>();

	path.add(node.point);
	if(node.parent == null){
	    return path;
	}else{
	    path.addAll(0,reconstructPath(node.parent));
	}

	return path;
    }

    public void insertionSort(ArrayList<NewAStarExp.Node> frontier, NewAStarExp.Node child){
	Iterator<NewAStarExp.Node> itr = frontier.iterator();
	int i=0;
	while(itr.hasNext()){
	    NewAStarExp.Node object = itr.next();
	    if (child.f < object.f){
		break;
	    }

	    i++;
	}
	frontier.add(i, child);
	return;
    }

    private double getHeuristics(final TerrainMap map, final Point pt1, final Point pt2){
	double VT = map.getTile(pt2) - map.getTile(pt1);
	int yd = pt2.y - pt1.y;
	int xd = pt2.x - pt1.x;
	int HT = (Math.abs(yd)==0)?((Math.abs(xd)==0)?0:(Math.abs(xd)-1)):(Math.abs(yd)-1);
	// double Q = Math.ceil(Math.abs(VT)/(double)Math.abs(HT));
	// double B = (Q - (Math.abs(VT)/(double)Math.abs(HT)));
	// double R = Math.abs(VT) - Q*B;
	// double S = Math.abs(HT) - Q*B;
	// double p = (VT<0)?-1.0:1.0;

	// return B*Math.exp(Q*p) + Math.exp(R*p) + S;
	return HT*Math.exp(VT/(double)HT);
    }

}
