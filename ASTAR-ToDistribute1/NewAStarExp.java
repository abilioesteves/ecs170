import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Hashtable;
// A-Star implementation
// @authors Abilio Oliveira and James Dryden
public class NewAStarExp implements AIModule{
    // public class NodeComparator implements Comparator<NewAStarExp.Node>, Serializable{
    // 	public int compare(NewAStarExp.Node n1, NewAStarExp.Node n2){
    // 	    return n2.f - n1.f;
    // 	}
    // }

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
	Point goal = map.getEndPoint();
	Point start = map.getStartPoint();
	Hashtable<String, NewAStarExp.Node> closedset = new Hashtable<String, NewAStarExp.Node>(map.getWidth()*map.getHeight());
	Hashtable<String, NewAStarExp.Node> came_from = new Hashtable<String, NewAStarExp.Node>(map.getWidth()*map.getHeight());
	ArrayList<NewAStarExp.Node> openset = new ArrayList<NewAStarExp.Node>();
	NewAStarExp.Node probe = new NewAStarExp.Node(map,start,null,0.0);
	NewAStarExp.Node child = null;
	Point[] neighbors = new Point[8];
	
	openset.add(probe);
	
	while (!openset.isEmpty()){
	    probe = openset.get(0);
	    if (probe.equals(goal)){
		return reconstructPath(came_from, probe);
	    }
	    closedset.put(probe.point.x + "," + probe.point.y, probe);
	    neighbors = map.getNeighbors(probe.point);
	    for (int i = 0; i < neighbors.length; i++){
		if(closedset.get(neighbors[i].x + "," + neighbors[i].y)!=null) continue;
		child = new Node(map, neighbors[i], probe, probe.g + map.getCost(probe.point, neighbors[i]));
		int j = openset.indexOf(child);
		if (j != -1){
		    if (child.g<openset.get(j).g){
			came_from.put(child.point.x + "" + child.point.y, probe);
		    }
		}else{
		    insertionSort(openset, child);
		}
	    }
	}
	return null; //failure
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

    public List<Point> reconstructPath(Hashtable<String, NewAStarExp.Node> came_from, NewAStarExp.Node node){
	ArrayList<Point> path = new ArrayList<Point>();

	path.add(node.point);

	if (came_from.containsValue(node)){
	    path.addAll(0, reconstructPath(came_from, came_from.get(node.point.x + "," + node.point.y)));
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
	
	if(VT>=0){
	    if(HT>=Math.abs(VT)){
		return VT*Math.exp(1) + (VT-HT-1);
	    }else{
		return HT*Math.exp(VT/(double)HT);
	    }
	}else{
	    if(HT>=Math.abs(VT)){
		return Math.abs(VT)*Math.exp(-1) + (VT-HT-1);
	    }else{
		return HT*Math.exp(VT/(double)HT);
	    }
	}
    }

}
