package geometry_objects.delegates.intersections;

import geometry_objects.delegates.FigureDelegate;
import geometry_objects.points.Point;
import geometry_objects.Segment;

public class SegmentIntersectionDelegate extends FigureDelegate
{
    /*
     * <Segment, Segment> intersection
     * @param thisS -- (this Segment)
     * @param that -- a Segment to intersect with
     * @return the intersection of @thisS and @that
     */
    public static Point findIntersection(Segment thisS, Segment that)
    {
        // <line, line> intersection
        Point inter = LineIntersectionDelegate.intersection(thisS, that);
 
        // Point lies on both segments
        
        if (!thisS.pointLiesBetweenEndpoints(inter)) return null;

        if (!that.pointLiesBetweenEndpoints(inter)) return null;
        
        return inter;        
    }
}
