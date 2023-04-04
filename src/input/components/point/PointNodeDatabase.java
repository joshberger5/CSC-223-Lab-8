/**
* Creates a database for the PointNodes.
*
* <p>Bugs: none known
*
* @author Sam Luck-Leonard, Mason Taylor, and Josh Berger
* @date 1/27/2023
*/
package input.components.point;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import input.components.ComponentNode;
import input.visitor.ComponentNodeVisitor;
import utilities.io.StringUtilities;


public class PointNodeDatabase implements ComponentNode, Iterable<PointNode> {
	Set<PointNode> _points;
	
	public PointNodeDatabase() 
	{
		_points = new LinkedHashSet<PointNode>();
	}
	
	public Set<PointNode> getPoints(){ return _points; }
	
	/**
	 * 
	 * Constructs a new PointNodeDatabase.
	 * If the provided list is null or empty, an empty LinkedHashSet is used instead.
	 * The provided PointNode objects are added to the database via the put method.
	 * @param points A List of PointNode objects to add to the database
	 */
	public PointNodeDatabase(List<PointNode> points) {
		
		if(points == null || points.size() == 0) _points = new LinkedHashSet<PointNode>();
		
        else {_points = new LinkedHashSet<PointNode>(points);}
		
        for(PointNode point : points) {
        	
        	this.put(point);
        	
        }
        
	}
	
	public void put(PointNode point){
		
		if(point == null) return;
		
		_points.add(point);
		
	}
	
	public boolean contains(PointNode point){
		
		return _points.contains(point);
		
	}
	public boolean contains(double x, double y) {
		//if null return false
	    PointNode point = getPoint(x, y);
	    
	    return _points.contains(point);
	    
	}
	
	
	public String getName(PointNode point){
		
		for (PointNode point1:_points){
			
			if(point1.equals(point)) return point._name;
			
		}
		
		return null;
		
	}
	public String getName(double x, double y){
		
		PointNode point1 = getPoint(x,y);
		
		if (point1 == null) return null;

		return point1.getName();

	}
	
	/**
	 * 
	 * Returns the PointNode object with the given point or with the given x and y coordinates. 
	 * @param point The point to be returned if it is within points.
	 * @param x The x-coordinate of the PointNode to be returned.
	 * @param y The y-coordinate of the PointNode to be returned.
	 * @return The PointNode object, or null if no PointNode exists in the list of points.
	 */
	public PointNode getPoint(PointNode point){
		
		if(_points.contains(point)) return point;
		//use the below implementation this should do the work
		else return null;
		
	}
	public PointNode getPoint(double x, double y){
		
        PointNode point1 = new PointNode(x, y);
        
        if (_points.contains(point1)) { //return false then do the loop
        	
            for (PointNode point:_points) {
            	
                if (point1.equals(point)) return point;
            }
            
        }
        
        return null;
        
    }
	
	public PointNode getPoint(String name) {
		
		for(PointNode point : _points) {
			
			if(point.getName().equals(name)) return point;
			
		}
		
		return null;
	}
	
	protected int size() { return _points.size(); }

	@Override
	public Iterator<PointNode> iterator() {
	
		return _points.iterator();
	}

//	@Override
//	public void unparse(StringBuilder sb, int level) {
//		
//		sb.append(StringUtilities.indent(level) + "Points: \n" + StringUtilities.indent(level) + "{ \n");
//		
//		for(PointNode point : _points) {
//			
//			point.unparse(sb, level + 1);
//			
//		}
//		
//		sb.append(StringUtilities.indent(level) + "}\n");
//		
//		
//	}

	@Override
public Object accept(ComponentNodeVisitor visitor, Object o) {
		
return visitor.visitPointNodeDatabase(this, o); }
	
}