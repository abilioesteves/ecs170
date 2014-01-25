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
    public Point getNodeWithLowestFCostInOpenSet(final TerrainMap map, final ArrayList<Point> openSet){
        Iterator itr = openSet.iterator();
        
        while (itr.hasNext()){//
            map.getcost
        }
    }
    
    // A-star attempt
    public List<Point> createPath(final TerrainMap map){
        
        // initializations
        final Point EndPoint = map.getEndPoint();
        final ArrayList<Point> openSet = new ArrayList<Point>();
        final ArrayList<Point> closedSet = new ArrayList<Point>();
        final ArrayList<Point> path = new ArrayList<Point>();
        final ArrayList<Point> Neighbors = new ArrayList<Point>();
        int g = 0;
        openSet.add(new Point(map.getStartPoint()));
        
        while (!openSet.isEmpty()){
            current = getNodeWithLowestFCostInOpenSet(map, openSet);
            if (current == EndPoint){
                path.add(current);
                return path;
            }
            openSet.remove(current);
            closedSet.add(current);
            
            tentative_g_score = g + map.getCost(current, EndPoint);
            
            Neighbors = map.getNeighbors(current);
            Iterator itr = Neighbors.iterator();
            while(itr.hasNext()){
                Point neighbor = itr.next();
                if (closedSet.contains(neighbor)) {
                    continue
                }
                
                
            }
        }
        
        
        
        
    }
    
    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();

        // Keep track of where we are and add the start point.
        final Point CurrentPoint = map.getStartPoint();
        final Point FinalPoint = map.getEndPoint();
        path.add(new Point(CurrentPoint));

        Point Neighbors[] = map.getNeighbors(CurrentPoint);
        
        // Keep moving horizontally until we match the target.
        while(map.getEndPoint().x != CurrentPoint.x)
        {
            if(map.getEndPoint().x > CurrentPoint.x)
                ++CurrentPoint.x;
            else
                --CurrentPoint.x;
            path.add(new Point(CurrentPoint));
        }

        // Keep moving vertically until we match the target.
        while(map.getEndPoint().y != CurrentPoint.y)
        {
            if(map.getEndPoint().y > CurrentPoint.y)
                ++CurrentPoint.y;
            else
                --CurrentPoint.y;
            path.add(new Point(CurrentPoint));
        }

        // We're done!  Hand it back.
        return path;
    }
}
