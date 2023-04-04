package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
import geometry_objects.delegates.intersections.IntersectionDelegate;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessor
{
	/**
	 * It is possible that some of the defined segments intersect
	 * and points that are not named; we need to capture those
	 * points and name them.
	 * 
	 * Algorithm:
	 *    check for intersection between all segments
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();
		
		// Iterate through all combinations of segments
        for(int i=0; i<givenSegments.size()-1; i++) {
        	for(int j=i+1; j<givenSegments.size(); j++) {
        		Point intersect = IntersectionDelegate.segmentIntersection(	givenSegments.get(i),
        																	givenSegments.get(j));
        		if(intersect != null && givenPoints.getPoint(intersect) == null) {
        			implicitPoints.add(intersect);
        		}
            }
        }
		
		return implicitPoints;
	}

}
